package org.judovana.calendarmaker;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class AllView extends JPanel {


    private static class HistoryItem {
        private final String img;
        private final BigDecimal rotation;

        public HistoryItem(String img, BigDecimal rotation) {
            this.img = img;
            this.rotation = rotation;
        }
    }

    private static class HistoryMoment {
        private final List<HistoryItem> state;

        public HistoryMoment(PageView[] data) {
            this.state = new ArrayList<>(data.length);
            for (int i = 0; i < data.length; i++) {
                state.add(new HistoryItem(data[i].getData().getPhoto().getSrc(), data[i].getData().getPhoto().getRotate()));
            }
        }

        public void apply(PageView[] data) throws IOException {
            for (int i = 0; i < data.length; i++) {
                state.add(new HistoryItem(data[i].getData().getPhoto().getSrc(), data[i].getData().getPhoto().getRotate()));
                if (!data[i].getData().getPhoto().getSrc().equals(state.get(i).img)){
                    data[i].getData().getPhoto().setData(state.get(i).img);
                }
                if (!data[i].getData().getPhoto().getRotate().equals(state.get(i).rotation)){
                    data[i].getData().getPhoto().setRotate(state.get(i).rotation.toString());
                }
            }
        }
    }

    private final List<HistoryMoment> toUndo = new LinkedList<>();
    private final List<HistoryMoment> toRedo = new LinkedList<>();

    public void undo() throws IOException {
        if (toUndo.size()<2){
            return;
        }
        HistoryMoment now = toUndo.get(toUndo.size()-1);
        HistoryMoment last = toUndo.get(toUndo.size()-2);
        toUndo.remove(now);
        toRedo.add(now);
        last.apply(data);
    }

    public void redo() throws IOException {
        if (toRedo.size()<1){
            return;
        }
        HistoryMoment last = toRedo.get(toRedo.size()-1);
        toRedo.remove(last);
        toUndo.add(last);
        last.apply(data);
    }


    private final PageView[] data;
    private int offset = 0;

    private AllView() {
        data = null;
        setResize();
    }

    private void setResize() {
        this.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                for (PageView page : data) {
                    page.resize();
                }
            }

            @Override
            public void componentMoved(ComponentEvent componentEvent) {

            }

            @Override
            public void componentShown(ComponentEvent componentEvent) {

            }

            @Override
            public void componentHidden(ComponentEvent componentEvent) {

            }
        });
    }

    public AllView(PageView... pages) {
        setResize();
        data = pages;
    }

    public AllView(Collection<PageView> pages) {
        setResize();
        int i = 0;
        data = new PageView[pages.size()];
        for (PageView page : pages) {
            data[i] = page;
            i++;
        }
        addHistory();
    }

    void addHistory() {
        toUndo.add(new HistoryMoment(data));
    }

    public void adjsutOffset(int by) {
        offset += by;
        setOfset(offset);
    }

    private void setOfset(int off) {
        offset = off;
        if (offset > 0) {
            offset = 0;
        }
        repaint();
    }

    public void resetOffset() {
        offset = 0;
        setOfset(offset);
    }

    public void upsetOffset() {
        setOfset(-data.length * this.getHeight() + this.getHeight());
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.black);
        for (int i = 0; i < data.length; i++) {
            Integer week = null;
            if (isWeekCal()) {
                week = i;
            }
            data[i].paint((Graphics2D) g, 0, offset + i * this.getHeight(), this.getWidth(), this.getHeight(), week);
        }
    }

    public PageView get(int x, int y) {
        for (int i = 0; i < data.length; i++) {
            int h = i * getHeight() + offset;
            if (y > h && y < h + getHeight()) {
                return data[i];
            }
        }
        return null;
    }

    public void exportOnePageOnePage_month(final String s) throws IOException {
        float margins = 20;
        PdfWriter writer = new PdfWriter(s);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document d = new Document(pdfDoc, PageSize.A4);
        for (int i = 0; i < data.length; i++) {
            Integer week = null;
            if (isWeekCal()) {
                week = i;
            }
            BufferedImage bi = data[i].getImage(getWidth(), getHeight(), week);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", os);
            ImageData id = ImageDataFactory.createPng(os.toByteArray());
            Image im = new Image(id);
            im.setFixedPosition(margins, margins);
            im.scaleAbsolute(PageSize.A4.getWidth() - 2f * margins, PageSize.A4.getHeight() - 2f * margins);
            d.add(im);
            if (i < data.length - 1) {
                d.add(new AreaBreak());
            }
        }
        d.close();
        writer.flush();
        writer.close();
    }

    public void exportOnePageOnePage_week(final String s) throws IOException {
        float margins = 20;
        PdfWriter writer = new PdfWriter(s);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document d = new Document(pdfDoc, PageSize.A4);
        int half = data.length / 2;
        if (data.length % 2 != 0) {
            half++;
        }
        for (int i = 0; i < half; i++) {
            Integer week = null;
            if (isWeekCal()) {
                week = i;
            }
            BufferedImage bi1 = PhotoFrame.rotate180(data[i].getImage(getWidth(), getHeight(), week), Math.PI);
            ByteArrayOutputStream os1 = new ByteArrayOutputStream();
            ImageIO.write(bi1, "png", os1);
            ImageData id1 = ImageDataFactory.createPng(os1.toByteArray());
            Image im1 = new Image(id1);
            im1.setFixedPosition(margins, PageSize.A4.getHeight() / 2f + margins);
            im1.scaleAbsolute(PageSize.A4.getWidth() - 2f * margins, (PageSize.A4.getHeight() - 2f * margins) / 2);
            d.add(im1);

            int ii = data.length - 1 - i;
            week = null;
            if (isWeekCal()) {
                week = ii;
            }
            if (i != ii) {
                BufferedImage bi2 = data[ii].getImage(getWidth(), getHeight(), week);
                ByteArrayOutputStream os2 = new ByteArrayOutputStream();
                ImageIO.write(bi2, "png", os2);
                ImageData id2 = ImageDataFactory.createPng(os2.toByteArray());
                Image im2 = new Image(id2);
                im2.setFixedPosition(margins, margins);
                im2.scaleAbsolute(PageSize.A4.getWidth() - 2f * margins, (PageSize.A4.getHeight() - 2f * margins) / 2);
                d.add(im2);
            }
            if (i < half - 1) {
                d.add(new AreaBreak());
            }
        }
        d.close();
        writer.flush();
        writer.close();
    }

    private boolean isWeekCal() {
        return data.length > 12;
    }

    public void save(String s) throws IOException {
        List<String> l = new ArrayList<>(data.length);
        for (PageView p : data) {
            l.add(p.getData().getPhoto().getSrc() + "|" + p.getData().getPhoto().getRotate());
        }
        Files.write(new File(s).toPath(), l, Charset.forName("utf-8"));
    }

    public void load(String s) throws IOException {
        List<String> lines = Files.readAllLines(new File(s).toPath(), Charset.forName("utf-8"));
        for (int i = 0; i < Math.min(lines.size(), data.length); i++) {
            String[] imgRotate = lines.get(i).split("\\|");
            data[i].getData().getPhoto().setData(imgRotate[0]);
            data[i].getData().getPhoto().setRotate(imgRotate[1]);
        }
        addHistory();
    }

    public void moveUp(PageView page) {
        for (int i = 1/*first can not go up*/; i < data.length; i++) {
            if (page == data[i]) {
                PhotoFrame swap = data[i - 1].getData().getPhoto();
                data[i - 1].getData().setPhoto(page.getData().getPhoto());
                data[i].getData().setPhoto(swap);
                break;
            }
        }
        addHistory();
    }

    public void moveDown(PageView page) {
        for (int i = 0; i < data.length - 1/*last can not go down*/; i++) {
            if (page == data[i]) {
                PhotoFrame swap = data[i + 1].getData().getPhoto();
                data[i + 1].getData().setPhoto(page.getData().getPhoto());
                data[i].getData().setPhoto(swap);
                break;
            }
        }
        addHistory();
    }

    public void reData(PageView page, String f) throws IOException {
        page.getData().getPhoto().setData(f);
        addHistory();
    }
}

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
import java.io.IOException;
import java.util.Collection;

public class AllView extends JPanel {

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
            data[i].paint((Graphics2D) g, 0, offset + i * this.getHeight(), this.getWidth(), this.getHeight());
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

    public void exportOnePageOnePage(final String s) throws IOException {
        float margins = 20;
        PdfWriter writer = new PdfWriter(s);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document d = new Document(pdfDoc, PageSize.A4);
        for (int i = 0; i < data.length; i++) {
            BufferedImage bi = data[i].getImage(getWidth(), getHeight());
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
}

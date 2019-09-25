package org.judovana.calendarmaker;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoFrame {

    private BufferedImage data;
    private String src;
    private double rotate = 0;
    private int scaleType = 1;
    private static final boolean rangeTesting = false;

    public PhotoFrame(String src) throws IOException {
        setData(src);
    }

    public void setData(String src) throws IOException {
        if (rangeTesting) {
            this.data = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics g = data.createGraphics();
            g.setColor(new Color(Math.abs(src.hashCode()) % 250, Math.abs(new File(src).hashCode()) % 250, Math.abs(src.hashCode() + new File(src).hashCode()) % 250));
            g.fillRect(0, 0, 100, 100);
        } else {
            this.data = ImageIO.read(new File(src));
        }
        this.rotate = 0;
        this.src = src;
    }

    public String getSrc() {
        return src;
    }

    public void draw(double x, double y, double w, double h, Graphics2D g, int border) {
        draw((int) x, (int) y, (int) w, (int) h, g, border);
    }

    public void draw(int x, int y, int w, int h, Graphics2D g2d, int border) {
        x = x + border;
        y = y + border;
        w = w - 2 * border;
        h = h - 2 * border;
        if (scaleType == 1) {
            double spaceRatio = (double) w / (double) h;
            double imgRatio = (double) data.getWidth() / (double) data.getHeight();
            int nW;
            int nH;
            if (spaceRatio <= imgRatio) {
                double ratio = (double) data.getWidth() / (double) w;
                nW = (int) (1d / ratio * (double) data.getWidth());
                nH = (int) (1d / ratio * (double) data.getHeight());
                g2d.drawImage(data, x, y + (h - nH) / 2, nW, nH, null);
            } else {
                double ratio = (double) data.getHeight() / (double) h;
                nW = (int) (1d / ratio * (double) data.getWidth());
                nH = (int) (1d / ratio * (double) data.getHeight());
                g2d.drawImage(data, x + (w - nW) / 2, y, nW, nH, null);
            }
        } else {
            g2d.drawImage(data, x, y, w, h, null);
        }
    }

    public static final SimpleDateFormat dayThis = new SimpleDateFormat("dd/MM/yyyy");

    public String getFooter() {
        File f = new File(src);
        return f.getParentFile().getName() + "/" + f.getName() + " " + dayThis.format(new Date(f.lastModified()));

    }

    public  static BufferedImage rotate90(BufferedImage src, double angleRad){
        BufferedImage bi = new BufferedImage(src.getHeight(), src.getWidth(), src.getType());
        AffineTransform aff = new AffineTransform();
        aff.translate(src.getHeight() / 2, src.getWidth() / 2);
        aff.rotate(angleRad);
        aff.translate(-src.getWidth() / 2, -src.getHeight() / 2);
        bi.createGraphics().drawImage(src, aff, null);

        return bi;
    }

    public void rotateImgClock() {
        rotate = +(Math.PI / 2d);
        data = rotate90(data, rotate);
    }


    public void rotateImgAntiClock() {
        rotate = -(Math.PI / 2d);
        data = rotate90(data, rotate);

    }


    public static BufferedImage rotate180(BufferedImage src, double angleRad){
        BufferedImage bi = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        AffineTransform aff = new AffineTransform();
        aff.translate(src.getWidth() / 2, src.getHeight() / 2);
        aff.rotate(angleRad);
        aff.translate(-src.getWidth() / 2, -src.getHeight() / 2);
        bi.createGraphics().drawImage(src, aff, null);
        return bi;
    }

    public void rotateImg180() {
        rotate = +Math.PI;
        data = rotate180(data, rotate);

    }
}

package org.judovana.calendarmaker;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
            g.setColor(new Color(Math.abs(src.hashCode()) % 250,
                    Math.abs(new File(src).hashCode()) % 250,
                    Math.abs(src.hashCode() + new File(src).hashCode()) % 250));
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

    public BigDecimal getRotate() {
        BigDecimal bd = BigDecimal.valueOf(rotate);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd;
    }

    public void setRotate(String src) {
        rotate = Double.valueOf(src);
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
            BufferedImage rotatedData = rotatedImg();
            double imgRatio = (double) rotatedData.getWidth() / (double) rotatedData.getHeight();
            int nW;
            int nH;
            if (spaceRatio <= imgRatio) {
                double ratio = (double) rotatedData.getWidth() / (double) w;
                nW = (int) (1d / ratio * (double) rotatedData.getWidth());
                nH = (int) (1d / ratio * (double) rotatedData.getHeight());
                g2d.drawImage(rotatedData, x, y + (h - nH) / 2, nW, nH, null);
            } else {
                double ratio = (double) rotatedData.getHeight() / (double) h;
                nW = (int) (1d / ratio * (double) rotatedData.getWidth());
                nH = (int) (1d / ratio * (double) rotatedData.getHeight());
                g2d.drawImage(rotatedData, x + (w - nW) / 2, y, nW, nH, null);
            }
        } else {
            g2d.drawImage(rotatedImg(), x, y, w, h, null);
        }
    }

    public static final SimpleDateFormat dayThis = new SimpleDateFormat("dd/MM/yyyy");

    public String getFotoTitle(List<String> sortedStringList) {
        File f = new File(src);
        if (sortedStringList == null) {
            return f.getParentFile().getName() + "/" + f.getName() + " " + dayThis.format(
                    new Date(f.lastModified()));
        } else {
            String ff = f.getAbsolutePath();
            for (String s : sortedStringList) {
                if (ff.startsWith(s)) {
                    ff = ff.substring(s.length());
                    while  (ff.startsWith("/") || ff.startsWith("\\")) {
                        ff = ff.substring(1);
                    }
                    break;
                }
            }
            return ff + " " + dayThis.format(new Date(f.lastModified()));
        }


    }

    public static BufferedImage rotate90(BufferedImage src, double angleRad) {
        BufferedImage bi = new BufferedImage(src.getHeight(), src.getWidth(), src.getType());
        AffineTransform aff = new AffineTransform();
        aff.translate(src.getHeight() / 2, src.getWidth() / 2);
        aff.rotate(angleRad);
        aff.translate(-src.getWidth() / 2, -src.getHeight() / 2);
        bi.createGraphics().drawImage(src, aff, null);

        return bi;
    }

    public void rotateImgClock() {
        rotate = round(rotate + (Math.PI / 2d));
    }


    public void rotateImgAntiClock() {
        rotate = round(rotate - (Math.PI / 2d));

    }


    public static BufferedImage rotate180(BufferedImage src, double angleRad) {
        BufferedImage bi = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        AffineTransform aff = new AffineTransform();
        aff.translate(src.getWidth() / 2, src.getHeight() / 2);
        aff.rotate(angleRad);
        aff.translate(-src.getWidth() / 2, -src.getHeight() / 2);
        bi.createGraphics().drawImage(src, aff, null);
        return bi;
    }

    public void rotateImg180() {
        rotate = round(rotate + Math.PI);

    }

    private double round(double v) {
        int rounded = ((int) (v * 100d)) + 628/*to keep in positives*/;
        rounded = rounded % 628;
        double vv = ((double) rounded) / 100d;
        if (vv < 0.2) {
            return 0;
        }
        if (vv > 0.2 && vv < 1.7) {
            return 1.57;
        }
        if (vv > 1.7 && vv < 3.5) {
            return 3.14;
        }
        if (vv > 3.5 && vv < 5) {
            return 4.71;
        }
        if (vv > 5) {
            return 0;
        }
        return vv;
    }


    private BufferedImage rotatedImg() {
        String niceRot = getRotate().toString();
        if (niceRot.equals("3.14") || niceRot.equals("-3.14")) {
            return rotate180(data, Math.PI);
        } else if (niceRot.equals("1.57") || niceRot.equals("-4.71")) {
            return rotate90(data, Math.PI / 2d);
        } else if (niceRot.equals("-1.57") || niceRot.equals("4.71")) {
            return rotate90(data, -Math.PI / 2d);
        } else {
            return data;
        }
    }
}

package org.judovana.calendarmaker;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PhotoFrame {

    private final BufferedImage data;
    private final String src;
    private int scaleType = 1;

    public PhotoFrame(String src) throws IOException {
        this.data = ImageIO.read(new File(src));
        this.src=src;
    }


    public void draw(int x, int y, int w, int h, Graphics2D g2d) {
        if (scaleType ==1) {
            double spaceRatio = (double) w / (double) h;
            double imgRatio = (double) data.getWidth() / (double) data.getHeight();
            int nW;
            int nH;
            if (spaceRatio <= imgRatio) {
                double ratio = (double) data.getWidth() / (double) w;
                nW = (int) (1d/ratio * (double) data.getWidth());
                nH = (int) (1d/ratio * (double) data.getHeight());
                g2d.drawImage(data, x, y+(h-nH)/2, nW, nH, null);
            } else {
                double ratio = (double) data.getHeight() / (double) h;
                nW = (int) (1d/ratio * (double) data.getWidth());
                nH = (int) (1d/ratio * (double) data.getHeight());
                g2d.drawImage(data, x+(w-nW)/2, y, nW, nH, null);
            }
        } else {
            g2d.drawImage(data, x, y, w, h, null);
        }
    }
}

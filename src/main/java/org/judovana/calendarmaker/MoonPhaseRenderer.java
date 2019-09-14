package org.judovana.calendarmaker;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MoonPhaseRenderer {

    public static BufferedImage getMoonRectGauge(int y, int m, /*1-12!*/int d, int w, int h, int alpha) {
        BufferedImage bi =  new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = bi.createGraphics();
        g.setColor(new Color(0,0,0 ,alpha));
        g.fillRect(0, 0, w-1, h-1);
        if (!MoonPhases.isDarkFirstInLeft(y, m, d)) {
            int delimiter = (int) ((double) w * MoonPhases.getDark(y, m, d));
            g.setColor(new Color(255,255,255 ,alpha));
            g.fillRect(delimiter, 0,  w, h);
        } else {
            int delimiter = (int) ((double) w * MoonPhases.getWhite(y, m, d));
            g.setColor(new Color(255,255,255 ,alpha));
            g.fillRect(0,0, delimiter, h);
        }
        g.setColor(new Color(0,0,0 ,alpha));
        g.drawRect(0, 0, w-1, h-1

        );
        return bi;
    }

    public static void main(String... args) {
        int y = 2019;
        int m = 8;
        int d = 22;
        final BufferedImage bi = MoonPhaseRenderer.getMoonRectGauge(y, m, d, 150, 80, 255);
        //for (int d = 1; d <= 1; d++)
        {
            JFrame jf = new JFrame();
            jf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            jf.setSize(200, 50);
            jf.add(new JPanel() {

                @Override
                public void paint(Graphics g) {
                    g.drawImage(bi, 0, 0, getWidth(), getHeight(), null);

                }
            });
            jf.setVisible(true);
        }
    }
}

package org.judovana.calendarmaker;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class MoonPhaseRenderer {

    public static BufferedImage getMoonGauge(int y, int m, /*1-12!*/int d, int w, int h, int alpha, boolean clip) {
        BufferedImage bi =  new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = bi.createGraphics();
        Ellipse2D e = new Ellipse2D.Double();
        e.setFrame(1, 1, w-2, h-2);
        if (clip) {
            g.setClip(e);
        }
        g.setColor(new Color(0,0,0 ,alpha));
        g.fillRect(0, 0, w, h);
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
        g.drawRect(0, 0, w-1, h-1);
        if (clip) {
            g.draw(e);
        }
        return bi;
    }

    public static void main(String... args) {
        int y = 2019;
        int m = 8;
        int d = 22;
        final BufferedImage bi = MoonPhaseRenderer.getMoonGauge(y, m, d, 150, 80, 255, true);
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

package org.judovana.calendarmaker;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PageView {

    private final CalendarPage data;

    public PageView(CalendarPage p) {
        data = p;
    }

    private BufferedImage cache = null;

    public void paint(Graphics g, int x, int y, int w, int h, Integer week) {
        getImage(w, h, week);
        g.drawImage(cache, x , y, null);
    }

    public BufferedImage getImage(int w, int h, Integer week) {
        if (cache == null){
            cache = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
        }
        Graphics g = cache.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, w, h );
        data.paint((Graphics2D) cache.createGraphics(), 0, 0, w, h, week);
        return cache;
    }

    public void resize(){
        cache = null;
    }

    public CalendarPage getData() {
        return data;
    }
}

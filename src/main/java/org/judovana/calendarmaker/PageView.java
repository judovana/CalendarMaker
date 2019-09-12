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

    public void paint(Graphics g, int x, int y, int w, int h) {
        getImage(w, h);
        g.drawImage(cache, x , y, null);
    }

    public BufferedImage getImage(int w, int h) {
        if (cache == null){
            cache = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
        }
        data.paint((Graphics2D) cache.createGraphics(), 0, 0, w, h);
        return cache;
    }

    public void resize(){
        cache = null;
    }

    public CalendarPage getData() {
        return data;
    }
}

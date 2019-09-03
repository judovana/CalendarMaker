package org.judovana.calendarmaker;

import javax.swing.*;
import java.awt.*;

public class PageView {

    private final CalendarPage data;

    public PageView(CalendarPage p) {
        data = p;
    }

    public void paint(Graphics g, int x, int y, int w, int h) {
        data.paint((Graphics2D) g, x, y, w, h);
    }
}

package org.judovana.calendarmaker;

import javax.swing.*;
import java.awt.*;

public class PageView extends JPanel {

    private final CalendarPage data;

    public PageView(CalendarPage p) {
        data = p;
    }

    @Override
    public void paint(Graphics g) {
        data.paint((Graphics2D)g);
    }
}

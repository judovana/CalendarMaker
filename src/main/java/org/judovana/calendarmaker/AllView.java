package org.judovana.calendarmaker;

import javax.swing.*;
import java.awt.*;

public class AllView extends JPanel {

    private final PageView[] data;

    public AllView(PageView... pages) {
        data = pages;
    }

    @Override
    public void paint(Graphics g) {
        for (int i = 0 ; i < data.length; i++) {
            data[i].paint((Graphics2D) g,0, i*this.getHeight(), this.getWidth(), this.getHeight());
        }
    }
}

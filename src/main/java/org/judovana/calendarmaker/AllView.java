package org.judovana.calendarmaker;

import javax.swing.*;
import java.awt.*;

public class AllView extends JPanel {

    private final PageView[] data;
    private int offset = 0;

    public AllView(PageView... pages) {
        data = pages;
    }

    public void adjsutOffset(int by){
        offset+=by;
        if (offset>0){
            offset = 0;
        }
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.black);
        for (int i = 0 ; i < data.length; i++) {
            data[i].paint((Graphics2D) g,0, offset+i*this.getHeight(), this.getWidth(), this.getHeight());
        }
    }
}

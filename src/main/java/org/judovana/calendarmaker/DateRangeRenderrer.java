package org.judovana.calendarmaker;

import java.awt.*;
import java.util.Date;
import java.util.List;

public class DateRangeRenderrer {

    private final List<Date> range;

    public DateRangeRenderrer(List<Date> dates) {
        this.range = dates;
    }

    public void draw(int x, int y, int w, int h, Graphics2D g) {
        int border = 5;
        if (range.size() == 7) {
            int step = h / range.size();
            g.drawRect(x + border, +border, w - 2 * border, h - 2 * border);
            for (int i = 0; i < range.size(); i++) {
                int hh = border + (i + 1) * step;
                g.drawString(range.get(i).toString(), 2 * border, hh - (step - g.getFontMetrics().getHeight()));
                g.drawLine(border, hh, w - border, hh);
            }
        } else {
            int ww = (w-(2*border))/7;
            int hh = (h-(2*border))/(range.size()/6);
            for (int i = 0; i < range.size(); i++) {
                int row = (i / 7);
                int inRow = (i % 7);
                g.drawRect(border+inRow*ww, border+row*hh, ww, hh);
                g.drawString(i+"", border+inRow*ww, border+row*hh+hh);
            }
        }
    }
}

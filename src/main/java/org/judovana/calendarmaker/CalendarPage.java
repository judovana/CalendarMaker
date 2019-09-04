package org.judovana.calendarmaker;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;

public class CalendarPage {

    private final PhotoFrame photo;
    private final DateRangeRenderrer dates;

    public CalendarPage(List<Date> dates, PhotoFrame photo) {
        this.dates = new DateRangeRenderrer(dates);
        this.photo = photo;
    }

    public void paint(Graphics2D g, int x, int y, int w, int h) {
        dates.draw(x, y, w / 2, h, g);
        photo.draw(x + w / 2, y, w / 2, h, g);
        String title = dates.getTitle();
        int th = g.getFontMetrics().getHeight();
        int tw = (int) g.getFontMetrics().getStringBounds(title, g).getWidth();
        g.drawString(title, w / 2 + w / 4 - tw / 2, y + th);

        String footer = photo.getFooter();
        int fh = g.getFontMetrics().getHeight();
        int fw = (int) g.getFontMetrics().getStringBounds(footer, g).getWidth();
        g.drawString(footer, w / 2 + w / 4 - fw / 2, y + h - fh);
    }
}

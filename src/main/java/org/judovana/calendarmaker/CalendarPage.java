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
        dates.draw(x, y, w/2, h, g);
        photo.draw(x+w / 2, y, w / 2, h, g);
    }
}

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

    public void paint(Graphics2D g, int w, int h) {
        photo.draw(w / 2, 0, w / 2, h, g);
        dates.draw(0, 0, w/2, h, g);
    }
}

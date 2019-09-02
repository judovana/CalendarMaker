package org.judovana.calendarmaker;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;

public class CalendarPage {

    private final List<Date> dates;
    private final PhotoFrame photo;

    public CalendarPage(List<Date> dates, PhotoFrame photo) {
        this.dates = dates;
        this.photo = photo;
    }

    public void paint(Graphics2D g) {
        g.drawImage(photo.getImage(), null, null);
    }
}

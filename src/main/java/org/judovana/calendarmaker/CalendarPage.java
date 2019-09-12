package org.judovana.calendarmaker;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;

public class CalendarPage {

    private final PhotoFrame photo;
    private final DateRangeRenderrer dates;
    private final Template t = new Template.Horizontal();

    public CalendarPage(List<Date> dates, PhotoFrame photo) {
        this.dates = new DateRangeRenderrer(dates);
        this.photo = photo;
    }

    public PhotoFrame getPhoto() {
        return photo;
    }

    public DateRangeRenderrer getDates() {
        return dates;
    }

    public void paint(Graphics2D g, double xx, double yy, double ww, double hh) {
        int x = (int) xx;
        int y = (int) yy;
        int w = (int) ww;
        int h = (int) hh;
        dates.draw(x, y, w / 2, h, g,t.calBorder);
        photo.draw(x + w / 2, y, w / 2, h, g, t.imgBorder);


        String title = dates.getTitle();
        Rectangle2D rect1 = g.getFontMetrics().getStringBounds(title, g);
        int th = g.getFontMetrics().getHeight();
        int tw = (int) rect1.getWidth();
        g.setColor(new Color(255, 255, 225, 125));
        g.fillRect(w / 2 + w / 4 - tw / 2, y, tw, th);
        g.setColor(Color.black);
        g.drawString(title, w / 2 + w / 4 - tw / 2, y + th);

        String footer = photo.getFooter();
        Rectangle2D rect2 = g.getFontMetrics().getStringBounds(footer, g);
        int fh = g.getFontMetrics().getHeight();
        int fw = (int) rect2.getWidth();
        g.setColor(new Color(255, 255, 225, 125));
        g.fillRect(w / 2 + w / 4 - fw / 2, y + h - 2 * fh, fw, fh);
        g.setColor(Color.black);
        g.drawString(footer, w / 2 + w / 4 - fw / 2, y + h - fh);
    }
}

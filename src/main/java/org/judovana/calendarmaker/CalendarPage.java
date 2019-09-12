package org.judovana.calendarmaker;

import java.awt.*;
import java.awt.geom.Rectangle2D;
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
        dates.draw(ww*t.calX, hh*t.calY, ww*t.calWidth, hh*t.calHeight, g,t.calBorder);
        photo.draw(ww*t.imgX, hh*t.imgY, ww*t.imgWidth, hh*t.imgHeight, g,t.imgBorder);


        String title = dates.getTitle();
        Rectangle2D rect1 = g.getFontMetrics().getStringBounds(title, g);
        int th = g.getFontMetrics().getHeight();
        int tw = (int) rect1.getWidth();
        int rectX1=(int)(ww*t.monthX);
        int rectY1=(int)(hh*t.monthY);
        int rectW1=(int)(ww*t.monthWidht);
        int rectH1=(int)(hh*t.monthHeight);
        g.setColor(new Color(255, 255, 225, 125));
        g.fillRect(rectX1+rectW1/2-tw/2, rectY1, tw, th);
        g.setColor(Color.black);
        g.drawString(title, rectX1+rectW1/2-tw/2, rectY1 + th);

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

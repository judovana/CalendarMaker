package org.judovana.calendarmaker;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateRangeRenderrer {

    private final List<Date> range;

    public DateRangeRenderrer(List<Date> dates) {
        this.range = dates;
    }

    public static final SimpleDateFormat anotherMonth = new SimpleDateFormat("dd.MM yy");
    public static final SimpleDateFormat thisMonth = new SimpleDateFormat("EEE dd.MM");

    public static final SimpleDateFormat monthName = new SimpleDateFormat("MMMMMMMM yyyy");
    public static final SimpleDateFormat dayThis = new SimpleDateFormat("EEEEEEEE dd.MM");

    public void draw(int x, int y, int w, int h, Graphics2D g) {
        int border = 5;
        g.setColor(Color.black);
        if (range.size() == 7) {
            int step = (h - (2 * border)) / range.size();
            g.drawRect(x + border, y + border, w - 2 * border, h - 2 * border);
            for (int i = 0; i < range.size(); i++) {
                int hh = y + border + (i) * step;
                g.drawString(dayThis.format(range.get(i)), 2 * border, hh - (step - g.getFontMetrics().getHeight()));
                g.setColor(Color.black);
                if (i < range.size() - 1) {
                    Calendar c1 = new GregorianCalendar();
                    c1.setTime(range.get(i + 1));
                    Calendar c2 = new GregorianCalendar();
                    c2.setTime(range.get(i));
                    if (c1.get(Calendar.MONTH) != c2.get(Calendar.MONTH)) {
                        g.setColor(Color.red);
                    }
                }
                g.drawLine(border, hh, w - border, hh);
            }
        } else {
            int ww = (w - (2 * border)) / 7;
            int hh = (h - (2 * border)) / (range.size() / 6);
            Calendar c1 = new GregorianCalendar();
            c1.setTime(range.get(range.size() / 2));
            for (int i = 0; i < range.size(); i++) {
                Calendar c2 = new GregorianCalendar();
                c2.setTime(range.get(i));
                g.setColor(Color.black);
                if (c2.get(Calendar.MONTH) != c1.get(Calendar.MONTH)) {
                    g.setColor(new Color(0, 0, 0, 125));
                }
                int row = (i / 7);
                int inRow = (i % 7);
                g.drawRect(border + inRow * ww, y + border + row * hh, ww, hh);
                g.drawString(thisMonth.format(range.get(i)), border + inRow * ww, y + border + row * hh + g.getFontMetrics().getHeight());
            }
        }
    }

    public String getTitle() {
        if (range.size() == 7) {
            Calendar cS = new GregorianCalendar();
            cS.setTime(range.get(0));
            Calendar cE = new GregorianCalendar();
            cE.setTime(range.get(range.size() - 1));
            if (cS.get(Calendar.MONTH) == cE.get(Calendar.MONTH)) {
                Date midle = range.get(range.size() / 2);
                return monthName.format(midle);
            } else {

                return monthName.format(cS.getTime()) + " - " + monthName.format(cE.getTime());
            }
        } else {
            Date midle = range.get(range.size() / 2);
            return monthName.format(midle);
        }

    }
}

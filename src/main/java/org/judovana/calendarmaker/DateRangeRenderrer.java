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
    public static final SimpleDateFormat dayThis= new SimpleDateFormat("EEEEEEEE dd.MM");

    public void draw(int x, int y, int w, int h, Graphics2D g) {
        int border = 5;
        if (range.size() == 7) {
            int step = h / range.size();
            g.drawRect(x + border, y + border, w - 2 * border, h - 2 * border);
            for (int i = 0; i < range.size(); i++) {
                int hh = border + (i + 1) * step;
                g.drawString(dayThis.format(range.get(i)), 2 * border, y + hh - (step - g.getFontMetrics().getHeight()));
                g.drawLine(border, y + hh, w - border, y + hh);
            }
        } else {
            int ww = (w - (2 * border)) / 7;
            int hh = (h - (2 * border)) / (range.size() / 6);
            for (int i = 0; i < range.size(); i++) {
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
            if (cS.get(Calendar.MONTH) == cE.get(Calendar.MONTH)){
                Date midle = range.get(range.size() / 2);
                return monthName.format(midle);
            }else {

                return monthName.format(cS.getTime()) + " - " + monthName.format(cE.getTime());
            }
        } else {
            Date midle = range.get(range.size() / 2);
            return monthName.format(midle);
        }

    }
}

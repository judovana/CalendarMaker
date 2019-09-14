package org.judovana.calendarmaker;

import java.awt.*;
import java.awt.image.BufferedImage;
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

    public void draw(double x, double y, double w, double h, Graphics2D g, int border) {
        draw((int) x, (int) y, (int) w, (int) h, g, border);
    }

    public void draw(int x, int y, int w, int h, Graphics2D g, int border) {
        g.setColor(Color.black);
        if (range.size() == 7) {
            int step = (h - (2 * border)) / range.size();
            g.drawRect(x + border, y + border, w - 2 * border, h - 2 * border);
            //todo, x is likely used only above
            for (int i = 0; i < range.size(); i++) {
                Calendar c2 = new GregorianCalendar();
                c2.setTime(range.get(i));
                int hh = y + border + (i) * step;
                if (c2.get(Calendar.DAY_OF_MONTH) == 1) {
                    g.setColor(Color.red);
                } else {
                    g.setColor(Color.black);
                }
                g.drawString(dayThis.format(range.get(i)), 2 * border, hh - (step - g.getFontMetrics().getHeight()));
                g.drawLine(border, hh - step, w - border, hh - step);
                String s = NamesLoader.NAMES.getDaysMeaning(c2);
                if (s.startsWith("má svátek")) {
                    g.setColor(Color.black);
                    s = s.replace("má svátek", "").trim();
                } else {
                    g.setColor(Color.red);
                }
                if (NamesLoader.NAMES.isInterestin(s)) {
                    g.setColor(Color.blue);
                }
                g.drawString(s, 2 * border, hh - (step - 2 * g.getFontMetrics().getHeight()));

                Object[] event = (NamesLoader.NAMES.getDaysEvent(c2));
                String eS = (String) event[0];
                Color eC = (Color) event[1];
                if (event != null && eS.trim().length() > 0) {
                    if (eC == null) {
                        g.setColor(Color.blue);
                    } else {
                        g.setColor(eC);
                    }
                    g.drawString(eS, 2 * border, hh - (step - 3 * g.getFontMetrics().getHeight()));
                }
            }
        } else {
            int ww = (w - (2 * border)) / 7;
            int hh = (h - (2 * border)) / (range.size() / 6);
            Calendar c1 = new GregorianCalendar();
            c1.setTime(range.get(range.size() / 2));
            for (int i = 0; i < range.size(); i++) {
                Calendar c2 = new GregorianCalendar();
                c2.setTime(range.get(i));
                int alpha = 255;
                if (c2.get(Calendar.MONTH) != c1.get(Calendar.MONTH)) {
                    alpha = 125;
                }
                g.setColor(new Color(0, 0, 0, alpha));
                int row = (i / 7);
                int inRow = (i % 7);
                //todo, broken x. is not used here
                g.drawRect(border + inRow * ww, y + border + row * hh, ww, hh);
                g.drawString(thisMonth.format(range.get(i)), border + inRow * ww, y + border + row * hh + g.getFontMetrics().getHeight());

                String s = NamesLoader.NAMES.getDaysMeaning(c2);
                if (s.startsWith("má svátek")) {
                    g.setColor(new Color(0, 0, 0, alpha));
                    s = s.replace("má svátek", "").trim();
                } else {
                    g.setColor(new Color(255, 0, 0, alpha));
                }
                if (NamesLoader.NAMES.isInterestin(s)) {
                    g.setColor(new Color(0, 0, 255, alpha));
                }
                g.drawString(s, border + inRow * ww, y + border + row * hh + 2 * g.getFontMetrics().getHeight());

                Object[] event = (NamesLoader.NAMES.getDaysEvent(c2));
                String eS = (String) event[0];
                Color eC = (Color) event[1];
                if (event != null && eS.trim().length() > 0) {
                    if (eC == null) {
                        g.setColor(new Color(0, 0, 255, alpha));
                    } else {
                        g.setColor(new Color(eC.getRed(), eC.getGreen(), eC.getBlue(), alpha));
                    }
                    g.drawString(eS, border + inRow * ww, y + border + row * hh + 3 * g.getFontMetrics().getHeight());
                }
                BufferedImage bi = MoonPhaseRenderer.getMoonRectGauge(c2.get(Calendar.YEAR), c2.get(Calendar.MONTH) + 1, c2.get(Calendar.DAY_OF_MONTH), ww / 2, hh / 4, alpha);
                g.drawImage(bi, border + inRow * ww + ww / 2, y + border + row * hh + (2 * hh) / 3, null);

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

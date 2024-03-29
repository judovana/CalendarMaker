package org.judovana.calendarmaker;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateRangeRenderrer {

    private final List<Date> range;
    private static final boolean clipMoon = true;
    private final boolean drawHugeNumber;

    public DateRangeRenderrer(List<Date> dates, Boolean dh) {
        this.range = dates;
        if (dh == null){
            drawHugeNumber = false;
        } else {
            drawHugeNumber = dh;
        }
    }

    public static final SimpleDateFormat anotherMonth = new SimpleDateFormat("dd.MM yy");
    public static final SimpleDateFormat thisMonth = new SimpleDateFormat("EEE dd.MM");

    public static final SimpleDateFormat monthName = new SimpleDateFormat("MMMMMMMM yyyy");
    public static final SimpleDateFormat dayThis = new SimpleDateFormat("EEEEEEEE dd.MM");

    public void draw(double x, double y, double w, double h, Graphics2D g, Template.Border border) {
        draw((int) x, (int) y, (int) w, (int) h, g, border);
    }

    public Color defaultInterestingNameColor(int alpha) {
        return new Color(0,125,255, alpha);
    }

    public void draw(int x, int y, int w, int h, Graphics2D g, Template.Border border) {
        g.setFont(g.getFont().deriveFont(Font.BOLD));
        g.setColor(Color.black);
        if (range.size() == 7) {
            int step = (h - (border.tb())) / range.size();
            g.drawRect(x + border.l , y + border.t, w - (border.lr()), h - (border.t+border.b));
            //todo, x is likely used only above
            for (int i = 0; i < range.size(); i++) {
                Calendar c2 = new GregorianCalendar();
                c2.setTime(range.get(i));
                int hh = y + border.t + (i) * step;
                if (c2.get(Calendar.DAY_OF_MONTH) == 1) {
                    g.setColor(Color.red);
                } else {
                    g.setColor(Color.black);
                }
                g.drawString(dayThis.format(range.get(i)), x + (border.lr()), hh + (g.getFontMetrics().getHeight()));
                g.drawLine(x + border.l, hh, x + w - border.r, hh);
                String s = NamesLoader.NAMES.getDaysMeaning(c2);
                if (isName(s)) {
                    g.setColor(Color.black);
                    s = clearName(s);
                } else if (isHoliday(s)) {
                    g.setColor(Color.red);
                    s = clearHoliday(s);
                }
                if (NamesLoader.NAMES.isInterestin(s)) {
                    g.setColor(defaultInterestingNameColor(255));
                }
                g.drawString(s, x + border.lr(), hh + (2 * g.getFontMetrics().getHeight()));

                NamesLoader.Anniversary event = (NamesLoader.NAMES.getDaysEvent(c2));
                String eS = (String) event.text;
                Color eC = (Color) event.color;
                if (event != null && eS.trim().length() > 0) {
                    if (eC == null) {
                        g.setColor(Color.blue);
                    } else {
                        g.setColor(eC);
                    }
                    g.drawString(eS, x + border.lr(), hh + (3 * g.getFontMetrics().getHeight()));
                }
                BufferedImage bi = MoonPhaseRenderer.getMoonGauge(c2.get(Calendar.YEAR), c2.get(Calendar.MONTH) + 1, c2.get(Calendar.DAY_OF_MONTH), step, step, 255, clipMoon);
                g.drawImage(bi, x + w - step - border.lr(), hh, null);
                if (drawHugeNumber) {
                    //String bigDay = c2.get(Calendar.DAY_OF_MONTH)+"";
                    NumberFormat numberFormat = NumberFormat.getNumberInstance();
                    String bigDay = numberFormat.format(c2.get(Calendar.DAY_OF_MONTH));
                    Color exC = g.getColor();
                    if (exC.equals(Color.black)) {
                        exC = Color.GRAY;
                    }
                    g.setColor(new Color(exC.getRed(), exC.getGreen(), exC.getBlue(), 100));
                    Font niceOne = g.getFont();
                    int size = 0;
                    while (true) {
                        size++;
                        g.setFont(g.getFont().deriveFont((float) size));
                        if (g.getFontMetrics().getHeight() >= step) {
                            size--;
                            g.setFont(g.getFont().deriveFont(size));
                            break;
                        }
                    }
                    g.setFont(g.getFont().deriveFont(size));
                    g.drawString(bigDay, x + w - step - border.lr(), hh + (g.getFontMetrics().getHeight()) - g.getFontMetrics().getDescent());
                    g.setFont(niceOne);
                }
            }
            {
                Calendar c2 = new GregorianCalendar();
                c2.setTime(range.get(range.size() - 1));
                if (c2.getActualMaximum(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) {
                    g.setColor(Color.red);
                    int hh = y + border.t + (range.size()) * step;
                    if (!clipMoon) {
                        g.drawLine(x + border.l, hh, x + w - border .lr()- step/*width of moon*/, hh);
                    } else {
                        g.drawLine(x + border.l, hh, x + w - border.lr(), hh);
                    }
                }
            }
        } else {
            int ww = (w - (border.lr())) / 7;
            int hh = (h - (border.tb())) / (range.size() / 6);
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
                g.drawRect(x + border.l + inRow * ww, y + border.t + row * hh, ww, hh);
                g.drawString(thisMonth.format(range.get(i)), x + border.l + inRow * ww, y + border.t + row * hh + g.getFontMetrics().getHeight());

                String s = NamesLoader.NAMES.getDaysMeaning(c2);
                if (isName(s)) {
                    g.setColor(new Color(0, 0, 0, alpha));
                    s=clearName(s);
                } else if (isHoliday(s)){
                    g.setColor(new Color(255, 0, 0, alpha));
                    s=clearHoliday(s);
                }
                if (NamesLoader.NAMES.isInterestin(s)) {
                    g.setColor(defaultInterestingNameColor(alpha));
                }
                g.drawString(s, x + border.l + inRow * ww, y + border.t + row * hh + 2 * g.getFontMetrics().getHeight());

                NamesLoader.Anniversary event = (NamesLoader.NAMES.getDaysEvent(c2));
                String eS = (String) event.text;
                Color eC = (Color) event.color;
                if (event != null && eS.trim().length() > 0) {
                    if (eC == null) {
                        g.setColor(new Color(0, 0, 255, alpha));
                    } else {
                        g.setColor(new Color(eC.getRed(), eC.getGreen(), eC.getBlue(), alpha));
                    }
                    g.drawString(eS, x + border.l + inRow * ww, y + border.t + row * hh + 3 * g.getFontMetrics().getHeight());
                }
                BufferedImage bi = MoonPhaseRenderer.getMoonGauge(c2.get(Calendar.YEAR), c2.get(Calendar.MONTH) + 1, c2.get(Calendar.DAY_OF_MONTH), ww / 2, hh / 4, alpha, clipMoon);
                g.drawImage(bi, x + border.l + inRow * ww + ww / 2, y + border.t + row * hh + (2 * hh) / 3, null);
                if (drawHugeNumber) {
                    //String bigay = c2.get(Calendar.DAY_OF_MONTH) + "";
                    NumberFormat numberFormat = NumberFormat.getNumberInstance();
                    String bigDay = numberFormat.format(c2.get(Calendar.DAY_OF_MONTH));
                    Color exC = g.getColor();
                    if (exC.equals(Color.black)) {
                        exC = Color.GRAY;
                    }
                    g.setColor(new Color(exC.getRed(), exC.getGreen(), exC.getBlue(), 100));
                    Font niceOne = g.getFont();
                    int size = 0;
                    while (true) {
                        size++;
                        g.setFont(g.getFont().deriveFont((float) size));
                        Rectangle2D sizes = g.getFontMetrics().getStringBounds(bigDay, g);
                        if (sizes.getHeight() >= hh || sizes.getWidth() >= ww) {
                            size--;
                            g.setFont(g.getFont().deriveFont(size));
                            break;
                        }
                    }
                    g.setFont(g.getFont().deriveFont(size));
                    Rectangle2D sizes = g.getFontMetrics().getStringBounds(bigDay, g);
                    g.drawString(bigDay, (int) (x + border.l + inRow * ww + (ww - sizes.getWidth())), (int) (y + border.t + row * hh + sizes.getHeight()));
                    g.setFont(niceOne);
                }

            }
        }

    }

    private String clearName(String s) {
        return s.replace("* ", "").trim();
    }

    private boolean isName(String s) {
        return s.startsWith("* ");
    }

    private String clearHoliday(String s) {
        return s.replace("# ", "").trim();
    }

    private boolean isHoliday(String s) {
        return s.startsWith("# ");
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

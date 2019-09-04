package org.judovana.calendarmaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {

    public MainFrame() throws IOException {
        this.setSize(800, 600);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        PhotoLoader pl = new PhotoLoader();

        Calendar c1 = new GregorianCalendar();
        c1.setTime(new Date());
        Calendar c2 = new GregorianCalendar();
        c2.set(c1.get(Calendar.YEAR), 0, 1);
        rewindToStartofWeek(c2);
        List<PageView> pages = new ArrayList<>(60);

        List<Date> dates = new ArrayList<>(40);
        boolean week = false;
        if (week) {
            while (true) {
                Date theDay = c2.getTime();
                dates.add(theDay);
                c2.add(Calendar.DAY_OF_YEAR, 1);
                if (dates.size() >= 7) {
                    PageView pv = new PageView(new CalendarPage(dates, new PhotoFrame(pl.getRandomImage("/usr/share/backgrounds"))));
                    pages.add(pv);
                    dates = new ArrayList<>(40);
                }
                if (c2.get(Calendar.YEAR) > c1.get(Calendar.YEAR)) {
                    while (dates.size() < 7) {
                        Date theTheDay = c2.getTime();
                        dates.add(theTheDay);
                        c2.add(Calendar.DAY_OF_YEAR, 1);
                    }
                    PageView pv = new PageView(new CalendarPage(dates, new PhotoFrame(pl.getRandomImage("/usr/share/backgrounds"))));
                    pages.add(pv);
                    break;
                }
            }
        } else {
            int lastMonth=0;
            while (true) {
                Date theDay = c2.getTime();
                dates.add(theDay);
                c2.add(Calendar.DAY_OF_YEAR, 1);
                if (c2.get(Calendar.MONTH)>lastMonth) {
                    lastMonth++;
                    while (c2.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                        Date theTheDay = c2.getTime();
                        dates.add(theTheDay);
                        c2.add(Calendar.DAY_OF_YEAR, +1);
                    }
                    PageView pv = new PageView(new CalendarPage(dates, new PhotoFrame(pl.getRandomImage("/usr/share/backgrounds"))));
                    pages.add(pv);
                    dates = new ArrayList<>(40);
                    rewindToStartofWeek(c2);
                }
                if (c2.get(Calendar.YEAR) > c1.get(Calendar.YEAR)) {
                    while (c2.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                        Date theTheDay = c2.getTime();
                        dates.add(theTheDay);
                        c2.add(Calendar.DAY_OF_YEAR, +1);
                    }
                    PageView pv = new PageView(new CalendarPage(dates, new PhotoFrame(pl.getRandomImage("/usr/share/backgrounds"))));
                    pages.add(pv);
                    break;
                }
            }
        }


        final AllView all = new AllView(pages);
        this.add(all);
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
                    all.adjsutOffset(-MainFrame.this.getHeight() / 3);
                }
                if (keyEvent.getKeyCode() == KeyEvent.VK_PAGE_UP) {
                    all.adjsutOffset(+MainFrame.this.getHeight() / 3);
                }
                if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                    all.adjsutOffset(-1);
                }
                if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                    all.adjsutOffset(+1);
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
    }

    private void rewindToStartofWeek(Calendar cal) {
        //todo make first day of week adjsutable
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cal.add(Calendar.DAY_OF_YEAR, -1);
        }
    }


}

package org.judovana.calendarmaker;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {

    public MainFrame() throws IOException {
        this.setSize(800, 600);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        List<Date> dates = new ArrayList<>();

        for (int x = 0 ; x < 7; x++){
            dates.add(new Date(new Date().getTime()+24*60*60*1000*x));
        }

//        dates = new ArrayList<>();
//        for (int x = 0 ; x < 30; x++){
//            dates.add(new Date(new Date().getTime()+24*60*60*1000*x));
//        }
        PhotoLoader pl = new PhotoLoader();
        AllView all = new AllView(new PageView(new CalendarPage(dates, new PhotoFrame(pl.getRandomImage("/usr/share/backgrounds")))));
        this.add(all);
    }


}

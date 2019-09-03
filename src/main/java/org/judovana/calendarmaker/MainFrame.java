package org.judovana.calendarmaker;

import javax.swing.*;
import java.io.IOException;
import java.util.*;

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
        this.add(new PageView(new CalendarPage(dates, new PhotoFrame(new PhotoLoader().getRandomImage("/usr/share/backgrounds")))));
    }


}

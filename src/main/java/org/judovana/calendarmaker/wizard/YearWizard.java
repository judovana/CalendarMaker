package org.judovana.calendarmaker.wizard;

import org.judovana.calendarmaker.App;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

public class YearWizard {

    public static Component createYear(final App.Args year) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel("Year, calendar is for"), BorderLayout.CENTER);
        final JSpinner sp = new JSpinner(new SpinnerNumberModel(suggestYear(year.year), -2000, 20000, 1));
        p.add(sp, BorderLayout.SOUTH);
        sp.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                year.year = (Integer) sp.getValue();
            }
        });
        return p;
    }

    public static int suggestYear(Integer year) {
        if (year == null) {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());
            if (now.get(Calendar.MONTH) == 0) {
                return now.get(Calendar.YEAR);
            } else {
                return now.get(Calendar.YEAR) + 1;
            }
        } else {
            return year;
        }
    }
}

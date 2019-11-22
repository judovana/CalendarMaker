package org.judovana.calendarmaker;

import java.util.*;

public class RangeProvider {

    private final int yearOfChoice;
    private final boolean week;

    public RangeProvider(int i, Boolean week) {
        this.yearOfChoice = i;
        if (week == null){
            this.week = false;
        } else {
            this.week = week;
        }
    }

    public int getYearOfChoice() {
        return yearOfChoice;
    }

    public boolean isWeek() {
        return week;
    }


    public List<List<Date>> getRanges() {

        List<List<Date>> pages = new ArrayList<>(60);
        Calendar c2 = new GregorianCalendar();
        c2.set(yearOfChoice, 0, 1);
        rewindToStartofWeek(c2);

        List<Date> dates = new ArrayList<>(40);
        if (isWeek()) {
            while (true) {
                Date theDay = c2.getTime();
                dates.add(theDay);
                c2.add(Calendar.DAY_OF_YEAR, 1);
                if (dates.size() >= 7) {
                    pages.add(dates);
                    dates = new ArrayList<>(40);
                }
                if (c2.get(Calendar.YEAR) > yearOfChoice) {
                    while (dates.size() < 7) {
                        Date theTheDay = c2.getTime();
                        dates.add(theTheDay);
                        c2.add(Calendar.DAY_OF_YEAR, 1);
                    }
                    pages.add(dates);
                    break;
                }
            }
        } else {
            //we are on monday
            //c2.add(Calendar.DAY_OF_YEAR, -1);
            //so we canadd in loop
            int lastMonth = 0;
            while (true) {
                Date theDay = c2.getTime();
                dates.add(theDay);
                c2.add(Calendar.DAY_OF_YEAR, 1);
                if (c2.get(Calendar.MONTH) > lastMonth && (c2.get(Calendar.MONTH) != 11 || lastMonth > 0)) {
                    lastMonth++;
                    while (c2.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                        Date theTheDay = c2.getTime();
                        dates.add(theTheDay);
                        c2.add(Calendar.DAY_OF_YEAR, +1);
                    }
                    pages.add(dates);
                    dates = new ArrayList<>(40);
                    rewindToStartofWeek(c2);
                }
                if (c2.get(Calendar.YEAR) > yearOfChoice) {
                    while (c2.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                        Date theTheDay = c2.getTime();
                        dates.add(theTheDay);
                        c2.add(Calendar.DAY_OF_YEAR, +1);
                    }
                    pages.add(dates);
                    break;
                }
            }
        }

        return pages;
    }

    private void rewindToStartofWeek(Calendar cal) {
        //todo make first day of week adjsutable
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cal.add(Calendar.DAY_OF_YEAR, -1);
        }
    }

}

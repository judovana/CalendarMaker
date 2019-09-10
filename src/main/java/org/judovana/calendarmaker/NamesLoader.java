package org.judovana.calendarmaker;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class NamesLoader {

    public static final NamesLoader NAMES = new NamesLoader();

    private Map<String, String> all;
    private List<String> names;
    private Map<String, MyInterestingDay> dates;


    public String getDaysMeaning(Calendar date) {
        try {
            return getDaysMeaningImp(date);
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    public Object[] getDaysEvent(Calendar date) {
        try {
            return getDaysEventImpl(date);
        } catch (IOException e) {
            e.printStackTrace();
            return new Object[]{"", null};
        }
    }

    private String getDaysMeaningImp(Calendar date) throws IOException {
        if (all == null) {
            all = loadAllNames("org/judovana/calendarmaker/data/czDb");
        }

        String name = all.get(date.get(Calendar.DAY_OF_MONTH) + "." + (date.get(Calendar.MONTH) + 1) + ".");
        if (name == null) {
            return "";
        } else {
            return name;
        }
    }

    private Object[] getDaysEventImpl(Calendar date) throws IOException {
        if (dates == null) {
            dates = loadCustomDates("org/judovana/calendarmaker/data/InterestingDates");
        }
        MyInterestingDay thisDay = dates.get(date.get(Calendar.DAY_OF_MONTH) + "." + (date.get(Calendar.MONTH) + 1) + ".");
        if (thisDay == null) {
            return new Object[]{"", null};
        } else {
            String s;
            if (thisDay.haveYear) {
                int thisYear = date.get(Calendar.YEAR);
                int thatYear = thisDay.c.get(Calendar.YEAR);
                s = thisDay.text + " (" + (thisYear - thatYear) + ")";
            } else {
                s = thisDay.text;
            }
            if (thisDay.color == null) {
                return new Object[]{s, null};
            } else {
                return new Object[]{s, thisDay.color};
            }
        }
    }

    private static Map<String, String> loadAllNames(String resource) throws IOException {
        Map<String, String> all = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(NamesLoader.class.getClassLoader().getResourceAsStream(resource)))) {
            while (true) {
                String s = br.readLine();
                if (s == null) {
                    break;
                }
                if (s != null && s.trim().length() > 0) {
                    String start = s.charAt(0) + "";
                    try {
                        Integer.valueOf(start);
                        String[] ss = s.split("\t+");
                        all.put(ss[0], ss[1]);
                    } catch (NumberFormatException ex) {
                        //comment
                    }
                }
            }
        }
        return all;
    }

    private static Map<String, MyInterestingDay> loadCustomDates(String resource) throws IOException {
        Map<String, MyInterestingDay> all = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(NamesLoader.class.getClassLoader().getResourceAsStream(resource)))) {
            while (true) {
                String s = br.readLine();
                if (s == null) {
                    break;
                }
                if (s.startsWith("#")) {
                    continue;
                }
                if (s != null && s.trim().length() > 0) {
                    try {
                        MyInterestingDay md = new MyInterestingDay(s);
                        all.put(md.getKey(), md);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        return all;
    }

    private static List<String> loadMyNames(String resource) throws IOException {
        List<String> all = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(NamesLoader.class.getClassLoader().getResourceAsStream(resource)))) {
            while (true) {
                String s = br.readLine();
                if (s == null) {
                    break;
                }
                if (s != null && s.trim().length() > 0) {
                    all.add(s);
                }
            }
        }
        return all;
    }

    public boolean isInterestin(String s) {
        try {
            return isInterestingImpl(s);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isInterestingImpl(String s) throws IOException {
        if (names == null) {
            names = loadMyNames("org/judovana/calendarmaker/data/InterestingNames");
        }
        return names.contains(s);
    }

    private static class MyInterestingDay {

        private static final SimpleDateFormat main = new SimpleDateFormat("dd.MM.yyyy");
        private static final SimpleDateFormat secondary = new SimpleDateFormat("dd.MM");

        private Date date;
        private Calendar c;
        private String text;
        private Color color;
        private boolean haveYear;

        public MyInterestingDay(String s) throws ParseException {
            String[] ss = s.split("\t+");
            try {
                date = main.parse(ss[0]);
                haveYear = true;
            } catch (ParseException ex) {
                haveYear = false;
                date = secondary.parse(ss[0]);
            }
            c = new GregorianCalendar();
            c.setTime(date);
            text = ss[1];
            if (ss.length > 2) {
                color = Color.decode(ss[2]);
            }

        }

        String getKey() {
            return c.get(Calendar.DAY_OF_MONTH) + "." + (c.get(Calendar.MONTH) + 1) + ".";
        }
    }
}

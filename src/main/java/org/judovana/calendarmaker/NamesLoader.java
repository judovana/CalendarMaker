package org.judovana.calendarmaker;

import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class NamesLoader {

    public static NamesLoader NAMES;
    private final String allSrc;
    private final String namesSrc;
    private final String datesSrc;

    private Map<String, String> all;
    private List<String> names;
    private Map<String, MyInterestingDay> dates;

    public NamesLoader(String names, String interesting, String anniversaries) {
        this.allSrc = names;
        this.namesSrc = interesting;
        this.datesSrc = anniversaries;
    }

    private static boolean useInternal(String s) {
        //likely to treat null in featuere as some default file in .config
        if (s == null || s.toUpperCase().equals("EXAMPLE")) {
            return true;
        } else {
            return false;
        }
    }


    public String getDaysMeaning(Calendar date) {
        try {
            return getDaysMeaningImp(date);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public Anniversary getDaysEvent(Calendar date) {
        try {
            return getDaysEventImpl(date);
        } catch (IOException e) {
            e.printStackTrace();
            return new Anniversary("", null);
        }
    }

    private String getDaysMeaningImp(Calendar date) throws IOException {
        if (all == null) {
            if (useInternal(allSrc)) {
                all = loadAllNames(getExemplatNameList());
            } else {
                all = loadAllNames(new FileInputStream(allSrc));
            }
        }

        String name = all.get(date.get(Calendar.DAY_OF_MONTH) + "." + (date.get(Calendar.MONTH) + 1) + ".");
        if (name == null) {
            return "";
        } else {
            return name;
        }
    }

        private Anniversary getDaysEventImpl(Calendar date) throws IOException {
        if (dates == null) {
            if (useInternal(datesSrc)) {
                dates = loadCustomDates(getExemplarAnniversaries());
            } else {
                dates = loadCustomDates(new FileInputStream(datesSrc));
            }
        }
        MyInterestingDay thisDay = dates.get(date.get(Calendar.DAY_OF_MONTH) + "." + (date.get(Calendar.MONTH) + 1) + ".");
        if (thisDay == null) {
            return new Anniversary("", null);
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
                return new Anniversary(s, null);
            } else {
                return new Anniversary(s, thisDay.color);
            }
        }
    }

    private static Map<String, String> loadAllNames(InputStream resource) throws IOException {
        Map<String, String> all = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource))) {
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

    private static Map<String, MyInterestingDay> loadCustomDates(InputStream resource) throws IOException {
        Map<String, MyInterestingDay> all = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource))) {
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

    private static List<String> loadMyNames(InputStream resource) throws IOException {
        List<String> all = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource))) {
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
            if (useInternal(namesSrc)) {
                names = loadMyNames(getInterestingNamesExampleStream());
            } else {
                names = loadMyNames(new FileInputStream((namesSrc)));
            }
        }
        return names.contains(s);
    }

    private InputStream getInterestingNamesExampleStream() {
        return getExemplarStream("org/judovana/calendarmaker/data/InterestingNames");
    }

    private InputStream getExemplarAnniversaries() {
        return getExemplarStream("org/judovana/calendarmaker/data/InterestingDates");
    }

    private InputStream getExemplatNameList() {
        return getExemplarStream("org/judovana/calendarmaker/data/czDb");
    }


    private InputStream getExemplarStream(String s) {
        return NamesLoader.class.getClassLoader().getResourceAsStream(s);
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

    public static class Anniversary {
        public final String text;
        public final Color color;

        public Anniversary(String text, Color color) {
            this.text = text;
            this.color = color;
        }
    }
}

package org.judovana.calendarmaker;

import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class NamesLoader {

    public static final String EXAMPLE = "EXAMPLE";
    public static final String NAMES_EXAMPLE = "org/judovana/calendarmaker/data/czDb";
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

    public static boolean useInternal(String s) {
        if (s.toUpperCase().equals(EXAMPLE)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean useNothing(String s) {
        if (s == null) {
            return true;
        }
        if (useInternal(s)){
            return false;
        }
        if (s.trim().isEmpty() || !new File(s).exists()) {
            if (!new File(s).exists()) {
                new FileNotFoundException(s).printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }

    public static InputStream getStream(String s, String internalDefault) {
        if (useNothing(s)) {
            return new ByteArrayInputStream(new byte[0]);
        } else if (useInternal(s)) {
            return getExemplarStream(internalDefault);
        } else {
            try {
                return new FileInputStream(s);
            }catch (Exception ex){
                //already handled in useNothing/internal
                ex.printStackTrace();
                return new ByteArrayInputStream(new byte[0]);
            }
        }
    }

    public static InputStream getExemplarStream(String s) {
        return NamesLoader.class.getClassLoader().getResourceAsStream(s);
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
            all = loadAllNames(getStream(allSrc, NAMES_EXAMPLE));
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
            dates = loadCustomDates(getStream(datesSrc, "org/judovana/calendarmaker/data/InterestingDates"));
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
            names = loadMyNames(getStream(namesSrc, "org/judovana/calendarmaker/data/InterestingNames"));
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

    public static class Anniversary {
        public final String text;
        public final Color color;

        public Anniversary(String text, Color color) {
            this.text = text;
            this.color = color;
        }
    }
}

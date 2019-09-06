package org.judovana.calendarmaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class NamesLoader {

    public static final NamesLoader NAMES = new NamesLoader();

    private Map<String, String> all;
    private List<String> names;
    private Map<String, String> dates;


    public String getDaysMeaning(Calendar date) {
        try {
            return getDaysMeaningImp(date);
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    public String getDaysEvent(Calendar date) {
        try {
            return getDaysEventImpl(date);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
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

    private String getDaysEventImpl(Calendar date) throws IOException {
        if (dates == null) {
            dates = loadAllNames("org/judovana/calendarmaker/data/InterestingDates");
        }
        String name = dates.get(date.get(Calendar.DAY_OF_MONTH) + "." + (date.get(Calendar.MONTH) + 1) + ".");
        if (name == null) {
            return "";
        } else {
            return name;
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
}

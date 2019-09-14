package org.judovana.calendarmaker;

public class MoonPhases {

    public static void main(String... args) {
        int y = 2019;
        int m = 8;
        for (int d = 1; d <= 31; d++) {
            System.out.println(d + "." + m + "." + y);
            System.out.println("id " + getMoonPhase(y, m, d));
            System.out.println("d  " + getDarkPercent(y, m, d));
            System.out.println("w  " + getWhitePercent(y, m, d));
            System.out.println("   " + isDarkFirstInLeft(y, m, d));
        }
    }

    /**
     * 0	XX Nov	Měsíc je v novu (novoluní)
     * 7.5	XO První čtvrt	Měsíc je v první čtvrti
     * 15	OO Úplněk	Měsíc je v úplňku
     * 22.5	OX Poslední čtvrt	Měsíc je v poslední čtvrti
     *
     * @param year  year:)
     * @param month 1-12
     * @param day   1-xx
     * @return 0-29?
     */
    private static int getMoonPhase(int year, int month, int day) {
        int base = getMoonBase(year);
        int phase = (base + month + day) % 30;
        return phase;
    }

    //
//0   : ############################
//    : #####################0000000
//7.5 : ##############00000000000000
//    : #######000000000000000000000
//15  : 0000000000000000000000000000
//    : 000000000000000000000#######
//22.5: 00000000000000##############
//    : 0000000#####################
//0   : ############################
//    : #####################0000000
//7.5 : ##############00000000000000
//    : #######000000000000000000000
//15  : 0000000000000000000000000000

    /*
    0=>0
    1 0.1
    2 0.2
    3 0.24
    4 0.26
    5 0.3
    6 0.4
    7=>0.5
    8=>0.5
    9 0.6
    10 0.7
    11 0.74
    12 0.76
    13 0.8
    14 0.9
    15=>1
    16 0.9
    17 0.8
    180.76
    19 0.74
    20 0.7
    12 0.6
    22=0.5
    23=0.5
    24 0.4
    25 0.3
    26 0.25
    27 0.2
    28 0.1
    29=0
     */
    public static double getWhite(int year, int month, int day) {
        int mp = getMoonPhase(year, month, day);
        if (!isDarkFirstInLeft(mp)) {
            return (double) ((mp % 15)) / 15d;
        } else {
            return 1d - ((double) ((mp % 15)) / 15d);
        }
    }

    public static boolean isDarkFirstInLeft(int year, int month, int day) {
        return isDarkFirstInLeft(getDarkPercent(year, month, day));

    }

    private static boolean isDarkFirstInLeft(int moonPhase) {
        return !(moonPhase < 15);
    }

    public static double getDark(int year, int month, int day) {
        return 1d - getWhite(year, month, day);
    }

    public static int getWhitePercent(int year, int month, int day) {
        return (int) (getWhite(year, month, day) * 100d);
    }

    public static int getDarkPercent(int year, int month, int day) {
        return (int) (getDark(year, month, day) * 100d);
    }

    private static int getMoonBase(int year) {
        int base = ((year - 5) % 19);
        switch (base) {
            case (0):
                return 23;
            case (1):
                return 4;
            case (2):
                return 15;
            case (3):
                return 26;
            case (4):
                return 7;
            case (5):
                return 18;
            case (6):
                return -1;
            case (7):
                return 10;
            case (8):
                return 21;
            case (9):
                return 2;
            case (10):
                return 13;
            case (11):
                return 24;
            case (12):
                return 5;
            case (13):
                return 16;
            case (14):
                return 28;
            case (15):
                return 9;
            case (16):
                return 20;
            case (17):
                return 1;
            case (18):
                return 12;
            default:
                throw new RuntimeException("Unreachable moon case");
        }
    }
}

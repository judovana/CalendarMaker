package org.judovana.calendarmaker;

public class MoonPhases {

    public static void main(String... args) {
        System.out.println("" + +getMoonPhase(2019, 11, 26));

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
//0   : ############################
//    : #####################0000000
//7.5 : ##############00000000000000
//    : #######000000000000000000000
//15  : 0000000000000000000000000000
//    : 000000000000000000000#######
//22.5: 00000000000000##############
//    : 0000000#####################

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

package org.judovana.calendarmaker;

public abstract class Template {
    private int calBorder = 5;
    private int imgBorder = 5;
    private double calWidth;
    private double calHeight;
    private double imgWidth;
    private double imgHeight;
    private double calX;
    private double calY;
    private double imgX;
    private double imgY;
    private String type = MONTH;
    private double monthWidht;
    private int monthHeight = 15;
    private double monthX;
    private double monthY;//on lower half of screen needs adjust by height (or on upper)
    private double labelWidht;
    private int labelHeight = 10;
    private double labelX;
    private double labelY;//on lower half of screen needs adjust by height (or on upper)

    private static final String MONTH = "month";
    private static final String WEEK = "week";

    public class Horizontal extends Template {
        public Horizontal() {
            calWidth = 0.5;
            calHeight = 1;
            imgWidth = 0.5;
            imgHeight = 1;
            calX = 0;
            calY = 0;
            imgX = 0.5;
            imgY = 0;
            monthWidht = 0.5;
            monthX = 0.5;
            monthY = 0;
            labelWidht = 0.5;
            labelX = 0.5;
            labelY = 0.5;
        }
    }

    public class Vertical extends Template {
        public Vertical() {
            calWidth = 1;
            calHeight = 0.5;
            imgWidth = 1;
            imgHeight = 0.5;
            calX = 0;
            calY = 0;
            imgX = 0;
            imgY = 0.5;
            monthWidht = 1;
            monthX = 0;
            monthY = 0.5;
            labelWidht = 1;
            labelX = 0;
            labelY = 1;
        }
    }


}

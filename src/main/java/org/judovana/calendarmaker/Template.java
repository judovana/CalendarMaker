package org.judovana.calendarmaker;

public abstract class Template {
    public int calBorder = 5;
    public int imgBorder = 5;
    public double calWidth;
    public double calHeight;
    public double imgWidth;
    public double imgHeight;
    public double calX;
    public double calY;
    public double imgX;
    public double imgY;
    public String type = MONTH;
    public double monthWidht;
    public int monthHeight = 15;
    public double monthX;
    public double monthY;//on lower half of screen needs adjust by height (or on upper)
    public double labelWidht;
    public int labelHeight = 10;
    public double labelX;
    public double labelY;//on lower half of screen needs adjust by height (or on upper)
    //font
    //formaters...
    //name...
    //aligns...
    //moon

    private static final String MONTH = "month";
    private static final String WEEK = "week";

    public static class HorizontalImageRight extends Template {
        public HorizontalImageRight() {
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
            labelY = 0.95;
        }
    }

    public static class HorizontalImageLeft extends Template {
        public HorizontalImageLeft() {
            calWidth = 0.5;
            calHeight = 1;
            imgWidth = 0.5;
            imgHeight = 1;
            calX = 0.5;
            calY = 0;
            imgX = 0;
            imgY = 0;
            monthWidht = 0.5;
            monthX = 0;
            monthY = 0;
            labelWidht = 0.5;
            labelX = 0;
            labelY = 0.95;
        }
    }

    public static class VerticalImageDown extends Template {
        public VerticalImageDown() {
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
            labelY = 0.95;
        }
    }

    public static class VerticalImageUp extends Template {
        public VerticalImageUp() {
            calWidth = 1;
            calHeight = 0.5;
            imgWidth = 1;
            imgHeight = 0.5;
            calX = 0;
            calY = 0.5;
            imgX = 0;
            imgY = 0;
            monthWidht = 1;
            monthX = 0;
            monthY = 0;
            labelWidht = 1;
            labelX = 0;
            labelY = 0.45;
        }
    }


}

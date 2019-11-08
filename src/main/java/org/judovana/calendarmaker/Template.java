package org.judovana.calendarmaker;

public abstract class Template {

    public static class Border{
        public final int t;
        public final int r;
        public final int b;
        public final int l;

        public Border(int t, int r, int b, int l) {
            this.t = t;
            this.r = r;
            this.b = b;
            this.l = l;
        }

        public int lr(){
            return r+l;
        }

        public int tb(){
            return t+b;
        }
    }

    public Border calBorder = new Border(45,5,5,5);
    public int imgBorder = 5;
    public double calWidth;
    public double calHeight;
    public double imgWidth;
    public double imgHeight;
    public double calX;
    public double calY;
    public double imgX;
    public double imgY;
    public double monthWidht;
    public int monthHeight = 15;
    public double titleX;
    public double titleY;//on lower half of screen needs adjust by height (or on upper)
    public double labelWidht;
    public int labelHeight = 10;
    public double footerX;
    public double footerY;//on lower half of screen needs adjust by height (or on upper)
    //font
    //formaters...
    //name...
    //aligns...
    //moon

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
            titleX = 0.5;
            titleY = 0.05;
            labelWidht = 0.5;
            footerX = 0.5;
            footerY = 0.95;
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
            titleX = 0;
            titleY = 0.05;
            labelWidht = 0.5;
            footerX = 0;
            footerY = 0.95;
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
            titleX = 0;
            titleY = 0.5;
            labelWidht = 1;
            footerX = 0;
            footerY = 0.95;
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
            titleX = 0;
            titleY = 0.05;
            labelWidht = 1;
            footerX = 0;
            footerY = 0.45;
        }
    }


}

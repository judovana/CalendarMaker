package org.judovana.calendarmaker;

public abstract class Rummable {

    String rum;

    public abstract void rum() throws Exception;

    public void setRum(String s) {
        rum = s;
    }

    public String getRum() {
        return rum;

    }
}

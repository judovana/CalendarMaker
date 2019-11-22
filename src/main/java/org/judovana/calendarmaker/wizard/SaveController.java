package org.judovana.calendarmaker.wizard;


import org.judovana.calendarmaker.App;

import java.io.IOException;

public interface SaveController {

    public boolean isSave();
    public void setArgs(App.Args arg);
    public void save() throws IOException;
}

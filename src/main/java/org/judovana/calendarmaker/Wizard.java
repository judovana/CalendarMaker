package org.judovana.calendarmaker;

import javax.swing.*;
import java.awt.*;

public class Wizard extends JDialog {

    private final App.Args args;

    public Wizard(App.Args args) throws HeadlessException {
        this.setSize(600,200);
        this.args = args;
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setModal(true);
        JTabbedPane panes = new JTabbedPane();
        JPanel year = new JPanel();
        JPanel mOrW= new JPanel();
        JPanel templates= new JPanel();
        JPanel photoDirs= new JPanel();
        JPanel names= new JPanel(); //dont forget internal!
        JPanel myNames= new JPanel(); //dont forget internal!
        JPanel datesAndAniversaries= new JPanel(); //dont forget internal!
        JPanel load= new JPanel();
        JPanel misc= new JPanel();//w,h explain printing
        //save to some defaults?
        panes.add(year);
        panes.add(mOrW);
        panes.add(templates);
        panes.add(photoDirs);
        panes.add(names);
        panes.add(myNames);
        panes.add(datesAndAniversaries);
        panes.add(load);
        panes.add(misc);
        panes.setTitleAt(0, "Year");
        panes.setTitleAt(1, "Type");
        panes.setTitleAt(2, "Templates");
        panes.setTitleAt(3, "Directories with photos");
        panes.setTitleAt(4, "File with meaings of days (holidays, name days");
        panes.setTitleAt(5, "Names important to you");
        panes.setTitleAt(6, "Anniversaries and interesting days");
        panes.setTitleAt(7, "Load saved work");
        panes.setTitleAt(8, "Misc");
        this.add(panes);
    }
}

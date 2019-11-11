package org.judovana.calendarmaker;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;

public class Wizard extends JDialog {

    private class WizardPanel extends JPanel {

        private final JTabbedPane parent;
        private final int id;

        public WizardPanel(JTabbedPane pparent, int iid) {
            this.parent = pparent;
            this.id = iid;
            this.setLayout(new BorderLayout());
            JPanel mainButtons = new JPanel(new GridLayout(1, 2));
            JPanel buttons = new JPanel(new GridLayout(1, 3));
            JButton prev = new JButton("<< previous");
            JButton done = new JButton(" finish ");
            JButton next = new JButton(" next >>");
            buttons.add(prev);
            buttons.add(done);
            buttons.add(next);
            mainButtons.add(new JPanel());
            mainButtons.add(buttons);

            if (id <= 0) {
                prev.setEnabled(false);
            }
            if (id >= parent.getTabCount() - 1) {
                next.setEnabled(false);
            }

            next.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    parent.setSelectedIndex(id + 1);
                }
            });
            prev.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    parent.setSelectedIndex(id - 1);
                }
            });
            done.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Wizard.this.dispatchEvent(new WindowEvent(Wizard.this, WindowEvent.WINDOW_CLOSING));
                }
            });
            this.add(mainButtons, BorderLayout.SOUTH);
        }

        public WizardPanel  setMainPane(Component c){
            this.add(c);
            return this;
        }

    }

    private final App.Args args;

    public Wizard(App.Args args) throws HeadlessException {
        Dimension d = ScreenFinder.getCurrentScreenSizeWithoutBounds().getSize();
        int w = (4 * d.width) / 5;
        int h = 300;
        this.setSize(w, h);
        this.setLocationRelativeTo(null);

        this.args = args;
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setModal(true);
        JTabbedPane panes = new JTabbedPane();
        JPanel year = new JPanel(new BorderLayout());
        JPanel mOrW = new JPanel(new BorderLayout());
        JPanel templates = new JPanel(new BorderLayout());
        JPanel photoDirs = new JPanel(new BorderLayout());
        JPanel names = new JPanel(new BorderLayout()); //dont forget internal!
        JPanel myNames = new JPanel(new BorderLayout()); //dont forget internal!
        JPanel datesAndAniversaries = new JPanel(new BorderLayout()); //dont forget internal!
        JPanel load = new JPanel(new BorderLayout());
        JPanel misc = new JPanel(new BorderLayout());//w,h explain printing, save?
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

        year.add(new WizardPanel(panes, 0).setMainPane(createYear(args.year)));
        mOrW.add(new WizardPanel(panes, 1));
        templates.add(new WizardPanel(panes, 2));
        photoDirs.add(new WizardPanel(panes, 3));
        names.add(new WizardPanel(panes, 4));
        myNames.add(new WizardPanel(panes, 5));
        datesAndAniversaries.add(new WizardPanel(panes, 6));
        load.add(new WizardPanel(panes, 7));
        misc.add(new WizardPanel(panes, 8));
    }

    private Component createYear(Integer year) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel("Year, calendar is for"), BorderLayout.CENTER);
        final JSpinner sp = new JSpinner(new SpinnerNumberModel(suggestYear(year), -2000, 20000, 1));
        p.add(sp, BorderLayout.SOUTH);
        sp.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                args.year=(Integer)sp.getValue();
            }
        });
        return p;
    }

    public static int suggestYear(Integer year){
        if (year == null) {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());
            if (now.get(Calendar.MONTH) == 0) {
                return now.get(Calendar.YEAR);
            } else {
                return now.get(Calendar.YEAR) + 1;
            }
        } else {
            return year;
        }
    }
}

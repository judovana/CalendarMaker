package org.judovana.calendarmaker;

import org.judovana.calendarmaker.wizard.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

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

        public WizardPanel setMainPane(Component c) {
            this.add(c);
            return this;
        }

    }

    private final App.Args args;

    /**
     * IMportant implementation detail is, that although gui shows some details, it do not affect users values, unless he especially edit tat
     * It is imrotant, as the result of wizard is by default is by default saved. We do not wont to save some defaults, but oly user's values
     *
     * @param args
     * @throws HeadlessException
     */
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

        year.add(new WizardPanel(panes, 0).setMainPane(YearWizard.createYear(args)));
        mOrW.add(new WizardPanel(panes, 1).setMainPane(TypeWizard.createWeekMonth(args)));
        templates.add(new WizardPanel(panes, 2).setMainPane(TemplateWizard.createTemplate(args)));
        photoDirs.add(new WizardPanel(panes, 3).setMainPane(DirsWizard.createPhotoDirs(args)));
        names.add(new WizardPanel(panes, 4).setMainPane(FilesWizard.createNames(args)));
        myNames.add(new WizardPanel(panes, 5).setMainPane(FilesWizard.createImportantNames(args)));
        datesAndAniversaries.add(new WizardPanel(panes, 6).setMainPane(FilesWizard.createAnniversaries(args)));
        load.add(new WizardPanel(panes, 7));
        misc.add(new WizardPanel(panes, 8));
    }


    public static String[] filePreview(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
                stream.forEach(s -> contentBuilder.append(s).append("\n"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new String[]{"error reading '" + filePath + "'. File missing?", null};
        }
        return new String[]{contentBuilder.toString(),"ok"};
    }



    public static String[] inputStreamToString(InputStream inputStream) {
        try {
            try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                return new String[]{result.toString("UTF-8"),"ok"};
            }
        } catch(Exception ex){
            return new String[]{"Error reading example",null};
        }
    }
}

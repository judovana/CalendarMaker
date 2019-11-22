package org.judovana.calendarmaker;

import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static class Args {
        public boolean week = false;
        public Integer year = null;
        public List<String> dirs = new ArrayList<>();
        public String template;
        public String toLoad;
        public Integer w, h;
        public String names, anniversaries, interesting;

    }
    private static boolean showWizard = true;

    public static void main(final String[] args) throws IOException {
        //preload args from default file?
        //override by cmdline
        final Args a = new Args();
        for (String arg : args) {
            if (arg.matches("^-+type=.+$")) {
                if (arg.split("=")[1].toUpperCase().equals("WEEK")) {
                    a.week = true;
                }
            }
            if (arg.matches("^-+template=.+$")) {
                a.template = arg.split("=")[1];
            }
            if (arg.matches("^-+year=.+$")) {
                a.year = Integer.valueOf(arg.split("=")[1]);
            }
            if (arg.matches("^-+dir=.+$")) {
                a.dirs.add(arg.split("=")[1]);
            }
            if (arg.matches("^-+load=.+$")) {
                a.toLoad = arg.split("=")[1];
            }
            if (arg.matches("^-+names=.+$")) {
                a.names = (arg.split("=")[1]); //EXAMPLE, DEFAULT?
            }
            if (arg.matches("^-+super-names=.+$") || arg.matches("^-+supernames=.+$")) {
                a.interesting = (arg.split("=")[1]); //EXAMPLE, DEFAULT?
            }
            if (arg.matches("^-+dates=.+$") || arg.matches("^-+anniversaries=.+$")) {
                a.anniversaries = (arg.split("=")[1]); //EXAMPLE, DEFAULT?
            }
            if (arg.matches("^-+nowizard$") || arg.matches("^-+no-wizard$")) {
                showWizard = false;
            }
            if (arg.matches("^-+save-wall=.+$")) {
                //arg.split("=")[1]);
            }
            if (arg.matches("^-+save-table-1=.+$")) {
                //arg.split("=")[1]);
            }
            if (arg.matches("^-+save-table-2=.+$")) {
                //arg.split("=")[1]);
            }
            if (arg.matches("^-+width=.+$")) {
                a.w = Integer.valueOf(arg.split("=")[1]);
            }
            if (arg.matches("^-+height=.+$")) {
                a.h = Integer.valueOf(arg.split("=")[1]);
            }
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    if (showWizard) {
                        new Wizard(a).setVisible(true);
                        //what to skip in no wizard?
                        //no defaults switch?
                    }
                    new MainFrame(a.week, a.year, a.dirs, a.template, a.toLoad, a.w, a.h, a.names, a.interesting, a.anniversaries).setVisible(true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}

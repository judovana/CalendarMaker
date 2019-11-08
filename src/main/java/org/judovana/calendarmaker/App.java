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
    private static class Args {
        boolean week = false;
        Integer year = null;
        List<String> dirs = new ArrayList<>();
        String template;
        private List<String> loaded;

        public void load(String s) throws IOException {
            this.loaded = Files.readAllLines(new File(s).toPath(), Charset.forName("utf-8"));
        }
    }

    public static void main(String[] args) throws IOException {
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
                a.load(arg.split("=")[1]);
            }
            if (arg.matches("^-+names=.+$")) {
                //(arg.split("=")[1]); EXAMPLE
            }
            if (arg.matches("^-+super-names=.+$") || arg.matches("^-+supernames=.+$")) {
                //(arg.split("=")[1]); EXAMPLE
            }
            if (arg.matches("^-+dates=.+$") || arg.matches("^-+anniversaries=.+$")) {
                //(arg.split("=")[1]); EXAMPLE
            }
            if (arg.matches("^-+nowizard$") || arg.matches("^-+no-wizard$")) {
                //no op now, no param
            }
            if (arg.matches("^-+save=.+$")) {
                //arg.split("=")[1]);
            }
            if (arg.matches("^-+width=.+$")) {
                //arg.split("=")[1]);
            }
            if (arg.matches("^-+heigh=.+$")) {
                //arg.split("=")[1]);
            }
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new MainFrame(a.week, a.year, a.dirs, a.template, a.loaded).setVisible(true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}

package org.judovana.calendarmaker;

import org.judovana.calendarmaker.wizard.FilesWizard;

import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class App {
    private static boolean headless;

    public static class Args {

        //saved
        public Boolean week;
        public Set<String> dirs = new HashSet<>();
        public String template;
        public Integer w, h;
        public String names, anniversaries, interesting;

        //not saved
        public String toLoad;
        public Integer year = null;
        private String save_wall;
        private String save_table1;
        private String save_table2;


        public void parse(String... args) {
            for (String arg : args) {
                if (arg.matches("^-+type=.+$")) {
                    if (arg.split("=")[1].toUpperCase().equals("WEEK")) {
                        this.week = true;
                    }
                    if (arg.split("=")[1].toUpperCase().equals("MONTH")) {
                        this.week = false; //default anyway, however not null anymore
                    }
                }
                if (arg.matches("^-+template=.+$")) {
                    this.template = arg.split("=")[1];
                }
                if (arg.matches("^-+year=.+$")) {
                    this.year = Integer.valueOf(arg.split("=")[1]);
                }
                if (arg.matches("^-+dir=.+$")) {
                    this.dirs.add(arg.split("=")[1]);
                }
                if (arg.matches("^-+load=.+$")) {
                    this.toLoad = arg.split("=")[1];
                }
                if (arg.matches("^-+names=.+$")) {
                    this.names = (arg.split("=")[1]); //EXAMPLE, DEFAULT?
                }
                if (arg.matches("^-+super-names=.+$") || arg.matches("^-+supernames=.+$")) {
                    this.interesting = (arg.split("=")[1]); //EXAMPLE, DEFAULT?
                }
                if (arg.matches("^-+dates=.+$") || arg.matches("^-+anniversaries=.+$")) {
                    this.anniversaries = (arg.split("=")[1]); //EXAMPLE, DEFAULT?
                }
                if (arg.matches("^-+nowizard$") || arg.matches("^-+no-wizard$")) {
                    showWizard = false;
                }
                if (arg.matches("^-+save-wall=.+$")) {
                    this.save_wall = arg.split("=")[1];
                }
                if (arg.matches("^-+save-table-1=.+$")) {
                    this.save_table1 = arg.split("=")[1];
                }
                if (arg.matches("^-+save-table-2=.+$")) {
                    this.save_table2 = arg.split("=")[1];
                }
                if (arg.matches("^-+width=.+$")) {
                    this.w = Integer.valueOf(arg.split("=")[1]);
                }
                if (arg.matches("^-+height=.+$")) {
                    this.h = Integer.valueOf(arg.split("=")[1]);
                }
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (week != null) {
                if (week) {
                    sb.append("-type=WEEK\n");
                } else {
                    sb.append("-type=MONTH\n");
                }
            }
            for (String dir : dirs) {
                if (dir != null && !dir.trim().isEmpty()) {
                    sb.append("-dir=" + dir + "\n");
                }
            }
            if (template != null) {
                sb.append("-template=" + template + "\n");
            }
            if (w != null) {
                sb.append("-width=" + w + "\n");
            }
            if (h != null) {
                sb.append("-height=" + h + "\n");
            }
            if (names != null) {
                sb.append("-names=" + names + "\n");
            }
            if (anniversaries != null) {
                sb.append("-dates=" + anniversaries + "\n");
            }
            if (interesting != null) {
                sb.append("-supernames=" + interesting + "\n");
            }
            return sb.toString();
        }

        public void save() throws IOException {
            FilesWizard.getMainConfig().getParentFile().mkdirs();
            Files.write(FilesWizard.getMainConfig().toPath(), this.toString().getBytes("utf-8"));
        }

        public void load() throws IOException {
            if (FilesWizard.getMainConfig().exists()) {
                List<String> r = Files.readAllLines(FilesWizard.getMainConfig().toPath(), Charset.forName("utf-8"));
                parse(r.toArray(new String[0]));
            }
        }
    }

    private static boolean showWizard = true;
    private static boolean load = true;


    public static boolean getHeadless(){
        return headless || GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadless();
    }

    public static void main(final String[] args) throws IOException {
        final Args a = new Args();
        for (String arg : args) {
            if (arg.matches("^-+no-load$") || arg.matches("^-+noload$")) {
                load = false;
            }
            if (arg.matches("^-+h$") || arg.matches("^-+help$")) {
                printHelp();
                System.exit(0);
            }
        }
        if (load) {
            try {
                a.load();
            } catch (Exception ex) {
                ex.printStackTrace();
                if (GraphicsEnvironment.isHeadless()) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        }
        a.parse(args);
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    if (showWizard) {
                        new Wizard(a).setVisible(true);
                    }
                    MainFrame main = new MainFrame(a.week, a.year, a.dirs, a.template, a.toLoad, a.w, a.h, a.names, a.interesting, a.anniversaries);
                    boolean gui = true;
                    if (a.save_wall != null) {
                        gui = false;
                        main.exportOnePageOnePage_month(a.save_wall);
                    }
                    if (a.save_table1 != null) {
                        gui = false;
                        main.exportOnePageOnePage_weekSingleSide(a.save_table1);
                    }
                    if (a.save_table2 != null) {
                        gui = false;
                        main.exportOnePageOnePage_weekDoubleSide(a.save_table2);
                    }
                    if (gui) {
                        main.show();
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {

                }
            });
        }catch (Error | Exception ex){
            headless = true;
        }

        if (getHeadless()){
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }
    }

    private static void printHelp() {
        System.out.println(getCmdHelp());
        System.out.println(getDescription());
    }

    static String getCmdHelp() {
        return "" +
                "-type= WEEK or MONTH\n" +
                "  To set the type of calendar\n"+
                "  Default is month\n"+
                "-template= one of HR HL VD VU\n" +
                "  To set vertical or horizontal layout with image on right/left ot down/up\n"+
                "  Default is HR\n"+
                "-year= number" +
                "  Year of calendar.\n"+
                "  Default is  next year for every day use, except january, when it is this year\n"+
                "-dir= path\n" +
                "  one directory to load pictures from\n"+
                "  The only option you can specify more times\n"+
                "  Each dir will be used\n"+
                "-load= path\n" +
                "  If you saved your work, you can load thar file by this switch\n"+
                "-names= path\n" +
                "  path to file with ames and holidays\n"+
                "-super-names= -supernames= path\n" +
                "  path to file with names to highlight\n"+
                "-dates= or-anniversaries= path\n" +
                "  apth to file with important dates or anniversaries\n"+
                "-nowizard or -+no-wizard\n" +
                "  the gui wizard will be skipped\n"+
                "-save-wall= path\n" +
                "  no gui will start, and loaded work will be saved to specified file as calendar for wall\n"+
                "-save-table-1= path\n" +
                "  no gui will start, and loaded work will be saved to specified file as calendar to stay on table, for single side printing\n"+
                "-save-table-2= path\n" +
                "  no gui will start, and loaded work will be saved to specified file as calendar to stay on table, for double side printing\n"+
                "-width= number\n" +
                "  width of single frame in pixels\n" +
                "-height\n="+
                "  heightof single frame in pixels\n" +
                "-no-load or -noload\n"+
                "  defaults saved by wizard will not be loaded\n"+
                "Set java to -Djava.awt.headless=true if you are running with invalid display veriable (unset is ok)\n"+
                "-h or -help\n"+
                "  help";
    }

    static String getDescription() {
        return ""+
                "This is Nearly What You Se is What you Get\n"+
                "The page will indeed look as yuu see it, however will be squezed to the page\n"+
                "You will easily manage to fit it as you wish by changing size of window or playing with width/height swithces\n"+
                "If you are creating the table calendar, do not cut the construction paper\n";
        }
}

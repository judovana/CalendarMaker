package org.judovana.calendarmaker;

import org.judovana.calendarmaker.wizard.YearWizard;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

public class MainFrame  {
    private static File lastDir = new File(System.getProperty("user.home"));

    public static String[] photoFolders =
            new String[]{"/usr/share/backgrounds", "/usr/share/icons/"};

    final AllView all;
    private JFrame frame;


    public MainFrame(Boolean week, Integer year, Collection<String> dirs, String template,
            String toLoad, Integer w, Integer h, String names, String interesting,
            String anniversaries, Boolean dh) throws IOException {
        int wii, hee;
        if (w == null || w == 0) {
            wii = 800;
        } else {
            wii = w;
        }
        if (h == null || h == 0) {
            hee = 600;
        } else {
            hee = h;
        }
        NamesLoader.NAMES = new NamesLoader(names, interesting, anniversaries);
        final PhotoLoader pl = createDefaultLoader(dirs);
        final RangeProvider rp = getYearOfCal(week, year);
        final List<List<Date>> ranges = rp.getRanges();
        final Template tmplt;
        if ("HR".equalsIgnoreCase(template)) {
            tmplt = new Template.HorizontalImageRight();
        } else if ("HL".equalsIgnoreCase(template)) {
            tmplt = new Template.HorizontalImageLeft();
        } else if ("VD".equalsIgnoreCase(template)) {
            tmplt = new Template.VerticalImageDown();
        } else if ("VU".equalsIgnoreCase(template)) {
            tmplt = new Template.VerticalImageUp();
        } else {
            tmplt = new Template.HorizontalImageRight();
        }
        List<PageView> pages = new ArrayList<>(ranges.size());
        List<String> loaded = null;
        if (toLoad != null) {
            File toad = new File(toLoad);
            try {
                loaded = Files.readAllLines(toad.toPath(), Charset.forName("utf-8"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
        if (loaded == null) {
            for (List<Date> range : ranges) {
                CalendarPage cp = new CalendarPage(range, new PhotoFrame(pl.getRandomImage()),
                        tmplt, pl.getSrcs(), dh);
                PageView p = new PageView(cp);
                pages.add(p);
            }
        } else {
            int i = 0;
            for (List<Date> range : ranges) {
                PhotoFrame pf;
                if (i < loaded.size()) {
                    String[] imgRotate = loaded.get(i).split("\\|");
                    pf = new PhotoFrame(imgRotate[0]);
                    pf.setRotate(imgRotate[1]);
                } else {
                    pf = new PhotoFrame(pl.getRandomImage());
                    System.err.println("Warning! Exceeded range of loaded list! Filling by random");
                }
                CalendarPage cp = new CalendarPage(range, pf, tmplt, pl.getSrcs(), dh);
                PageView p = new PageView(cp);
                pages.add(p);
                i++;
            }
        }
        all = new AllView(pages);
        if (App.getHeadless()) {
            all.setSize(new Dimension(wii, hee));
        } else {
            fun(wii, hee, pl, rp);
            all.setSize(frame.getSize());//for headless mode
        }
    }
    public void fun(int wii, int hee, PhotoLoader pl, RangeProvider rp){
        this.frame = new JFrame();
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                System.out.println("-width=" + frame.getWidth() + " -height="
                        + frame.getHeight());
            }
        });
        frame.setSize(wii, hee);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                final PageView page = all.get(frame.getWidth() / 2,
                        frame.getHeight() / 2);
                if (e.getKeyCode() == KeyEvent.VK_Z) {
                    undo(all);
                }
                if (e.getKeyCode() == KeyEvent.VK_Y) {
                    redo(all);
                }
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    randomzieOne(page, pl, all);
                }
                if (e.getKeyCode() == KeyEvent.VK_F) {
                    randomizeSameFolder(page, all);
                }
                if (e.getKeyCode() == KeyEvent.VK_N) {
                    selectNext(page, pl, all);
                }
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    selectPrev(page, pl, all);
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    select(page, all);
                }

                if (e.getKeyCode() == KeyEvent.VK_U) {
                    all.moveUp(page);
                    all.repaint();

                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    all.moveDown(page);
                    all.repaint();

                }
                if (e.getKeyCode() == KeyEvent.VK_9 || e.getKeyCode() == KeyEvent.VK_NUMPAD9) {
                    rotateClock(page, all);
                }
                if (e.getKeyCode() == KeyEvent.VK_7 || e.getKeyCode() == KeyEvent.VK_NUMPAD7) {
                    rotateAntiClock(page, all);
                }
                if (e.getKeyCode() == KeyEvent.VK_8 || e.getKeyCode() == KeyEvent.VK_NUMPAD8) {
                    rotate180(page, all);
                }
            }
        });
        all.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                final PageView page = all.get(e.getX(), e.getY());
                final String s;
                if (page != null) {
                    s = page.getData().getDates().getTitle() + " ||| "
                            + page.getData().getPhoto().getFotoTitle(null);
                } else {
                    s = "???";
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    JPopupMenu menu = new JPopupMenu();
                    JMenuItem title = new JMenuItem(s);
                    title.setEnabled(false);
                    menu.add(title);
                    JMenuItem prev = new JMenuItem("P - previous");
                    menu.add(prev);
                    JMenuItem next = new JMenuItem("N - next");
                    menu.add(next);
                    JMenuItem sel = new JMenuItem("S - select");
                    menu.add(sel); //from its curerent photodir!
                    JMenuItem rnd = new JMenuItem("R - random again");
                    menu.add(rnd);
                    JMenuItem rndLocal = new JMenuItem("F - random from this folder");
                    menu.add(rndLocal);
                    JMenuItem rot1 = new JMenuItem("9 - rotate 90 clock");
                    menu.add(rot1);
                    JMenuItem rot2 = new JMenuItem("7 - rotate 90 anti-clock");
                    menu.add(rot2);
                    JMenuItem rot3 = new JMenuItem("8 - rotate 180");
                    menu.add(rot3);
                    JMenuItem more = new JMenuItem("------- more -------");
                    more.setEnabled(false);
                    menu.add(more);
                    JMenuItem up = new JMenuItem("U - up");
                    menu.add(up);
                    JMenuItem down = new JMenuItem("D - down");
                    menu.add(down);
                    JMenuItem undo = new JMenuItem("Z - undo");
                    menu.add(undo);
                    JMenuItem redo = new JMenuItem("Y - redo");
                    menu.add(redo);
                    JMenuItem footer = new JMenu("------- I/O-------");
                    menu.add(footer);
                    JMenuItem export1 = new JMenuItem("print pdf WALL");
                    footer.add(export1);
                    JMenuItem export2 = new JMenuItem("print pdf TABLE - single side");
                    footer.add(export2);
                    JMenuItem export3 = new JMenuItem("print pdf TABLE - two sided page");
                    footer.add(export3);
                    JMenuItem save = new JMenuItem("save");
                    footer.add(save);
                    JMenuItem load = new JMenuItem("load");
                    footer.add(load);

                    undo.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            undo(all);
                        }
                    });

                    rndLocal.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            randomizeSameFolder(page, all);

                        }
                    });

                    up.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            all.moveUp(page);
                            all.repaint();

                        }
                    });
                    down.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            all.moveDown(page);
                            all.repaint();

                        }
                    });


                    save.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            try {
                                JFileChooser jf = new JFileChooser(lastDir);
                                String sf = "calendar" + rp.getYearOfChoice() + "-month.save";
                                if (rp.isWeek()) {
                                    sf = "calendar" + rp.getYearOfChoice() + "-week.save";
                                }
                                jf.setSelectedFile(new File(sf));
                                if (jf.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                                    File f = jf.getSelectedFile();
                                    if (f != null) {
                                        lastDir = f.getParentFile();
                                        final String ex = f.getAbsolutePath();
                                        checkExists(f, new Rummable() {
                                            @Override
                                            public void rum() throws Exception {
                                                all.save(ex);
                                            }
                                        });
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(null, ex);
                            }
                        }
                    });

                    load.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            try {
                                JFileChooser jf = new JFileChooser(lastDir);
                                String sf = "calendar" + rp.getYearOfChoice() + "-month.save";
                                if (rp.isWeek()) {
                                    sf = "calendar" + rp.getYearOfChoice() + "-week.save";
                                }
                                jf.setSelectedFile(new File(sf));
                                if (jf.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                                    File f = jf.getSelectedFile();
                                    if (f != null) {
                                        lastDir = f.getParentFile();
                                        String ex = f.getAbsolutePath();
                                        all.load(ex);
                                        all.repaint();
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(null, ex);
                            }
                        }
                    });

                    export1.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            exportDialog(rp.getYearOfChoice(), "-wall", ".pdf", new Rummable() {
                                @Override
                                public void rum() throws Exception {
                                    exportOnePageOnePage_month(this.getRum());
                                }
                            });
                        }
                    });
                    export2.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            exportDialog(rp.getYearOfChoice(), "-single_side-table", ".pdf",
                                    new Rummable() {
                                        @Override
                                        public void rum() throws Exception {
                                            exportOnePageOnePage_weekSingleSide(this.getRum());
                                        }
                                    });
                        }
                    });
                    export3.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            exportDialog(rp.getYearOfChoice(), "-two_side-table", ".pdf",
                                    new Rummable() {
                                        @Override
                                        public void rum() throws Exception {
                                            exportOnePageOnePage_weekDoubleSide(this.getRum());
                                        }
                                    });
                        }
                    });

                    rot1.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            rotateClock(page, all);
                        }
                    });
                    rot2.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            rotateAntiClock(page, all);
                        }
                    });
                    rot3.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            rotate180(page, all);
                        }
                    });

                    sel.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            select(page, all);
                        }
                    });

                    rnd.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            randomzieOne(page, pl, all);
                        }
                    });
                    prev.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            selectPrev(page, pl, all);
                        }
                    });

                    next.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            selectNext(page, pl, all);
                        }
                    });
                    menu.show(all.getView(), e.getX(), e.getY());
                }
            }
        });
        frame.add(all.getView());
        frame.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                all.adjsutOffset(-e.getUnitsToScroll() * 20);
            }
        });
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
                    all.adjsutOffset(-frame.getHeight() / 3);
                }
                if (keyEvent.getKeyCode() == KeyEvent.VK_PAGE_UP) {
                    all.adjsutOffset(+frame.getHeight() / 3);
                }
                if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                    all.adjsutOffset(+1);
                }
                if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                    all.adjsutOffset(-1);
                }
                if (keyEvent.getKeyCode() == KeyEvent.VK_HOME) {
                    all.resetOffset();
                }
                if (keyEvent.getKeyCode() == KeyEvent.VK_END) {
                    all.upsetOffset();
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
    }

    public void exportOnePageOnePage_month(String path) throws IOException {
        all.exportOnePageOnePage_month(path);
    }

    public void exportOnePageOnePage_weekDoubleSide(String path) throws IOException {
        all.exportOnePageOnePage_weekDoubleSide(path);
    }

    public void exportOnePageOnePage_weekSingleSide(String path) throws IOException {
        all.exportOnePageOnePage_weekSingleSide(path);
    }

    private PhotoLoader createDefaultLoader(Collection<String> dirs) throws IOException {
        if (dirs == null || dirs.isEmpty()) {
            return new PhotoLoader(photoFolders);
        } else {
            return new PhotoLoader(dirs);
        }
    }

    private void undo(AllView all) {
        try {
            all.undo();
            all.repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void redo(AllView all) {
        try {
            all.redo();
            all.repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void rotate180(PageView page, AllView all) {
        page.getData().getPhoto().rotateImg180();
        all.repaint();
        all.addHistory();
    }

    private void rotateClock(PageView page, AllView all) {
        page.getData().getPhoto().rotateImgClock();
        all.repaint();
        all.addHistory();
    }

    private void rotateAntiClock(PageView page, AllView all) {
        page.getData().getPhoto().rotateImgAntiClock();
        all.repaint();
        all.addHistory();
    }

    private void select(PageView page, AllView all) {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(
                    new File(page.getData().getPhoto().getSrc()).getParentFile());
            chooser.setSelectedFile(new File(page.getData().getPhoto().getSrc()));
            int r = chooser.showOpenDialog(all.getView());
            if (r == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                if (f != null) {
                    all.reData(page, f.getAbsolutePath());
                    all.repaint();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(all.getView(), ex);
            ex.printStackTrace();
        }
    }

    private void selectPrev(PageView page, PhotoLoader pl, AllView all) {
        try {
            all.reData(page, pl.getPrev(page.getData().getPhoto().getSrc()));
            all.repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(all.getView(), ex);
            ex.printStackTrace();
        }
    }

    private void selectNext(PageView page, PhotoLoader pl, AllView all) {
        try {
            all.reData(page, pl.getNext(page.getData().getPhoto().getSrc()));
            all.repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(all.getView(), ex);
            ex.printStackTrace();
        }
    }

    private void randomizeSameFolder(PageView page, AllView all) {
        try {
            all.reData(page, new PhotoLoader(
                    new File(page.getData().getPhoto().getSrc()).getParent()).getRandomImage());
            all.repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void randomzieOne(PageView page, PhotoLoader pl, AllView all) {
        try {
            all.reData(page, pl.getRandomImage());
            all.repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(all.getView(), ex);
            ex.printStackTrace();
        }
    }

    private RangeProvider getYearOfCal(Boolean week, Integer year) {
        return new RangeProvider(YearWizard.suggestYear(year), week);

    }

    public static void checkExists(File f, Rummable r) throws Exception {
        if (f.exists()) {
            int a = JOptionPane.showConfirmDialog(null, f.getName() + " exists, overwrite?");
            if (a == JOptionPane.OK_OPTION || a == JOptionPane.YES_OPTION) {
                r.rum();
                System.out.println("overwritten " + f.getAbsolutePath());
            }
        } else {
            r.rum();
            System.out.println("saved " + f.getAbsolutePath());
        }
    }

    public static void exportDialog(int year, String suffixNice, String suffixFile, Rummable r) {
        try {
            JFileChooser jf = new JFileChooser(lastDir);
            jf.setSelectedFile(new File("calendar-" + year + suffixNice + suffixFile));
            if (jf.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File f = jf.getSelectedFile();
                if (f != null) {
                    lastDir = f.getParentFile();
                    final String ex;
                    if (!f.getAbsolutePath().endsWith(suffixFile)) {
                        ex = f.getAbsolutePath() + suffixNice + suffixFile;
                    } else {
                        ex = f.getAbsolutePath();
                    }
                    r.setRum(ex);
                    checkExists(f, r);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex);
        }
    }


    public void show() {
        frame.setVisible(true);
    }
}

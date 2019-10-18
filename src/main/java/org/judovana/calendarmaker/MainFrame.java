package org.judovana.calendarmaker;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {
    private static File lastDir = new File(System.getProperty("user.home"));

    public static String[] photoFolders = new String[]{"/home/jvanek/tripshare/Context/Data/Fotky", "/usr/share/backgrounds", "/usr/share/icons/"};

    public MainFrame(boolean  week, Integer year) throws IOException {
        this.setSize(800, 600);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        final PhotoLoader pl = new PhotoLoader(photoFolders);
        final RangeProvider rp = getYearOfCal(week, year);
        final List<List<Date>> ranges = rp.getRanges();
        List<PageView> pages = new ArrayList<>(ranges.size());
        for (List<Date> range : ranges) {
            CalendarPage cp = new CalendarPage(range, new PhotoFrame(pl.getRandomImage()));
            PageView p = new PageView(cp);
            pages.add(p);
        }

        final AllView all = new AllView(pages);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                final PageView page = all.get(MainFrame.this.getWidth() / 2, MainFrame.this.getHeight() / 2);
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
                    s = page.getData().getDates().getTitle() + " ||| " + page.getData().getPhoto().getFooter();
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
                    JMenuItem footer = new JMenuItem("------- global -------");
                    footer.setEnabled(false);
                    menu.add(footer);
                    JMenuItem export1 = new JMenuItem("print pdf WALL");
                    menu.add(export1);
                    JMenuItem export2 = new JMenuItem("print pdf TABLE - single side");
                    menu.add(export2);
                    JMenuItem export3 = new JMenuItem("print pdf TABLE - two sided page");
                    menu.add(export3);
                    JMenuItem save = new JMenuItem("save");
                    menu.add(save);
                    JMenuItem load = new JMenuItem("load");
                    menu.add(load);

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
                                String sf = "calendar"+rp.getYearOfChoice()+"-month.save";
                                if (rp.isWeek()){
                                    sf = "calendar"+rp.getYearOfChoice()+"-week.save";
                                }
                                jf.setSelectedFile(new File(sf));
                                if (jf.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                                    File f = jf.getSelectedFile();
                                    if (f != null) {
                                        lastDir = f.getParentFile();
                                        String ex = f.getAbsolutePath();
                                        all.save(ex);
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
                                String sf = "calendar"+rp.getYearOfChoice()+"-month.save";
                                if (rp.isWeek()){
                                    sf = "calendar"+rp.getYearOfChoice()+"-week.save";
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
                            try {
                                JFileChooser jf = new JFileChooser(lastDir);
                                jf.setSelectedFile(new File("calendar-" + rp.getYearOfChoice() + "-wall.pdf"));
                                if (jf.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                                    File f = jf.getSelectedFile();
                                    if (f != null) {
                                        lastDir = f.getParentFile();
                                        String ex = f.getAbsolutePath();
                                        if (!ex.endsWith(".pdf")) {
                                            ex = ex + "-wall.pdf";
                                        }
                                        all.exportOnePageOnePage_month(ex);
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(null, ex);
                            }
                        }
                    });
                    export2.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            try {
                                JFileChooser jf = new JFileChooser(lastDir);
                                jf.setSelectedFile(new File("calendar-" + rp.getYearOfChoice() + "-single_side-table.pdf"));
                                if (jf.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                                    File f = jf.getSelectedFile();
                                    if (f != null) {
                                        lastDir = f.getParentFile();
                                        String ex = f.getAbsolutePath();
                                        if (!ex.endsWith(".pdf")) {
                                            ex = ex + "single_side-table.pdf";
                                        }
                                        all.exportOnePageOnePage_weekSingleSide(ex);
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(null, ex);
                            }
                        }
                    });
                    export3.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            try {
                                JFileChooser jf = new JFileChooser(lastDir);
                                jf.setSelectedFile(new File("calendar-" + rp.getYearOfChoice() + "-two_side-table.pdf"));
                                if (jf.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                                    File f = jf.getSelectedFile();
                                    if (f != null) {
                                        lastDir = f.getParentFile();
                                        String ex = f.getAbsolutePath();
                                        if (!ex.endsWith(".pdf")) {
                                            ex = ex + "-two_sidetable.pdf";
                                        }
                                        throw  new RuntimeException(ex+" not yet supported");
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(null, ex);
                            }
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
                    menu.show(all, e.getX(), e.getY());
                }
            }
        });
        this.add(all);
        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                all.adjsutOffset(-e.getUnitsToScroll() * 20);
            }
        });
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
                    all.adjsutOffset(-MainFrame.this.getHeight() / 3);
                }
                if (keyEvent.getKeyCode() == KeyEvent.VK_PAGE_UP) {
                    all.adjsutOffset(+MainFrame.this.getHeight() / 3);
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
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
    }

    private void undo(AllView all) {
        try{
            all.undo();
            all.repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void redo(AllView all) {
        try{
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
            chooser.setCurrentDirectory(new File(page.getData().getPhoto().getSrc()).getParentFile());
            chooser.setSelectedFile(new File(page.getData().getPhoto().getSrc()));
            int r = chooser.showOpenDialog(all);
            if (r == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                if (f != null) {
                    all.reData(page, f.getAbsolutePath());
                    all.repaint();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(all, ex);
            ex.printStackTrace();
        }
    }

    private void selectPrev(PageView page, PhotoLoader pl, AllView all) {
        try {
            all.reData(page, pl.getPrev(page.getData().getPhoto().getSrc()));
            all.repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(all, ex);
            ex.printStackTrace();
        }
    }

    private void selectNext(PageView page, PhotoLoader pl, AllView all) {
        try {
            all.reData(page, pl.getNext(page.getData().getPhoto().getSrc()));
            all.repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(all, ex);
            ex.printStackTrace();
        }
    }

    private void randomizeSameFolder(PageView page, AllView all) {
        try {
            all.reData(page, new PhotoLoader(new File(page.getData().getPhoto().getSrc()).getParent()).getRandomImage());
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
            JOptionPane.showMessageDialog(all, ex);
            ex.printStackTrace();
        }
    }

    private RangeProvider getYearOfCal(boolean week, Integer year) {
        if (year == null) {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());
            if (now.get(Calendar.MONTH)==0) {
                return new RangeProvider(now.get(Calendar.YEAR), week);
            }else {
                return new RangeProvider(now.get(Calendar.YEAR) + 1, week);
            }
        } else {
            return new RangeProvider(year, week);
        }
    }


}

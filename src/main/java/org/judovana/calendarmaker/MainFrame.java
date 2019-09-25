package org.judovana.calendarmaker;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {

    public static String[] photoFolders = new String[]{"/home/jvanek/tripshare/Context/Data/Fotky", "/usr/share/backgrounds"};

    public MainFrame() throws IOException {
        this.setSize(800, 600);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        final PhotoLoader pl = new PhotoLoader(photoFolders);

        final RangeProvider rp = new RangeProvider(2020, false);
        final List<List<Date>> ranges = rp.getRanges();
        List<PageView> pages = new ArrayList<>(ranges.size());
        for (List<Date> range : ranges) {
            CalendarPage cp = new CalendarPage(range, new PhotoFrame(pl.getRandomImage()));
            PageView p = new PageView(cp);
            pages.add(p);
        }

        final AllView all = new AllView(pages);
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
                    JMenuItem sel = new JMenuItem("select");
                    menu.add(sel); //from its curerent photodir!
                    JMenuItem prev = new JMenuItem("previous");
                    menu.add(prev);
                    JMenuItem next = new JMenuItem("next");
                    menu.add(next);
                    JMenuItem rnd = new JMenuItem("random again");
                    menu.add(rnd);
                    JMenuItem rot1 = new JMenuItem("rotate 90 clock");
                    menu.add(rot1);
                    JMenuItem rot2 = new JMenuItem("rotate 90 anti-clock");
                    menu.add(rot2);
                    JMenuItem rot3 = new JMenuItem("rotate 180");
                    menu.add(rot3);
                    JMenuItem export1 = new JMenuItem("print pdf WALL");
                    menu.add(export1);
                    JMenuItem export2 = new JMenuItem("print pdf TABLE");
                    menu.add(export2);

                    export1.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            try {
                                all.exportOnePageOnePage_month(System.getProperty("user.home") + "/Desktop/calendar-" + rp.getYearOfChoice() + "-wall.pdf");
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
                                all.exportOnePageOnePage_week(System.getProperty("user.home") + "/Desktop/calendar-" + rp.getYearOfChoice() + "-table.pdf");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(null, ex);
                            }
                        }
                    });

                    rot1.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            page.getData().getPhoto().rotateImgClock();
                            all.repaint();
                        }
                    });
                    rot2.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            page.getData().getPhoto().rotateImgAntiClock();
                            all.repaint();
                        }
                    });
                    rot3.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            page.getData().getPhoto().rotateImg180();
                            all.repaint();
                        }
                    });

                    sel.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                JFileChooser chooser = new JFileChooser();
                                chooser.setCurrentDirectory(new File(page.getData().getPhoto().getSrc()).getParentFile());
                                chooser.setSelectedFile(new File(page.getData().getPhoto().getSrc()));
                                int r = chooser.showOpenDialog(all);
                                if (r == JFileChooser.APPROVE_OPTION) {
                                    File f = chooser.getSelectedFile();
                                    if (f != null) {
                                        page.getData().getPhoto().setData(f.getAbsolutePath());
                                        all.repaint();
                                    }
                                }
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(all, ex);
                                ex.printStackTrace();
                            }
                        }
                    });

                    rnd.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                page.getData().getPhoto().setData(pl.getRandomImage());
                                all.repaint();
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(all, ex);
                                ex.printStackTrace();
                            }
                        }
                    });
                    prev.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                page.getData().getPhoto().setData(pl.getPrev(page.getData().getPhoto().getSrc()));
                                all.repaint();
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(all, ex);
                                ex.printStackTrace();
                            }
                        }
                    });

                    next.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                page.getData().getPhoto().setData(pl.getNext(page.getData().getPhoto().getSrc()));
                                all.repaint();
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(all, ex);
                                ex.printStackTrace();
                            }
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


}

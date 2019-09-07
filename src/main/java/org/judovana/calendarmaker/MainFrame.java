package org.judovana.calendarmaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {

    public static String[] photoFolders = new String[]{"/home/jvanek/tripshare/Context/Data/Fotky", "/usr/share/backgrounds"};

    public MainFrame() throws IOException {
        this.setSize(800, 600);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        PhotoLoader pl = new PhotoLoader(photoFolders);

        RangeProvider rp = new RangeProvider(2019, false);
        List<List<Date>> ranges = rp.getRanges();
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
                if (e.getButton() == MouseEvent.BUTTON3) {
                    JPopupMenu menu = new JPopupMenu();
                    menu.add(new JMenuItem("Select"));
                    menu.add(new JMenuItem("previous"));
                    menu.add(new JMenuItem("next"));
                    menu.add(new JMenuItem("random again"));
                    menu.add(new JMenuItem("rotate"));
                    menu.show(all, e.getX(), e.getY());
                }
            }
        });
        this.add(all);
        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                all.adjsutOffset(e.getUnitsToScroll() * 20);
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

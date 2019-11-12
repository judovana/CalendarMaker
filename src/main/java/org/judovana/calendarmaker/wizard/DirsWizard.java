package org.judovana.calendarmaker.wizard;

import org.judovana.calendarmaker.App;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DirsWizard {

    public static Component createPhotoDirs(App.Args args) {
        final JPanel pp = new JPanel(new BorderLayout());
        pp.add(new JLabel("Directories where photos/images you wish to use are to be searched for (recursively)"), BorderLayout.NORTH);
        pp.add(new PhotoDirsCotroller(args), BorderLayout.CENTER);
        return pp;
    }


    private static class PhotoDirsCotroller extends JPanel {


        private class DirPane extends JPanel {
            boolean mod = false;

            public boolean isMod() {
                return mod;
            }

            public void setMod(boolean mod) {
                this.mod = mod;
            }

            final JTextField text;

            public DirPane(String dir) {
                this.setLayout(new BorderLayout());
                final JButton rem = new JButton("-");
                this.add(rem, BorderLayout.WEST);
                text = new JTextField(dir);
                text.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        mod = true;
                        PhotoDirsCotroller.this.publish();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        mod = true;
                        PhotoDirsCotroller.this.publish();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        mod = true;
                        PhotoDirsCotroller.this.publish();
                    }
                });
                this.add(text);
                JPanel pp1 = new JPanel(new BorderLayout());
                final JButton select = new JButton("...");
                pp1.add(select, BorderLayout.WEST);
                final JButton addB = new JButton("+");
                pp1.add(addB, BorderLayout.EAST);
                this.add(pp1, BorderLayout.EAST);

                select.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser jf = new JFileChooser(text.getText());
                        jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        int a = jf.showOpenDialog(PhotoDirsCotroller.this);
                        if (a == JFileChooser.APPROVE_OPTION) {
                            text.setText(jf.getSelectedFile().getAbsolutePath());
                        }
                    }
                });
                rem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PhotoDirsCotroller.this.remove(DirPane.this);
                        if (PhotoDirsCotroller.this.getComponents().length == 0) {
                            final JButton add = new JButton("+");
                            add.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    PhotoDirsCotroller.this.remove(add);
                                    PhotoDirsCotroller.this.add(new DirPane("/usr/share/icons"));
                                    PhotoDirsCotroller.this.validate();
                                }
                            });
                            PhotoDirsCotroller.this.add(add);
                        }
                        PhotoDirsCotroller.this.validate();
                    }
                });
                addB.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PhotoDirsCotroller.this.add(new DirPane("/usr/share/icons"));
                        addB.setEnabled(false);
                        PhotoDirsCotroller.this.validate();
                    }
                });
            }
        }

        private void publish() {
            aargs.dirs.clear();
            for (Component c : getComponents()) {
                if (c instanceof DirPane) {
                    if (((DirPane) c).isMod()) {
                        aargs.dirs.add(((DirPane) c).text.getText());
                    }
                }
            }
        }

        private final App.Args aargs;

        public PhotoDirsCotroller(App.Args aargs) {
            this.aargs = aargs;
            this.setLayout(new GridLayout(0, 1));
            if (aargs.dirs == null || aargs.dirs.isEmpty()) {
                this.add(new DirPane("/usr/share/icons"));
            } else {
                for (String s : aargs.dirs) {
                    DirPane dd = new DirPane(s);
                    this.add(dd);
                    dd.setMod(true);
                }
            }

        }
    }
}

package org.judovana.calendarmaker.wizard;

import org.judovana.calendarmaker.App;
import org.judovana.calendarmaker.NamesLoader;
import org.judovana.calendarmaker.Wizard;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

public class FilesWizard {
    public static Component createNames(final App.Args args) {
        final String config_path = getMainPath() + "/names";
        final String rbut1Label = "no names/holidays";
        final String infoLabel = "Example file with czech date-names and holidays.";
        final String example = NamesLoader.NAMES_EXAMPLE;
        final ArgsSetter as = new ArgsSetter() {
            @Override
            public String get() {
                return args.names;
            }

            @Override
            public void set(String value) {
                args.names = value;
            }
        };
        final ActionListener testFileListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    NamesLoader.AllNamesKeeper all = NamesLoader.loadAllNames(
                            NamesLoader.getStream(as.get(), example));
                    JOptionPane.showMessageDialog(null, all);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        };
        return createGenereicSuperPanel(
                new TerribleSetup(config_path, rbut1Label, infoLabel, example, as,
                        testFileListener, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //names
                    }

                }));
    }

    public static Component createImportantNames(final App.Args args) {
        final String config_path = getMainPath() + "/importantNames";
        final String rbut1Label = "no important names";
        final String infoLabel = "Example file with some random czech names to be highlighted";
        final String example = NamesLoader.INTERESTING_NAMES_EXAMPLE;
        final ArgsSetter as = new ArgsSetter() {
            @Override
            public String get() {
                return args.interesting;
            }

            @Override
            public void set(String value) {
                args.interesting = value;
            }
        };
        final ActionListener testFileListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    NamesLoader.NamesKeeper all = NamesLoader.loadMyNames(
                            NamesLoader.getStream(as.get(), example));
                    JOptionPane.showMessageDialog(null, all);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        };
        return createGenereicSuperPanel(
                new TerribleSetup(config_path, rbut1Label, infoLabel, example, as,
                        testFileListener, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //important names
                        String r = JOptionPane.showInputDialog("Enter just some name to be highligted in calendar");
                        JTextArea text = findTextField((Component) e.getSource());
                        if (r != null) {
                            append(text, r);
                        }
                    }
                }));
    }

    public static Component createAnniversaries(final App.Args args) {
        final String config_path = getMainPath() + "/anniversaries";
        final String rbut1Label = "no anniversaries/dates";
        final String infoLabel = "Example file with some random dates";
        final String example = NamesLoader.INTERESTING_DATES_EXAMPLE;
        final ArgsSetter as = new ArgsSetter() {
            @Override
            public String get() {
                return args.anniversaries;
            }

            @Override
            public void set(String value) {
                args.anniversaries = value;
            }
        };
        final ActionListener testFileListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    NamesLoader.DatesKeeper all = NamesLoader.loadCustomDates(
                            NamesLoader.getStream(as.get(), example));
                    JOptionPane.showMessageDialog(null, all);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        };
        return createGenereicSuperPanel(
                new TerribleSetup(config_path, rbut1Label, infoLabel, example, as,
                        testFileListener, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //aniversaries
                    }
                }));
    }

    public static String getMainPath() {
        return System.getProperty("user.home") + "/.config/CalendarMaker";
    }

    public static File getMainConfig() {
        return new File(getMainPath(), "conf.conf");
    }

    private interface ArgsSetter {
        String get();

        void set(String value);
    }

    private static class TerribleSetup {

        private final String config_path;
        private final String rbut1Label;
        private final String infoLabel;
        private final String example;
        private final ArgsSetter as;
        private final ActionListener testFileListener;
        private final ActionListener adl;

        public TerribleSetup(String config_path, String rbut1Label, String infoLabel,
                String example, ArgsSetter as, ActionListener testFileListener, ActionListener adl) {
            this.config_path = config_path;
            this.rbut1Label = rbut1Label;
            this.infoLabel = infoLabel;
            this.example = example;
            this.as = as;
            this.testFileListener = testFileListener;
            this.adl = adl;
        }
    }

    private static Component createGenereicSuperPanel(final TerribleSetup ts) {
        final String rbut2Label = "Internal example";
        final String rbut3Label = "your custom file";

        final JPanel textfieldPane = new JPanel(new BorderLayout());
        final JTextField file = new JTextField(ts.config_path);
        final JButton select = new JButton("...");
        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser jf = new JFileChooser(file.getText());
                jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int a = jf.showOpenDialog(null);
                if (a == JFileChooser.APPROVE_OPTION) {
                    file.setText(jf.getSelectedFile().getAbsolutePath());
                }
            }
        });
        textfieldPane.add(file);
        textfieldPane.add(select, BorderLayout.EAST);
        final JPanel radios = new JPanel(new GridLayout(3, 2));
        final JRadioButton nothing = new JRadioButton(ts.rbut1Label);
        final JRadioButton internal = new JRadioButton(rbut2Label);
        final JRadioButton external = new JRadioButton(rbut3Label);
        radios.add(nothing);
        radios.add(new JLabel(""));
        radios.add(internal);
        radios.add(new JLabel(ts.infoLabel));
        radios.add(external);
        radios.add(textfieldPane);
        final JPanel main = new JPanel(new BorderLayout());
        main.add(radios, BorderLayout.NORTH);
        final JTextArea text = new JTextArea();
        main.add(new JScrollPane(text));
        text.getDocument().addDocumentListener(new DocumentListener() {
            private void work() {
                if (text.isEnabled()) {
                    try {
                        File f = new File(file.getText());
                        f.getParentFile().mkdirs();
                        Files.write(f.toPath(),
                                text.getText().getBytes("utf-8"));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, ex);
                    }
                }

            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                work();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                work();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                work();
            }
        });
        final JButton copyEditCreate = new JButton("?");
        final JButton addLine = new JButton("Add Line");
        final JButton test = new JButton("test");
        final JPanel tools = new JPanel(new GridLayout(1, 3));
        test.addActionListener(ts.testFileListener);
        tools.add(copyEditCreate);
        tools.add(test);
        tools.add(addLine);
        addLine.addActionListener(ts.adl);
        main.add(tools, BorderLayout.SOUTH);
        final ButtonGroup bg = new ButtonGroup();
        bg.add(internal);
        bg.add(external);
        bg.add(nothing);
        final ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (internal.isSelected()) {
                    select.setEnabled(false);
                    file.setEnabled(false);
                    if (!"NO".equals(e.getActionCommand())) {
                        ts.as.set(NamesLoader.EXAMPLE);
                    }
                    String[] t = Wizard.inputStreamToString(
                            NamesLoader.getExemplarStream(ts.example));
                    disableText(text, t[0]);
                    copyEditCreate.setText("Copy to clipboard");
                    setAction(copyEditCreate, e1 -> {
                        StringSelection stringSelection = new StringSelection(text.getText());
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(stringSelection, null);
                    });
                    copyEditCreate.setEnabled(true);
                    addLine.setEnabled(false);
                    test.setEnabled(true);
                } else if (external.isSelected()) {
                    select.setEnabled(true);
                    file.setEnabled(true);
                    if (!"NO".equals(e.getActionCommand())) {
                        ts.as.set(file.getText());
                    }
                    String[] t = Wizard.filePreview(file.getText());
                    enableText(text, t[0]);
                    if (t[1] == null) {
                        text.setEnabled(false);
                        setCreateAction(copyEditCreate, text, addLine, file);
                        addLine.setEnabled(false);
                        test.setEnabled(true);
                    } else {
                        setDeleteAction(copyEditCreate, text, addLine, file);
                        addLine.setEnabled(true);
                        test.setEnabled(true);
                    }
                } else {
                    select.setEnabled(false);
                    file.setEnabled(false);
                    disableText(text, "");
                    if (!"NO".equals(e.getActionCommand())) {
                        ts.as.set(null);
                    }
                    copyEditCreate.setText("?");
                    copyEditCreate.setEnabled(false);
                    addLine.setEnabled(false);
                    test.setEnabled(false);
                }
            }
        };
        internal.addActionListener(al);
        external.addActionListener(al);
        nothing.addActionListener(al);
        if (NamesLoader.useNothing(ts.as.get())) {
            nothing.setSelected(true);
        } else if ((!NamesLoader.useInternal(ts.as.get()))) {
            external.setSelected(true);
            file.setText(ts.as.get());
        } else {
            internal.setSelected(true);
        }

        al.actionPerformed(new ActionEvent(new JPanel(), 1, "NO"));
        //after the action, to not disturb the initial setting
        file.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                updatePreview();
            }

            private void updatePreview() {
                ts.as.set(file.getText());
                String[] t = Wizard.filePreview(file.getText());
                enableText(text, t[0]);
                if (t[1] == null) {
                    text.setEnabled(false);
                    setCreateAction(copyEditCreate, text, addLine, file);
                    addLine.setEnabled(false);
                    test.setEnabled(true);
                } else {
                    setDeleteAction(copyEditCreate, text, addLine, file);
                    addLine.setEnabled(true);
                    test.setEnabled(true);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                updatePreview();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                updatePreview();
            }
        });
        return main;
    }

    private static void setDeleteAction(JButton copyEditCreate, JTextArea text, JButton addLine,
            JTextField file) {
        copyEditCreate.setText("delete");
        copyEditCreate.setEnabled(true);
        setAction(copyEditCreate, e1 -> {
            File f = new File(file.getText());
            if (f.exists()) {
                int a = JOptionPane.showConfirmDialog(null,
                        "Delete " + f.getAbsolutePath() + " ?");
                if (a == JOptionPane.YES_OPTION) {
                    f.delete();
                    addLine.setEnabled(false);
                    disableText(text, "");
                    setCreateAction(copyEditCreate, text, addLine, file);
                }
            }
        });
    }

    private static void setCreateAction(JButton copyEditCreate, JTextArea text, JButton addLine,
            JTextField file) {
        copyEditCreate.setText("create");
        copyEditCreate.setEnabled(true);
        setAction(copyEditCreate, e1 -> {
            enableText(text, "");
            addLine.setEnabled(true);
            setDeleteAction(copyEditCreate, text, addLine, file);
        });
    }

    private static void setAction(JButton b, ActionListener a) {
        clearActions(b);
        b.addActionListener(a);
    }

    private static void clearActions(JButton button) {
        ActionListener[] ls = button.getActionListeners();
        for (ActionListener l : ls) {
            button.removeActionListener(l);
        }
    }

    private static void enableText(JTextArea text, String s) {
        //it crucial to set doc dirst, then enable
        //see tis doc listener
        text.setText(s);
        text.setEnabled(true);
    }

    private static void disableText(JTextArea text, String s) {
        //it is crucial to disable first, and then set, see text
        //see tis doc listener
        text.setEnabled(false);
        text.setText(s);
    }


    private static JTextArea findTextField(Component source) {
        return findTextField(source, new ArrayList<>());
    }

    private static JTextArea findTextField(Component source, ArrayList<Object> found) {
        if (source == null || found.contains(source)) {
            return null;
        }
        if (source instanceof JTextArea) {
            return (JTextArea) source;
        }
        found.add(source);
        if (source instanceof Container) {
            Container q = (Container) (source);
            Component[] cs = q.getComponents();
            for (Component c : cs) {
                JTextArea qq = findTextField(c, found);
                if (qq != null) {
                    return qq;
                }
            }
        }
        return findTextField(source.getParent(), found);

    }

    private static void append(JTextArea text, String r) {
        if (text.getText().endsWith("\n")) {
            text.append(r + "\n");
        } else {
            String[] lines = text.getText().split("\n");
            if (lines[lines.length - 1].isEmpty()) {
                text.append(r + "\n");
            } else {
                text.append("\n" + r + "\n");
            }
        }
    }

}

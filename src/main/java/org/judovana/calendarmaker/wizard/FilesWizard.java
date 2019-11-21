package org.judovana.calendarmaker.wizard;

import org.judovana.calendarmaker.App;
import org.judovana.calendarmaker.NamesLoader;
import org.judovana.calendarmaker.Wizard;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultEditorKit;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FilesWizard {
    public static Component createNames(final App.Args args) {
        final String config_path = getMainPath()+"/names";
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
                        testFileListener));
    }

    public static Component createImportantNames(final App.Args args) {
        final String config_path = getMainPath()+"/importantNames";
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
                        testFileListener));
    }

    public static Component createAnniversaries(final App.Args args) {
        final String config_path = getMainPath()+"/anniversaries";
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
                        testFileListener));
    }

    private static String getMainPath() {
        return System.getProperty("user.home")+ "/.config/CalendarMaker";
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

        public TerribleSetup(String config_path, String rbut1Label, String infoLabel,
                String example, ArgsSetter as, ActionListener testFileListener) {
            this.config_path = config_path;
            this.rbut1Label = rbut1Label;
            this.infoLabel = infoLabel;
            this.example = example;
            this.as = as;
            this.testFileListener = testFileListener;
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
        final JButton copyEditCreate = new JButton("?");
        final JButton addLine = new JButton("Add Line");
        final JButton test = new JButton("test");
        final JPanel tools = new JPanel(new GridLayout(1, 3));
        test.addActionListener(ts.testFileListener);
        tools.add(copyEditCreate);
        tools.add(test);
        tools.add(addLine);
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
                    text.setEnabled(false);
                    if (!"NO".equals(e.getActionCommand())) {
                        ts.as.set(NamesLoader.EXAMPLE);
                    }
                    String[] t = Wizard.inputStreamToString(
                            NamesLoader.getExemplarStream(ts.example));
                    text.setText(t[0]);
                    copyEditCreate.setText("Copy to clipboard");
                    clearActions(copyEditCreate);
                    copyEditCreate.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            StringSelection stringSelection = new StringSelection(text.getText());
                            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                            clipboard.setContents(stringSelection, null);
                        }
                    });
                    copyEditCreate.setEnabled(true);
                    addLine.setEnabled(false);
                    test.setEnabled(true);
                } else if (external.isSelected()) {
                    select.setEnabled(true);
                    file.setEnabled(true);
                    text.setEnabled(true);
                    if (!"NO".equals(e.getActionCommand())) {
                        ts.as.set(file.getText());
                    }
                    String[] t = Wizard.filePreview(file.getText());
                    text.setText(t[0]);
                    if (t[1] == null) {
                        text.setEnabled(false);
                        copyEditCreate.setText("create");
                        copyEditCreate.setEnabled(true);
                        addLine.setEnabled(false);
                        test.setEnabled(true);
                    } else {
                        copyEditCreate.setText("save");
                        copyEditCreate.setEnabled(true);
                        addLine.setEnabled(true);
                        test.setEnabled(true);
                    }
                } else {
                    select.setEnabled(false);
                    file.setEnabled(false);
                    text.setEnabled(false);
                    if (!"NO".equals(e.getActionCommand())) {
                        ts.as.set(null);
                    }
                    text.setText("");
                    copyEditCreate.setText("?");
                    copyEditCreate.setEnabled(false);
                    addLine.setEnabled(false);
                    test.setEnabled(false);
                }
            }

            private void clearActions(JButton copyEditCreate) {
                ActionListener[] ls = copyEditCreate.getActionListeners();
                for (ActionListener l : ls) {
                    copyEditCreate.removeActionListener(l);
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
                text.setEnabled(true);
                ts.as.set(file.getText());
                String[] t = Wizard.filePreview(file.getText());
                text.setText(t[0]);
                if (t[1] == null) {
                    text.setEnabled(false);
                    copyEditCreate.setText("create");
                    copyEditCreate.setEnabled(true);
                    addLine.setEnabled(false);
                    test.setEnabled(true);
                } else {
                    copyEditCreate.setText("save");
                    copyEditCreate.setEnabled(true);
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
}

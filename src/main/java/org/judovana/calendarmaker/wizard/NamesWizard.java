package org.judovana.calendarmaker.wizard;

import org.judovana.calendarmaker.App;
import org.judovana.calendarmaker.NamesLoader;
import org.judovana.calendarmaker.Wizard;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NamesWizard {
    public static Component create(final App.Args args) {
        final JPanel textfieldPane = new JPanel(new BorderLayout());
        final JTextField file = new JTextField(
                System.getProperty("user.home") + "/.config/CalendarMaker/names");
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
        final JRadioButton nothing = new JRadioButton("no names/holidays");
        final JRadioButton internal = new JRadioButton("Internal example");
        final JRadioButton external = new JRadioButton("your custom file");
        radios.add(nothing);
        radios.add(new JLabel(""));
        radios.add(internal);
        radios.add(new JLabel("Example file with czech date-names and holidays."));
        radios.add(external);
        radios.add(textfieldPane);
        final JPanel main = new JPanel(new BorderLayout());
        main.add(radios, BorderLayout.NORTH);
        final JTextArea text = new JTextArea();
        main.add(new JScrollPane(text));
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
                        args.names = NamesLoader.EXAMPLE;
                    }
                    String[] t = Wizard.inputStreamToString(NamesLoader.getExemplarStream(NamesLoader.NAMES_EXAMPLE));
                    text.setText(t[0]);
                } else if (external.isSelected()) {
                    select.setEnabled(true);
                    file.setEnabled(true);
                    text.setEnabled(true);
                    if (!"NO".equals(e.getActionCommand())) {
                        args.names = file.getText();
                    }
                    String[] t = Wizard.filePreview(file.getText());
                    text.setText(t[0]);
                    if (t[1] == null) {
                        text.setEnabled(false);
                    }
                } else {
                    select.setEnabled(false);
                    file.setEnabled(false);
                    text.setEnabled(false);
                    if (!"NO".equals(e.getActionCommand())) {
                        args.names = null;
                    }
                    text.setText("");
                }
            }
        };
        internal.addActionListener(al);
        external.addActionListener(al);
        nothing.addActionListener(al);
        if (NamesLoader.useNothing(args.names)) {
            nothing.setSelected(true);
        } else if ((!NamesLoader.useInternal(args.names))) {
            external.setSelected(true);
            file.setText(args.names);
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
                args.names = text.getText();
                String[] t = Wizard.filePreview(file.getText());
                text.setText(t[0]);
                if (t[1] == null) {
                    text.setEnabled(false);
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

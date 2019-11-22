package org.judovana.calendarmaker.wizard;

import org.judovana.calendarmaker.App;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class LoadWizard {

    public static Component createLoad(final App.Args file) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel("File to load previsously saved list of pictures (with rotations) from"), BorderLayout.CENTER);
        final JTextField sp = new JTextField("");
        if (file.toLoad != null) {
            sp.setText(file.toLoad);
        }
        p.add(sp);
        sp.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                file.toLoad = sp.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                file.toLoad = sp.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                file.toLoad = sp.getText();
            }
        });
        JButton bb = new JButton("...");
        bb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jf = new JFileChooser(sp.getText());
                jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int a = jf.showOpenDialog(null);
                if (a == JFileChooser.APPROVE_OPTION) {
                    sp.setText(jf.getSelectedFile().getAbsolutePath());
                }
            }
        });
        p.add(bb, BorderLayout.EAST);
        return p;
    }

}

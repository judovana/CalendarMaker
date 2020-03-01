package org.judovana.calendarmaker.wizard;

import org.judovana.calendarmaker.App;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MiscWizard extends JPanel implements SaveController {


    private App.Args args;
    private JCheckBox saveOnExit;
    private JComboBox<String> page;

    public MiscWizard(GridLayout gridLayout) {
        super(gridLayout);
    }

    @Override
    public boolean isSave() {
        return saveOnExit.isSelected();
    }

    @Override
    public void setArgs(App.Args arg) {
        this.args = arg;
    }

    @Override
    public void save() throws IOException {
        args.save();

    }

    public static MiscWizard create(App.Args args) {
        MiscWizard p = new MiscWizard(new GridLayout(6, 2));
        p.setArgs(args);
        p.add(new JLabel("width"));
        final JSpinner w = new JSpinner(new SpinnerNumberModel(800, 50, 20000, 50));
        p.add(w);
        p.add(new JLabel("height"));
        final JSpinner h = new JSpinner(new SpinnerNumberModel(600, 50, 20000, 50));
        p.add(h);
        p.saveOnExit = new JCheckBox("Save on Finish", null, true);
        p.add(p.saveOnExit);
        p.add(new JLabel());
        p.add(new JLabel("output size"));
        p.page = new JComboBox<String>(new String[]{"A4"});
        p.add(p.page);
        final JCheckBox hugeNumbers = new JCheckBox("Huge numbers");
        p.add(hugeNumbers);
        p.add(new JLabel(""));
        JButton delte = new JButton("delete: " + FilesWizard.getMainConfig());
        delte.setEnabled(FilesWizard.getMainConfig().exists());
        delte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean deleted = FilesWizard.getMainConfig().delete();
                    if (!deleted) {
                        throw new IOException("Probelm to delte " + FilesWizard.getMainConfig());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        });
        p.add(delte);
        if (args.w != null) {
            w.setValue(args.w);
        }
        if (args.h != null) {
            h.setValue(args.h);
        }
        if (args.hugeNumbers != null) {
            hugeNumbers.setSelected(args.hugeNumbers);
        }
        w.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                args.w = (Integer) w.getValue();
            }
        });
        h.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                args.h = (Integer) h.getValue();
            }
        });
        hugeNumbers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                args.hugeNumbers = hugeNumbers.isSelected();
            }
        });
        return p;
    }
}

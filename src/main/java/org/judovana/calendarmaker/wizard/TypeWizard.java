package org.judovana.calendarmaker.wizard;

import org.judovana.calendarmaker.App;
import org.judovana.calendarmaker.Wizard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TypeWizard {
    static void clean(JPanel pp) {
        for (Component c : pp.getComponents()) {
            if (c instanceof JLabel) {
                pp.remove(c);
            }
        }
    }


    public static Component createWeekMonth(final App.Args args) {
        JPanel p = new JPanel(new GridLayout(1, 2));
        final JRadioButton b1 = new JRadioButton("Week");
        final JRadioButton b2 = new JRadioButton("Month");
        p.add(b1);
        p.add(b2);
        ButtonGroup bg = new ButtonGroup();
        bg.add(b1);
        bg.add(b2);
        final JPanel pp = new JPanel(new BorderLayout());
        pp.add(p, BorderLayout.SOUTH);
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (b1.isSelected()) {
                    args.week = true;
                    clean(pp);
                    ImageIcon ii = new ImageIcon(Wizard.class.getClassLoader().getResource("org/judovana/calendarmaker/data/53.png"));
                    JLabel jl = new JLabel(ii);
                    pp.add(jl);
                    pp.validate();
                }
            }
        });
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (b2.isSelected()) {
                    args.week = false;
                    clean(pp);
                    ImageIcon ii = new ImageIcon(Wizard.class.getClassLoader().getResource("org/judovana/calendarmaker/data/12.png"));
                    JLabel jl = new JLabel(ii);
                    pp.add(jl);
                    pp.validate();
                }
            }
        });
        if (args.week == null || args.week == false) {
            b2.setSelected(true);
            b2.getActionListeners()[0].actionPerformed(null);
        } else {
            b1.setSelected(true);
            b1.getActionListeners()[0].actionPerformed(null);
        }
        return pp;
    }
}
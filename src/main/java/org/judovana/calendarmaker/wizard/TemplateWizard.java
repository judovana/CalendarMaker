package org.judovana.calendarmaker.wizard;

import org.judovana.calendarmaker.App;
import org.judovana.calendarmaker.Wizard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TemplateWizard {

    public static Component createTemplate(final App.Args args) {
        JPanel p = new JPanel(new GridLayout(1, 4));
        final JRadioButton hr = new JRadioButton("Image right");
        final JRadioButton hl = new JRadioButton("Image left");
        final JRadioButton vd = new JRadioButton("Image down");
        final JRadioButton vu = new JRadioButton("Image up");
        final JRadioButton custom = new JRadioButton("custom - it is already done, but now read only");
        custom.setEnabled(false);
        p.add(hr);
        p.add(hl);
        p.add(vd);
        p.add(vu);
        p.add(custom);
        ButtonGroup bg = new ButtonGroup();
        bg.add(hr);
        bg.add(hl);
        bg.add(vd);
        bg.add(vu);
        bg.add(custom);
        final JPanel pp = new JPanel(new BorderLayout());
        pp.add(p, BorderLayout.SOUTH);
        hr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (hr.isSelected()) {
                    args.template = "hr";
                    TypeWizard.clean(pp);
                    ImageIcon ii = new ImageIcon(Wizard.class.getClassLoader().getResource("org/judovana/calendarmaker/data/hr.png"));
                    JLabel jl = new JLabel(ii);
                    pp.add(jl);
                    pp.validate();
                }
            }
        });
        hl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (hl.isSelected()) {
                    args.template = "hl";
                    TypeWizard.clean(pp);
                    ImageIcon ii = new ImageIcon(Wizard.class.getClassLoader().getResource("org/judovana/calendarmaker/data/hl.png"));
                    JLabel jl = new JLabel(ii);
                    pp.add(jl);
                    pp.validate();
                }
            }
        });
        vd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (vd.isSelected()) {
                    args.template = "vd";
                    TypeWizard.clean(pp);
                    ImageIcon ii = new ImageIcon(Wizard.class.getClassLoader().getResource("org/judovana/calendarmaker/data/vd.png"));
                    JLabel jl = new JLabel(ii);
                    pp.add(jl);
                    pp.validate();
                }
            }
        });
        vu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (vu.isSelected()) {
                    args.template = "vu";
                    TypeWizard.clean(pp);
                    ImageIcon ii = new ImageIcon(Wizard.class.getClassLoader().getResource("org/judovana/calendarmaker/data/vu.png"));
                    JLabel jl = new JLabel(ii);
                    pp.add(jl);
                    pp.validate();
                }
            }
        });
        if ("HR".equalsIgnoreCase(args.template)) {
            //tmplt = new Template.HorizontalImageRight();
            hr.setSelected(true);
            hr.getActionListeners()[0].actionPerformed(null);
        } else if ("HL".equalsIgnoreCase(args.template)) {
            //tmplt = new Template.HorizontalImageLeft();
            hl.setSelected(true);
            hl.getActionListeners()[0].actionPerformed(null);
        } else if ("VD".equalsIgnoreCase(args.template)) {
            //tmplt = new Template.VerticalImageDown();
            vd.setSelected(true);
            vd.getActionListeners()[0].actionPerformed(null);
        } else if ("VU".equalsIgnoreCase(args.template)) {
            //tmplt = new Template.VerticalImageUp();
            vu.setSelected(true);
            vu.getActionListeners()[0].actionPerformed(null);
        } else {
            ImageIcon ii = new ImageIcon(Wizard.class.getClassLoader().getResource("org/judovana/calendarmaker/data/hr.png"));
            JLabel jl = new JLabel(ii);
            pp.add(jl);
            pp.validate();
        }
        return pp;
    }

}

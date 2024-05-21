package com.example;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.io.*;
import java.util.*;

public class Main extends JFrame {

    //Menu Bar Elements
    private static JMenuBar menuBar;
    static JMenu menu;
    static JMenuItem file, timesheet, shifts, edit;

    //GUI Elements
    private JTextArea confirmed_shift_profile = new JTextArea (20,17);

    private JTextArea pre_added_shifts = new JTextArea (10, 40);

    //Buttons
    JButton generateTimesheet = new JButton ("Generate Timesheet");
    JButton budgetcode = new JButton ("Edit Budget Code");
    JButton manual_add = new JButton ("Manual Add");

    final static boolean RIGHT_TO_LEFT = false;

    public void styleButton(JButton button) {
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 10));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(Color.decode("#a68164"));
    }

    public void addMenuBarComponents() {
        menuBar = new JMenuBar();
        menu = new JMenu("File");
        menuBar.add(menu);

        JMenu menu1 = new JMenu("New");


        timesheet = new JMenuItem("Generate Timesheet");
        menu.add(timesheet);

        shifts = new JMenuItem("Edit Shifts");
        menu.add(shifts);

        edit = new JMenuItem("Edit Budget Code");
        menu.add(edit);
    }
    public void addComponentsToPane (Container content) {

        if (RIGHT_TO_LEFT) {
            content.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        JPanel mainbg = new JPanel() {
            public Dimension getPreferredSize() {
                return new Dimension(1000,700);
            }
        };

        setContentPane(mainbg);
        setTitle("SA Timesheet Generator V1.0");

        mainbg.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //Budget Code
        JScrollPane budgetcodeScroll = new JScrollPane(budgetcode);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(250, 0, 15, 225);
        mainbg.add(budgetcodeScroll, c);

        //Confirmed Shifts
        confirmed_shift_profile.setEditable(false);
        confirmed_shift_profile.setLineWrap(true);
        confirmed_shift_profile.setWrapStyleWord(true);
        confirmed_shift_profile.setText("Confirmed Shift Profile");
        JScrollPane confirmed_shift_profileScroll = new JScrollPane(confirmed_shift_profile);
        c.insets = new Insets(40,0,0,150);
        c.fill = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        mainbg.add(confirmed_shift_profileScroll, c);

        //Manual Add
        JScrollPane manual_addScroll = new JScrollPane(manual_add);
        c.fill = GridBagConstraints.LINE_END;
        c.insets = new Insets(232,150,0,0);
        c.gridx = 4;
        c.gridy = 0;
        mainbg.add(manual_addScroll, c);

        //Pre-Added Shifts
        pre_added_shifts.setEditable(false);
        pre_added_shifts.setLineWrap(true);
        pre_added_shifts.setText("Pre-Added Shifts");
        pre_added_shifts.setWrapStyleWord(true);
        JScrollPane pre_added_shiftsScroll = new JScrollPane(pre_added_shifts);
        c.fill = GridBagConstraints.CENTER;
        c.insets = new Insets(0,0,0,190);
        c.gridx = 4;
        c.gridy = 0;
        mainbg.add(pre_added_shiftsScroll, c);

        //Generate Timesheet
        JScrollPane generateTimesheetScroll = new JScrollPane(generateTimesheet);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_END;
        c.gridx = 3;
        c.gridy = 3;
        mainbg.add(generateTimesheetScroll, c);
    }

    public static void main(String[] args) {
        Main frame = new Main();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addComponentsToPane(frame.getContentPane());
        frame.addMenuBarComponents();
        frame.setJMenuBar(frame.menuBar);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }
}
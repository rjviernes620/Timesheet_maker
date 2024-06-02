package com.example;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.io.*;
import java.util.*;
import java.time.Year;

public class Main extends JFrame {

    //Menu Bar Elements
    private static JMenuBar menuBar;
    static JMenu menu;
    static JMenuItem file, timesheet, shifts, edit;

    //GUI Elements
    private JTextArea confirmed_shift_profile = new JTextArea (20,17);
    private ImageIcon uni_logo = new ImageIcon("demo\\src\\main\\resources\\uni_logo.png");
    private JLabel uni_logo_label = new JLabel(uni_logo);  
    private JTextArea pre_added_shifts = new JTextArea (10, 40);
    String[] monthList = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    

    //Buttons
    JButton generateTimesheet = new JButton ("Generate Timesheet");
    JButton budgetcode = new JButton ("Edit Budget Code");
    JButton manual_add = new JButton ("Manual Add");

    //Manual Add -> Text Fields
    private JTextArea shift_name = new JTextArea(1, 10);
    private JTextArea budget_code = new JTextArea(1, 10);

    JButton manual_add_shift = new JButton("Add Shift");

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

        //University Logo
        c.fill = GridBagConstraints.FIRST_LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 360, 140);
        mainbg.add(uni_logo_label, c);

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

        //Month List
        JComboBox<String> monthListScroll = new JComboBox<>(monthList);
        monthListScroll.setMaximumRowCount(monthList.length);

        Dimension preferredSize = monthListScroll.getPreferredSize();
        preferredSize.width = 10; // Replace 100 with your desired width
        monthListScroll.setMaximumSize(preferredSize);

        c.fill = GridBagConstraints.FIRST_LINE_START;
        c.gridx = 3;
        c.gridy = 0;
        c.insets = new Insets(0,0,0,0);
        mainbg.add(monthListScroll, c);

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

    

    public void addComponentstoManualAdd() {
            JFrame manualAddFrame = new JFrame("Manually add shift");
            manualAddFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            manualAddFrame.setSize(500, 300);
            manualAddFrame.setVisible(true);
            manualAddFrame.setResizable(false);
            manualAddFrame.setLocationRelativeTo(null);

            manualAddFrame.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            shift_name.setEditable(true);
            shift_name.setLineWrap(true);
            shift_name.setWrapStyleWord(true);
            shift_name.setText("Shift Name");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 0;
            manualAddFrame.add(shift_name, c);

            budget_code.setEditable(true);
            budget_code.setLineWrap(true);
            budget_code.setWrapStyleWord(true);
            budget_code.setText("Budget Code");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 1;
            manualAddFrame.add(budget_code, c);
    }

    public Main() {

        Container content = getContentPane();
        getContentPane().setBackground(Color.decode("#f0f0f0"));

        manual_add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addComponentstoManualAdd();
            }
        });
        }
    

    public static void main(String[] args) {
        ImageIcon small = new ImageIcon("demo\\src\\main\\resources\\small.png");
        Main frame = new Main();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addComponentsToPane(frame.getContentPane());
        frame.addMenuBarComponents();
        frame.setJMenuBar(frame.menuBar);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        Image sml = small.getImage();
        frame.setIconImage(sml);
    }
}
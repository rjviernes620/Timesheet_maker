package com.example;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.time.Year;
import java.time.YearMonth;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Main extends JFrame {

    //Menu Bar Elements
    private JMenuBar menuBar;
    static JMenu menu, boo, help;
    static JMenuItem timesheet, shifts, edit, profile;

    //GUI Elements
    private JTextArea confirmed_shift_profile = new JTextArea (20,17);
    private ImageIcon uni_logo = new ImageIcon("demo\\src\\main\\resources\\uni_logo.png");
    private JLabel uni_logo_label = new JLabel(uni_logo);  
    private JTextArea pre_added_shifts = new JTextArea (10, 40);
    String[] monthList = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public String selectedMonth;
    Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
    

    //Buttons
    JButton generateTimesheet = new JButton ("Generate Timesheet");
    JButton budgetcode = new JButton ("Edit Budget Code");
    JButton manual_add = new JButton ("Manual Add");
    JButton upload_request_sheet = new JButton ("Upload Request Sheet");

    //Setup Profile -> Text Fields
    private JTextArea first_name = new JTextArea(1, 5);
    private JTextArea last_name = new JTextArea(1, 5);
    private JTextArea Employee_ID = new JTextArea(1, 5);
    
    //Manual Add -> Fields
    private JTextArea shift_name = new JTextArea(1, 20);
    private JTextArea budget_code = new JTextArea(1, 20);
    String[] shiftLocationList = {"Inside London (Inc. OEP Shifts, Greenwich & Avery Hill)", "Outside London (Inc. Medway)"};
    JComboBox<String> shiftLocation = new JComboBox<>(shiftLocationList);
    private JTextArea start_time = new JTextArea(1, 10);
    private JTextArea end_time = new JTextArea(1, 10);
    private JTextArea break_time = new JTextArea(1, 10);
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
        boo = new JMenu("New");
        help = new JMenu("Help");
        menuBar.add(menu);
        menuBar.add(boo);
        menuBar.add(help);

        shifts = new JMenuItem("Edit Shifts");
        menu.add(shifts);

        edit = new JMenuItem("Edit Budget Code");
        menu.add(edit);

        profile = new JMenuItem("New Profile");
        boo.add(profile);

        edit = new JMenuItem("New Timesheet");
        boo.add(edit);


        profile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addComponentstoSetupProfile();
            }
        });
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
        preferredSize.width = 10;
        monthListScroll.setMaximumSize(preferredSize);
        c.fill = GridBagConstraints.FIRST_LINE_START;
        c.gridx = 3;
        c.gridy = 0;
        c.insets = new Insets(300,0,0,0);
        mainbg.add(monthListScroll, c);
        selectedMonth = monthListScroll.getSelectedItem().toString();

        //Upload Request Sheet
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 1;
        c.insets = new Insets(0,0,0,0);
        mainbg.add(upload_request_sheet, c);

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
            manualAddFrame.setSize(1000, 300);
            manualAddFrame.setVisible(true);
            manualAddFrame.setResizable(false);
            manualAddFrame.setLocationRelativeTo(null);

            manualAddFrame.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            shift_name.setEditable(true);
            shift_name.setLineWrap(false);
            shift_name.setWrapStyleWord(true);
            shift_name.setBorder(border);
            c.fill = GridBagConstraints.PAGE_START;
            c.insets = new Insets(0, 0, 10, 0);
            c.gridx = 1;
            c.gridy = 0;
            manualAddFrame.add(shift_name, c);

            JLabel shift_name_label = new JLabel("Shift Name");
            c.gridx = 0;
            c.gridy = 0;
            c.fill = GridBagConstraints.PAGE_START;
            c.insets = new Insets(0, 0, 10, 0);
            manualAddFrame.add(shift_name_label, c);

            budget_code.setEditable(true);
            budget_code.setLineWrap(false);
            budget_code.setWrapStyleWord(true);
            budget_code.setBorder(border);
            c.fill = GridBagConstraints.PAGE_START;
            c.gridx = 1;
            c.gridy = 1;
            manualAddFrame.add(budget_code, c);

            JLabel budget_code_label = new JLabel("Budget Code");
            c.gridx = 0;
            c.gridy = 1;
            c.fill = GridBagConstraints.PAGE_START;
            manualAddFrame.add(budget_code_label, c);

            int daysInMonth = getDaysInMonth(selectedMonth, Year.now().getValue());
            String[] day = new String[daysInMonth];
            for (int i = 0; i < daysInMonth; i++) { // list needs to update dynamically for month change
                day[i] = String.valueOf(i + 1);
            }
            JComboBox<String> dayList = new JComboBox<>(day);
            dayList.setMaximumRowCount(day.length);
            c.fill = GridBagConstraints.CENTER;
            c.gridx = 1;
            c.gridy = 3;
            c.insets = new Insets(10, 0, 0, 0);
            manualAddFrame.add(dayList, c);

            JLabel day_label = new JLabel("Day");
            c.gridx = 0;
            c.gridy = 3;
            c.fill = GridBagConstraints.CENTER;
            c.insets = new Insets(10, 0, 0, 0);
            manualAddFrame.add(day_label, c);

            shiftLocation.setMaximumRowCount(shiftLocationList.length);
            c.fill = GridBagConstraints.PAGE_END;
            c.gridx = 1;
            c.gridy = 2;
            c.insets = new Insets(0, 20, 0, 0);
            manualAddFrame.add(shiftLocation, c);

            JLabel shiftLocation_label = new JLabel("Shift Location");
            c.gridx = 0;
            c.gridy = 2;
            c.fill = GridBagConstraints.PAGE_END;
            manualAddFrame.add(shiftLocation_label, c);

            start_time.setEditable(true);
            start_time.setLineWrap(false);
            start_time.setWrapStyleWord(true);
            start_time.setBorder(border);
            c.fill = GridBagConstraints.PAGE_END;
            c.gridx = 1;
            c.gridy = 4;
            manualAddFrame.add(start_time, c);

            JLabel start_time_label = new JLabel("Shift Start - Shift End (24hr format)");
            c.gridx = 0;
            c.gridy = 4;
            c.fill = GridBagConstraints.PAGE_END;
            manualAddFrame.add(start_time_label, c);

            end_time.setEditable(true);
            end_time.setLineWrap(false);
            end_time.setWrapStyleWord(true);
            end_time.setBorder(border);
            c.fill = GridBagConstraints.PAGE_END;
            c.gridx = 2;
            c.gridy = 4;
            manualAddFrame.add(end_time, c);

            break_time.setEditable(true);
            break_time.setLineWrap(false);
            break_time.setWrapStyleWord(true);
            break_time.setBorder(border);
            c.fill = GridBagConstraints.PAGE_END;
            c.gridx = 1;
            c.gridy = 6;
            manualAddFrame.add(break_time, c);

            JLabel break_time_label = new JLabel("Break Time (Minutes)");
            c.gridx = 0;
            c.gridy = 6;
            c.fill = GridBagConstraints.PAGE_END;
            manualAddFrame.add(break_time_label, c);

            JLabel split = new JLabel(" - ");
            c.gridx = 1;
            c.gridy = 4;
            c.fill = GridBagConstraints.PAGE_END;
            c.insets = new Insets(0, 60, 0, 0);
            manualAddFrame.add(split, c);

            c.gridx = 1;
            c.gridy = 7;
            c.fill = GridBagConstraints.PAGE_END;
            manualAddFrame.add(manual_add_shift, c);

    }

    public void addComponentstoSetupProfile() {
        JFrame setupProfileFrame = new JFrame("Setup Profile");
        setupProfileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setupProfileFrame.setSize(500, 300);
        setupProfileFrame.setVisible(true);
        setupProfileFrame.setResizable(false);
        setupProfileFrame.setLocationRelativeTo(null);

        setupProfileFrame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        first_name.setEditable(true);
        first_name.setLineWrap(true);
        first_name.setWrapStyleWord(true);
        first_name.setBorder(border);
        first_name.setText("First Name");
        c.fill = GridBagConstraints.PAGE_START;
        c.insets = new Insets(0, 0, 10, 0);
        c.gridx = 0;
        c.gridy = 0;
        setupProfileFrame.add(first_name, c);

        last_name.setEditable(true);
        last_name.setLineWrap(true);
        last_name.setBorder(border);
        last_name.setWrapStyleWord(true);
        last_name.setText("Last Name");
        c.fill = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 1;
        setupProfileFrame.add(last_name, c);

        Employee_ID.setEditable(true);
        Employee_ID.setLineWrap(true);
        Employee_ID.setBorder(border);
        Employee_ID.setWrapStyleWord(true);
        Employee_ID.setText("Employee ID");
        c.fill = GridBagConstraints.PAGE_END;
        c.gridx = 0;
        c.gridy = 2;
        setupProfileFrame.add(Employee_ID, c);
    }

    public int getDaysInMonth(String month, int year) {
        // Convert the month name to a month number
        java.time.format.DateTimeFormatter parser = java.time.format.DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH);
        java.time.Month javaMonth = java.time.Month.from(parser.parse(month));
    
        // Get the length of the month
        YearMonth yearMonthObject = YearMonth.of(year, javaMonth);
        int daysInMonth = yearMonthObject.lengthOfMonth();
    
        return daysInMonth;
    }

    public Main() {

        getContentPane().setBackground(Color.decode("#f0f0f0"));

        manual_add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addComponentstoManualAdd();
            }
        });

        upload_request_sheet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser request_sheet = new JFileChooser();
                        // Set the file filter
                        FileNameExtensionFilter filter = new FileNameExtensionFilter("Word Documents", "doc", "docx");
                        request_sheet.setFileFilter(filter);
                int returnValue = request_sheet.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = request_sheet.getSelectedFile();
                    System.out.println("File selected");

                    File target_area = new File("demo\\src\\main\\resources\\uploaded_sheets" + selectedFile.getName());

                    try {
                    // Copy the file
                    Files.copy(selectedFile.toPath(), target_area.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("File copied to: " + target_area.getAbsolutePath());

                    String command = "python -c \"import timesheet_main; timesheet_main.request_sheet('" + target_area.getAbsolutePath().replace("\\", "\\\\") + "')\"";
                    Process p = Runtime.getRuntime().exec(command);
                    BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                }
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
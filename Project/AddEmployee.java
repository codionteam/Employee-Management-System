/*package employee.management.system;

import java.awt.*;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.event.*;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class AddEmployee extends JFrame implements ActionListener {

    private JTextField tfname, tffname, tfphone, tfemail, tfaddress, tfnid;
    private JDateChooser dcdob, dcjobStartDate;
    private JComboBox<String> cbeducation, cbdesignation;
    private JLabel lblempId, lsalary;
    private JButton add, back;

    public AddEmployee() {
        setTitle("Add New Staff");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // fullscreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 250, 255)); // হালকা গ্রে ব্লু ব্যাকগ্রাউন্ড
        setLayout(new BorderLayout());

        // Heading Panel with EmpID on top
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 204)); // ডার্ক ব্লু

        JLabel heading = new JLabel("Add New Staff", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 42));
        heading.setForeground(Color.WHITE);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        headerPanel.add(heading, BorderLayout.CENTER);

        lblempId = new JLabel("Employee ID: " + generateUniqueEmpId());
        lblempId.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblempId.setForeground(new Color(33, 64, 96));
        lblempId.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 0));
        headerPanel.add(lblempId, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 80, 80));
        add(formPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 1: Name and Father's Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("Name *"), gbc);
        gbc.gridx = 1;
        tfname = new JTextField(25);
        formPanel.add(tfname, gbc);

        gbc.gridx = 2;
        formPanel.add(createLabel("Father's Name *"), gbc);
        gbc.gridx = 3;
        tffname = new JTextField(25);
        formPanel.add(tffname, gbc);

        // Row 2: DOB and Designation
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createLabel("Date of Birth *"), gbc);
        gbc.gridx = 1;
        dcdob = new JDateChooser();
        dcdob.setPreferredSize(new Dimension(250, 30));
        formPanel.add(dcdob, gbc);

        gbc.gridx = 2;
        formPanel.add(createLabel("Designation *"), gbc);
        gbc.gridx = 3;
        String[] designations = {"Lead Manager", "Senior Developer", "Junior Developer", "Intern"};
        cbdesignation = new JComboBox<>(designations);
        cbdesignation.setPreferredSize(new Dimension(250, 30));
        formPanel.add(cbdesignation, gbc);

        // Row 3: Address and NID
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createLabel("Address"), gbc);
        gbc.gridx = 1;
        tfaddress = new JTextField(25);
        formPanel.add(tfaddress, gbc);

        gbc.gridx = 2;
        formPanel.add(createLabel("NID *"), gbc);
        gbc.gridx = 3;
        tfnid = new JTextField(25);
        formPanel.add(tfnid, gbc);

        // Row 4: Phone and Email
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createLabel("Phone"), gbc);
        gbc.gridx = 1;
        tfphone = new JTextField(25);
        formPanel.add(tfphone, gbc);

        gbc.gridx = 2;
        formPanel.add(createLabel("Email *"), gbc);
        gbc.gridx = 3;
        tfemail = new JTextField(25);
        formPanel.add(tfemail, gbc);

        // Row 5: Education and Salary (auto)
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(createLabel("Highest Education"), gbc);
        gbc.gridx = 1;
        String[] educations = {
            "SSC", "HSC", "Diploma",
            "B.Sc in CSE", "B.Sc in EEE", "B.Sc in BBA", "B.Sc in ME", "B.Sc in SWE",
            "M.Sc in CSE", "M.Sc in EEE", "M.Sc in BBA", "M.Sc in ME", "M.Sc in SWE",
            "Non-Science", "Other"
        };
        cbeducation = new JComboBox<>(educations);
        cbeducation.setPreferredSize(new Dimension(250, 30));
        formPanel.add(cbeducation, gbc);

        gbc.gridx = 2;
        formPanel.add(createLabel("Salary"), gbc);
        gbc.gridx = 3;
        lsalary = new JLabel("Auto-filled");
        lsalary.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lsalary.setForeground(new Color(39, 174, 96)); // সবুজ রঙ
        formPanel.add(lsalary, gbc);

        // Row 6: Job Start Date
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(createLabel("Job Start Date *"), gbc);
        gbc.gridx = 1;
        dcjobStartDate = new JDateChooser();
        dcjobStartDate.setPreferredSize(new Dimension(250, 30));
        formPanel.add(dcjobStartDate, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        add = new JButton("Submit");
        back = new JButton("Back");
        styleButton(add, new Color(46, 204, 113));   // Green
        styleButton(back, new Color(231, 76, 60));   // Red
        buttonPanel.add(add);
        buttonPanel.add(back);
        add(buttonPanel, BorderLayout.SOUTH);

        add.addActionListener(this);
        back.addActionListener(this);
        cbdesignation.addActionListener(e -> updateSalaryBasedOnDesignation());

        updateSalaryBasedOnDesignation();

        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(44, 62, 80));
        return label;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(140, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void updateSalaryBasedOnDesignation() {
        String designation = (String) cbdesignation.getSelectedItem();
        switch (designation) {
            case "Lead Manager" -> lsalary.setText("80000");
            case "Senior Developer" -> lsalary.setText("60000");
            case "Junior Developer" -> lsalary.setText("40000");
            case "Intern" -> lsalary.setText("15000");
            default -> lsalary.setText("Auto-filled");
        }
    }

    private String generateUniqueEmpId() {
        int startId = 1001;
        try {
            Conn conn = new Conn();
            ResultSet rs = conn.s.executeQuery("SELECT empId FROM employee");
            Set<Integer> existingIds = new HashSet<>();

            while (rs.next()) {
                existingIds.add(rs.getInt("empId"));
            }
            while (existingIds.contains(startId)) {
                startId++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(startId);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == add) {
            String name = tfname.getText().trim();
            String fname = tffname.getText().trim();
            String nid = tfnid.getText().trim();
            String phone = tfphone.getText().trim();
            String email = tfemail.getText().trim();
            String education = (String) cbeducation.getSelectedItem();
            String empId = lblempId.getText().replace("Employee ID: ", "").trim();
            String address = tfaddress.getText().trim();
            String designation = (String) cbdesignation.getSelectedItem();
            String salary = lsalary.getText().trim();

            if (name.isEmpty() || fname.isEmpty() || nid.isEmpty() || designation == null || designation.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all mandatory (*) fields.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            java.util.Date dobDate = dcdob.getDate();
            if (dobDate == null) {
                JOptionPane.showMessageDialog(null, "Please select Date of Birth", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            java.util.Date jobStart = dcjobStartDate.getDate();
            if (jobStart == null) {
                JOptionPane.showMessageDialog(null, "Please select Job Start Date", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            /*
            if (!email.isEmpty() && (!email.contains("@") || !email.contains("."))) {
                JOptionPane.showMessageDialog(null, "Please enter a valid email address.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (email.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Email is required.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
            }
            if (!email.contains("@") || !email.contains(".") || !email.contains("gmail")) {
            JOptionPane.showMessageDialog(null, "Please enter a valid email address.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
            }

            try {
                Conn conn = new Conn();

                // Check duplicate NID
                ResultSet rs = conn.s.executeQuery("SELECT * FROM employee WHERE nid = '" + nid + "'");
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "NID already exists. Employee already registered.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String insertQuery = "INSERT INTO employee (name, fname, dob, nid, phone, email, education, empId, address, designation, salary, job_start_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement pst = conn.c.prepareStatement(insertQuery);
                pst.setString(1, name);
                pst.setString(2, fname);
                pst.setDate(3, new java.sql.Date(dobDate.getTime()));
                pst.setString(4, nid);
                pst.setString(5, phone);
                pst.setString(6, email);
                pst.setString(7, education);
                pst.setInt(8, Integer.parseInt(empId));
                pst.setString(9, address);
                pst.setString(10, designation);
                pst.setString(11, salary);
                pst.setDate(12, new java.sql.Date(jobStart.getTime()));

                pst.executeUpdate();

                // Insert login credentials to employeelogin (include email)
                String loginQuery = "INSERT INTO employeelogin (username, password, email) VALUES (?, ?, ?)";
                PreparedStatement loginPst = conn.c.prepareStatement(loginQuery);
                loginPst.setString(1, empId);
                loginPst.setString(2, phone); // default password = phone
                loginPst.setString(3, email); // email from form
                loginPst.executeUpdate();

                JOptionPane.showMessageDialog(null, "Employee added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);
                new Home();

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error adding employee: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (ae.getSource() == back) {
            setVisible(false);
            new Home();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddEmployee::new);
    }
}
*/

package employee.management.system;

import java.awt.*;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.event.*;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.awt.geom.RoundRectangle2D;

public class AddEmployee extends JFrame implements ActionListener {

    private JTextField tfname, tffname, tfphone, tfemail, tfaddress, tfnid;
    private JDateChooser dcdob, dcjobStartDate;
    private JComboBox<String> cbeducation, cbdesignation;
    private JLabel lblempId, lsalary;
    private JButton add, back;

    // RoundedButton class
    private static class RoundedButton extends JButton {
        private Color backgroundColor;
        private Color foregroundColor;

        public RoundedButton(String text, Color bg, Color fg) {
            super(text);
            backgroundColor = bg;
            foregroundColor = fg;
            setForeground(foregroundColor);
            setFont(new Font("Segoe UI", Font.BOLD, 18));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(140, 45));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            super.paintComponent(g);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            // No border
        }

        @Override
        public boolean contains(int x, int y) {
            return new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20).contains(x, y);
        }
    }

    public AddEmployee() {
        setTitle("Add New Staff");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // fullscreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 250, 255)); // হালকা গ্রে ব্লু ব্যাকগ্রাউন্ড
        setLayout(new BorderLayout());

        // Heading Panel with EmpID on top
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 204)); // ডার্ক ব্লু

        JLabel heading = new JLabel("Add New Staff", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 42));
        heading.setForeground(Color.WHITE);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        headerPanel.add(heading, BorderLayout.CENTER);

        lblempId = new JLabel("Employee ID: " + generateUniqueEmpId());
        lblempId.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblempId.setForeground(Color.WHITE); // Employee ID white color
        lblempId.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 0));
        headerPanel.add(lblempId, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 80, 80));
        add(formPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 1: Name and Father's Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("Name *"), gbc);
        gbc.gridx = 1;
        tfname = new JTextField(25);
        formPanel.add(tfname, gbc);

        gbc.gridx = 2;
        formPanel.add(createLabel("Father's Name *"), gbc);
        gbc.gridx = 3;
        tffname = new JTextField(25);
        formPanel.add(tffname, gbc);

        // Row 2: DOB and Designation
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createLabel("Date of Birth *"), gbc);
        gbc.gridx = 1;
        dcdob = new JDateChooser();
        dcdob.setPreferredSize(new Dimension(250, 30));
        formPanel.add(dcdob, gbc);

        gbc.gridx = 2;
        formPanel.add(createLabel("Designation *"), gbc);
        gbc.gridx = 3;
        String[] designations = {"Lead Manager", "Senior Developer", "Junior Developer", "Intern"};
        cbdesignation = new JComboBox<>(designations);
        cbdesignation.setPreferredSize(new Dimension(250, 30));
        formPanel.add(cbdesignation, gbc);

        // Row 3: Address and NID
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createLabel("Address"), gbc);
        gbc.gridx = 1;
        tfaddress = new JTextField(25);
        formPanel.add(tfaddress, gbc);

        gbc.gridx = 2;
        formPanel.add(createLabel("NID *"), gbc);
        gbc.gridx = 3;
        tfnid = new JTextField(25);
        formPanel.add(tfnid, gbc);

        // Row 4: Phone and Email
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createLabel("Phone"), gbc);
        gbc.gridx = 1;
        tfphone = new JTextField(25);
        formPanel.add(tfphone, gbc);

        gbc.gridx = 2;
        formPanel.add(createLabel("Email *"), gbc);
        gbc.gridx = 3;
        tfemail = new JTextField(25);
        formPanel.add(tfemail, gbc);

        // Row 5: Education and Salary (auto)
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(createLabel("Highest Education"), gbc);
        gbc.gridx = 1;
        String[] educations = {
                "SSC", "HSC", "Diploma",
                "B.Sc in CSE", "B.Sc in EEE", "B.Sc in BBA", "B.Sc in ME", "B.Sc in SWE",
                "M.Sc in CSE", "M.Sc in EEE", "M.Sc in BBA", "M.Sc in ME", "M.Sc in SWE",
                "Non-Science", "Other"
        };
        cbeducation = new JComboBox<>(educations);
        cbeducation.setPreferredSize(new Dimension(250, 30));
        formPanel.add(cbeducation, gbc);

        gbc.gridx = 2;
        formPanel.add(createLabel("Salary"), gbc);
        gbc.gridx = 3;
        lsalary = new JLabel("Auto-filled");
        lsalary.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lsalary.setForeground(new Color(39, 174, 96)); // সবুজ রঙ
        formPanel.add(lsalary, gbc);

        // Row 6: Job Start Date
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(createLabel("Job Start Date *"), gbc);
        gbc.gridx = 1;
        dcjobStartDate = new JDateChooser();
        dcjobStartDate.setPreferredSize(new Dimension(250, 30));
        formPanel.add(dcjobStartDate, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        add = new RoundedButton("Submit", new Color(46, 204, 113), Color.WHITE);   // Green
        back = new RoundedButton("Back", new Color(231, 76, 60), Color.WHITE);   // Red
        buttonPanel.add(add);
        buttonPanel.add(back);
        add(buttonPanel, BorderLayout.SOUTH);

        add.addActionListener(this);
        back.addActionListener(this);
        cbdesignation.addActionListener(e -> updateSalaryBasedOnDesignation());

        updateSalaryBasedOnDesignation();

        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(44, 62, 80));
        return label;
    }

    private void updateSalaryBasedOnDesignation() {
        String designation = (String) cbdesignation.getSelectedItem();
        switch (designation) {
            case "Lead Manager" -> lsalary.setText("80000");
            case "Senior Developer" -> lsalary.setText("60000");
            case "Junior Developer" -> lsalary.setText("40000");
            case "Intern" -> lsalary.setText("15000");
            default -> lsalary.setText("Auto-filled");
        }
    }

    private String generateUniqueEmpId() {
        int startId = 1001;
        try {
            Conn conn = new Conn();
            ResultSet rs = conn.s.executeQuery("SELECT empId FROM employee");
            Set<Integer> existingIds = new HashSet<>();

            while (rs.next()) {
                existingIds.add(rs.getInt("empId"));
            }
            while (existingIds.contains(startId)) {
                startId++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(startId);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == add) {
            String name = tfname.getText().trim();
            String fname = tffname.getText().trim();
            String nid = tfnid.getText().trim();
            String phone = tfphone.getText().trim();
            String email = tfemail.getText().trim();
            String education = (String) cbeducation.getSelectedItem();
            String empId = lblempId.getText().replace("Employee ID: ", "").trim();
            String address = tfaddress.getText().trim();
            String designation = (String) cbdesignation.getSelectedItem();
            String salary = lsalary.getText().trim();

            if (name.isEmpty() || fname.isEmpty() || nid.isEmpty() || designation == null || designation.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all mandatory (*) fields.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            java.util.Date dobDate = dcdob.getDate();
            if (dobDate == null) {
                JOptionPane.showMessageDialog(null, "Please select Date of Birth", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            java.util.Date jobStart = dcjobStartDate.getDate();
            if (jobStart == null) {
                JOptionPane.showMessageDialog(null, "Please select Job Start Date", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            /*
            if (!email.isEmpty() && (!email.contains("@") || !email.contains("."))) {
                JOptionPane.showMessageDialog(null, "Please enter a valid email address.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
*/
            if (email.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Email is required.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
            }
            if (!email.contains("@") || !email.contains(".") || !email.contains("gmail")) {
            JOptionPane.showMessageDialog(null, "Please enter a valid email address.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
            }

            try {
                Conn conn = new Conn();

                // Check duplicate NID
                ResultSet rs = conn.s.executeQuery("SELECT * FROM employee WHERE nid = '" + nid + "'");
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "NID already exists. Employee already registered.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String insertQuery = "INSERT INTO employee (name, fname, dob, nid, phone, email, education, empId, address, designation, salary, job_start_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement pst = conn.c.prepareStatement(insertQuery);
                pst.setString(1, name);
                pst.setString(2, fname);
                pst.setDate(3, new java.sql.Date(dobDate.getTime()));
                pst.setString(4, nid);
                pst.setString(5, phone);
                pst.setString(6, email);
                pst.setString(7, education);
                pst.setInt(8, Integer.parseInt(empId));
                pst.setString(9, address);
                pst.setString(10, designation);
                pst.setString(11, salary);
                pst.setDate(12, new java.sql.Date(jobStart.getTime()));

                pst.executeUpdate();

                // Insert login credentials to employeelogin (include email)
                String loginQuery = "INSERT INTO employeelogin (username, password, email) VALUES (?, ?, ?)";
                PreparedStatement loginPst = conn.c.prepareStatement(loginQuery);
                loginPst.setString(1, empId);
                loginPst.setString(2, phone); // default password = phone
                loginPst.setString(3, email); // email from form
                loginPst.executeUpdate();

                JOptionPane.showMessageDialog(null, "Employee added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);
                new Home();

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error adding employee: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (ae.getSource() == back) {
            setVisible(false);
            new Home();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddEmployee::new);
    }
}
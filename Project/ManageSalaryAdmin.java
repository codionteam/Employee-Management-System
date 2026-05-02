/*package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class ManageSalaryAdmin extends JFrame {

    private JTextField empIdField, salaryField;
    private JTable salaryTable;
    private JFrame parent;

    public ManageSalaryAdmin(JFrame parent) {
        this.parent = parent;

        setTitle("Manage Salary - Admin");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        JLabel heading = new JLabel("Manage Salary", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(new Color(0, 51, 102));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(heading, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);

        JLabel empIdLabel = new JLabel("Employee ID:");
        empIdLabel.setFont(new Font("Arial", Font.BOLD, 18));
        empIdField = new JTextField(15);
        empIdField.setFont(new Font("Arial", Font.PLAIN, 18));

        JLabel salaryLabel = new JLabel("Salary :");
        salaryLabel.setFont(new Font("Arial", Font.BOLD, 18));
        salaryField = new JTextField(15);
        salaryField.setFont(new Font("Arial", Font.PLAIN, 18));

        JButton fetchBtn = new JButton("Fetch");
        styleButton(fetchBtn, new Color(0, 102, 204));
        fetchBtn.addActionListener(e -> fetchSalary());

        JButton updateBtn = new JButton("Update Salary");
        styleButton(updateBtn, new Color(34, 139, 34));
        updateBtn.addActionListener(e -> updateSalary());

        JButton bonusBtn = new JButton("Add Bonus");
        styleButton(bonusBtn, new Color(255, 140, 0));
        bonusBtn.addActionListener(e -> applyBonusByPercentage());

        gbc.gridx = 0; gbc.gridy = 0; centerPanel.add(empIdLabel, gbc);
        gbc.gridx = 1; centerPanel.add(empIdField, gbc);
        gbc.gridx = 2; centerPanel.add(fetchBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 1; centerPanel.add(salaryLabel, gbc);
        gbc.gridx = 1; centerPanel.add(salaryField, gbc);
        gbc.gridx = 2; centerPanel.add(updateBtn, gbc);

        gbc.gridx = 1; gbc.gridy = 2; centerPanel.add(bonusBtn, gbc);

        add(centerPanel, BorderLayout.NORTH);

        salaryTable = new JTable();
        styleTable(salaryTable);
        JScrollPane tablePane = new JScrollPane(salaryTable);
        tablePane.getViewport().setBackground(Color.WHITE);
        add(tablePane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(240, 248, 255));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));

        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Arial", Font.BOLD, 18));
        backBtn.setBackground(Color.RED);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setPreferredSize(new Dimension(130, 45));
        backBtn.addActionListener(e -> {
            dispose();
            if (parent != null) parent.setVisible(true);
        });
        backBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                backBtn.setBackground(new Color(200, 0, 0));
            }
            public void mouseExited(MouseEvent e) {
                backBtn.setBackground(Color.RED);
            }
        });

        JButton printBtn = new JButton("Print");
        styleButton(printBtn, new Color(0, 102, 204));
        printBtn.addActionListener(e -> {
            try {
                salaryTable.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Printing failed: " + ex.getMessage());
            }
        });

        bottomPanel.add(backBtn);
        bottomPanel.add(printBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void fetchSalary() {
        String empId = empIdField.getText().trim();
        if (empId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Employee ID.");
            return;
        }

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT salary, bonus, bonus_month FROM employee WHERE empId = '" + empId + "'");
            if (rs.next()) {
                double baseSalary = rs.getDouble("salary");
                double bonus = rs.getDouble("bonus");
                String bonusMonth = rs.getString("bonus_month");

                salaryField.setText(String.valueOf(baseSalary));

                double annualSalary = baseSalary * 12;
                double tax = annualSalary * 0.05;
                double afterTaxMonthly = (annualSalary - tax) / 12;
                double totalPayMonthly = afterTaxMonthly + bonus;

                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("Description");
                model.addColumn("Amount");
                model.addRow(new Object[]{"Base Salary (Monthly)", baseSalary});
                model.addRow(new Object[]{"Bonus (Monthly)", bonus});
                model.addRow(new Object[]{"Bonus Month", bonusMonth});
                model.addRow(new Object[]{"Annual Base Salary", annualSalary});
                model.addRow(new Object[]{"5% Annual Tax on Base Salary", tax});
                model.addRow(new Object[]{"Monthly Salary After Tax", String.format("%.2f", afterTaxMonthly)});
                model.addRow(new Object[]{"Total Pay (Monthly)", String.format("%.2f", totalPayMonthly)});

                salaryTable.setModel(model);
            } else {
                JOptionPane.showMessageDialog(this, "No record found for Employee ID: " + empId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching salary: " + e.getMessage());
        }
    }

    private void updateSalary() {
        String empId = empIdField.getText().trim();
        String salary = salaryField.getText().trim();
        if (empId.isEmpty() || salary.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both fields are required.");
            return;
        }

        try {
            double salaryNum = Double.parseDouble(salary);
            Conn c = new Conn();
            int result = c.s.executeUpdate("UPDATE employee SET salary = '" + salaryNum + "' WHERE empId = '" + empId + "'");
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Salary updated successfully!");
                fetchSalary();
            } else {
                JOptionPane.showMessageDialog(this, "Employee not found.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric salary.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Update failed: " + e.getMessage());
        }
    }

    private void applyBonusByPercentage() {
        String[] options = {"Eid", "Pohela Boishakh", "Extra"};
        String reason = (String) JOptionPane.showInputDialog(this, "Select Bonus Reason:", "Bonus Type",
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (reason == null) return;

        String bonusPercentStr = JOptionPane.showInputDialog(this, "Enter Bonus Percentage (e.g., 30 for 30%):");
        if (bonusPercentStr == null || bonusPercentStr.trim().isEmpty()) return;

        double bonusPercent;
        try {
            bonusPercent = Double.parseDouble(bonusPercentStr);
            if (bonusPercent < 0) {
                JOptionPane.showMessageDialog(this, "Bonus percentage cannot be negative.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid percentage value.");
            return;
        }

        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        String selectedMonth = (String) JOptionPane.showInputDialog(this, "Select Bonus Month:", "Bonus Month",
                JOptionPane.PLAIN_MESSAGE, null, months, months[LocalDate.now().getMonthValue() - 1]);
        if (selectedMonth == null) return;

        String year = JOptionPane.showInputDialog(this, "Enter Bonus Year (e.g., 2025):");
        if (year == null || year.trim().isEmpty()) return;

        String monthYear = selectedMonth + " " + year;

        try {
            Conn c = new Conn();
            // Update bonus as percentage of salary, update bonus_month
            int updated = c.s.executeUpdate("UPDATE employee SET bonus = salary * (" + (bonusPercent / 100) + "), bonus_month = '" + monthYear + "'");

            if (updated > 0) {
                JOptionPane.showMessageDialog(this, bonusPercent + "% Bonus applied to all employees for: " + reason + " (" + monthYear + ")");
                fetchSalary();
            } else {
                JOptionPane.showMessageDialog(this, "No employees updated.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error applying bonus.");
        }
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(180, 45));
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Tahoma", Font.PLAIN, 18));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(220, 235, 255));
        table.getTableHeader().setForeground(new Color(0, 102, 204));
        table.setSelectionBackground(new Color(204, 229, 255));
        table.setGridColor(Color.LIGHT_GRAY);
    }

    public static void main(String[] args) {
        new ManageSalaryAdmin(null);
    }
}
*//*
package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.awt.geom.RoundRectangle2D;

public class ManageSalaryAdmin extends JFrame {

    private JTextField empIdField, salaryField;
    private JTable salaryTable;
    private JFrame parent;

    // Custom RoundedButton class
    private static class RoundedButton extends JButton {
        public RoundedButton(String text, Color baseColor) {
            super(text);
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setBackground(baseColor);
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBackground(baseColor.darker());
                    repaint();
                }
                public void mouseExited(MouseEvent e) {
                    setBackground(baseColor);
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
            // No border
        }
    }

    public ManageSalaryAdmin(JFrame parent) {
        this.parent = parent;

        setTitle("Manage Salary - Admin");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Header Panel
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        
        JPanel headerContainer = new JPanel(null);
        headerContainer.setPreferredSize(new Dimension(screenWidth, 80));
        headerContainer.setBackground(new Color(0, 102, 204));
        
        JLabel heading = new JLabel("Manage Salary", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 32));
        heading.setForeground(Color.WHITE);
        heading.setBounds(0, 0, screenWidth, 80);
        
        headerContainer.add(heading);
        add(headerContainer, BorderLayout.NORTH);

        // Center Panel for input fields and buttons
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));
        centerPanel.setBackground(new Color(240, 248, 255));

        JLabel empIdLabel = new JLabel("Employee ID:");
        empIdLabel.setFont(new Font("Arial", Font.BOLD, 18));
        empIdField = new JTextField(15);
        empIdField.setFont(new Font("Arial", Font.PLAIN, 18));

        JLabel salaryLabel = new JLabel("Salary :");
        salaryLabel.setFont(new Font("Arial", Font.BOLD, 18));
        salaryField = new JTextField(15);
        salaryField.setFont(new Font("Arial", Font.PLAIN, 18));

        JButton searchBtn = new RoundedButton("Search", new Color(0, 102, 204));
        searchBtn.addActionListener(e -> fetchSalary());

        JButton updateBtn = new RoundedButton("Update Salary", new Color(34, 139, 34));
        updateBtn.addActionListener(e -> updateSalary());

        JButton bonusBtn = new RoundedButton("Add Bonus", new Color(255, 140, 0));
        bonusBtn.addActionListener(e -> applyBonusByPercentage());
        
        centerPanel.add(empIdLabel);
        centerPanel.add(empIdField);
        centerPanel.add(searchBtn);
        centerPanel.add(salaryLabel);
        centerPanel.add(salaryField);
        centerPanel.add(updateBtn);
        centerPanel.add(bonusBtn);

        // Main content panel to hold the center panel and table
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBackground(new Color(240, 248, 255));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        mainContentPanel.add(centerPanel);
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        salaryTable = new JTable();
        styleTable(salaryTable);
        JScrollPane tablePane = new JScrollPane(salaryTable);
        tablePane.getViewport().setBackground(Color.WHITE);
        tablePane.setMaximumSize(new Dimension(1000, 400));
        mainContentPanel.add(tablePane);
        
        // Add main content panel to the frame
        JPanel containerPanel = new JPanel(new GridBagLayout());
        containerPanel.setBackground(new Color(240, 248, 255));
        containerPanel.add(mainContentPanel);
        add(containerPanel, BorderLayout.CENTER);

        // Bottom panel for back and print buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(240, 248, 255));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));

        JButton backBtn = new RoundedButton("Back", Color.RED);
        backBtn.addActionListener(e -> {
            dispose();
            if (parent != null) parent.setVisible(true);
        });

        JButton printBtn = new RoundedButton("Print", new Color(0, 102, 204));
        printBtn.addActionListener(e -> {
            try {
                salaryTable.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Printing failed: " + ex.getMessage());
            }
        });

        bottomPanel.add(backBtn);
        bottomPanel.add(printBtn);
        add(bottomPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }

    private void fetchSalary() {
        String empId = empIdField.getText().trim();
        if (empId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Employee ID.");
            return;
        }

        try {
            Conn c = new Conn();
            String query = "SELECT salary, bonus, bonus_month FROM employee WHERE empId = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, empId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double baseSalary = rs.getDouble("salary");
                double bonus = rs.getDouble("bonus");
                String bonusMonth = rs.getString("bonus_month");

                salaryField.setText(String.valueOf(baseSalary));

                double annualSalary = baseSalary * 12;
                double tax = annualSalary * 0.05;
                double afterTaxMonthly = (annualSalary - tax) / 12;
                double totalPayMonthly = afterTaxMonthly + bonus;

                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("Description");
                model.addColumn("Amount");
                model.addRow(new Object[]{"Base Salary (Monthly)", String.format("%.2f", baseSalary)});
                model.addRow(new Object[]{"Bonus", String.format("%.2f", bonus)});
                model.addRow(new Object[]{"Bonus Month", bonusMonth != null ? bonusMonth : "N/A"});
                model.addRow(new Object[]{"Annual Base Salary", String.format("%.2f", annualSalary)});
                model.addRow(new Object[]{"5% Annual Tax", String.format("%.2f", tax)});
                model.addRow(new Object[]{"Monthly Salary After Tax", String.format("%.2f", afterTaxMonthly)});
                model.addRow(new Object[]{"Total Pay (Monthly)", String.format("%.2f", totalPayMonthly)});

                salaryTable.setModel(model);
            } else {
                JOptionPane.showMessageDialog(this, "No record found for Employee ID: " + empId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching salary: " + e.getMessage());
        }
    }

    private void updateSalary() {
        String empId = empIdField.getText().trim();
        String salary = salaryField.getText().trim();
        if (empId.isEmpty() || salary.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both fields are required.");
            return;
        }

        try {
            double salaryNum = Double.parseDouble(salary);
            Conn c = new Conn();
            String query = "UPDATE employee SET salary = ? WHERE empId = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setDouble(1, salaryNum);
            ps.setString(2, empId);
            int result = ps.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Salary updated successfully!");
                fetchSalary();
            } else {
                JOptionPane.showMessageDialog(this, "Employee not found.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric salary.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Update failed: " + e.getMessage());
        }
    }

    private void applyBonusByPercentage() {
        String[] options = {"Eid", "Pohela Boishakh", "Extra"};
        String reason = (String) JOptionPane.showInputDialog(this, "Select Bonus Reason:", "Bonus Type",
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (reason == null) return;

        String bonusPercentStr = JOptionPane.showInputDialog(this, "Enter Bonus Percentage (e.g., 30 for 30%):");
        if (bonusPercentStr == null || bonusPercentStr.trim().isEmpty()) return;

        double bonusPercent;
        try {
            bonusPercent = Double.parseDouble(bonusPercentStr);
            if (bonusPercent < 0) {
                JOptionPane.showMessageDialog(this, "Bonus percentage cannot be negative.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid percentage value.");
            return;
        }

        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        String selectedMonth = (String) JOptionPane.showInputDialog(this, "Select Bonus Month:", "Bonus Month",
                JOptionPane.PLAIN_MESSAGE, null, months, months[LocalDate.now().getMonthValue() - 1]);
        if (selectedMonth == null) return;

        String year = JOptionPane.showInputDialog(this, "Enter Bonus Year (e.g., 2025):");
        if (year == null || year.trim().isEmpty()) return;
        
        String monthYear = selectedMonth + " " + year;

        try {
            Conn c = new Conn();
            String query = "UPDATE employee SET bonus = salary * (? / 100), bonus_month = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setDouble(1, bonusPercent);
            ps.setString(2, monthYear);
            int updated = ps.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this, bonusPercent + "% Bonus applied to all employees for: " + reason + " (" + monthYear + ")");
                fetchSalary(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, "No employees updated.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error applying bonus.");
        }
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Tahoma", Font.PLAIN, 18));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(220, 235, 255));
        table.getTableHeader().setForeground(new Color(0, 102, 204));
        table.setSelectionBackground(new Color(204, 229, 255));
        table.setGridColor(Color.LIGHT_GRAY);
    }

    public static void main(String[] args) {
        new ManageSalaryAdmin(null);
    }
}*/
/*
package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.time.LocalDate;
import java.util.Vector;

public class ManageSalaryAdmin extends JFrame implements ActionListener {
    JTextField empIdField;
    JTable table;
    JButton backBtn, printBtn, searchBtn;
    JComboBox<String> monthCombo, yearCombo;

    private static class RoundedButton extends JButton {
        public RoundedButton(String text, Color bgColor, Color fgColor, Font font, Dimension size) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setBackground(bgColor);
            setForeground(fgColor);
            setFont(font);
            setPreferredSize(size);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBackground(bgColor.darker());
                }
                public void mouseExited(MouseEvent e) {
                    setBackground(bgColor);
                }
            });
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
            super.paintComponent(g);
            g2.dispose();
        }
    }

    public ManageSalaryAdmin() {
        setTitle("Manage Salary - Admin");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Manage Salary (Admin)", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(new Color(60, 90, 153));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        headerPanel.add(heading, BorderLayout.NORTH);

        // Search panel (Employee ID + Month + Year + Search button)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        searchPanel.setBackground(new Color(240, 248, 255));

        searchPanel.add(new JLabel("Employee ID:"));
        empIdField = new JTextField(10);
        empIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(empIdField);

        searchPanel.add(new JLabel("Select Month:"));
        String[] months = {"January", "February", "March", "April", "May", "June", 
                           "July", "August", "September", "October", "November", "December"};
        monthCombo = new JComboBox<>(months);
        monthCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(monthCombo);

        searchPanel.add(new JLabel("Select Year:"));
        int currentYear = LocalDate.now().getYear();
        Vector<String> years = new Vector<>();
        for (int i = 2020; i <= currentYear; i++) {
            years.add(String.valueOf(i));
        }
        yearCombo = new JComboBox<>(years);
        yearCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(yearCombo);

        searchBtn = createRoundedButton("Search", new Color(0, 123, 255), Color.WHITE, new Font("Arial", Font.BOLD, 16), new Dimension(100, 40));
        searchPanel.add(searchBtn);

        headerPanel.add(searchPanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        table = new JTable();
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel (Back and Print only, no Loan Request button)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        buttonPanel.setBackground(new Color(240, 248, 255));

        backBtn = createRoundedButton("Back", Color.RED, Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(250, 45));
        printBtn = createRoundedButton("Print Payslip", new Color(34, 139, 34), Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(250, 45));

        buttonPanel.add(backBtn);
        buttonPanel.add(printBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Set default selections to current month/year
        String currentMonth = LocalDate.now().getMonth().toString().substring(0,1) 
                              + LocalDate.now().getMonth().toString().substring(1).toLowerCase();
        monthCombo.setSelectedItem(currentMonth);
        yearCombo.setSelectedItem(String.valueOf(currentYear));

        setVisible(true);
    }

    private JButton createRoundedButton(String text, Color bgColor, Color fgColor, Font font, Dimension size) {
        JButton btn = new RoundedButton(text, bgColor, fgColor, font, size);
        btn.addActionListener(this);
        return btn;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Tahoma", Font.PLAIN, 18));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(60, 90, 153));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setGridColor(Color.LIGHT_GRAY);
    }

    private void loadSalary(String empId, String month, String year) {
        if (empId == null || empId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Employee ID to search.");
            return;
        }

        double baseSalary = 0;
        double bonus = 0;
        String dbBonusMonth = "";
        String designation = "";

        try {
            Conn c = new Conn();
            String query = "SELECT designation, bonus, bonus_month FROM employee WHERE empId = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, empId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                designation = rs.getString("designation");
                bonus = rs.getDouble("bonus");
                dbBonusMonth = rs.getString("bonus_month");
            } else {
                JOptionPane.showMessageDialog(this, "Employee Not Found!");
                return;
            }

            if (designation != null) {
                switch (designation.toLowerCase()) {
                    case "leader":
                        baseSalary = 80000;
                        break;
                    case "senior developer":
                        baseSalary = 60000;
                        break;
                    case "junior developer":
                        baseSalary = 40000;
                        break;
                    case "intern":
                        baseSalary = 15000;
                        break;
                    default:
                        baseSalary = 30000;
                }
            }

            double monthlyBonus = 0;
            if (dbBonusMonth != null && month != null && dbBonusMonth.trim().equalsIgnoreCase(month.trim() + " " + year.trim())) {
                monthlyBonus = bonus;
            }

            double annualBaseSalary = baseSalary * 12;
            double tax = annualBaseSalary * 0.05;
            double monthlyAfterTax = (annualBaseSalary - tax) / 12;
            double totalPayMonthly = monthlyAfterTax + monthlyBonus;

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Description");
            model.addColumn("Amount");

            model.addRow(new Object[]{"Designation", designation});
            model.addRow(new Object[]{"Base Salary (Monthly)", baseSalary});
            model.addRow(new Object[]{"Bonus", monthlyBonus});
            if (month != null && year != null) {
                model.addRow(new Object[]{"Payslip Month & Year", month + ", " + year});
            }
            model.addRow(new Object[]{"Annual Base Salary", String.format("%.2f", annualBaseSalary)});
            model.addRow(new Object[]{"5% Annual Tax", String.format("%.2f", tax)});
            model.addRow(new Object[]{"Monthly Salary After Tax", String.format("%.2f", monthlyAfterTax)});
            model.addRow(new Object[]{"Total Pay (Monthly)", String.format("%.2f", totalPayMonthly)});

            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching salary details: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchBtn) {
            String empIdInput = empIdField.getText().trim();
            String selectedMonth = (String) monthCombo.getSelectedItem();
            String selectedYear = (String) yearCombo.getSelectedItem();
            loadSalary(empIdInput, selectedMonth, selectedYear);
        } else if (e.getSource() == backBtn) {
            dispose();
            // You can add code here to show admin main panel if exists
        } else if (e.getSource() == printBtn) {
            try {
                table.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Printing failed: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new ManageSalaryAdmin();
    }
}
*//*
package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.time.LocalDate;
import java.util.Vector;

public class ManageSalaryAdmin extends JFrame implements ActionListener {
    JTextField empIdField;
    JTable table;
    JButton backBtn, printBtn, searchBtn, bonusBtn;
    JComboBox<String> monthCombo, yearCombo;

    private static class RoundedButton extends JButton {
        public RoundedButton(String text, Color bgColor, Color fgColor, Font font, Dimension size) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setBackground(bgColor);
            setForeground(fgColor);
            setFont(font);
            setPreferredSize(size);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBackground(bgColor.darker());
                }
                public void mouseExited(MouseEvent e) {
                    setBackground(bgColor);
                }
            });
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
            super.paintComponent(g);
            g2.dispose();
        }
    }

    public ManageSalaryAdmin() {
        setTitle("Manage Salary - Admin");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Manage Salary (Admin)", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(new Color(60, 90, 153));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        headerPanel.add(heading, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        searchPanel.setBackground(new Color(240, 248, 255));

        searchPanel.add(new JLabel("Employee ID:"));
        empIdField = new JTextField(10);
        empIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(empIdField);

        searchPanel.add(new JLabel("Select Month:"));
        String[] months = {"January", "February", "March", "April", "May", "June",
                           "July", "August", "September", "October", "November", "December"};
        monthCombo = new JComboBox<>(months);
        monthCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(monthCombo);

        searchPanel.add(new JLabel("Select Year:"));
        int currentYear = LocalDate.now().getYear();
        Vector<String> years = new Vector<>();
        for (int i = 2020; i <= currentYear; i++) {
            years.add(String.valueOf(i));
        }
        yearCombo = new JComboBox<>(years);
        yearCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(yearCombo);

        searchBtn = createRoundedButton("Search", new Color(0, 123, 255), Color.WHITE, new Font("Arial", Font.BOLD, 16), new Dimension(100, 40));
        searchPanel.add(searchBtn);

        headerPanel.add(searchPanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        table = new JTable();
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        buttonPanel.setBackground(new Color(240, 248, 255));

        backBtn = createRoundedButton("Back", Color.RED, Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(200, 45));
        printBtn = createRoundedButton("Print Payslip", new Color(34, 139, 34), Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(200, 45));
        bonusBtn = createRoundedButton("Assign Bonus", new Color(255, 140, 0), Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(200, 45));

        buttonPanel.add(backBtn);
        buttonPanel.add(printBtn);
        buttonPanel.add(bonusBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Set default month/year
        String currentMonth = LocalDate.now().getMonth().toString().substring(0,1)
                            + LocalDate.now().getMonth().toString().substring(1).toLowerCase();
        monthCombo.setSelectedItem(currentMonth);
        yearCombo.setSelectedItem(String.valueOf(currentYear));

        setVisible(true);
    }

    private JButton createRoundedButton(String text, Color bgColor, Color fgColor, Font font, Dimension size) {
        JButton btn = new RoundedButton(text, bgColor, fgColor, font, size);
        btn.addActionListener(this);
        return btn;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Tahoma", Font.PLAIN, 18));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(60, 90, 153));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setGridColor(Color.LIGHT_GRAY);
    }

    private void loadSalary(String empId, String month, String year) {
        if (empId == null || empId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Employee ID to search.");
            return;
        }

        double baseSalary = 0;
        double bonus = 0;
        String dbBonusMonth = "";
        String designation = "";

        try {
            Conn c = new Conn();
            String query = "SELECT designation, bonus, bonus_month FROM employee WHERE empId = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, empId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                designation = rs.getString("designation");
                bonus = rs.getDouble("bonus");
                dbBonusMonth = rs.getString("bonus_month");
            } else {
                JOptionPane.showMessageDialog(this, "Employee Not Found!");
                return;
            }

            switch (designation.toLowerCase()) {
                case "leader": baseSalary = 80000; break;
                case "senior developer": baseSalary = 60000; break;
                case "junior developer": baseSalary = 40000; break;
                case "intern": baseSalary = 15000; break;
                default: baseSalary = 30000;
            }

            double monthlyBonus = (dbBonusMonth != null && dbBonusMonth.equalsIgnoreCase(month + " " + year)) ? bonus : 0;

            double annualBaseSalary = baseSalary * 12;
            double tax = annualBaseSalary * 0.05;
            double monthlyAfterTax = (annualBaseSalary - tax) / 12;
            double totalPayMonthly = monthlyAfterTax + monthlyBonus;

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Description");
            model.addColumn("Amount");

            model.addRow(new Object[]{"Designation", designation});
            model.addRow(new Object[]{"Base Salary (Monthly)", baseSalary});
            model.addRow(new Object[]{"Bonus", monthlyBonus});
            model.addRow(new Object[]{"Payslip Month & Year", month + ", " + year});
            model.addRow(new Object[]{"Annual Base Salary", String.format("%.2f", annualBaseSalary)});
            model.addRow(new Object[]{"5% Annual Tax", String.format("%.2f", tax)});
            model.addRow(new Object[]{"Monthly Salary After Tax", String.format("%.2f", monthlyAfterTax)});
            model.addRow(new Object[]{"Total Pay (Monthly)", String.format("%.2f", totalPayMonthly)});

            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching salary details: " + e.getMessage());
        }
    }

    private void assignBonus() {
        String empId = empIdField.getText().trim();
        if (empId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Employee ID.");
            return;
        }

        String amountStr = JOptionPane.showInputDialog(this, "Enter Bonus Amount:");
        if (amountStr == null || amountStr.trim().isEmpty()) return;

        try {
            double bonus = Double.parseDouble(amountStr);
            String bonusMonth = monthCombo.getSelectedItem() + " " + yearCombo.getSelectedItem();

            Conn c = new Conn();
            String updateQuery = "UPDATE employee SET bonus = ?, bonus_month = ? WHERE empId = ?";
            PreparedStatement ps = c.c.prepareStatement(updateQuery);
            ps.setDouble(1, bonus);
            ps.setString(2, bonusMonth);
            ps.setString(3, empId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Bonus updated successfully.");
                loadSalary(empId, (String) monthCombo.getSelectedItem(), (String) yearCombo.getSelectedItem());
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update bonus. Check Employee ID.");
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Invalid bonus amount.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating bonus: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchBtn) {
            loadSalary(empIdField.getText().trim(), (String) monthCombo.getSelectedItem(), (String) yearCombo.getSelectedItem());
        } else if (e.getSource() == printBtn) {
            try {
                table.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Printing failed: " + ex.getMessage());
            }
        } else if (e.getSource() == backBtn) {
            dispose();
        } else if (e.getSource() == bonusBtn) {
            assignBonus();
        }
    }

    public static void main(String[] args) {
        new ManageSalaryAdmin();
    }
}
*/
/*
package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.time.LocalDate;
import java.util.Vector;

public class ManageSalaryAdmin extends JFrame implements ActionListener {
    JTextField empIdField;
    JTable table;
    JButton backBtn, printBtn, searchBtn, bonusBtn, bonusAllBtn;
    JComboBox<String> monthCombo, yearCombo;

    private static class RoundedButton extends JButton {
        public RoundedButton(String text, Color bgColor, Color fgColor, Font font, Dimension size) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setBackground(bgColor);
            setForeground(fgColor);
            setFont(font);
            setPreferredSize(size);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBackground(bgColor.darker());
                }
                public void mouseExited(MouseEvent e) {
                    setBackground(bgColor);
                }
            });
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
            super.paintComponent(g);
            g2.dispose();
        }
    }

    public ManageSalaryAdmin() {
        setTitle("Manage Salary - Admin");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Manage Salary (Admin)", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(new Color(60, 90, 153));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        headerPanel.add(heading, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        searchPanel.setBackground(new Color(240, 248, 255));

        searchPanel.add(new JLabel("Employee ID:"));
        empIdField = new JTextField(10);
        empIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(empIdField);

        searchPanel.add(new JLabel("Select Month:"));
        String[] months = {"January", "February", "March", "April", "May", "June",
                            "July", "August", "September", "October", "November", "December"};
        monthCombo = new JComboBox<>(months);
        monthCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(monthCombo);

        searchPanel.add(new JLabel("Select Year:"));
        int currentYear = LocalDate.now().getYear();
        Vector<String> years = new Vector<>();
        for (int i = 2020; i <= currentYear; i++) {
            years.add(String.valueOf(i));
        }
        yearCombo = new JComboBox<>(years);
        yearCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(yearCombo);

        searchBtn = createRoundedButton("Search", new Color(0, 123, 255), Color.WHITE, new Font("Arial", Font.BOLD, 16), new Dimension(100, 40));
        searchPanel.add(searchBtn);

        headerPanel.add(searchPanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        table = new JTable();
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        buttonPanel.setBackground(new Color(240, 248, 255));

        backBtn = createRoundedButton("Back", Color.RED, Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(200, 45));
        printBtn = createRoundedButton("Print Payslip", new Color(34, 139, 34), Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(200, 45));
        bonusBtn = createRoundedButton("Assign Bonus to Employee", new Color(255, 140, 0), Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(250, 45));
        bonusAllBtn = createRoundedButton("Assign Bonus to All", new Color(0, 102, 204), Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(250, 45));

        buttonPanel.add(backBtn);
        buttonPanel.add(printBtn);
        buttonPanel.add(bonusBtn);
        buttonPanel.add(bonusAllBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Set default month/year
        String currentMonth = LocalDate.now().getMonth().toString().substring(0,1)
                             + LocalDate.now().getMonth().toString().substring(1).toLowerCase();
        monthCombo.setSelectedItem(currentMonth);
        yearCombo.setSelectedItem(String.valueOf(currentYear));

        setVisible(true);
    }

    private JButton createRoundedButton(String text, Color bgColor, Color fgColor, Font font, Dimension size) {
        JButton btn = new RoundedButton(text, bgColor, fgColor, font, size);
        btn.addActionListener(this);
        return btn;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Tahoma", Font.PLAIN, 18));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(60, 90, 153));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setGridColor(Color.LIGHT_GRAY);
    }

    private void loadSalary(String empId, String month, String year) {
        if (empId == null || empId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Employee ID to search.");
            return;
        }

        double baseSalary = 0;
        double bonus = 0;
        String dbBonusMonth = "";
        String designation = "";

        try {
            Conn c = new Conn();
            String query = "SELECT designation, bonus, bonus_month FROM employee WHERE empId = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, empId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                designation = rs.getString("designation");
                bonus = rs.getDouble("bonus");
                dbBonusMonth = rs.getString("bonus_month");
            } else {
                JOptionPane.showMessageDialog(this, "Employee Not Found!");
                return;
            }
            
            // Set base salary based on designation (as per previous logic)
            switch (designation.toLowerCase()) {
                case "leader": baseSalary = 80000; break;
                case "senior developer": baseSalary = 60000; break;
                case "junior developer": baseSalary = 40000; break;
                case "intern": baseSalary = 15000; break;
                default: baseSalary = 30000;
            }

            double monthlyBonus = (dbBonusMonth != null && dbBonusMonth.equalsIgnoreCase(month + " " + year)) ? bonus : 0;

            double annualBaseSalary = baseSalary * 12;
            double tax = annualBaseSalary * 0.05;
            double monthlyAfterTax = (annualBaseSalary - tax) / 12;
            double totalPayMonthly = monthlyAfterTax + monthlyBonus;

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Description");
            model.addColumn("Amount");

            model.addRow(new Object[]{"Designation", designation});
            model.addRow(new Object[]{"Base Salary (Monthly)", baseSalary});
            model.addRow(new Object[]{"Bonus", monthlyBonus});
            model.addRow(new Object[]{"Payslip Month & Year", month + ", " + year});
            model.addRow(new Object[]{"Annual Base Salary", String.format("%.2f", annualBaseSalary)});
            model.addRow(new Object[]{"5% Annual Tax", String.format("%.2f", tax)});
            model.addRow(new Object[]{"Monthly Salary After Tax", String.format("%.2f", monthlyAfterTax)});
            model.addRow(new Object[]{"Total Pay (Monthly)", String.format("%.2f", totalPayMonthly)});

            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching salary details: " + e.getMessage());
        }
    }
    
    private void assignBonus() {
        String empId = empIdField.getText().trim();
        if (empId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Employee ID to assign bonus.");
            return;
        }
        
        String amountStr = JOptionPane.showInputDialog(this, "Enter Bonus Amount:");
        if (amountStr == null || amountStr.trim().isEmpty()) return;

        try {
            double bonus = Double.parseDouble(amountStr);
            String bonusMonth = monthCombo.getSelectedItem() + " " + yearCombo.getSelectedItem();

            Conn c = new Conn();
            String updateQuery = "UPDATE employee SET bonus = ?, bonus_month = ? WHERE empId = ?";
            PreparedStatement ps = c.c.prepareStatement(updateQuery);
            ps.setDouble(1, bonus);
            ps.setString(2, bonusMonth);
            ps.setString(3, empId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Bonus updated successfully for employee " + empId + ".");
                loadSalary(empId, (String) monthCombo.getSelectedItem(), (String) yearCombo.getSelectedItem());
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update bonus. Check Employee ID.");
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Invalid bonus amount.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating bonus: " + e.getMessage());
        }
    }
    
    private void applyBonusToAllEmployees() {
        String bonusPercentStr = JOptionPane.showInputDialog(this, "Enter Bonus Percentage (e.g., 30 for 30%):");
        if (bonusPercentStr == null || bonusPercentStr.trim().isEmpty()) return;

        double bonusPercent;
        try {
            bonusPercent = Double.parseDouble(bonusPercentStr);
            if (bonusPercent < 0) {
                JOptionPane.showMessageDialog(this, "Bonus percentage cannot be negative.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid percentage value.");
            return;
        }
        
        String selectedMonth = (String) monthCombo.getSelectedItem();
        String selectedYear = (String) yearCombo.getSelectedItem();
        String monthYear = selectedMonth + " " + selectedYear;
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to apply a " + bonusPercent + "% bonus to ALL employees for " + monthYear + "?", "Confirm Bonus", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            Conn c = new Conn();
            String query = "UPDATE employee SET bonus = salary * (? / 100), bonus_month = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setDouble(1, bonusPercent);
            ps.setString(2, monthYear);
            
            int updated = ps.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Bonus applied to " + updated + " employees for: " + monthYear + ".");
                // Clear the table or reload a sample employee to show the effect
                if (!empIdField.getText().isEmpty()) {
                    loadSalary(empIdField.getText().trim(), selectedMonth, selectedYear);
                } else {
                     DefaultTableModel model = new DefaultTableModel();
                     model.addColumn("Message");
                     model.addRow(new Object[]{"Bonus applied to all employees successfully!"});
                     table.setModel(model);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No employees updated.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error applying bonus: " + e.getMessage());
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchBtn) {
            loadSalary(empIdField.getText().trim(), (String) monthCombo.getSelectedItem(), (String) yearCombo.getSelectedItem());
        } else if (e.getSource() == printBtn) {
            try {
                table.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Printing failed: " + ex.getMessage());
            }
        } else if (e.getSource() == backBtn) {
            dispose();
        } else if (e.getSource() == bonusBtn) {
            assignBonus();
        } else if (e.getSource() == bonusAllBtn) {
            applyBonusToAllEmployees();
        }
    }

    public static void main(String[] args) {
        new ManageSalaryAdmin();
    }
}
*/
/*
package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.time.LocalDate;
import java.util.Vector;

public class ManageSalaryAdmin extends JFrame implements ActionListener {
    JTextField empIdField;
    JTable table;
    JButton backBtn, printBtn, searchBtn, bonusBtn, bonusAllBtn;
    JComboBox<String> monthCombo, yearCombo;

    private static class RoundedButton extends JButton {
        public RoundedButton(String text, Color bgColor, Color fgColor, Font font, Dimension size) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setBackground(bgColor);
            setForeground(fgColor);
            setFont(font);
            setPreferredSize(size);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBackground(bgColor.darker());
                }
                public void mouseExited(MouseEvent e) {
                    setBackground(bgColor);
                }
            });
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
            super.paintComponent(g);
            g2.dispose();
        }
    }

    public ManageSalaryAdmin() {
        setTitle("Manage Salary - Admin");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Manage Salary (Admin)", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(new Color(60, 90, 153));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        headerPanel.add(heading, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        searchPanel.setBackground(new Color(240, 248, 255));

        searchPanel.add(new JLabel("Employee ID:"));
        empIdField = new JTextField(10);
        empIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(empIdField);

        searchPanel.add(new JLabel("Select Month:"));
        String[] months = {"", "January", "February", "March", "April", "May", "June",
                            "July", "August", "September", "October", "November", "December"};
        monthCombo = new JComboBox<>(months);
        monthCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(monthCombo);

        searchPanel.add(new JLabel("Select Year:"));
        int currentYear = LocalDate.now().getYear();
        Vector<String> years = new Vector<>();
        years.add("");
        for (int i = 2020; i <= currentYear; i++) {
            years.add(String.valueOf(i));
        }
        yearCombo = new JComboBox<>(years);
        yearCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(yearCombo);

        searchBtn = createRoundedButton("Search", new Color(0, 123, 255), Color.WHITE, new Font("Arial", Font.BOLD, 16), new Dimension(100, 40));
        searchPanel.add(searchBtn);

        headerPanel.add(searchPanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        table = new JTable();
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        buttonPanel.setBackground(new Color(240, 248, 255));

        backBtn = createRoundedButton("Back", Color.RED, Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(200, 45));
        printBtn = createRoundedButton("Print Payslip", new Color(34, 139, 34), Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(200, 45));
        bonusBtn = createRoundedButton("Assign Bonus to Employee", new Color(255, 140, 0), Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(250, 45));
        bonusAllBtn = createRoundedButton("Assign Bonus to All", new Color(0, 102, 204), Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(250, 45));

        buttonPanel.add(backBtn);
        buttonPanel.add(printBtn);
        buttonPanel.add(bonusBtn);
        buttonPanel.add(bonusAllBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Set default year
        yearCombo.setSelectedItem(String.valueOf(currentYear));

        setVisible(true);
    }

    private JButton createRoundedButton(String text, Color bgColor, Color fgColor, Font font, Dimension size) {
        JButton btn = new RoundedButton(text, bgColor, fgColor, font, size);
        btn.addActionListener(this);
        return btn;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Tahoma", Font.PLAIN, 18));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(60, 90, 153));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setGridColor(Color.LIGHT_GRAY);
    }

    private void loadSalary(String empId, String month, String year) {
        double baseSalary = 0;
        double bonus = 0;
        String dbBonusMonth = "";
        String designation = "";

        try {
            Conn c = new Conn();
            String query = "SELECT designation, salary, bonus, bonus_month FROM employee WHERE empId = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, empId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                designation = rs.getString("designation");
                baseSalary = rs.getDouble("salary");
                bonus = rs.getDouble("bonus");
                dbBonusMonth = rs.getString("bonus_month");
            } else {
                JOptionPane.showMessageDialog(this, "Employee Not Found!");
                return;
            }

            double monthlyBonus = (dbBonusMonth != null && (month + " " + year).equalsIgnoreCase(dbBonusMonth)) ? bonus : 0;

            double annualBaseSalary = baseSalary * 12;
            double tax = annualBaseSalary * 0.05;
            double monthlyAfterTax = (annualBaseSalary - tax) / 12;
            double totalPayMonthly = monthlyAfterTax + monthlyBonus;

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Description");
            model.addColumn("Amount");

            model.addRow(new Object[]{"Designation", designation});
            model.addRow(new Object[]{"Base Salary (Monthly)", baseSalary});
            model.addRow(new Object[]{"Bonus", monthlyBonus});
            model.addRow(new Object[]{"Payslip Month & Year", month + ", " + year});
            model.addRow(new Object[]{"Annual Base Salary", String.format("%.2f", annualBaseSalary)});
            model.addRow(new Object[]{"5% Annual Tax", String.format("%.2f", tax)});
            model.addRow(new Object[]{"Monthly Salary After Tax", String.format("%.2f", monthlyAfterTax)});
            model.addRow(new Object[]{"Total Pay (Monthly)", String.format("%.2f", totalPayMonthly)});

            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching salary details: " + e.getMessage());
        }
    }
    
    private void loadCompanyPayroll(String month, String year) {
        String monthYear = month + " " + year;
        double totalSalary = 0;
        double totalBonus = 0;

        try {
            Conn c = new Conn();
            String query = "SELECT SUM(salary) as total_salary, SUM(CASE WHEN bonus_month = ? THEN bonus ELSE 0 END) as total_bonus FROM employee";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, monthYear);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalSalary = rs.getDouble("total_salary");
                totalBonus = rs.getDouble("total_bonus");
            }

            double totalPayroll = totalSalary + totalBonus;

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Description");
            model.addColumn("Amount");

            model.addRow(new Object[]{"Total Monthly Salaries", String.format("%.2f", totalSalary)});
            model.addRow(new Object[]{"Total Bonuses", String.format("%.2f", totalBonus)});
            model.addRow(new Object[]{"Total Payroll for " + monthYear, String.format("%.2f", totalPayroll)});

            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching company payroll: " + e.getMessage());
        }
    }
    
    private void assignBonus() {
        String empId = empIdField.getText().trim();
        if (empId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Employee ID to assign a bonus.");
            return;
        }
        
        String amountStr = JOptionPane.showInputDialog(this, "Enter Bonus Amount:");
        if (amountStr == null || amountStr.trim().isEmpty()) return;

        try {
            double bonus = Double.parseDouble(amountStr);
            String bonusMonth = monthCombo.getSelectedItem() + " " + yearCombo.getSelectedItem();

            Conn c = new Conn();
            String updateQuery = "UPDATE employee SET bonus = ?, bonus_month = ? WHERE empId = ?";
            PreparedStatement ps = c.c.prepareStatement(updateQuery);
            ps.setDouble(1, bonus);
            ps.setString(2, bonusMonth);
            ps.setString(3, empId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Bonus updated successfully for employee " + empId + ".");
                loadSalary(empId, (String) monthCombo.getSelectedItem(), (String) yearCombo.getSelectedItem());
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update bonus. Check Employee ID.");
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Invalid bonus amount.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating bonus: " + e.getMessage());
        }
    }
    
    private void applyBonusToAllEmployees() {
        String bonusPercentStr = JOptionPane.showInputDialog(this, "Enter Bonus Percentage (e.g., 30 for 30%):");
        if (bonusPercentStr == null || bonusPercentStr.trim().isEmpty()) return;

        double bonusPercent;
        try {
            bonusPercent = Double.parseDouble(bonusPercentStr);
            if (bonusPercent < 0) {
                JOptionPane.showMessageDialog(this, "Bonus percentage cannot be negative.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid percentage value.");
            return;
        }
        
        String selectedMonth = (String) monthCombo.getSelectedItem();
        String selectedYear = (String) yearCombo.getSelectedItem();
        
        if (selectedMonth == null || selectedMonth.isEmpty() || selectedYear == null || selectedYear.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a Month and Year to apply the bonus.");
            return;
        }
        
        String monthYear = selectedMonth + " " + selectedYear;
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to apply a " + bonusPercent + "% bonus to ALL employees for " + monthYear + "?", "Confirm Bonus", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            Conn c = new Conn();
            String query = "UPDATE employee SET bonus = salary * (? / 100), bonus_month = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setDouble(1, bonusPercent);
            ps.setString(2, monthYear);
            
            int updated = ps.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Bonus applied to " + updated + " employees for: " + monthYear + ".");
                // Reload the company payroll to show the effect
                loadCompanyPayroll(selectedMonth, selectedYear);
            } else {
                JOptionPane.showMessageDialog(this, "No employees updated.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error applying bonus: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String empId = empIdField.getText().trim();
        String selectedMonth = (String) monthCombo.getSelectedItem();
        String selectedYear = (String) yearCombo.getSelectedItem();
        
        if (e.getSource() == searchBtn) {
            if (!empId.isEmpty() && (selectedMonth == null || selectedMonth.isEmpty()) && (selectedYear == null || selectedYear.isEmpty())) {
                // Search by Employee ID only
                loadSalary(empId, "N/A", "N/A"); // Month/Year are not relevant here, but we pass them to avoid errors
            } else if ((empId == null || empId.isEmpty()) && !selectedMonth.isEmpty() && !selectedYear.isEmpty()) {
                // Search by Month and Year for company payroll
                loadCompanyPayroll(selectedMonth, selectedYear);
            } else {
                JOptionPane.showMessageDialog(this, "Please provide a valid search criteria.\n\n" +
                                                      "Search options:\n" +
                                                      "1. Employee ID only (for a detailed payslip)\n" +
                                                      "2. Month and Year only (for total company payroll)");
            }
        } else if (e.getSource() == printBtn) {
            try {
                table.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Printing failed: " + ex.getMessage());
            }
        } else if (e.getSource() == backBtn) {
            dispose();
        } else if (e.getSource() == bonusBtn) {
            assignBonus();
        } else if (e.getSource() == bonusAllBtn) {
            applyBonusToAllEmployees();
        }
    }

    public static void main(String[] args) {
        new ManageSalaryAdmin();
    }
}*/
/*
package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.time.LocalDate;
import java.util.Vector;

public class ManageSalaryAdmin extends JFrame implements ActionListener {
    JTextField empIdField;
    JTable table;
    JButton backBtn, printBtn, searchBtn, bonusBtn;
    JComboBox<String> monthCombo, yearCombo;
    JPopupMenu bonusMenu;
    JMenuItem assignBonusToEmployeeItem, assignBonusToAllItem;

    private static class RoundedButton extends JButton {
        public RoundedButton(String text, Color bgColor, Color fgColor, Font font, Dimension size) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setBackground(bgColor);
            setForeground(fgColor);
            setFont(font);
            setPreferredSize(size);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBackground(bgColor.darker());
                }
                public void mouseExited(MouseEvent e) {
                    setBackground(bgColor);
                }
            });
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
            super.paintComponent(g);
            g2.dispose();
        }
    }

    public ManageSalaryAdmin() {
        setTitle("Manage Salary - Admin");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Manage Salary (Admin)", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(new Color(60, 90, 153));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        headerPanel.add(heading, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        searchPanel.setBackground(new Color(240, 248, 255));

        searchPanel.add(new JLabel("Employee ID:"));
        empIdField = new JTextField(10);
        empIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(empIdField);

        searchPanel.add(new JLabel("Select Month:"));
        String[] months = {"", "January", "February", "March", "April", "May", "June",
                            "July", "August", "September", "October", "November", "December"};
        monthCombo = new JComboBox<>(months);
        monthCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(monthCombo);

        searchPanel.add(new JLabel("Select Year:"));
        int currentYear = LocalDate.now().getYear();
        Vector<String> years = new Vector<>();
        years.add("");
        for (int i = 2020; i <= currentYear; i++) {
            years.add(String.valueOf(i));
        }
        yearCombo = new JComboBox<>(years);
        yearCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(yearCombo);

        searchBtn = createRoundedButton("Search", new Color(0, 123, 255), Color.WHITE, new Font("Arial", Font.BOLD, 16), new Dimension(100, 40));
        searchPanel.add(searchBtn);

        headerPanel.add(searchPanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        table = new JTable();
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        buttonPanel.setBackground(new Color(240, 248, 255));

        backBtn = createRoundedButton("Back", Color.RED, Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(200, 45));
        printBtn = createRoundedButton("Print Payslip", new Color(34, 139, 34), Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(200, 45));
        
        bonusBtn = createRoundedButton("Bonus", new Color(255, 140, 0), Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(200, 45));
        
        // Create the pop-up menu and its items
        bonusMenu = new JPopupMenu();
        assignBonusToEmployeeItem = new JMenuItem("Assign Bonus to Employee");
        assignBonusToAllItem = new JMenuItem("Assign Bonus to All");
        
        assignBonusToEmployeeItem.addActionListener(this);
        assignBonusToAllItem.addActionListener(this);
        
        bonusMenu.add(assignBonusToEmployeeItem);
        bonusMenu.add(assignBonusToAllItem);
        
        // Add a mouse listener to the bonusBtn to show the popup menu
        bonusBtn.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                bonusMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });
        
        buttonPanel.add(backBtn);
        buttonPanel.add(printBtn);
        buttonPanel.add(bonusBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);

        // Set default year
        yearCombo.setSelectedItem(String.valueOf(currentYear));

        setVisible(true);
    }

    private JButton createRoundedButton(String text, Color bgColor, Color fgColor, Font font, Dimension size) {
        JButton btn = new RoundedButton(text, bgColor, fgColor, font, size);
        btn.addActionListener(this);
        return btn;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Tahoma", Font.PLAIN, 18));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(60, 90, 153));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setGridColor(Color.LIGHT_GRAY);
    }

    private void loadSalary(String empId, String month, String year) {
        double baseSalary = 0;
        double bonus = 0;
        String dbBonusMonth = "";
        String designation = "";

        try {
            Conn c = new Conn();
            String query = "SELECT designation, salary, bonus, bonus_month FROM employee WHERE empId = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, empId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                designation = rs.getString("designation");
                baseSalary = rs.getDouble("salary");
                bonus = rs.getDouble("bonus");
                dbBonusMonth = rs.getString("bonus_month");
            } else {
                JOptionPane.showMessageDialog(this, "Employee Not Found!");
                return;
            }

            double monthlyBonus = (dbBonusMonth != null && (month + " " + year).equalsIgnoreCase(dbBonusMonth)) ? bonus : 0;

            double annualBaseSalary = baseSalary * 12;
            double tax = annualBaseSalary * 0.05;
            double monthlyAfterTax = (annualBaseSalary - tax) / 12;
            double totalPayMonthly = monthlyAfterTax + monthlyBonus;

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Description");
            model.addColumn("Amount");

            model.addRow(new Object[]{"Designation", designation});
            model.addRow(new Object[]{"Base Salary (Monthly)", baseSalary});
            model.addRow(new Object[]{"Bonus", monthlyBonus});
            model.addRow(new Object[]{"Payslip Month & Year", month + ", " + year});
            model.addRow(new Object[]{"Annual Base Salary", String.format("%.2f", annualBaseSalary)});
            model.addRow(new Object[]{"5% Annual Tax", String.format("%.2f", tax)});
            model.addRow(new Object[]{"Monthly Salary After Tax", String.format("%.2f", monthlyAfterTax)});
            model.addRow(new Object[]{"Total Pay (Monthly)", String.format("%.2f", totalPayMonthly)});

            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching salary details: " + e.getMessage());
        }
    }
    
    private void loadCompanyPayroll(String month, String year) {
        String monthYear = month + " " + year;
        double totalSalary = 0;
        double totalBonus = 0;

        try {
            Conn c = new Conn();
            String query = "SELECT SUM(salary) as total_salary, SUM(CASE WHEN bonus_month = ? THEN bonus ELSE 0 END) as total_bonus FROM employee";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, monthYear);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalSalary = rs.getDouble("total_salary");
                totalBonus = rs.getDouble("total_bonus");
            }

            double totalPayroll = totalSalary + totalBonus;

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Description");
            model.addColumn("Amount");

            model.addRow(new Object[]{"Total Monthly Salaries", String.format("%.2f", totalSalary)});
            model.addRow(new Object[]{"Total Bonuses", String.format("%.2f", totalBonus)});
            model.addRow(new Object[]{"Total Payroll for " + monthYear, String.format("%.2f", totalPayroll)});

            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching company payroll: " + e.getMessage());
        }
    }
    
    private void assignBonus() {
        String empId = empIdField.getText().trim();
        if (empId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Employee ID to assign a bonus.");
            return;
        }
        
        String amountStr = JOptionPane.showInputDialog(this, "Enter Bonus Amount:");
        if (amountStr == null || amountStr.trim().isEmpty()) return;

        try {
            double bonus = Double.parseDouble(amountStr);
            String bonusMonth = monthCombo.getSelectedItem() + " " + yearCombo.getSelectedItem();

            Conn c = new Conn();
            String updateQuery = "UPDATE employee SET bonus = ?, bonus_month = ? WHERE empId = ?";
            PreparedStatement ps = c.c.prepareStatement(updateQuery);
            ps.setDouble(1, bonus);
            ps.setString(2, bonusMonth);
            ps.setString(3, empId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Bonus updated successfully for employee " + empId + ".");
                loadSalary(empId, (String) monthCombo.getSelectedItem(), (String) yearCombo.getSelectedItem());
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update bonus. Check Employee ID.");
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Invalid bonus amount.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating bonus: " + e.getMessage());
        }
    }
    
    private void applyBonusToAllEmployees() {
        String bonusPercentStr = JOptionPane.showInputDialog(this, "Enter Bonus Percentage (e.g., 30 for 30%):");
        if (bonusPercentStr == null || bonusPercentStr.trim().isEmpty()) return;

        double bonusPercent;
        try {
            bonusPercent = Double.parseDouble(bonusPercentStr);
            if (bonusPercent < 0) {
                JOptionPane.showMessageDialog(this, "Bonus percentage cannot be negative.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid percentage value.");
            return;
        }
        
        String selectedMonth = (String) monthCombo.getSelectedItem();
        String selectedYear = (String) yearCombo.getSelectedItem();
        
        if (selectedMonth == null || selectedMonth.isEmpty() || selectedYear == null || selectedYear.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a Month and Year to apply the bonus.");
            return;
        }
        
        String monthYear = selectedMonth + " " + selectedYear;
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to apply a " + bonusPercent + "% bonus to ALL employees for " + monthYear + "?", "Confirm Bonus", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            Conn c = new Conn();
            String query = "UPDATE employee SET bonus = salary * (? / 100), bonus_month = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setDouble(1, bonusPercent);
            ps.setString(2, monthYear);
            
            int updated = ps.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Bonus applied to " + updated + " employees for: " + monthYear + ".");
                // Reload the company payroll to show the effect
                loadCompanyPayroll(selectedMonth, selectedYear);
            } else {
                JOptionPane.showMessageDialog(this, "No employees updated.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error applying bonus: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String empId = empIdField.getText().trim();
        String selectedMonth = (String) monthCombo.getSelectedItem();
        String selectedYear = (String) yearCombo.getSelectedItem();
        
        if (e.getSource() == searchBtn) {
            if (!empId.isEmpty() && (selectedMonth == null || selectedMonth.isEmpty()) && (selectedYear == null || selectedYear.isEmpty())) {
                // Search by Employee ID only
                loadSalary(empId, "N/A", "N/A"); // Month/Year are not relevant here, but we pass them to avoid errors
            } else if ((empId == null || empId.isEmpty()) && !selectedMonth.isEmpty() && !selectedYear.isEmpty()) {
                // Search by Month and Year for company payroll
                loadCompanyPayroll(selectedMonth, selectedYear);
            } else {
                JOptionPane.showMessageDialog(this, "Please provide a valid search criteria.\n\n" +
                                                      "Search options:\n" +
                                                      "1. Employee ID only (for a detailed payslip)\n" +
                                                      "2. Month and Year only (for total company payroll)");
            }
        } else if (e.getSource() == printBtn) {
            try {
                table.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Printing failed: " + ex.getMessage());
            }
        } else if (e.getSource() == backBtn) {
            dispose();
        } else if (e.getSource() == assignBonusToEmployeeItem) {
            assignBonus();
        } else if (e.getSource() == assignBonusToAllItem) {
            applyBonusToAllEmployees();
        }
    }

    public static void main(String[] args) {
        new ManageSalaryAdmin();
    }
}*//*
package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.time.LocalDate;
import java.util.Vector;

public class ManageSalaryAdmin extends JFrame implements ActionListener {
    JTextField empIdField;
    JTable table;
    JButton backBtn, printBtn, searchBtn, bonusBtn;
    JComboBox<String> monthCombo, yearCombo;
    JPopupMenu bonusMenu;
    JMenuItem assignBonusToEmployeeItem, assignBonusToAllItem;

    private static class RoundedButton extends JButton {
        public RoundedButton(String text, Color bgColor, Color fgColor, Font font, Dimension size) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setBackground(bgColor);
            setForeground(fgColor);
            setFont(font);
            setPreferredSize(size);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBackground(bgColor.darker());
                }
                public void mouseExited(MouseEvent e) {
                    setBackground(bgColor);
                }
            });
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
            super.paintComponent(g);
            g2.dispose();
        }
    }

    public ManageSalaryAdmin() {
        setTitle("Manage Salary - Admin");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Manage Salary (Admin)", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(new Color(60, 90, 153));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        headerPanel.add(heading, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        searchPanel.setBackground(new Color(240, 248, 255));

        searchPanel.add(new JLabel("Employee ID:"));
        empIdField = new JTextField(10);
        empIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(empIdField);

        searchPanel.add(new JLabel("Select Month:"));
        String[] months = {"", "January", "February", "March", "April", "May", "June",
                            "July", "August", "September", "October", "November", "December"};
        monthCombo = new JComboBox<>(months);
        monthCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(monthCombo);

        searchPanel.add(new JLabel("Select Year:"));
        int currentYear = LocalDate.now().getYear();
        Vector<String> years = new Vector<>();
        years.add("");
        for (int i = 2020; i <= currentYear; i++) {
            years.add(String.valueOf(i));
        }
        yearCombo = new JComboBox<>(years);
        yearCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(yearCombo);

        searchBtn = createRoundedButton("Search", new Color(0, 123, 255), Color.WHITE, new Font("Arial", Font.BOLD, 16), new Dimension(100, 40));
        searchPanel.add(searchBtn);

        headerPanel.add(searchPanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        table = new JTable();
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        buttonPanel.setBackground(new Color(240, 248, 255));

        backBtn = createRoundedButton("Back", Color.RED, Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(200, 45));
        printBtn = createRoundedButton("Print Payslip", new Color(34, 139, 34), Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(200, 45));
        
        bonusBtn = createRoundedButton("Bonus", new Color(255, 140, 0), Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(200, 45));
        
        // Create the pop-up menu and its items
        bonusMenu = new JPopupMenu();
        assignBonusToEmployeeItem = new JMenuItem("Assign Bonus to Employee");
        assignBonusToAllItem = new JMenuItem("Assign Bonus to All");
        
        assignBonusToEmployeeItem.addActionListener(this);
        assignBonusToAllItem.addActionListener(this);
        
        bonusMenu.add(assignBonusToEmployeeItem);
        bonusMenu.add(assignBonusToAllItem);
        
        // Add a mouse listener to the bonusBtn to show the popup menu
        bonusBtn.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // Calculate the center of the frame
                int x = ManageSalaryAdmin.this.getX() + ManageSalaryAdmin.this.getWidth() / 2;
                int y = ManageSalaryAdmin.this.getY() + ManageSalaryAdmin.this.getHeight() / 2;
                
                // Get the size of the popup menu
                bonusMenu.show(e.getComponent(), e.getX(), e.getY()); // First, show it to validate and get its size
                int menuWidth = bonusMenu.getWidth();
                int menuHeight = bonusMenu.getHeight();
                
                // Then, hide it and show it again at the calculated center
                bonusMenu.setVisible(false); // Hide the menu
                bonusMenu.show(ManageSalaryAdmin.this, x - menuWidth / 2, y - menuHeight / 2); // Show at the center
            }
        });
        
        buttonPanel.add(backBtn);
        buttonPanel.add(printBtn);
        buttonPanel.add(bonusBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);

        // Set default year
        yearCombo.setSelectedItem(String.valueOf(currentYear));

        setVisible(true);
    }

    private JButton createRoundedButton(String text, Color bgColor, Color fgColor, Font font, Dimension size) {
        JButton btn = new RoundedButton(text, bgColor, fgColor, font, size);
        btn.addActionListener(this);
        return btn;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Tahoma", Font.PLAIN, 18));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(60, 90, 153));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setGridColor(Color.LIGHT_GRAY);
    }

    private void loadSalary(String empId, String month, String year) {
        double baseSalary = 0;
        double bonus = 0;
        String dbBonusMonth = "";
        String designation = "";

        try {
            Conn c = new Conn();
            String query = "SELECT designation, salary, bonus, bonus_month FROM employee WHERE empId = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, empId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                designation = rs.getString("designation");
                baseSalary = rs.getDouble("salary");
                bonus = rs.getDouble("bonus");
                dbBonusMonth = rs.getString("bonus_month");
            } else {
                JOptionPane.showMessageDialog(this, "Employee Not Found!");
                return;
            }

            double monthlyBonus = (dbBonusMonth != null && (month + " " + year).equalsIgnoreCase(dbBonusMonth)) ? bonus : 0;

            double annualBaseSalary = baseSalary * 12;
            double tax = annualBaseSalary * 0.05;
            double monthlyAfterTax = (annualBaseSalary - tax) / 12;
            double totalPayMonthly = monthlyAfterTax + monthlyBonus;

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Description");
            model.addColumn("Amount");

            model.addRow(new Object[]{"Designation", designation});
            model.addRow(new Object[]{"Base Salary (Monthly)", baseSalary});
            model.addRow(new Object[]{"Bonus", monthlyBonus});
            model.addRow(new Object[]{"Payslip Month & Year", month + ", " + year});
            model.addRow(new Object[]{"Annual Base Salary", String.format("%.2f", annualBaseSalary)});
            model.addRow(new Object[]{"5% Annual Tax", String.format("%.2f", tax)});
            model.addRow(new Object[]{"Monthly Salary After Tax", String.format("%.2f", monthlyAfterTax)});
            model.addRow(new Object[]{"Total Pay (Monthly)", String.format("%.2f", totalPayMonthly)});

            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching salary details: " + e.getMessage());
        }
    }
    
    private void loadCompanyPayroll(String month, String year) {
        String monthYear = month + " " + year;
        double totalSalary = 0;
        double totalBonus = 0;

        try {
            Conn c = new Conn();
            String query = "SELECT SUM(salary) as total_salary, SUM(CASE WHEN bonus_month = ? THEN bonus ELSE 0 END) as total_bonus FROM employee";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, monthYear);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalSalary = rs.getDouble("total_salary");
                totalBonus = rs.getDouble("total_bonus");
            }

            double totalPayroll = totalSalary + totalBonus;

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Description");
            model.addColumn("Amount");

            model.addRow(new Object[]{"Total Monthly Salaries", String.format("%.2f", totalSalary)});
            model.addRow(new Object[]{"Total Bonuses", String.format("%.2f", totalBonus)});
            model.addRow(new Object[]{"Total Payroll for " + monthYear, String.format("%.2f", totalPayroll)});

            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching company payroll: " + e.getMessage());
        }
    }
    
    private void assignBonus() {
        String empId = empIdField.getText().trim();
        if (empId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Employee ID to assign a bonus.");
            return;
        }
        
        String amountStr = JOptionPane.showInputDialog(this, "Enter Bonus Amount:");
        if (amountStr == null || amountStr.trim().isEmpty()) return;

        try {
            double bonus = Double.parseDouble(amountStr);
            String bonusMonth = monthCombo.getSelectedItem() + " " + yearCombo.getSelectedItem();

            Conn c = new Conn();
            String updateQuery = "UPDATE employee SET bonus = ?, bonus_month = ? WHERE empId = ?";
            PreparedStatement ps = c.c.prepareStatement(updateQuery);
            ps.setDouble(1, bonus);
            ps.setString(2, bonusMonth);
            ps.setString(3, empId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Bonus updated successfully for employee " + empId + ".");
                loadSalary(empId, (String) monthCombo.getSelectedItem(), (String) yearCombo.getSelectedItem());
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update bonus. Check Employee ID.");
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Invalid bonus amount.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating bonus: " + e.getMessage());
        }
    }
    
    private void applyBonusToAllEmployees() {
        String bonusPercentStr = JOptionPane.showInputDialog(this, "Enter Bonus Percentage (e.g., 30 for 30%):");
        if (bonusPercentStr == null || bonusPercentStr.trim().isEmpty()) return;

        double bonusPercent;
        try {
            bonusPercent = Double.parseDouble(bonusPercentStr);
            if (bonusPercent < 0) {
                JOptionPane.showMessageDialog(this, "Bonus percentage cannot be negative.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid percentage value.");
            return;
        }
        
        String selectedMonth = (String) monthCombo.getSelectedItem();
        String selectedYear = (String) yearCombo.getSelectedItem();
        
        if (selectedMonth == null || selectedMonth.isEmpty() || selectedYear == null || selectedYear.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a Month and Year to apply the bonus.");
            return;
        }
        
        String monthYear = selectedMonth + " " + selectedYear;
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to apply a " + bonusPercent + "% bonus to ALL employees for " + monthYear + "?", "Confirm Bonus", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            Conn c = new Conn();
            String query = "UPDATE employee SET bonus = salary * (? / 100), bonus_month = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setDouble(1, bonusPercent);
            ps.setString(2, monthYear);
            
            int updated = ps.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Bonus applied to " + updated + " employees for: " + monthYear + ".");
                // Reload the company payroll to show the effect
                loadCompanyPayroll(selectedMonth, selectedYear);
            } else {
                JOptionPane.showMessageDialog(this, "No employees updated.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error applying bonus: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String empId = empIdField.getText().trim();
        String selectedMonth = (String) monthCombo.getSelectedItem();
        String selectedYear = (String) yearCombo.getSelectedItem();
        
        if (e.getSource() == searchBtn) {
            if (!empId.isEmpty() && (selectedMonth == null || selectedMonth.isEmpty()) && (selectedYear == null || selectedYear.isEmpty())) {
                // Search by Employee ID only
                loadSalary(empId, "N/A", "N/A"); // Month/Year are not relevant here, but we pass them to avoid errors
            } else if ((empId == null || empId.isEmpty()) && !selectedMonth.isEmpty() && !selectedYear.isEmpty()) {
                // Search by Month and Year for company payroll
                loadCompanyPayroll(selectedMonth, selectedYear);
            } else {
                // Modified JOptionPane to be centered
                JOptionPane.showMessageDialog(this, "Please provide a valid search criteria.\n\n" +
                                                      "Search options:\n" +
                                                      "1. Employee ID only (for a detailed payslip)\n" +
                                                      "2. Month and Year only (for total company payroll)");
            }
        } else if (e.getSource() == printBtn) {
            try {
                table.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Printing failed: " + ex.getMessage());
            }
        } else if (e.getSource() == backBtn) {
            dispose();
        } else if (e.getSource() == assignBonusToEmployeeItem) {
            assignBonus();
        } else if (e.getSource() == assignBonusToAllItem) {
            applyBonusToAllEmployees();
        }
    }

    public static void main(String[] args) {
        new ManageSalaryAdmin();
    }
}
*//*
package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.time.LocalDate;
import java.util.Vector;

public class ManageSalaryAdmin extends JFrame implements ActionListener {
    JTextField empIdField;
    JTable table;
    JButton backBtn, printBtn, searchBtn, bonusBtn;
    JComboBox<String> monthCombo, yearCombo;
    JPopupMenu bonusMenu;
    JMenuItem assignBonusToEmployeeItem, assignBonusToAllItem;

    private static class RoundedButton extends JButton {
        public RoundedButton(String text, Color bgColor, Color fgColor, Font font, Dimension size) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setBackground(bgColor);
            setForeground(fgColor);
            setFont(font);
            setPreferredSize(size);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBackground(bgColor.darker());
                }
                public void mouseExited(MouseEvent e) {
                    setBackground(bgColor);
                }
            });
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
            super.paintComponent(g);
            g2.dispose();
        }
    }

    public ManageSalaryAdmin() {
        setTitle("Manage Salary - Admin");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Manage Salary (Admin)", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(new Color(60, 90, 153));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        headerPanel.add(heading, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        searchPanel.setBackground(new Color(240, 248, 255));

        searchPanel.add(new JLabel("Employee ID:"));
        empIdField = new JTextField(10);
        empIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(empIdField);

        searchPanel.add(new JLabel("Select Month:"));
        String[] months = {"", "January", "February", "March", "April", "May", "June",
                            "July", "August", "September", "October", "November", "December"};
        monthCombo = new JComboBox<>(months);
        monthCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(monthCombo);

        searchPanel.add(new JLabel("Select Year:"));
        int currentYear = LocalDate.now().getYear();
        Vector<String> years = new Vector<>();
        years.add("");
        for (int i = 2020; i <= currentYear; i++) {
            years.add(String.valueOf(i));
        }
        yearCombo = new JComboBox<>(years);
        yearCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(yearCombo);

        searchBtn = createRoundedButton("Search", new Color(0, 123, 255), Color.WHITE, new Font("Arial", Font.BOLD, 16), new Dimension(100, 40));
        searchPanel.add(searchBtn);

        headerPanel.add(searchPanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        table = new JTable();
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        buttonPanel.setBackground(new Color(240, 248, 255));

        backBtn = createRoundedButton("Back", Color.RED, Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(200, 45));
        printBtn = createRoundedButton("Print Payslip", new Color(34, 139, 34), Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(200, 45));
        
        bonusBtn = createRoundedButton("Bonus", new Color(255, 140, 0), Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(200, 45));
        
        // Create the pop-up menu and its items
        bonusMenu = new JPopupMenu();
        assignBonusToEmployeeItem = new JMenuItem("Assign Bonus to Employee");
        assignBonusToAllItem = new JMenuItem("Assign Bonus to All");
        
        // Set a larger font for the menu items
        Font menuItemFont = new Font("Arial", Font.BOLD, 18);
        assignBonusToEmployeeItem.setFont(menuItemFont);
        assignBonusToAllItem.setFont(menuItemFont);
        
        assignBonusToEmployeeItem.addActionListener(this);
        assignBonusToAllItem.addActionListener(this);
        
        bonusMenu.add(assignBonusToEmployeeItem);
        bonusMenu.add(assignBonusToAllItem);
        
        // Add a mouse listener to the bonusBtn to show the popup menu at the center
        bonusBtn.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // Calculate the center of the frame
                int x = ManageSalaryAdmin.this.getX() + ManageSalaryAdmin.this.getWidth() / 2;
                int y = ManageSalaryAdmin.this.getY() + ManageSalaryAdmin.this.getHeight() / 2;
                
                bonusMenu.show(ManageSalaryAdmin.this, x - bonusMenu.getWidth() / 2, y - bonusMenu.getHeight() / 2);
            }
        });
        
        buttonPanel.add(backBtn);
        buttonPanel.add(printBtn);
        buttonPanel.add(bonusBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);

        // Set default year
        yearCombo.setSelectedItem(String.valueOf(currentYear));

        setVisible(true);
    }

    private JButton createRoundedButton(String text, Color bgColor, Color fgColor, Font font, Dimension size) {
        JButton btn = new RoundedButton(text, bgColor, fgColor, font, size);
        btn.addActionListener(this);
        return btn;
    }

    private void styleTable(JTable table) {
        // Set font size for table data
        table.setFont(new Font("Tahoma", Font.PLAIN, 18));
        // Set row height to match the larger font
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(60, 90, 153));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setGridColor(Color.LIGHT_GRAY);
    }

    private void loadSalary(String empId, String month, String year) {
        double baseSalary = 0;
        double bonus = 0;
        String dbBonusMonth = "";
        String designation = "";

        try {
            Conn c = new Conn();
            String query = "SELECT designation, salary, bonus, bonus_month FROM employee WHERE empId = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, empId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                designation = rs.getString("designation");
                baseSalary = rs.getDouble("salary");
                bonus = rs.getDouble("bonus");
                dbBonusMonth = rs.getString("bonus_month");
            } else {
                JOptionPane.showMessageDialog(this, "Employee Not Found!");
                return;
            }

            double monthlyBonus = (dbBonusMonth != null && (month + " " + year).equalsIgnoreCase(dbBonusMonth)) ? bonus : 0;

            double annualBaseSalary = baseSalary * 12;
            double tax = annualBaseSalary * 0.05;
            double monthlyAfterTax = (annualBaseSalary - tax) / 12;
            double totalPayMonthly = monthlyAfterTax + monthlyBonus;

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Description");
            model.addColumn("Amount");

            model.addRow(new Object[]{"Designation", designation});
            model.addRow(new Object[]{"Base Salary (Monthly)", baseSalary});
            model.addRow(new Object[]{"Bonus", monthlyBonus});
            model.addRow(new Object[]{"Payslip Month & Year", month + ", " + year});
            model.addRow(new Object[]{"Annual Base Salary", String.format("%.2f", annualBaseSalary)});
            model.addRow(new Object[]{"5% Annual Tax", String.format("%.2f", tax)});
            model.addRow(new Object[]{"Monthly Salary After Tax", String.format("%.2f", monthlyAfterTax)});
            model.addRow(new Object[]{"Total Pay (Monthly)", String.format("%.2f", totalPayMonthly)});

            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching salary details: " + e.getMessage());
        }
    }
    
    private void loadCompanyPayroll(String month, String year) {
        String monthYear = month + " " + year;
        double totalSalary = 0;
        double totalBonus = 0;

        try {
            Conn c = new Conn();
            String query = "SELECT SUM(salary) as total_salary, SUM(CASE WHEN bonus_month = ? THEN bonus ELSE 0 END) as total_bonus FROM employee";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, monthYear);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalSalary = rs.getDouble("total_salary");
                totalBonus = rs.getDouble("total_bonus");
            }

            double totalPayroll = totalSalary + totalBonus;

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Description");
            model.addColumn("Amount");

            model.addRow(new Object[]{"Total Monthly Salaries", String.format("%.2f", totalSalary)});
            model.addRow(new Object[]{"Total Bonuses", String.format("%.2f", totalBonus)});
            model.addRow(new Object[]{"Total Payroll for " + monthYear, String.format("%.2f", totalPayroll)});

            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching company payroll: " + e.getMessage());
        }
    }
    
    private void assignBonus() {
        String empId = empIdField.getText().trim();
        if (empId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Employee ID to assign a bonus.");
            return;
        }
        
        String amountStr = JOptionPane.showInputDialog(this, "Enter Bonus Amount:");
        if (amountStr == null || amountStr.trim().isEmpty()) return;

        try {
            double bonus = Double.parseDouble(amountStr);
            String bonusMonth = monthCombo.getSelectedItem() + " " + yearCombo.getSelectedItem();

            Conn c = new Conn();
            String updateQuery = "UPDATE employee SET bonus = ?, bonus_month = ? WHERE empId = ?";
            PreparedStatement ps = c.c.prepareStatement(updateQuery);
            ps.setDouble(1, bonus);
            ps.setString(2, bonusMonth);
            ps.setString(3, empId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Bonus updated successfully for employee " + empId + ".");
                loadSalary(empId, (String) monthCombo.getSelectedItem(), (String) yearCombo.getSelectedItem());
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update bonus. Check Employee ID.");
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Invalid bonus amount.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating bonus: " + e.getMessage());
        }
    }
    
    private void applyBonusToAllEmployees() {
        String bonusPercentStr = JOptionPane.showInputDialog(this, "Enter Bonus Percentage (e.g., 30 for 30%):");
        if (bonusPercentStr == null || bonusPercentStr.trim().isEmpty()) return;

        double bonusPercent;
        try {
            bonusPercent = Double.parseDouble(bonusPercentStr);
            if (bonusPercent < 0) {
                JOptionPane.showMessageDialog(this, "Bonus percentage cannot be negative.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid percentage value.");
            return;
        }
        
        String selectedMonth = (String) monthCombo.getSelectedItem();
        String selectedYear = (String) yearCombo.getSelectedItem();
        
        if (selectedMonth == null || selectedMonth.isEmpty() || selectedYear == null || selectedYear.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a Month and Year to apply the bonus.");
            return;
        }
        
        String monthYear = selectedMonth + " " + selectedYear;
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to apply a " + bonusPercent + "% bonus to ALL employees for " + monthYear + "?", "Confirm Bonus", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            Conn c = new Conn();
            String query = "UPDATE employee SET bonus = salary * (? / 100), bonus_month = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setDouble(1, bonusPercent);
            ps.setString(2, monthYear);
            
            int updated = ps.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Bonus applied to " + updated + " employees for: " + monthYear + ".");
                // Reload the company payroll to show the effect
                loadCompanyPayroll(selectedMonth, selectedYear);
            } else {
                JOptionPane.showMessageDialog(this, "No employees updated.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error applying bonus: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String empId = empIdField.getText().trim();
        String selectedMonth = (String) monthCombo.getSelectedItem();
        String selectedYear = (String) yearCombo.getSelectedItem();
        
        if (e.getSource() == searchBtn) {
            if (!empId.isEmpty() && (selectedMonth == null || selectedMonth.isEmpty()) && (selectedYear == null || selectedYear.isEmpty())) {
                // Search by Employee ID only
                loadSalary(empId, "N/A", "N/A"); // Month/Year are not relevant here, but we pass them to avoid errors
            } else if ((empId == null || empId.isEmpty()) && !selectedMonth.isEmpty() && !selectedYear.isEmpty()) {
                // Search by Month and Year for company payroll
                loadCompanyPayroll(selectedMonth, selectedYear);
            } else {
                // Modified JOptionPane to be centered
                JOptionPane.showMessageDialog(this, "Please provide a valid search criteria.\n\n" +
                                                      "Search options:\n" +
                                                      "1. Employee ID only (for a detailed payslip)\n" +
                                                      "2. Month and Year only (for total company payroll)");
            }
        } else if (e.getSource() == printBtn) {
            try {
                table.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Printing failed: " + ex.getMessage());
            }
        } else if (e.getSource() == backBtn) {
            dispose();
        } else if (e.getSource() == assignBonusToEmployeeItem) {
            assignBonus();
        } else if (e.getSource() == assignBonusToAllItem) {
            applyBonusToAllEmployees();
        }
    }

    public static void main(String[] args) {
        new ManageSalaryAdmin();
    }
}*//*
package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.time.LocalDate;
import java.util.Vector;

public class ManageSalaryAdmin extends JFrame implements ActionListener {
    JTextField empIdField;
    JTable table;
    JButton backBtn, printBtn, searchBtn, bonusBtn;
    JComboBox<String> monthCombo, yearCombo;
    JPopupMenu bonusMenu;
    JMenuItem assignBonusToEmployeeItem, assignBonusToAllItem;

    private static class RoundedButton extends JButton {
        public RoundedButton(String text, Color bgColor, Color fgColor, Font font, Dimension size) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setBackground(bgColor);
            setForeground(fgColor);
            setFont(font);
            setPreferredSize(size);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBackground(bgColor.darker());
                }
                public void mouseExited(MouseEvent e) {
                    setBackground(bgColor);
                }
            });
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
            super.paintComponent(g);
            g2.dispose();
        }
    }

    public ManageSalaryAdmin() {
        setTitle("Manage Salary - Admin");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Manage Salary (Admin)", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(new Color(60, 90, 153));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        headerPanel.add(heading, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        searchPanel.setBackground(new Color(240, 248, 255));

        searchPanel.add(new JLabel("Employee ID:"));
        empIdField = new JTextField(10);
        empIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(empIdField);

        searchPanel.add(new JLabel("Select Month:"));
        String[] months = {"", "January", "February", "March", "April", "May", "June",
                            "July", "August", "September", "October", "November", "December"};
        monthCombo = new JComboBox<>(months);
        monthCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(monthCombo);

        searchPanel.add(new JLabel("Select Year:"));
        int currentYear = LocalDate.now().getYear();
        Vector<String> years = new Vector<>();
        years.add("");
        for (int i = 2020; i <= currentYear; i++) {
            years.add(String.valueOf(i));
        }
        yearCombo = new JComboBox<>(years);
        yearCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(yearCombo);

        searchBtn = createRoundedButton("Search", new Color(0, 123, 255), Color.WHITE, new Font("Arial", Font.BOLD, 16), new Dimension(100, 40));
        searchPanel.add(searchBtn);

        headerPanel.add(searchPanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        table = new JTable();
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        buttonPanel.setBackground(new Color(240, 248, 255));

        backBtn = createRoundedButton("Back", Color.RED, Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(200, 45));
        printBtn = createRoundedButton("Print Payslip", new Color(34, 139, 34), Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(200, 45));
        
        bonusBtn = createRoundedButton("Bonus", new Color(255, 140, 0), Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(200, 45));
        
        // Create the pop-up menu and its items
        bonusMenu = new JPopupMenu();
        assignBonusToEmployeeItem = new JMenuItem("Assign Bonus to Employee");
        assignBonusToAllItem = new JMenuItem("Assign Bonus to All");
        
        // Set a larger font for the menu items
        Font menuItemFont = new Font("Arial", Font.BOLD, 18);
        assignBonusToEmployeeItem.setFont(menuItemFont);
        assignBonusToAllItem.setFont(menuItemFont);
        
        assignBonusToEmployeeItem.addActionListener(this);
        assignBonusToAllItem.addActionListener(this);
        
        bonusMenu.add(assignBonusToEmployeeItem);
        bonusMenu.add(assignBonusToAllItem);
        
        // Add a mouse listener to the bonusBtn to show the popup menu at the center
        bonusBtn.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // Calculate the center of the frame
                int x = ManageSalaryAdmin.this.getX() + ManageSalaryAdmin.this.getWidth() / 2;
                int y = ManageSalaryAdmin.this.getY() + ManageSalaryAdmin.this.getHeight() / 2;
                
                bonusMenu.show(ManageSalaryAdmin.this, x - bonusMenu.getWidth() / 2, y - bonusMenu.getHeight() / 2);
            }
        });
        
        buttonPanel.add(backBtn);
        buttonPanel.add(printBtn);
        buttonPanel.add(bonusBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);

        // Set default year
        yearCombo.setSelectedItem(String.valueOf(currentYear));

        setVisible(true);
    }

    private JButton createRoundedButton(String text, Color bgColor, Color fgColor, Font font, Dimension size) {
        JButton btn = new RoundedButton(text, bgColor, fgColor, font, size);
        btn.addActionListener(this);
        return btn;
    }

    private void styleTable(JTable table) {
        // Set font size for table data
        table.setFont(new Font("Tahoma", Font.PLAIN, 18));
        // Set row height to match the larger font
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(60, 90, 153));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setGridColor(Color.LIGHT_GRAY);
    }

    private void loadSalary(String empId, String month, String year) {
        double baseSalary = 0;
        double bonus = 0;
        String dbBonusMonth = "";
        String designation = "";

        try {
            Conn c = new Conn();
            String query = "SELECT designation, salary, bonus, bonus_month FROM employee WHERE empId = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, empId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                designation = rs.getString("designation");
                baseSalary = rs.getDouble("salary");
                bonus = rs.getDouble("bonus");
                dbBonusMonth = rs.getString("bonus_month");
            } else {
                JOptionPane.showMessageDialog(this, "Employee Not Found!");
                return;
            }

            double monthlyBonus = (dbBonusMonth != null && (month + " " + year).equalsIgnoreCase(dbBonusMonth)) ? bonus : 0;

            double annualBaseSalary = baseSalary * 12;
            double tax = annualBaseSalary * 0.05;
            double monthlyAfterTax = (annualBaseSalary - tax) / 12;
            double totalPayMonthly = monthlyAfterTax + monthlyBonus;

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Description");
            model.addColumn("Amount");

            model.addRow(new Object[]{"Designation", designation});
            model.addRow(new Object[]{"Base Salary (Monthly)", baseSalary});
            model.addRow(new Object[]{"Bonus", monthlyBonus});
            model.addRow(new Object[]{"Payslip Month & Year", month + ", " + year});
            model.addRow(new Object[]{"Annual Base Salary", String.format("%.2f", annualBaseSalary)});
            model.addRow(new Object[]{"5% Annual Tax", String.format("%.2f", tax)});
            model.addRow(new Object[]{"Monthly Salary After Tax", String.format("%.2f", monthlyAfterTax)});
            model.addRow(new Object[]{"Total Pay (Monthly)", String.format("%.2f", totalPayMonthly)});

            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching salary details: " + e.getMessage());
        }
    }
    
    private void loadCompanyPayroll(String month, String year) {
        String monthYear = month + " " + year;
        double totalSalary = 0;
        double totalBonus = 0;

        try {
            Conn c = new Conn();
            String query = "SELECT SUM(salary) as total_salary, SUM(CASE WHEN bonus_month = ? THEN bonus ELSE 0 END) as total_bonus FROM employee";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, monthYear);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalSalary = rs.getDouble("total_salary");
                totalBonus = rs.getDouble("total_bonus");
            }

            double totalPayroll = totalSalary + totalBonus;

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Description");
            model.addColumn("Amount");

            model.addRow(new Object[]{"Total Monthly Salaries", String.format("%.2f", totalSalary)});
            model.addRow(new Object[]{"Total Bonuses", String.format("%.2f", totalBonus)});
            model.addRow(new Object[]{"Total Payroll for " + monthYear, String.format("%.2f", totalPayroll)});

            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching company payroll: " + e.getMessage());
        }
    }

    private void loadYearlyCompanyPayroll(String year) {
        double totalYearlySalary = 0;
        double totalYearlyBonus = 0;

        try {
            Conn c = new Conn();
            // Assuming all employees have a monthly salary and bonuses are given for a specific month
            String query = "SELECT SUM(salary) as total_monthly_salary, SUM(CASE WHEN bonus_month LIKE ? THEN bonus ELSE 0 END) as total_bonus FROM employee";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, "%" + year);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double totalMonthlySalary = rs.getDouble("total_monthly_salary");
                totalYearlySalary = totalMonthlySalary * 12; // 12 months in a year
                totalYearlyBonus = rs.getDouble("total_bonus");
            }

            double totalAnnualPayroll = totalYearlySalary + totalYearlyBonus;

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Description");
            model.addColumn("Amount");

            model.addRow(new Object[]{"Total Annual Salaries", String.format("%.2f", totalYearlySalary)});
            model.addRow(new Object[]{"Total Bonuses for " + year, String.format("%.2f", totalYearlyBonus)});
            model.addRow(new Object[]{"Total Annual Payroll for " + year, String.format("%.2f", totalAnnualPayroll)});

            table.setModel(model);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching yearly company payroll: " + e.getMessage());
        }
    }
    
    private void assignBonus() {
        String empId = empIdField.getText().trim();
        if (empId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Employee ID to assign a bonus.");
            return;
        }
        
        String amountStr = JOptionPane.showInputDialog(this, "Enter Bonus Amount:");
        if (amountStr == null || amountStr.trim().isEmpty()) return;

        try {
            double bonus = Double.parseDouble(amountStr);
            String bonusMonth = monthCombo.getSelectedItem() + " " + yearCombo.getSelectedItem();

            Conn c = new Conn();
            String updateQuery = "UPDATE employee SET bonus = ?, bonus_month = ? WHERE empId = ?";
            PreparedStatement ps = c.c.prepareStatement(updateQuery);
            ps.setDouble(1, bonus);
            ps.setString(2, bonusMonth);
            ps.setString(3, empId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Bonus updated successfully for employee " + empId + ".");
                loadSalary(empId, (String) monthCombo.getSelectedItem(), (String) yearCombo.getSelectedItem());
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update bonus. Check Employee ID.");
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Invalid bonus amount.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating bonus: " + e.getMessage());
        }
    }
    
    private void applyBonusToAllEmployees() {
        String bonusPercentStr = JOptionPane.showInputDialog(this, "Enter Bonus Percentage (e.g., 30 for 30%):");
        if (bonusPercentStr == null || bonusPercentStr.trim().isEmpty()) return;

        double bonusPercent;
        try {
            bonusPercent = Double.parseDouble(bonusPercentStr);
            if (bonusPercent < 0) {
                JOptionPane.showMessageDialog(this, "Bonus percentage cannot be negative.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid percentage value.");
            return;
        }
        
        String selectedMonth = (String) monthCombo.getSelectedItem();
        String selectedYear = (String) yearCombo.getSelectedItem();
        
        if (selectedMonth == null || selectedMonth.isEmpty() || selectedYear == null || selectedYear.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a Month and Year to apply the bonus.");
            return;
        }
        
        String monthYear = selectedMonth + " " + selectedYear;
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to apply a " + bonusPercent + "% bonus to ALL employees for " + monthYear + "?", "Confirm Bonus", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            Conn c = new Conn();
            String query = "UPDATE employee SET bonus = salary * (? / 100), bonus_month = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setDouble(1, bonusPercent);
            ps.setString(2, monthYear);
            
            int updated = ps.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Bonus applied to " + updated + " employees for: " + monthYear + ".");
                // Reload the company payroll to show the effect
                loadCompanyPayroll(selectedMonth, selectedYear);
            } else {
                JOptionPane.showMessageDialog(this, "No employees updated.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error applying bonus: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String empId = empIdField.getText().trim();
        String selectedMonth = (String) monthCombo.getSelectedItem();
        String selectedYear = (String) yearCombo.getSelectedItem();
        
        if (e.getSource() == searchBtn) {
            if (!empId.isEmpty() && (selectedMonth == null || selectedMonth.isEmpty()) && (selectedYear == null || selectedYear.isEmpty())) {
                // Search by Employee ID only
                loadSalary(empId, "N/A", "N/A"); // Month/Year are not relevant here, but we pass them to avoid errors
            } else if ((empId == null || empId.isEmpty()) && !selectedMonth.isEmpty() && !selectedYear.isEmpty()) {
                // Search by Month and Year for company payroll
                loadCompanyPayroll(selectedMonth, selectedYear);
            } else if ((empId == null || empId.isEmpty()) && (selectedMonth == null || selectedMonth.isEmpty()) && !selectedYear.isEmpty()) {
                // Search by Year only for total yearly payroll
                loadYearlyCompanyPayroll(selectedYear);
            } else {
                // Show this message for all other invalid combinations
                JOptionPane.showMessageDialog(this, "Please provide a valid search criteria.\n\n" +
                                                      "Search options:\n" +
                                                      "1. Employee ID only (for a detailed payslip)\n" +
                                                      "2. Month and Year only (for total monthly company payroll)\n" +
                                                      "3. Year only (for total yearly company payroll)");
            }
        } else if (e.getSource() == printBtn) {
            try {
                table.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Printing failed: " + ex.getMessage());
            }
        } else if (e.getSource() == backBtn) {
            dispose();
        } else if (e.getSource() == assignBonusToEmployeeItem) {
            assignBonus();
        } else if (e.getSource() == assignBonusToAllItem) {
            applyBonusToAllEmployees();
        }
    }

    public static void main(String[] args) {
        new ManageSalaryAdmin();
    }
}*/

package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.time.LocalDate;
import java.util.Vector;

public class ManageSalaryAdmin extends JFrame implements ActionListener {
    JTextField empIdField;
    JTable table;
    JButton backBtn, printBtn, searchBtn, bonusBtn;
    JComboBox<String> monthCombo, yearCombo;
    JPopupMenu bonusMenu;
    JMenuItem assignBonusToEmployeeItem, assignBonusToAllItem;

    private static class RoundedButton extends JButton {
        public RoundedButton(String text, Color bgColor, Color fgColor, Font font, Dimension size) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setBackground(bgColor);
            setForeground(fgColor);
            setFont(font);
            setPreferredSize(size);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBackground(bgColor.darker());
                }
                public void mouseExited(MouseEvent e) {
                    setBackground(bgColor);
                }
            });
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
            super.paintComponent(g);
            g2.dispose();
        }
    }

    public ManageSalaryAdmin() {
        setTitle("Manage Salary - Admin");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Manage Salary (Admin)", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(new Color(60, 90, 153));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        headerPanel.add(heading, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        searchPanel.setBackground(new Color(240, 248, 255));

        searchPanel.add(new JLabel("Employee ID:"));
        empIdField = new JTextField(10);
        empIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(empIdField);

        searchPanel.add(new JLabel("Select Month:"));
        String[] months = {"", "January", "February", "March", "April", "May", "June",
                            "July", "August", "September", "October", "November", "December"};
        monthCombo = new JComboBox<>(months);
        monthCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(monthCombo);

        searchPanel.add(new JLabel("Select Year:"));
        int currentYear = LocalDate.now().getYear();
        Vector<String> years = new Vector<>();
        years.add("");
        for (int i = 2020; i <= currentYear; i++) {
            years.add(String.valueOf(i));
        }
        yearCombo = new JComboBox<>(years);
        yearCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(yearCombo);

        searchBtn = createRoundedButton("Search", new Color(0, 123, 255), Color.WHITE, new Font("Arial", Font.BOLD, 16), new Dimension(100, 40));
        searchPanel.add(searchBtn);

        headerPanel.add(searchPanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        table = new JTable();
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        buttonPanel.setBackground(new Color(240, 248, 255));

        backBtn = createRoundedButton("Back", Color.RED, Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(200, 45));
        printBtn = createRoundedButton("Print Payslip", new Color(34, 139, 34), Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(200, 45));
        
        bonusBtn = createRoundedButton("Bonus", new Color(255, 140, 0), Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(200, 45));
        
        // Create the pop-up menu and its items
        bonusMenu = new JPopupMenu();
        assignBonusToEmployeeItem = new JMenuItem("Assign Bonus to Employee");
        assignBonusToAllItem = new JMenuItem("Assign Bonus to All");
        
        // Set a larger font for the menu items
        Font menuItemFont = new Font("Arial", Font.BOLD, 18);
        assignBonusToEmployeeItem.setFont(menuItemFont);
        assignBonusToAllItem.setFont(menuItemFont);
        
        assignBonusToEmployeeItem.addActionListener(this);
        assignBonusToAllItem.addActionListener(this);
        
        bonusMenu.add(assignBonusToEmployeeItem);
        bonusMenu.add(assignBonusToAllItem);
        
        // Add a mouse listener to the bonusBtn to show the popup menu at the center
        bonusBtn.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // Calculate the center of the frame
                int x = ManageSalaryAdmin.this.getX() + ManageSalaryAdmin.this.getWidth() / 2;
                int y = ManageSalaryAdmin.this.getY() + ManageSalaryAdmin.this.getHeight() / 2;
                
                bonusMenu.show(ManageSalaryAdmin.this, x - bonusMenu.getWidth() / 2, y - bonusMenu.getHeight() / 2);
            }
        });
        
        buttonPanel.add(backBtn);
        buttonPanel.add(printBtn);
        buttonPanel.add(bonusBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);

        // Set default year
        yearCombo.setSelectedItem(String.valueOf(currentYear));

        setVisible(true);
    }

    private JButton createRoundedButton(String text, Color bgColor, Color fgColor, Font font, Dimension size) {
        JButton btn = new RoundedButton(text, bgColor, fgColor, font, size);
        btn.addActionListener(this);
        return btn;
    }

    private void styleTable(JTable table) {
        // Set font size for table data
        table.setFont(new Font("Tahoma", Font.PLAIN, 18));
        // Set row height to match the larger font
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(60, 90, 153));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setGridColor(Color.LIGHT_GRAY);
    }

    private void loadSalary(String empId, String month, String year) {
        double baseSalary = 0;
        double bonus = 0;
        String dbBonusMonth = "";
        String designation = "";

        try {
            Conn c = new Conn();
            String query = "SELECT designation, salary, bonus, bonus_month FROM employee WHERE empId = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, empId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                designation = rs.getString("designation");
                baseSalary = rs.getDouble("salary");
                bonus = rs.getDouble("bonus");
                dbBonusMonth = rs.getString("bonus_month");
            } else {
                JOptionPane.showMessageDialog(this, "Employee Not Found!");
                return;
            }

            double monthlyBonus = (dbBonusMonth != null && (month + " " + year).equalsIgnoreCase(dbBonusMonth)) ? bonus : 0;

            double annualBaseSalary = baseSalary * 12;
            double tax = annualBaseSalary * 0.05;
            double monthlyAfterTax = (annualBaseSalary - tax) / 12;
            double totalPayMonthly = monthlyAfterTax + monthlyBonus;

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Description");
            model.addColumn("Amount");

            model.addRow(new Object[]{"Designation", designation});
            model.addRow(new Object[]{"Base Salary (Monthly)", baseSalary});
            model.addRow(new Object[]{"Bonus", monthlyBonus});
            model.addRow(new Object[]{"Payslip Month & Year", month + ", " + year});
            model.addRow(new Object[]{"Annual Base Salary", String.format("%.2f", annualBaseSalary)});
            model.addRow(new Object[]{"5% Annual Tax", String.format("%.2f", tax)});
            model.addRow(new Object[]{"Monthly Salary After Tax", String.format("%.2f", monthlyAfterTax)});
            model.addRow(new Object[]{"Total Pay (Monthly)", String.format("%.2f", totalPayMonthly)});

            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching salary details: " + e.getMessage());
        }
    }
    
    private void loadCompanyPayroll(String month, String year) {
        String monthYear = month + " " + year;
        double totalSalary = 0;
        double totalBonus = 0;

        try {
            Conn c = new Conn();
            String query = "SELECT SUM(salary) as total_salary, SUM(CASE WHEN bonus_month = ? THEN bonus ELSE 0 END) as total_bonus FROM employee";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, monthYear);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalSalary = rs.getDouble("total_salary");
                totalBonus = rs.getDouble("total_bonus");
            }

            double totalPayroll = totalSalary + totalBonus;

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Description");
            model.addColumn("Amount");

            model.addRow(new Object[]{"Total Monthly Salaries", String.format("%.2f", totalSalary)});
            model.addRow(new Object[]{"Total Bonuses", String.format("%.2f", totalBonus)});
            model.addRow(new Object[]{"Total Payroll for " + monthYear, String.format("%.2f", totalPayroll)});

            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching company payroll: " + e.getMessage());
        }
    }

    private void loadYearlyCompanyPayroll(String year) {
        double totalYearlySalary = 0;
        double totalYearlyBonus = 0;

        try {
            Conn c = new Conn();
            // Assuming all employees have a monthly salary and bonuses are given for a specific month
            String query = "SELECT SUM(salary) as total_monthly_salary, SUM(CASE WHEN bonus_month LIKE ? THEN bonus ELSE 0 END) as total_bonus FROM employee";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, "%" + year);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double totalMonthlySalary = rs.getDouble("total_monthly_salary");
                totalYearlySalary = totalMonthlySalary * 12; // 12 months in a year
                totalYearlyBonus = rs.getDouble("total_bonus");
            }

            double totalAnnualPayroll = totalYearlySalary + totalYearlyBonus;

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Description");
            model.addColumn("Amount");

            model.addRow(new Object[]{"Total Annual Salaries", String.format("%.2f", totalYearlySalary)});
            model.addRow(new Object[]{"Total Bonuses for " + year, String.format("%.2f", totalYearlyBonus)});
            model.addRow(new Object[]{"Total Annual Payroll for " + year, String.format("%.2f", totalAnnualPayroll)});

            table.setModel(model);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching yearly company payroll: " + e.getMessage());
        }
    }
    
    private void assignBonus() {
        String empId = empIdField.getText().trim();
        if (empId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Employee ID to assign a bonus.");
            return;
        }
        
        String amountStr = JOptionPane.showInputDialog(this, "Enter Bonus Amount:");
        if (amountStr == null || amountStr.trim().isEmpty()) return;

        try {
            double bonus = Double.parseDouble(amountStr);
            String bonusMonth = monthCombo.getSelectedItem() + " " + yearCombo.getSelectedItem();

            Conn c = new Conn();
            String updateQuery = "UPDATE employee SET bonus = ?, bonus_month = ? WHERE empId = ?";
            PreparedStatement ps = c.c.prepareStatement(updateQuery);
            ps.setDouble(1, bonus);
            ps.setString(2, bonusMonth);
            ps.setString(3, empId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Bonus updated successfully for employee " + empId + ".");
                loadSalary(empId, (String) monthCombo.getSelectedItem(), (String) yearCombo.getSelectedItem());
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update bonus. Check Employee ID.");
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Invalid bonus amount.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating bonus: " + e.getMessage());
        }
    }
    
    private void applyBonusToAllEmployees() {
        String bonusPercentStr = JOptionPane.showInputDialog(this, "Enter Bonus Percentage (e.g., 30 for 30%):");
        if (bonusPercentStr == null || bonusPercentStr.trim().isEmpty()) return;

        double bonusPercent;
        try {
            bonusPercent = Double.parseDouble(bonusPercentStr);
            if (bonusPercent < 0) {
                JOptionPane.showMessageDialog(this, "Bonus percentage cannot be negative.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid percentage value.");
            return;
        }
        
        String selectedMonth = (String) monthCombo.getSelectedItem();
        String selectedYear = (String) yearCombo.getSelectedItem();
        
        if (selectedMonth == null || selectedMonth.isEmpty() || selectedYear == null || selectedYear.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a Month and Year to apply the bonus.");
            return;
        }
        
        String monthYear = selectedMonth + " " + selectedYear;
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to apply a " + bonusPercent + "% bonus to ALL employees for " + monthYear + "?", "Confirm Bonus", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            Conn c = new Conn();
            String query = "UPDATE employee SET bonus = salary * (? / 100), bonus_month = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setDouble(1, bonusPercent);
            ps.setString(2, monthYear);
            
            int updated = ps.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Bonus applied to " + updated + " employees for: " + monthYear + ".");
                // Reload the company payroll to show the effect
                loadCompanyPayroll(selectedMonth, selectedYear);
            } else {
                JOptionPane.showMessageDialog(this, "No employees updated.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error applying bonus: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String empId = empIdField.getText().trim();
        String selectedMonth = (String) monthCombo.getSelectedItem();
        String selectedYear = (String) yearCombo.getSelectedItem();
        
        if (e.getSource() == searchBtn) {
            boolean isEmpIdValid = !empId.isEmpty();
            boolean isMonthValid = (selectedMonth != null && !selectedMonth.isEmpty());
            boolean isYearValid = (selectedYear != null && !selectedYear.isEmpty());

            if (isEmpIdValid && !isMonthValid && !isYearValid) {
                // Search by Employee ID only
                loadSalary(empId, "N/A", "N/A");
            } else if (isEmpIdValid && isMonthValid && isYearValid) {
                // Search by Employee ID, Month, and Year
                loadSalary(empId, selectedMonth, selectedYear);
            } else if (!isEmpIdValid && isMonthValid && isYearValid) {
                // Search by Month and Year for company payroll
                loadCompanyPayroll(selectedMonth, selectedYear);
            } else if (!isEmpIdValid && !isMonthValid && isYearValid) {
                // Search by Year only for total yearly payroll
                loadYearlyCompanyPayroll(selectedYear);
            } else {
                // Show this message for all other invalid combinations
                JOptionPane.showMessageDialog(this, "Please provide a valid search criteria.\n\n" +
                                                      "Search options:\n" +
                                                      "1. Employee ID only (for a detailed payslip)\n" +
                                                      "2. Employee ID, Month, and Year (for a specific monthly payslip)\n" +
                                                      "3. Month and Year only (for total monthly company payroll)\n" +
                                                      "4. Year only (for total yearly company payroll)");
            }
        } else if (e.getSource() == printBtn) {
            try {
                table.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Printing failed: " + ex.getMessage());
            }
        } else if (e.getSource() == backBtn) {
            new Home();
            dispose();
        } else if (e.getSource() == assignBonusToEmployeeItem) {
            assignBonus();
        } else if (e.getSource() == assignBonusToAllItem) {
            applyBonusToAllEmployees();
        }
    }

    public static void main(String[] args) {
        new ManageSalaryAdmin();
    }
}
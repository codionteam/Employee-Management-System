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

public class SalaryDetails extends JFrame implements ActionListener {
    String empId;
    EmployeePanel parentPanel;
    JTable table;
    JButton backBtn, printBtn, loanStatusBtn, searchBtn;
    JComboBox<String> monthCombo, yearCombo;
    
    // Custom JButton with rounded corners
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
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(bgColor.darker());
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(bgColor);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
            super.paintComponent(g);
            g2.dispose();
        }
    }

    public SalaryDetails(String empId, EmployeePanel parentPanel) {
        this.empId = empId;
        this.parentPanel = parentPanel;

        setTitle("Salary Details");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());

        JLabel heading = new JLabel("Salary Details", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(new Color(60, 90, 153));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        headerPanel.add(heading, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        searchPanel.setBackground(new Color(240, 248, 255));
        
        searchPanel.add(new JLabel("Select Month:"));
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        monthCombo = new JComboBox<>(months);
        monthCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(monthCombo);
        
        searchPanel.add(new JLabel("Select Year:"));
        // Dynamically generating years from a start year to current year
        int currentYear = LocalDate.now().getYear();
        Vector<String> years = new Vector<>();
        for (int i = 2020; i <= currentYear; i++) {
            years.add(String.valueOf(i));
        }
        yearCombo = new JComboBox<>(years);
        yearCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(yearCombo);
        
        searchBtn = new JButton("Search");
        searchBtn.setFont(new Font("Arial", Font.BOLD, 16));
        searchBtn.setBackground(new Color(0, 123, 255));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.addActionListener(this);
        searchPanel.add(searchBtn);
        
        headerPanel.add(searchPanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        table = new JTable();
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));

        backBtn = createRoundedButton("Back", Color.RED, Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(250, 45));
        printBtn = createRoundedButton("Print Payslip", new Color(34, 139, 34), Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(250, 45));
        loanStatusBtn = createRoundedButton("Loan Request Status", new Color(0, 123, 255), Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(250, 45));
        
        buttonPanel.add(backBtn);
        buttonPanel.add(printBtn);
        buttonPanel.add(loanStatusBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load initial data for the current month and year
        loadSalary(String.valueOf(LocalDate.now().getMonth()), String.valueOf(LocalDate.now().getYear()));
        setVisible(true);
    }
    
    private JButton createRoundedButton(String text, Color bgColor, Color fgColor, Font font, Dimension size) {
        JButton button = new RoundedButton(text, bgColor, fgColor, font, size);
        button.addActionListener(this);
        return button;
    }

    private void loadSalary(String month, String year) {
        double baseSalary = 0;
        double bonus = 0;
        String dbBonusMonth = "";
        String designation = "";

        try {
            Conn c = new Conn();
            String query = "SELECT designation, bonus, bonus_month FROM employee WHERE empId = '" + empId + "'";
            ResultSet rs = c.s.executeQuery(query);
            if (rs.next()) {
                designation = rs.getString("designation");
                bonus = rs.getDouble("bonus");
                dbBonusMonth = rs.getString("bonus_month");
            } else {
                JOptionPane.showMessageDialog(this, "Employee Not Found!");
                return;
            }

            // Determine base salary based on designation
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
                        // Fallback salary if designation is not matched
                        baseSalary = 30000;
                }
            }
            
            // Check if the selected month matches the bonus month from DB
            double monthlyBonus = 0;
            if (dbBonusMonth != null && month != null && dbBonusMonth.equalsIgnoreCase(month + " " + year)) {
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
            JOptionPane.showMessageDialog(this, "Error fetching salary details from the database: " + e.getMessage());
        }
    }

    private void showLoanStatus() {
        new LoanStatusDialog(empId);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchBtn) {
            String selectedMonth = (String) monthCombo.getSelectedItem();
            String selectedYear = (String) yearCombo.getSelectedItem();
            loadSalary(selectedMonth, selectedYear);
        } else if (e.getSource() == backBtn) {
            dispose();
            if (parentPanel != null) parentPanel.setVisible(true);
        } else if (e.getSource() == printBtn) {
            try {
                table.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Printing failed: " + ex.getMessage());
            }
        } else if (e.getSource() == loanStatusBtn) {
            showLoanStatus();
        }
    }

    public static void main(String[] args) {
        new SalaryDetails("1006", null);
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

public class SalaryDetails extends JFrame implements ActionListener {
    String empId;
    EmployeePanel parentPanel;
    JTable table;
    JButton backBtn, printBtn, loanStatusBtn, searchBtn;
    JComboBox<String> monthCombo, yearCombo;

    // Custom JButton with rounded corners
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
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(bgColor.darker());
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(bgColor);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
            super.paintComponent(g);
            g2.dispose();
        }
    }

    public SalaryDetails(String empId, EmployeePanel parentPanel) {
        this.empId = empId;
        this.parentPanel = parentPanel;

        setTitle("Salary Details");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());

        JLabel heading = new JLabel("Salary Details", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(new Color(60, 90, 153));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        headerPanel.add(heading, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        searchPanel.setBackground(new Color(240, 248, 255));

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

        searchBtn = new JButton("Search");
        searchBtn.setFont(new Font("Arial", Font.BOLD, 16));
        searchBtn.setBackground(new Color(0, 123, 255));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.addActionListener(this);
        searchPanel.add(searchBtn);

        headerPanel.add(searchPanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        table = new JTable();
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));

        backBtn = createRoundedButton("Back", Color.RED, Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(250, 45));
        printBtn = createRoundedButton("Print Payslip", new Color(34, 139, 34), Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(250, 45));
        loanStatusBtn = createRoundedButton("Loan Request Status", new Color(0, 123, 255), Color.WHITE, new Font("Arial", Font.BOLD, 18), new Dimension(250, 45));

        buttonPanel.add(backBtn);
        buttonPanel.add(printBtn);
        buttonPanel.add(loanStatusBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Set initial combo selections to current month and year
        String currentMonth = LocalDate.now().getMonth().toString().substring(0,1) 
                              + LocalDate.now().getMonth().toString().substring(1).toLowerCase();
        monthCombo.setSelectedItem(currentMonth);
        yearCombo.setSelectedItem(String.valueOf(currentYear));

        // Load initial data for the current month and year
        loadSalary(currentMonth, String.valueOf(currentYear));

        setVisible(true);
    }

    private JButton createRoundedButton(String text, Color bgColor, Color fgColor, Font font, Dimension size) {
        JButton button = new RoundedButton(text, bgColor, fgColor, font, size);
        button.addActionListener(this);
        return button;
    }

    private void loadSalary(String month, String year) {
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

            // Determine base salary based on designation
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
            JOptionPane.showMessageDialog(this, "Error fetching salary details from the database: " + e.getMessage());
        }
    }

    private void showLoanStatus() {
        new LoanStatusDialog(empId);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchBtn) {
            String selectedMonth = (String) monthCombo.getSelectedItem();
            String selectedYear = (String) yearCombo.getSelectedItem();
            loadSalary(selectedMonth, selectedYear);
        } else if (e.getSource() == backBtn) {
            dispose();
            if (parentPanel != null) parentPanel.setVisible(true);
        } else if (e.getSource() == printBtn) {
            try {
                table.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Printing failed: " + ex.getMessage());
            }
        } else if (e.getSource() == loanStatusBtn) {
            showLoanStatus();
        }
    }

    public static void main(String[] args) {
        new SalaryDetails("1006", null);
    }
}

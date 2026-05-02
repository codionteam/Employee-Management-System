/*package employee.management.system;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoanRequestAdmin extends JFrame {

    JTable table;
    DefaultTableModel model;
    JButton back, searchBtn, showAllBtn;
    JTextField searchField, yearField;
    JComboBox<String> monthCombo;
    JLabel totalLoanLabel, pendingLoanLabel, rejectedLoanLabel;

    public LoanRequestAdmin() {
        setTitle("Loan Requests - Admin Panel");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JLabel heading = new JLabel("Loan Requests Management", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.add(heading, BorderLayout.CENTER);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 100));
        add(headerPanel, BorderLayout.NORTH);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));

        JLabel searchLabel = new JLabel("Search by Employee ID:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        topPanel.add(searchLabel);

        searchField = new JTextField(15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        topPanel.add(searchField);

        String[] months = {"All Months", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        monthCombo = new JComboBox<>(months);
        monthCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        topPanel.add(new JLabel("Month:"));
        topPanel.add(monthCombo);

        yearField = new JTextField(5);
        yearField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        topPanel.add(new JLabel("Year:"));
        topPanel.add(yearField);

        searchBtn = new JButton("Search");
        styleButton(searchBtn, new Color(52, 152, 219));
        searchBtn.addActionListener(e -> {
            String empId = searchField.getText().trim();
            String selectedMonth = (String) monthCombo.getSelectedItem();
            String year = yearField.getText().trim();

            if (!empId.isEmpty()) {
                fetchLoanRequests(empId, null, null);
            } else if (!year.isEmpty()) {
                String month = selectedMonth.equals("All Months") ? null : selectedMonth;
                if (!year.matches("\\d{4}")) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid 4-digit year.");
                    return;
                }
                fetchLoanRequests(null, month, year);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter Employee ID or Year to search.");
            }
        });
        topPanel.add(searchBtn);

        showAllBtn = new JButton("Show All");
        styleButton(showAllBtn, new Color(39, 174, 96));
        showAllBtn.addActionListener(e -> {
            searchField.setText("");
            yearField.setText("");
            monthCombo.setSelectedIndex(0);
            fetchLoanRequests(null, null, null);
        });
        topPanel.add(showAllBtn);

        back = new JButton("Back");
        styleButton(back, new Color(231, 76, 60));
        back.addActionListener(e -> {
            dispose();
            new Home();
        });
        topPanel.add(back);

        add(topPanel, BorderLayout.BEFORE_FIRST_LINE);

        String[] columns = {"No", "Employee ID", "Employee Name", "Amount", "Purpose", "Request Date", "Status", "Accepted Date", "Withdraw Status", "Action"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return col == 9;
            }
        };

        table = new JTable(model);
        table.setRowHeight(45);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(52, 152, 219));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setSelectionBackground(new Color(220, 220, 250));

        table.getColumn("Status").setCellRenderer(new StatusColorRenderer());
        table.getColumn("Action").setCellRenderer(new ButtonRenderer());
        table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        add(scroll, BorderLayout.CENTER);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 10));
        totalPanel.setBackground(new Color(52, 152, 219));

        totalLoanLabel = new JLabel("Total Loan Issues = 0.00");
        totalLoanLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalLoanLabel.setForeground(Color.WHITE);
        totalPanel.add(totalLoanLabel);

        pendingLoanLabel = new JLabel("   Pending Loan = 0.00");
        pendingLoanLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        pendingLoanLabel.setForeground(Color.WHITE);
        totalPanel.add(pendingLoanLabel);

        rejectedLoanLabel = new JLabel("   Rejected Loan = 0.00");
        rejectedLoanLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        rejectedLoanLabel.setForeground(Color.WHITE);
        totalPanel.add(rejectedLoanLabel);

        add(totalPanel, BorderLayout.SOUTH);

        fetchLoanRequests(null, null, null);
        setVisible(true);
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(140, 45));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void fetchLoanRequests(String empIdFilter, String monthFilter, String yearFilter) {
        model.setRowCount(0);
        double totalLoanAmount = 0.0, pendingLoanAmount = 0.0, rejectedLoanAmount = 0.0;

        try {
            Conn c = new Conn();
            StringBuilder query = new StringBuilder("SELECT l.id, l.emp_id, e.name, l.amount, l.purpose, l.request_date, l.status, l.accepted_date, l.withdraw_status FROM loan_requests l JOIN employee e ON l.emp_id = e.empId ");
            if (empIdFilter != null && !empIdFilter.isEmpty()) {
                query.append(" WHERE l.emp_id = ?");
            } else if (yearFilter != null && !yearFilter.isEmpty()) {
                query.append(" WHERE YEAR(l.request_date) = ?");
                if (monthFilter != null && !monthFilter.isEmpty()) {
                    query.append(" AND MONTH(l.request_date) = ?");
                }
            }
            query.append(" ORDER BY l.id DESC");

            PreparedStatement ps = c.c.prepareStatement(query.toString());
            if (empIdFilter != null && !empIdFilter.isEmpty()) {
                ps.setString(1, empIdFilter);
            } else if (yearFilter != null && !yearFilter.isEmpty()) {
                ps.setInt(1, Integer.parseInt(yearFilter));
                if (monthFilter != null && !monthFilter.isEmpty()) {
                    ps.setInt(2, Integer.parseInt(monthFilter));
                }
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("id"), rs.getString("emp_id"), rs.getString("name"), rs.getDouble("amount"), rs.getString("purpose"), rs.getDate("request_date"), rs.getString("status"), rs.getDate("accepted_date"), rs.getString("withdraw_status"), "Take Action"});

                double amt = rs.getDouble("amount");
                String status = rs.getString("status"), wd = rs.getString("withdraw_status");
                if ("Approved".equalsIgnoreCase(status) && "Yes".equalsIgnoreCase(wd)) totalLoanAmount += amt;
                if ("Pending".equalsIgnoreCase(status)) pendingLoanAmount += amt;
                if ("Rejected".equalsIgnoreCase(status)) rejectedLoanAmount += amt;
            }
            totalLoanLabel.setText(String.format("Total Loan Issues = %.2f", totalLoanAmount));
            pendingLoanLabel.setText(String.format("   Pending Loan = %.2f", pendingLoanAmount));
            rejectedLoanLabel.setText(String.format("   Rejected Loan = %.2f", rejectedLoanAmount));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load loan requests.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateLoanStatus(int id, String status) {
        try {
            Conn c = new Conn();
            String q = ("Rejected".equalsIgnoreCase(status)) ? "UPDATE loan_requests SET status = ?, withdraw_status = 'No', accepted_date = CURDATE() WHERE id = ?" : "UPDATE loan_requests SET status = ?, accepted_date = CURDATE() WHERE id = ?";
            PreparedStatement ps = c.c.prepareStatement(q);
            ps.setString(1, status);
            ps.setInt(2, id);
            if (ps.executeUpdate() > 0) {
                fetchLoanRequests(null, null, null);
                JOptionPane.showMessageDialog(this, "Status updated to " + status);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating loan status.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    class StatusColorRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            String status = value.toString().toLowerCase();
            c.setForeground("approved".equals(status) ? new Color(39, 174, 96) : "rejected".equals(status) ? new Color(192, 57, 43) : "pending".equals(status) ? new Color(243, 156, 18) : Color.BLACK);
            return c;
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setBackground(new Color(52, 152, 219));
            setForeground(Color.WHITE);
            setFocusPainted(false);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            setText("Take Action");
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Take Action");
            button.setFont(new Font("Segoe UI", Font.BOLD, 16));
            button.setBackground(new Color(52, 152, 219));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.addActionListener(e -> {
                fireEditingStopped();
                String withdraw = (String) model.getValueAt(row, 8);
                if ("Yes".equalsIgnoreCase(withdraw)) {
                    JOptionPane.showMessageDialog(null, "Withdrawn loan cannot be updated.");
                    return;
                }
                int id = (int) model.getValueAt(row, 0);
                String empId = (String) model.getValueAt(row, 1);
                String[] options = {"Approve", "Reject", "Cancel"};
                int choice = JOptionPane.showOptionDialog(LoanRequestAdmin.this, "Choose action for Employee ID: " + empId, "Update Loan Status", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
                if (choice == 0) updateLoanStatus(id, "Approved");
                else if (choice == 1) updateLoanStatus(id, "Rejected");
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
            this.row = row;
            return button;
        }

        public Object getCellEditorValue() {
            return "Take Action";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoanRequestAdmin::new);
    }
}
*//*
package employee.management.system;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.util.Vector;

public class LoanRequestAdmin extends JFrame {

    JTable table;
    DefaultTableModel model;
    JButton back, searchBtn, showAllBtn;
    JTextField searchField, yearField;
    JComboBox<String> monthCombo;
    JLabel totalLoanLabel, pendingLoanLabel, rejectedLoanLabel;

    public LoanRequestAdmin() {
        setTitle("Loan Requests - Admin Panel");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255));

        // --- Header Panel ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 100));
        
        JLabel heading = new JLabel("Loan Requests Management", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);
        headerPanel.add(heading, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // --- Top Panel for Search and Filters ---
        JPanel searchFilterPanel = new JPanel();
        searchFilterPanel.setBackground(new Color(240, 248, 255));
        searchFilterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        searchFilterPanel.add(new JLabel("<html><font size='5' face='Segoe UI' color='#34495e'><b>Search by Employee ID:</b></font></html>"));
        searchField = new JTextField(15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchFilterPanel.add(searchField);

        searchFilterPanel.add(new JLabel("<html><font size='5' face='Segoe UI' color='#34495e'><b>Month:</b></font></html>"));
        String[] months = {"All Months", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        monthCombo = new JComboBox<>(months);
        monthCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchFilterPanel.add(monthCombo);

        searchFilterPanel.add(new JLabel("<html><font size='5' face='Segoe UI' color='#34495e'><b>Year:</b></font></html>"));
        yearField = new JTextField(5);
        yearField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchFilterPanel.add(yearField);

        searchBtn = createRoundedButton("Search", new Color(52, 152, 219));
        showAllBtn = createRoundedButton("Show All", new Color(39, 174, 96));
        
        searchFilterPanel.add(searchBtn);
        searchFilterPanel.add(showAllBtn);
        
        add(searchFilterPanel, BorderLayout.NORTH);

        // --- Main Content Panel for Table ---
        String[] columns = {"No", "Employee ID", "Employee Name", "Amount", "Purpose", "Request Date", "Status", "Accepted Date", "Withdraw Status", "Action"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return col == 9;
            }
        };

        table = new JTable(model);
        table.setRowHeight(45);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(52, 152, 219));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setSelectionBackground(new Color(220, 220, 250));

        table.getColumn("Status").setCellRenderer(new StatusColorRenderer());
        table.getColumn("Action").setCellRenderer(new ButtonRenderer());
        table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        add(scroll, BorderLayout.CENTER);

        // --- Bottom Panel for Summary and Back Button ---
        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.setBackground(new Color(240, 248, 255));

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        totalPanel.setBackground(new Color(44, 62, 80));

        totalLoanLabel = new JLabel("Total Loan Issues = 0.00");
        totalLoanLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalLoanLabel.setForeground(Color.WHITE);
        totalPanel.add(totalLoanLabel);

        pendingLoanLabel = new JLabel("Pending Loan = 0.00");
        pendingLoanLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        pendingLoanLabel.setForeground(Color.WHITE);
        totalPanel.add(pendingLoanLabel);

        rejectedLoanLabel = new JLabel("Rejected Loan = 0.00");
        rejectedLoanLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        rejectedLoanLabel.setForeground(Color.WHITE);
        totalPanel.add(rejectedLoanLabel);
        
        bottomContainer.add(totalPanel, BorderLayout.NORTH);

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        backButtonPanel.setOpaque(false);
        back = createRoundedButton("Back", new Color(192, 57, 43));
        backButtonPanel.add(back);
        bottomContainer.add(backButtonPanel, BorderLayout.SOUTH);

        add(bottomContainer, BorderLayout.SOUTH);

        // --- Action Listeners ---
        searchBtn.addActionListener(e -> {
            String empId = searchField.getText().trim();
            String selectedMonth = (String) monthCombo.getSelectedItem();
            String year = yearField.getText().trim();

            if (!empId.isEmpty()) {
                fetchLoanRequests(empId, null, null);
            } else if (!year.isEmpty()) {
                String month = selectedMonth.equals("All Months") ? null : selectedMonth;
                if (!year.matches("\\d{4}")) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid 4-digit year.");
                    return;
                }
                fetchLoanRequests(null, month, year);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter Employee ID or Year to search.");
            }
        });
        
        showAllBtn.addActionListener(e -> {
            searchField.setText("");
            yearField.setText("");
            monthCombo.setSelectedIndex(0);
            fetchLoanRequests(null, null, null);
        });

        back.addActionListener(e -> {
            dispose();
            new Home();
        });

        fetchLoanRequests(null, null, null);
        setVisible(true);
    }
    
    // Custom rounded button creation method
    private JButton createRoundedButton(String text, Color bgColor) {
        JButton button = new RoundedButton(text, bgColor);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(140, 45));
        return button;
    }

    // Custom RoundedButton class
    private static class RoundedButton extends JButton {
        private Color bgColor;

        public RoundedButton(String text, Color bgColor) {
            super(text);
            this.bgColor = bgColor;
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBackground(bgColor.darker());
                }
                public void mouseExited(MouseEvent e) {
                    setBackground(bgColor);
                }
            });
        }
        
        @Override
        public void setBackground(Color bg) {
            this.bgColor = bg;
            super.setBackground(bg);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 25, 25));
            super.paintComponent(g);
            g2.dispose();
        }
    }

    private void fetchLoanRequests(String empIdFilter, String monthFilter, String yearFilter) {
        model.setRowCount(0);
        double totalLoanAmount = 0.0, pendingLoanAmount = 0.0, rejectedLoanAmount = 0.0;

        try {
            Conn c = new Conn();
            StringBuilder query = new StringBuilder("SELECT l.id, l.emp_id, e.name, l.amount, l.purpose, l.request_date, l.status, l.accepted_date, l.withdraw_status FROM loan_requests l JOIN employee e ON l.emp_id = e.empId ");
            
            boolean hasWhere = false;
            if (empIdFilter != null && !empIdFilter.isEmpty()) {
                query.append(" WHERE l.emp_id = ?");
                hasWhere = true;
            } else if (yearFilter != null && !yearFilter.isEmpty()) {
                query.append(" WHERE YEAR(l.request_date) = ?");
                hasWhere = true;
                if (monthFilter != null && !monthFilter.isEmpty()) {
                    query.append(" AND MONTH(l.request_date) = ?");
                }
            }
            query.append(" ORDER BY l.id DESC");

            PreparedStatement ps = c.c.prepareStatement(query.toString());
            int paramIndex = 1;
            if (empIdFilter != null && !empIdFilter.isEmpty()) {
                ps.setString(paramIndex++, empIdFilter);
            } else if (yearFilter != null && !yearFilter.isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(yearFilter));
                if (monthFilter != null && !monthFilter.isEmpty()) {
                    ps.setInt(paramIndex++, Integer.parseInt(monthFilter));
                }
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("emp_id"),
                    rs.getString("name"),
                    rs.getDouble("amount"),
                    rs.getString("purpose"),
                    rs.getDate("request_date"),
                    rs.getString("status"),
                    rs.getDate("accepted_date"),
                    rs.getString("withdraw_status"),
                    "Take Action"
                });

                double amt = rs.getDouble("amount");
                String status = rs.getString("status");
                String wd = rs.getString("withdraw_status");
                
                if ("Approved".equalsIgnoreCase(status) && "Yes".equalsIgnoreCase(wd)) totalLoanAmount += amt;
                if ("Pending".equalsIgnoreCase(status)) pendingLoanAmount += amt;
                if ("Rejected".equalsIgnoreCase(status)) rejectedLoanAmount += amt;
            }
            totalLoanLabel.setText(String.format("Total Loan Issues = %.2f", totalLoanAmount));
            pendingLoanLabel.setText(String.format("Pending Loan = %.2f", pendingLoanAmount));
            rejectedLoanLabel.setText(String.format("Rejected Loan = %.2f", rejectedLoanAmount));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load loan requests.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateLoanStatus(int id, String status) {
        try {
            Conn c = new Conn();
            String q;
            if ("Rejected".equalsIgnoreCase(status)) {
                q = "UPDATE loan_requests SET status = ?, withdraw_status = 'No', accepted_date = CURDATE() WHERE id = ?";
            } else {
                q = "UPDATE loan_requests SET status = ?, accepted_date = CURDATE() WHERE id = ?";
            }
            PreparedStatement ps = c.c.prepareStatement(q);
            ps.setString(1, status);
            ps.setInt(2, id);
            
            if (ps.executeUpdate() > 0) {
                fetchLoanRequests(null, null, null);
                JOptionPane.showMessageDialog(this, "Status updated to " + status);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating loan status.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    class StatusColorRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            String status = value.toString().toLowerCase();
            c.setForeground("approved".equals(status) ? new Color(39, 174, 96) : "rejected".equals(status) ? new Color(192, 57, 43) : "pending".equals(status) ? new Color(243, 156, 18) : Color.BLACK);
            return c;
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setBackground(new Color(52, 152, 219));
            setForeground(Color.WHITE);
            setFocusPainted(false);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            setText("Take Action");
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Take Action");
            button.setFont(new Font("Segoe UI", Font.BOLD, 16));
            button.setBackground(new Color(52, 152, 219));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.addActionListener(e -> {
                fireEditingStopped();
                String withdraw = (String) model.getValueAt(row, 8);
                if ("Yes".equalsIgnoreCase(withdraw)) {
                    JOptionPane.showMessageDialog(null, "Withdrawn loan cannot be updated.");
                    return;
                }
                int id = (int) model.getValueAt(row, 0);
                String empId = (String) model.getValueAt(row, 1);
                String[] options = {"Approve", "Reject", "Cancel"};
                int choice = JOptionPane.showOptionDialog(LoanRequestAdmin.this, "Choose action for Employee ID: " + empId, "Update Loan Status", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
                if (choice == 0) updateLoanStatus(id, "Approved");
                else if (choice == 1) updateLoanStatus(id, "Rejected");
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
            this.row = row;
            return button;
        }

        public Object getCellEditorValue() {
            return "Take Action";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoanRequestAdmin::new);
    }
}*/
package employee.management.system;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.util.Vector;

public class LoanRequestAdmin extends JFrame {

    JTable table;
    DefaultTableModel model;
    JButton back, searchBtn, showAllBtn;
    JTextField searchField, yearField;
    JComboBox<String> monthCombo;
    JLabel totalLoanLabel, pendingLoanLabel, rejectedLoanLabel;

    public LoanRequestAdmin() {
        setTitle("Loan Requests - Admin Panel");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255));

        // Create a main panel to hold both the header and search filter panels
        JPanel topContainer = new JPanel(new BorderLayout());

        // --- Header Panel ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 100));
        
        JLabel heading = new JLabel("Loan Requests Management", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);
        headerPanel.add(heading, BorderLayout.CENTER);
        
        JLabel detailsHeading = new JLabel("Loan Details  ", SwingConstants.RIGHT);
        detailsHeading.setFont(new Font("Segoe UI", Font.BOLD, 20));
        detailsHeading.setForeground(new Color(153, 204, 255)); // Light blue color
        headerPanel.add(detailsHeading, BorderLayout.EAST);
        
        // --- Top Panel for Search and Filters ---
        JPanel searchFilterPanel = new JPanel();
        searchFilterPanel.setBackground(new Color(240, 248, 255));
        searchFilterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        searchFilterPanel.add(new JLabel("<html><font size='5' face='Segoe UI' color='#34495e'><b>Search by Employee ID:</b></font></html>"));
        searchField = new JTextField(15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchFilterPanel.add(searchField);

        searchFilterPanel.add(new JLabel("<html><font size='5' face='Segoe UI' color='#34495e'><b>Month:</b></font></html>"));
        String[] months = {"All Months", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        monthCombo = new JComboBox<>(months);
        monthCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchFilterPanel.add(monthCombo);

        searchFilterPanel.add(new JLabel("<html><font size='5' face='Segoe UI' color='#34495e'><b>Year:</b></font></html>"));
        yearField = new JTextField(5);
        yearField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchFilterPanel.add(yearField);

        searchBtn = createRoundedButton("Search", new Color(52, 152, 219));
        showAllBtn = createRoundedButton("Show All", new Color(39, 174, 96));
        
        searchFilterPanel.add(searchBtn);
        searchFilterPanel.add(showAllBtn);
        
        // Add both panels to the new top container
        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(searchFilterPanel, BorderLayout.CENTER);
        
        // Add the top container to the main frame
        add(topContainer, BorderLayout.NORTH);

        // --- Main Content Panel for Table ---
        String[] columns = {"No", "Employee ID", "Employee Name", "Amount", "Purpose", "Request Date", "Status", "Accepted Date", "Withdraw Status", "Action"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return col == 9;
            }
        };

        table = new JTable(model);
        table.setRowHeight(45);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(52, 152, 219));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setSelectionBackground(new Color(220, 220, 250));

        table.getColumn("Status").setCellRenderer(new StatusColorRenderer());
        table.getColumn("Action").setCellRenderer(new ButtonRenderer());
        table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        add(scroll, BorderLayout.CENTER);

        // --- Bottom Panel for Summary and Back Button ---
        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.setBackground(new Color(240, 248, 255));

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        totalPanel.setBackground(new Color(44, 62, 80));

        totalLoanLabel = new JLabel("Total Loan Issues = 0.00");
        totalLoanLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalLoanLabel.setForeground(Color.WHITE);
        totalPanel.add(totalLoanLabel);

        pendingLoanLabel = new JLabel("Pending Loan = 0.00");
        pendingLoanLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        pendingLoanLabel.setForeground(Color.WHITE);
        totalPanel.add(pendingLoanLabel);

        rejectedLoanLabel = new JLabel("Rejected Loan = 0.00");
        rejectedLoanLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        rejectedLoanLabel.setForeground(Color.WHITE);
        totalPanel.add(rejectedLoanLabel);
        
        bottomContainer.add(totalPanel, BorderLayout.NORTH);

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        backButtonPanel.setOpaque(false);
        back = createRoundedButton("Back", new Color(192, 57, 43));
        backButtonPanel.add(back);
        bottomContainer.add(backButtonPanel, BorderLayout.SOUTH);

        add(bottomContainer, BorderLayout.SOUTH);

        // --- Action Listeners ---
        searchBtn.addActionListener(e -> {
            String empId = searchField.getText().trim();
            String selectedMonth = (String) monthCombo.getSelectedItem();
            String year = yearField.getText().trim();

            if (!empId.isEmpty()) {
                fetchLoanRequests(empId, null, null);
            } else if (!year.isEmpty()) {
                String month = selectedMonth.equals("All Months") ? null : selectedMonth;
                if (!year.matches("\\d{4}")) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid 4-digit year.");
                    return;
                }
                fetchLoanRequests(null, month, year);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter Employee ID or Year to search.");
            }
        });
        
        showAllBtn.addActionListener(e -> {
            searchField.setText("");
            yearField.setText("");
            monthCombo.setSelectedIndex(0);
            fetchLoanRequests(null, null, null);
        });

        back.addActionListener(e -> {
            dispose();
            new Home();
        });

        fetchLoanRequests(null, null, null);
        setVisible(true);
    }
    
    // Custom rounded button creation method
    private JButton createRoundedButton(String text, Color bgColor) {
        JButton button = new RoundedButton(text, bgColor);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(140, 45));
        return button;
    }

    // Custom RoundedButton class
    private static class RoundedButton extends JButton {
        private Color bgColor;

        public RoundedButton(String text, Color bgColor) {
            super(text);
            this.bgColor = bgColor;
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBackground(bgColor.darker());
                }
                public void mouseExited(MouseEvent e) {
                    setBackground(bgColor);
                }
            });
        }
        
        @Override
        public void setBackground(Color bg) {
            this.bgColor = bg;
            super.setBackground(bg);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 25, 25));
            super.paintComponent(g);
            g2.dispose();
        }
    }

    private void fetchLoanRequests(String empIdFilter, String monthFilter, String yearFilter) {
        model.setRowCount(0);
        double totalLoanAmount = 0.0, pendingLoanAmount = 0.0, rejectedLoanAmount = 0.0;

        try {
            Conn c = new Conn();
            StringBuilder query = new StringBuilder("SELECT l.id, l.emp_id, e.name, l.amount, l.purpose, l.request_date, l.status, l.accepted_date, l.withdraw_status FROM loan_requests l JOIN employee e ON l.emp_id = e.empId ");
            
            if (empIdFilter != null && !empIdFilter.isEmpty()) {
                query.append(" WHERE l.emp_id = ?");
            } else if (yearFilter != null && !yearFilter.isEmpty()) {
                query.append(" WHERE YEAR(l.request_date) = ?");
                if (monthFilter != null && !monthFilter.isEmpty()) {
                    query.append(" AND MONTH(l.request_date) = ?");
                }
            }
            query.append(" ORDER BY l.id DESC");

            PreparedStatement ps = c.c.prepareStatement(query.toString());
            int paramIndex = 1;
            if (empIdFilter != null && !empIdFilter.isEmpty()) {
                ps.setString(paramIndex++, empIdFilter);
            } else if (yearFilter != null && !yearFilter.isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(yearFilter));
                if (monthFilter != null && !monthFilter.isEmpty()) {
                    ps.setInt(paramIndex++, Integer.parseInt(monthFilter));
                }
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("emp_id"),
                    rs.getString("name"),
                    rs.getDouble("amount"),
                    rs.getString("purpose"),
                    rs.getDate("request_date"),
                    rs.getString("status"),
                    rs.getDate("accepted_date"),
                    rs.getString("withdraw_status"),
                    "Take Action"
                });

                double amt = rs.getDouble("amount");
                String status = rs.getString("status");
                String wd = rs.getString("withdraw_status");
                
                if ("Approved".equalsIgnoreCase(status) && "Yes".equalsIgnoreCase(wd)) totalLoanAmount += amt;
                if ("Pending".equalsIgnoreCase(status)) pendingLoanAmount += amt;
                if ("Rejected".equalsIgnoreCase(status)) rejectedLoanAmount += amt;
            }
            totalLoanLabel.setText(String.format("Total Loan Issues = %.2f", totalLoanAmount));
            pendingLoanLabel.setText(String.format("Pending Loan = %.2f", pendingLoanAmount));
            rejectedLoanLabel.setText(String.format("Rejected Loan = %.2f", rejectedLoanAmount));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load loan requests.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateLoanStatus(int id, String status) {
        try {
            Conn c = new Conn();
            String q;
            if ("Rejected".equalsIgnoreCase(status)) {
                q = "UPDATE loan_requests SET status = ?, withdraw_status = 'No', accepted_date = CURDATE() WHERE id = ?";
            } else {
                q = "UPDATE loan_requests SET status = ?, accepted_date = CURDATE() WHERE id = ?";
            }
            PreparedStatement ps = c.c.prepareStatement(q);
            ps.setString(1, status);
            ps.setInt(2, id);
            
            if (ps.executeUpdate() > 0) {
                fetchLoanRequests(null, null, null);
                JOptionPane.showMessageDialog(this, "Status updated to " + status);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating loan status.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    class StatusColorRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            String status = value.toString().toLowerCase();
            c.setForeground("approved".equals(status) ? new Color(39, 174, 96) : "rejected".equals(status) ? new Color(192, 57, 43) : "pending".equals(status) ? new Color(243, 156, 18) : Color.BLACK);
            return c;
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setBackground(new Color(52, 152, 219));
            setForeground(Color.WHITE);
            setFocusPainted(false);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            setText("Take Action");
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Take Action");
            button.setFont(new Font("Segoe UI", Font.BOLD, 16));
            button.setBackground(new Color(52, 152, 219));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.addActionListener(e -> {
                fireEditingStopped();
                String withdraw = (String) model.getValueAt(row, 8);
                if ("Yes".equalsIgnoreCase(withdraw)) {
                    JOptionPane.showMessageDialog(null, "Withdrawn loan cannot be updated.");
                    return;
                }
                int id = (int) model.getValueAt(row, 0);
                String empId = (String) model.getValueAt(row, 1);
                String[] options = {"Approve", "Reject", "Cancel"};
                int choice = JOptionPane.showOptionDialog(LoanRequestAdmin.this, "Choose action for Employee ID: " + empId, "Update Loan Status", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
                if (choice == 0) updateLoanStatus(id, "Approved");
                else if (choice == 1) updateLoanStatus(id, "Rejected");
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
            this.row = row;
            return button;
        }

        public Object getCellEditorValue() {
            return "Take Action";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoanRequestAdmin::new);
    }
}
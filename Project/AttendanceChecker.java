/*package employee.management.system;

import javax.swing.*;
import net.proteanit.sql.DbUtils;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AttendanceChecker extends JFrame implements ActionListener {

    JTable table;
    JScrollPane scrollPane;
    JButton back, searchBtn, showAllBtn;
    JTextField empIdField, dateField;
    JLabel todayCheckinLabel, todayCheckoutLabel;
    Home home;

    public AttendanceChecker(Home home) {
        this.home = home;

        setTitle("Attendance Checker");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Title
        JLabel heading = new JLabel("Employee Attendance Records", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        // Today summary panel
        JPanel todaySummaryPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        todaySummaryPanel.setBackground(new Color(240, 248, 255));
        todayCheckinLabel = new JLabel("Today's Check In: 0", JLabel.CENTER);
        todayCheckinLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        todayCheckoutLabel = new JLabel("Today's Check Out: 0", JLabel.CENTER);
        todayCheckoutLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        todaySummaryPanel.add(todayCheckinLabel);
        todaySummaryPanel.add(todayCheckoutLabel);

        // Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(new Color(240, 248, 255));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JLabel searchLabel = new JLabel("Employee ID:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        empIdField = new JTextField(10);
        empIdField.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        dateField = new JTextField(10);
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        searchBtn = new JButton("Search");
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchBtn.setBackground(new Color(52, 152, 219));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchBtn.setFocusPainted(false);
        searchBtn.addActionListener(this);

        showAllBtn = new JButton("Show All");
        showAllBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        showAllBtn.setBackground(new Color(46, 204, 113));
        showAllBtn.setForeground(Color.WHITE);
        showAllBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showAllBtn.setFocusPainted(false);
        showAllBtn.addActionListener(this);

        searchPanel.add(searchLabel);
        searchPanel.add(empIdField);
        searchPanel.add(dateLabel);
        searchPanel.add(dateField);
        searchPanel.add(searchBtn);
        searchPanel.add(showAllBtn);

        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(240, 248, 255));
        topPanel.add(heading, BorderLayout.NORTH);
        topPanel.add(todaySummaryPanel, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // Table
        table = new JTable();
        table.setRowHeight(25);
        table.setFont(new Font("Tahoma", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 18));
        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Back button
        back = new JButton("Back");
        back.setFont(new Font("Segoe UI", Font.BOLD, 18));
        back.setBackground(Color.RED);
        back.setForeground(Color.WHITE);
        back.setFocusPainted(false);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        back.setPreferredSize(new Dimension(100, 50));
        back.addActionListener(this);
        add(back, BorderLayout.SOUTH);

        populateAttendanceData(null, null); // Load all data initially
        setVisible(true);
    }

    private void populateAttendanceData(String empId, String date) {
        try {
            Conn c = new Conn();
            String query = "SELECT empId AS 'Employee ID', date AS 'Date', checkin_time AS 'Check In', checkout_time AS 'Check Out' FROM attendance";

            if ((empId != null && !empId.isEmpty()) || (date != null && !date.isEmpty())) {
                query += " WHERE";
                boolean addAnd = false;
                if (empId != null && !empId.isEmpty()) {
                    query += " empId = '" + empId + "'";
                    addAnd = true;
                }
                if (date != null && !date.isEmpty()) {
                    if (addAnd) query += " AND";
                    query += " date = '" + date + "'";
                }
            }

            query += " ORDER BY date DESC";

            ResultSet rs = c.s.executeQuery(query);
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load attendance records\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        updateTodayAttendanceCounts();
    }

    private void updateTodayAttendanceCounts() {
        try {
            Conn c = new Conn();
            String today = java.time.LocalDate.now().toString();

            String checkinQuery = "SELECT COUNT(*) FROM attendance WHERE date = '" + today + "' AND checkin_time IS NOT NULL";
            ResultSet rs1 = c.s.executeQuery(checkinQuery);
            if (rs1.next()) {
                todayCheckinLabel.setText("Today's Check In: " + rs1.getInt(1));
            }

            String checkoutQuery = "SELECT COUNT(*) FROM attendance WHERE date = '" + today + "' AND checkout_time IS NOT NULL";
            ResultSet rs2 = c.s.executeQuery(checkoutQuery);
            if (rs2.next()) {
                todayCheckoutLabel.setText("Today's Check Out: " + rs2.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == back) {
            this.dispose();
            home.setVisible(true);
        } else if (ae.getSource() == searchBtn) {
            String empId = empIdField.getText().trim();
            String date = dateField.getText().trim();
            populateAttendanceData(empId, date);
        } else if (ae.getSource() == showAllBtn) {
            empIdField.setText("");
            dateField.setText("");
            populateAttendanceData(null, null);
        }
    }
}
*/
package employee.management.system;

import javax.swing.*;
import net.proteanit.sql.DbUtils;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.awt.geom.RoundRectangle2D;

public class AttendanceChecker extends JFrame implements ActionListener {

    JTable table;
    JScrollPane scrollPane;
    JButton back, searchBtn, showAllBtn;
    JTextField empIdField, dateField;
    JLabel todayCheckinLabel, todayCheckoutLabel;
    Home home;

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

    public AttendanceChecker(Home home) {
        this.home = home;

        setTitle("Attendance Checker");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Header Panel
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;

        JPanel headerContainer = new JPanel(null);
        headerContainer.setPreferredSize(new Dimension(screenWidth, 80));
        headerContainer.setBackground(new Color(0, 102, 204));
        
        JLabel heading = new JLabel("Staff Attendance Records", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 32));
        heading.setForeground(Color.WHITE);
        heading.setBounds(0, 0, screenWidth, 80);
        
        headerContainer.add(heading);
        add(headerContainer, BorderLayout.NORTH);

        // Main content panel
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBackground(new Color(240, 248, 255));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Today summary panel
        JPanel todaySummaryPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        todaySummaryPanel.setBackground(new Color(240, 248, 255));
        todayCheckinLabel = new JLabel("Today's Check In: 0", JLabel.CENTER);
        todayCheckinLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        todayCheckinLabel.setForeground(new Color(34, 45, 65));
        todayCheckoutLabel = new JLabel("Today's Check Out: 0", JLabel.CENTER);
        todayCheckoutLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        todayCheckoutLabel.setForeground(new Color(34, 45, 65));
        todaySummaryPanel.add(todayCheckinLabel);
        todaySummaryPanel.add(todayCheckoutLabel);
        todaySummaryPanel.setMaximumSize(new Dimension(800, 50));
        mainContentPanel.add(todaySummaryPanel);
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(new Color(240, 248, 255));
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        searchPanel.setMaximumSize(new Dimension(1000, 50));

        JLabel searchLabel = new JLabel("Employee ID:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        empIdField = new JTextField(10);
        empIdField.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        dateField = new JTextField(10);
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        
        // Buttons
        searchBtn = new RoundedButton("Search", new Color(52, 152, 219));
        searchBtn.addActionListener(this);
        
        showAllBtn = new RoundedButton("Show All", new Color(46, 204, 113));
        showAllBtn.addActionListener(this);

        searchPanel.add(searchLabel);
        searchPanel.add(empIdField);
        searchPanel.add(dateLabel);
        searchPanel.add(dateField);
        searchPanel.add(searchBtn);
        searchPanel.add(showAllBtn);
        mainContentPanel.add(searchPanel);
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Table
        table = new JTable();
        table.setRowHeight(25);
        table.setFont(new Font("Tahoma", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 18));
        
        scrollPane = new JScrollPane(table);
        scrollPane.setMaximumSize(new Dimension(1000, 400));
        mainContentPanel.add(scrollPane);
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Back button panel
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backButtonPanel.setBackground(new Color(240, 248, 255));
        back = new RoundedButton("Back", Color.RED);
        back.addActionListener(this);
        backButtonPanel.add(back);
        mainContentPanel.add(backButtonPanel);

        // Final layout adjustment to center the main panel
        JPanel containerPanel = new JPanel(new GridBagLayout());
        containerPanel.setBackground(new Color(240, 248, 255));
        containerPanel.add(mainContentPanel);
        add(containerPanel, BorderLayout.CENTER);

        populateAttendanceData(null, null); // Load all data initially
        setVisible(true);
    }

    private void populateAttendanceData(String empId, String date) {
        try {
            Conn c = new Conn();
            String query = "SELECT empId AS 'Employee ID', date AS 'Date', checkin_time AS 'Check In', checkout_time AS 'Check Out' FROM attendance";
            
            boolean hasWhere = false;
            if (empId != null && !empId.isEmpty()) {
                query += " WHERE empId = '" + empId + "'";
                hasWhere = true;
            }
            if (date != null && !date.isEmpty()) {
                if (!hasWhere) {
                    query += " WHERE";
                } else {
                    query += " AND";
                }
                query += " date = '" + date + "'";
            }

            query += " ORDER BY date DESC, checkin_time DESC";

            ResultSet rs = c.s.executeQuery(query);
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load attendance records\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        updateTodayAttendanceCounts();
    }

    private void updateTodayAttendanceCounts() {
        try {
            Conn c = new Conn();
            String today = java.time.LocalDate.now().toString();

            String checkinQuery = "SELECT COUNT(*) FROM attendance WHERE date = '" + today + "' AND checkin_time IS NOT NULL";
            ResultSet rs1 = c.s.executeQuery(checkinQuery);
            if (rs1.next()) {
                todayCheckinLabel.setText("Today's Check In: " + rs1.getInt(1));
            }

            String checkoutQuery = "SELECT COUNT(*) FROM attendance WHERE date = '" + today + "' AND checkout_time IS NOT NULL";
            ResultSet rs2 = c.s.executeQuery(checkoutQuery);
            if (rs2.next()) {
                todayCheckoutLabel.setText("Today's Check Out: " + rs2.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == back) {
            this.dispose();
            home.setVisible(true);
        } else if (ae.getSource() == searchBtn) {
            String empId = empIdField.getText().trim();
            String date = dateField.getText().trim();
            populateAttendanceData(empId, date);
        } else if (ae.getSource() == showAllBtn) {
            empIdField.setText("");
            dateField.setText("");
            populateAttendanceData(null, null);
        }
    }
}
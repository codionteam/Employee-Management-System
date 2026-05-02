/*package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import com.toedter.calendar.JDateChooser;

public class Home extends JFrame implements ActionListener {

    JButton add, view, update, remove, attendance, leaves, salary, notice, logout, changePassword, loanRequest, btnSearchDate;
    JLabel lblTotalEmployees, lblTodayAttendance;
    JLabel lblPendingLeaves, lblApprovedLeaves, lblRejectedLeaves;
    JLabel lblPendingLoans, lblApprovedLoans, lblRejectedLoans;
    JDateChooser dateChooser;

    public Home() {
        setTitle("Admin Panel - Digital Staff Record Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel heading = new JLabel("Admin Panel", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 40));
        heading.setForeground(new Color(33, 64, 95));
        heading.setOpaque(true);
        heading.setBackground(new Color(230, 230, 250));
        heading.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        add(heading, BorderLayout.NORTH);

        // Left Navigation Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(300, getHeight()));
        leftPanel.setBackground(new Color(34, 45, 65));
        leftPanel.setLayout(new GridLayout(11, 1, 15, 15));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        changePassword = createRoundedButton("Change Password", new Color(52, 152, 219));
        add = createRoundedButton("Add Employee", new Color(39, 174, 96));
        view = createRoundedButton("View Employees", new Color(52, 152, 219));
        update = createRoundedButton("Update Employee", new Color(241, 196, 15));
        remove = createRoundedButton("Remove Employee", new Color(231, 76, 60));
        attendance = createRoundedButton("Check Attendance", new Color(155, 89, 182));
        leaves = createRoundedButton("Manage Leaves", new Color(46, 204, 113));
        salary = createRoundedButton("Manage Salaries", new Color(26, 188, 156));
        notice = createRoundedButton("Notice Board", new Color(52, 73, 94));
        loanRequest = createRoundedButton("Loan Requests", new Color(142, 68, 173));
        logout = createRoundedButton("Logout", new Color(231, 76, 60));

        leftPanel.add(changePassword);
        leftPanel.add(add);
        leftPanel.add(view);
        leftPanel.add(update);
        leftPanel.add(remove);
        leftPanel.add(attendance);
        leftPanel.add(leaves);
        leftPanel.add(salary);
        leftPanel.add(notice);
        leftPanel.add(loanRequest);
        leftPanel.add(logout);

        add(leftPanel, BorderLayout.WEST);

        // Center Panel with Stats
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));
        centerPanel.setBackground(new Color(245, 250, 255));

        // 1. Employee Stats
        JPanel employeeStatsPanel = new JPanel(new GridLayout(1, 2, 30, 20));
        employeeStatsPanel.setBackground(new Color(245, 250, 255));
        lblTotalEmployees = createStatLabel("Total Employees: 0", new Color(52, 152, 219));
        employeeStatsPanel.add(lblTotalEmployees);

        JPanel attendanceBox = new JPanel(new BorderLayout());
        attendanceBox.setBackground(Color.WHITE);
        attendanceBox.setBorder(BorderFactory.createLineBorder(new Color(241, 196, 15), 2));

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(Color.WHITE);
        JLabel lblDate = new JLabel("Select Date: ");
        dateChooser = new JDateChooser();
        dateChooser.setDate(new Date());
        btnSearchDate = new JButton("Search");
        btnSearchDate.addActionListener(this);
        topPanel.add(lblDate);
        topPanel.add(dateChooser);
        topPanel.add(btnSearchDate);

        attendanceBox.add(topPanel, BorderLayout.NORTH);

        lblTodayAttendance = new JLabel("Check-In: 0   |   Check-Out: 0", JLabel.CENTER);
        lblTodayAttendance.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTodayAttendance.setForeground(new Color(241, 196, 15));
        attendanceBox.add(lblTodayAttendance, BorderLayout.CENTER);

        employeeStatsPanel.add(attendanceBox);

        // 2. Leave Stats
        JPanel leaveStatsPanel = new JPanel(new GridLayout(1, 3, 30, 20));
        leaveStatsPanel.setBackground(new Color(245, 250, 255));
        lblPendingLeaves = createStatLabel("Requested Leaves: 0", new Color(243, 156, 18));
        lblApprovedLeaves = createStatLabel("Approved Leaves: 0", new Color(46, 204, 113));
        lblRejectedLeaves = createStatLabel("Rejected Leaves: 0", new Color(231, 76, 60));
        leaveStatsPanel.add(lblPendingLeaves);
        leaveStatsPanel.add(lblApprovedLeaves);
        leaveStatsPanel.add(lblRejectedLeaves);

        // 3. Loan Request Stats
        JPanel loanStatsPanel = new JPanel(new GridLayout(1, 3, 30, 20));
        loanStatsPanel.setBackground(new Color(245, 250, 255));
        lblPendingLoans = createStatLabel("Requested Loans: 0", new Color(243, 156, 18));
        lblApprovedLoans = createStatLabel("Approved Loans: 0", new Color(46, 204, 113));
        lblRejectedLoans = createStatLabel("Rejected Loans: 0", new Color(231, 76, 60));
        loanStatsPanel.add(lblPendingLoans);
        loanStatsPanel.add(lblApprovedLoans);
        loanStatsPanel.add(lblRejectedLoans);

        centerPanel.add(employeeStatsPanel);
        centerPanel.add(leaveStatsPanel);
        centerPanel.add(loanStatsPanel);

        add(centerPanel, BorderLayout.CENTER);

        refreshCounts(LocalDate.now().toString());

        setVisible(true);
    }

    private JLabel createStatLabel(String text, Color color) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 22));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setForeground(color);
        label.setBorder(BorderFactory.createLineBorder(color, 2));
        return label;
    }

    private JButton createRoundedButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                // No border
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(baseColor.darker());
                button.repaint();
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
                button.repaint();
            }
        });

        button.addActionListener(this);
        return button;
    }

    private void refreshCounts(String selectedDate) {
        try {
            Conn c = new Conn();

            ResultSet rsEmp = c.s.executeQuery("SELECT COUNT(*) FROM employee");
            if (rsEmp.next()) {
                lblTotalEmployees.setText("Total Employees: " + rsEmp.getInt(1));
            }

            ResultSet rsCheckIn = c.s.executeQuery("SELECT COUNT(*) FROM attendance WHERE date = '" + selectedDate + "' AND checkin_time IS NOT NULL");
            int checkIn = rsCheckIn.next() ? rsCheckIn.getInt(1) : 0;

            ResultSet rsCheckOut = c.s.executeQuery("SELECT COUNT(*) FROM attendance WHERE date = '" + selectedDate + "' AND checkout_time IS NOT NULL");
            int checkOut = rsCheckOut.next() ? rsCheckOut.getInt(1) : 0;

            lblTodayAttendance.setText("Check-In: " + checkIn + "   |   Check-Out: " + checkOut);

            ResultSet rsPending = c.s.executeQuery("SELECT COUNT(*) FROM leave_requests WHERE status='Pending'");
            if (rsPending.next()) {
                lblPendingLeaves.setText("Requested Leaves: " + rsPending.getInt(1));
            }

            ResultSet rsApproved = c.s.executeQuery("SELECT COUNT(*) FROM leave_requests WHERE status='Approved'");
            if (rsApproved.next()) {
                lblApprovedLeaves.setText("Approved Leaves: " + rsApproved.getInt(1));
            }

            ResultSet rsRejected = c.s.executeQuery("SELECT COUNT(*) FROM leave_requests WHERE status='Rejected'");
            if (rsRejected.next()) {
                lblRejectedLeaves.setText("Rejected Leaves: " + rsRejected.getInt(1));
            }

            ResultSet rsLoanPending = c.s.executeQuery("SELECT COUNT(*) FROM loan_requests WHERE status='Pending'");
            if (rsLoanPending.next()) {
                lblPendingLoans.setText("Requested Loans: " + rsLoanPending.getInt(1));
            }

            ResultSet rsLoanApproved = c.s.executeQuery("SELECT COUNT(*) FROM loan_requests WHERE status='Approved'");
            if (rsLoanApproved.next()) {
                lblApprovedLoans.setText("Approved Loans: " + rsLoanApproved.getInt(1));
            }

            ResultSet rsLoanRejected = c.s.executeQuery("SELECT COUNT(*) FROM loan_requests WHERE status='Rejected'");
            if (rsLoanRejected.next()) {
                lblRejectedLoans.setText("Rejected Loans: " + rsLoanRejected.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data counts from database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();

        if (source == btnSearchDate) {
            Date date = dateChooser.getDate();
            if (date != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String selectedDate = sdf.format(date);
                refreshCounts(selectedDate);
            }
        } else if (source == changePassword) {
            new ChangePasswordAdmin(this);
        } else if (source == add) {
            setVisible(false);
            new AddEmployee();
        } else if (source == view || source == update) {
            setVisible(false);
            new ViewEmployee();
        } else if (source == remove) {
            setVisible(false);
            new RemoveEmployee();
        } else if (source == attendance) {
            setVisible(false);
            new AttendanceChecker(this);
        } else if (source == leaves) {
            setVisible(false);
            new ManageLeaves(this);
        } else if (source == salary) {
            setVisible(false);
            new ManageSalaryAdmin(this);
        } else if (source == notice) {
            setVisible(false);
            new AdminNoticeBoard();
        } else if (source == loanRequest) {
            setVisible(false);
            new LoanRequestAdmin();
        } else if (source == logout) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new AdminLogin();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Home::new);
    }
}
*/
/*

package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import com.toedter.calendar.JDateChooser;

public class Home extends JFrame implements ActionListener {

    JButton add, view, update, remove, attendance, leaves, salary, notice, logout, changePassword, loanRequest, btnSearchDate;
    JLabel lblTotalEmployees, lblTodayAttendance;
    JLabel lblPendingLeaves, lblApprovedLeaves, lblRejectedLeaves;
    JLabel lblPendingLoans, lblApprovedLoans, lblRejectedLoans;
    JDateChooser dateChooser;

    public Home() {
        setTitle("Admin Panel - Digital Staff Record Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel heading = new JLabel("Admin Panel", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 40));
        heading.setForeground(new Color(33, 64, 95));
        heading.setOpaque(true);
        heading.setBackground(new Color(230, 230, 250));
        heading.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        add(heading, BorderLayout.NORTH);

        // Left Navigation Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(300, getHeight()));
        leftPanel.setBackground(new Color(34, 45, 65));
        leftPanel.setLayout(new GridLayout(11, 1, 15, 15));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        changePassword = createRoundedButton("Change Password", new Color(52, 152, 219));
        add = createRoundedButton("Add Employee", new Color(39, 174, 96));
        view = createRoundedButton("View Employees", new Color(52, 152, 219));
        update = createRoundedButton("Update Employee", new Color(241, 196, 15));
        remove = createRoundedButton("Remove Employee", new Color(231, 76, 60));
        attendance = createRoundedButton("Check Attendance", new Color(155, 89, 182));
        leaves = createRoundedButton("Manage Leaves", new Color(46, 204, 113));
        salary = createRoundedButton("Manage Salaries", new Color(26, 188, 156));
        notice = createRoundedButton("Notice Board", new Color(52, 73, 94));
        loanRequest = createRoundedButton("Loan Requests", new Color(142, 68, 173));
        logout = createRoundedButton("Logout", new Color(231, 76, 60));

        leftPanel.add(changePassword);
        leftPanel.add(add);
        leftPanel.add(view);
        leftPanel.add(update);
        leftPanel.add(remove);
        leftPanel.add(attendance);
        leftPanel.add(leaves);
        leftPanel.add(salary);
        leftPanel.add(notice);
        leftPanel.add(loanRequest);
        leftPanel.add(logout);

        add(leftPanel, BorderLayout.WEST);

        // Center Panel with Stats
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));
        centerPanel.setBackground(new Color(245, 250, 255));

        // 1. Employee Stats
        JPanel employeeStatsPanel = new JPanel(new GridLayout(1, 2, 30, 20));
        employeeStatsPanel.setBackground(new Color(245, 250, 255));
        lblTotalEmployees = createStatLabel("Total Employees: 0", new Color(52, 152, 219));
        employeeStatsPanel.add(lblTotalEmployees);

        JPanel attendanceBox = new JPanel(new BorderLayout());
        attendanceBox.setBackground(Color.WHITE);
        attendanceBox.setBorder(BorderFactory.createLineBorder(new Color(241, 196, 15), 2));

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(Color.WHITE);
        JLabel lblDate = new JLabel("Select Date: ");
        dateChooser = new JDateChooser();
        dateChooser.setDate(new Date());
        btnSearchDate = new JButton("Search");
        btnSearchDate.addActionListener(this);
        topPanel.add(lblDate);
        topPanel.add(dateChooser);
        topPanel.add(btnSearchDate);

        attendanceBox.add(topPanel, BorderLayout.NORTH);

        lblTodayAttendance = new JLabel("Check-In: 0    |    Check-Out: 0", JLabel.CENTER);
        lblTodayAttendance.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTodayAttendance.setForeground(new Color(241, 196, 15));
        attendanceBox.add(lblTodayAttendance, BorderLayout.CENTER);

        employeeStatsPanel.add(attendanceBox);

        // 2. Leave Stats
        JPanel leaveStatsPanel = new JPanel(new GridLayout(1, 3, 30, 20));
        leaveStatsPanel.setBackground(new Color(245, 250, 255));
        lblPendingLeaves = createStatLabel("Requested Leaves: 0", new Color(243, 156, 18));
        lblApprovedLeaves = createStatLabel("Approved Leaves: 0", new Color(46, 204, 113));
        lblRejectedLeaves = createStatLabel("Rejected Leaves: 0", new Color(231, 76, 60));
        leaveStatsPanel.add(lblPendingLeaves);
        leaveStatsPanel.add(lblApprovedLeaves);
        leaveStatsPanel.add(lblRejectedLeaves);

        // 3. Loan Request Stats
        JPanel loanStatsPanel = new JPanel(new GridLayout(1, 3, 30, 20));
        loanStatsPanel.setBackground(new Color(245, 250, 255));
        lblPendingLoans = createStatLabel("Requested Loans: 0", new Color(243, 156, 18));
        lblApprovedLoans = createStatLabel("Approved Loans: 0", new Color(46, 204, 113));
        lblRejectedLoans = createStatLabel("Rejected Loans: 0", new Color(231, 76, 60));
        loanStatsPanel.add(lblPendingLoans);
        loanStatsPanel.add(lblApprovedLoans);
        loanStatsPanel.add(lblRejectedLoans);

        centerPanel.add(employeeStatsPanel);
        centerPanel.add(leaveStatsPanel);
        centerPanel.add(loanStatsPanel);

        add(centerPanel, BorderLayout.CENTER);

        refreshCounts(LocalDate.now().toString());

        setVisible(true);
    }

    private JLabel createStatLabel(String text, Color color) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 22));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setForeground(color);
        label.setBorder(BorderFactory.createLineBorder(color, 2));
        return label;
    }

    private JButton createRoundedButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                // No border
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(baseColor.darker());
                button.repaint();
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
                button.repaint();
            }
        });

        button.addActionListener(this);
        return button;
    }

    private void refreshCounts(String selectedDate) {
        try {
            Conn c = new Conn();

            ResultSet rsEmp = c.s.executeQuery("SELECT COUNT(*) FROM employee");
            if (rsEmp.next()) {
                lblTotalEmployees.setText("Total Employees: " + rsEmp.getInt(1));
            }

            ResultSet rsCheckIn = c.s.executeQuery("SELECT COUNT(*) FROM attendance WHERE date = '" + selectedDate + "' AND checkin_time IS NOT NULL");
            int checkIn = rsCheckIn.next() ? rsCheckIn.getInt(1) : 0;

            ResultSet rsCheckOut = c.s.executeQuery("SELECT COUNT(*) FROM attendance WHERE date = '" + selectedDate + "' AND checkout_time IS NOT NULL");
            int checkOut = rsCheckOut.next() ? rsCheckOut.getInt(1) : 0;

            lblTodayAttendance.setText("Check-In: " + checkIn + "    |    Check-Out: " + checkOut);

            ResultSet rsPending = c.s.executeQuery("SELECT COUNT(*) FROM leave_requests WHERE status='Pending'");
            if (rsPending.next()) {
                lblPendingLeaves.setText("Requested Leaves: " + rsPending.getInt(1));
            }

            ResultSet rsApproved = c.s.executeQuery("SELECT COUNT(*) FROM leave_requests WHERE status='Approved'");
            if (rsApproved.next()) {
                lblApprovedLeaves.setText("Approved Leaves: " + rsApproved.getInt(1));
            }

            ResultSet rsRejected = c.s.executeQuery("SELECT COUNT(*) FROM leave_requests WHERE status='Rejected'");
            if (rsRejected.next()) {
                lblRejectedLeaves.setText("Rejected Leaves: " + rsRejected.getInt(1));
            }

            ResultSet rsLoanPending = c.s.executeQuery("SELECT COUNT(*) FROM loan_requests WHERE status='Pending'");
            if (rsLoanPending.next()) {
                lblPendingLoans.setText("Requested Loans: " + rsLoanPending.getInt(1));
            }

            ResultSet rsLoanApproved = c.s.executeQuery("SELECT COUNT(*) FROM loan_requests WHERE status='Approved'");
            if (rsLoanApproved.next()) {
                lblApprovedLoans.setText("Approved Loans: " + rsLoanApproved.getInt(1));
            }

            ResultSet rsLoanRejected = c.s.executeQuery("SELECT COUNT(*) FROM loan_requests WHERE status='Rejected'");
            if (rsLoanRejected.next()) {
                lblRejectedLoans.setText("Rejected Loans: " + rsLoanRejected.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data counts from database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();

        if (source == btnSearchDate) {
            Date date = dateChooser.getDate();
            if (date != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String selectedDate = sdf.format(date);
                refreshCounts(selectedDate);
            }
        } else if (source == changePassword) {
            new ChangePasswordAdmin(this);
        } else if (source == add) {
            setVisible(false);
            new AddEmployee();
        } else if (source == view) {
            setVisible(false);
            new ViewEmployee();
        } else if (source == update) {
            setVisible(false);
            // এখানে UpdateEmployee() ক্লাসটি খোলা হচ্ছে, যা এখন কোনো empId প্যারামিটার ছাড়াই কাজ করবে
            new UpdateEmployee(null); 
        } else if (source == remove) {
            setVisible(false);
            new RemoveEmployee();
        } else if (source == attendance) {
            setVisible(false);
            new AttendanceChecker(this);
        } else if (source == leaves) {
            setVisible(false);
            new ManageLeaves(this);
        } else if (source == salary) {
            setVisible(false);
            new ManageSalaryAdmin();
        } else if (source == notice) {
            setVisible(false);
            new AdminNoticeBoard();
        } else if (source == loanRequest) {
            setVisible(false);
            new LoanRequestAdmin();
        } else if (source == logout) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new AdminLogin();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Home::new);
    }
}
*/
/*
package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import com.toedter.calendar.JDateChooser;

public class Home extends JFrame implements ActionListener {

    JButton add, view, update, remove, attendance, leaves, salary, notice, logout, changePassword, loanRequest, btnSearchDate, staffOfTheMonth; // New button added
    JLabel lblTotalEmployees, lblTodayAttendance;
    JLabel lblPendingLeaves, lblApprovedLeaves, lblRejectedLeaves;
    JLabel lblPendingLoans, lblApprovedLoans, lblRejectedLoans;
    JDateChooser dateChooser;

    public Home() {
        setTitle("Admin Panel - Digital Staff Record Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel heading = new JLabel("Admin Panel", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 40));
        heading.setForeground(new Color(33, 64, 95));
        heading.setOpaque(true);
        heading.setBackground(new Color(230, 230, 250));
        heading.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        add(heading, BorderLayout.NORTH);

        // Left Navigation Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(300, getHeight()));
        leftPanel.setBackground(new Color(34, 45, 65));
        leftPanel.setLayout(new GridLayout(12, 1, 15, 15)); // Increased grid rows for new button
        leftPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        changePassword = createRoundedButton("Change Password", new Color(52, 152, 219));
        add = createRoundedButton("Add Employee", new Color(39, 174, 96));
        view = createRoundedButton("View Employees", new Color(52, 152, 219));
        update = createRoundedButton("Update Employee", new Color(241, 196, 15));
        remove = createRoundedButton("Remove Employee", new Color(231, 76, 60));
        attendance = createRoundedButton("Check Attendance", new Color(155, 89, 182));
        leaves = createRoundedButton("Manage Leaves", new Color(46, 204, 113));
        salary = createRoundedButton("Manage Salaries", new Color(26, 188, 156));
        notice = createRoundedButton("Notice Board", new Color(52, 73, 94));
        loanRequest = createRoundedButton("Loan Requests", new Color(142, 68, 173));
        staffOfTheMonth = createRoundedButton("Staff of the Month", new Color(255, 165, 0)); // New button
        logout = createRoundedButton("Logout", new Color(231, 76, 60));

        leftPanel.add(changePassword);
        leftPanel.add(add);
        leftPanel.add(view);
        leftPanel.add(update);
        leftPanel.add(remove);
        leftPanel.add(attendance);
        leftPanel.add(leaves);
        leftPanel.add(salary);
        leftPanel.add(notice);
        leftPanel.add(loanRequest);
        leftPanel.add(staffOfTheMonth); // Adding the new button
        leftPanel.add(logout);

        add(leftPanel, BorderLayout.WEST);

        // Center Panel with Stats
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));
        centerPanel.setBackground(new Color(245, 250, 255));

        // 1. Employee Stats
        JPanel employeeStatsPanel = new JPanel(new GridLayout(1, 2, 30, 20));
        employeeStatsPanel.setBackground(new Color(245, 250, 255));
        lblTotalEmployees = createStatLabel("Total Employees: 0", new Color(52, 152, 219));
        employeeStatsPanel.add(lblTotalEmployees);

        JPanel attendanceBox = new JPanel(new BorderLayout());
        attendanceBox.setBackground(Color.WHITE);
        attendanceBox.setBorder(BorderFactory.createLineBorder(new Color(241, 196, 15), 2));

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(Color.WHITE);
        JLabel lblDate = new JLabel("Select Date: ");
        dateChooser = new JDateChooser();
        dateChooser.setDate(new Date());
        btnSearchDate = new JButton("Search");
        btnSearchDate.addActionListener(this);
        topPanel.add(lblDate);
        topPanel.add(dateChooser);
        topPanel.add(btnSearchDate);

        attendanceBox.add(topPanel, BorderLayout.NORTH);

        lblTodayAttendance = new JLabel("Check-In: 0    |    Check-Out: 0", JLabel.CENTER);
        lblTodayAttendance.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTodayAttendance.setForeground(new Color(241, 196, 15));
        attendanceBox.add(lblTodayAttendance, BorderLayout.CENTER);

        employeeStatsPanel.add(attendanceBox);

        // 2. Leave Stats
        JPanel leaveStatsPanel = new JPanel(new GridLayout(1, 3, 30, 20));
        leaveStatsPanel.setBackground(new Color(245, 250, 255));
        lblPendingLeaves = createStatLabel("Requested Leaves: 0", new Color(243, 156, 18));
        lblApprovedLeaves = createStatLabel("Approved Leaves: 0", new Color(46, 204, 113));
        lblRejectedLeaves = createStatLabel("Rejected Leaves: 0", new Color(231, 76, 60));
        leaveStatsPanel.add(lblPendingLeaves);
        leaveStatsPanel.add(lblApprovedLeaves);
        leaveStatsPanel.add(lblRejectedLeaves);

        // 3. Loan Request Stats
        JPanel loanStatsPanel = new JPanel(new GridLayout(1, 3, 30, 20));
        loanStatsPanel.setBackground(new Color(245, 250, 255));
        lblPendingLoans = createStatLabel("Requested Loans: 0", new Color(243, 156, 18));
        lblApprovedLoans = createStatLabel("Approved Loans: 0", new Color(46, 204, 113));
        lblRejectedLoans = createStatLabel("Rejected Loans: 0", new Color(231, 76, 60));
        loanStatsPanel.add(lblPendingLoans);
        loanStatsPanel.add(lblApprovedLoans);
        loanStatsPanel.add(lblRejectedLoans);

        centerPanel.add(employeeStatsPanel);
        centerPanel.add(leaveStatsPanel);
        centerPanel.add(loanStatsPanel);

        add(centerPanel, BorderLayout.CENTER);

        refreshCounts(LocalDate.now().toString());

        setVisible(true);
    }

    private JLabel createStatLabel(String text, Color color) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 22));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setForeground(color);
        label.setBorder(BorderFactory.createLineBorder(color, 2));
        return label;
    }

    private JButton createRoundedButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                // No border
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(baseColor.darker());
                button.repaint();
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
                button.repaint();
            }
        });

        button.addActionListener(this);
        return button;
    }

    private void refreshCounts(String selectedDate) {
        try {
            Conn c = new Conn();

            ResultSet rsEmp = c.s.executeQuery("SELECT COUNT(*) FROM employee");
            if (rsEmp.next()) {
                lblTotalEmployees.setText("Total Employees: " + rsEmp.getInt(1));
            }

            ResultSet rsCheckIn = c.s.executeQuery("SELECT COUNT(*) FROM attendance WHERE date = '" + selectedDate + "' AND checkin_time IS NOT NULL");
            int checkIn = rsCheckIn.next() ? rsCheckIn.getInt(1) : 0;

            ResultSet rsCheckOut = c.s.executeQuery("SELECT COUNT(*) FROM attendance WHERE date = '" + selectedDate + "' AND checkout_time IS NOT NULL");
            int checkOut = rsCheckOut.next() ? rsCheckOut.getInt(1) : 0;

            lblTodayAttendance.setText("Check-In: " + checkIn + "    |    Check-Out: " + checkOut);

            ResultSet rsPending = c.s.executeQuery("SELECT COUNT(*) FROM leave_requests WHERE status='Pending'");
            if (rsPending.next()) {
                lblPendingLeaves.setText("Requested Leaves: " + rsPending.getInt(1));
            }

            ResultSet rsApproved = c.s.executeQuery("SELECT COUNT(*) FROM leave_requests WHERE status='Approved'");
            if (rsApproved.next()) {
                lblApprovedLeaves.setText("Approved Leaves: " + rsApproved.getInt(1));
            }

            ResultSet rsRejected = c.s.executeQuery("SELECT COUNT(*) FROM leave_requests WHERE status='Rejected'");
            if (rsRejected.next()) {
                lblRejectedLeaves.setText("Rejected Leaves: " + rsRejected.getInt(1));
            }

            ResultSet rsLoanPending = c.s.executeQuery("SELECT COUNT(*) FROM loan_requests WHERE status='Pending'");
            if (rsLoanPending.next()) {
                lblPendingLoans.setText("Requested Loans: " + rsLoanPending.getInt(1));
            }

            ResultSet rsLoanApproved = c.s.executeQuery("SELECT COUNT(*) FROM loan_requests WHERE status='Approved'");
            if (rsLoanApproved.next()) {
                lblApprovedLoans.setText("Approved Loans: " + rsLoanApproved.getInt(1));
            }

            ResultSet rsLoanRejected = c.s.executeQuery("SELECT COUNT(*) FROM loan_requests WHERE status='Rejected'");
            if (rsLoanRejected.next()) {
                lblRejectedLoans.setText("Rejected Loans: " + rsLoanRejected.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data counts from database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showStaffOfTheMonth() {
        try {
            Conn c = new Conn();

            // Step 1: Find the employee(s) with the highest attendance count for the current month.
            String attendanceQuery = "SELECT empId, COUNT(*) AS attendance_count FROM attendance " +
                                     "WHERE MONTH(date) = MONTH(CURRENT_DATE()) AND YEAR(date) = YEAR(CURRENT_DATE()) " +
                                     "GROUP BY empId ORDER BY attendance_count DESC LIMIT 1";

            ResultSet rsAttendance = c.s.executeQuery(attendanceQuery);
            
            String winnerEmpId = null;
            int maxAttendance = 0;
            
            if (rsAttendance.next()) {
                winnerEmpId = rsAttendance.getString("empId");
                maxAttendance = rsAttendance.getInt("attendance_count");
            }
            
            if (winnerEmpId == null) {
                JOptionPane.showMessageDialog(this, "No attendance data found for the current month.", "Staff of the Month", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Check for a tie in attendance
            String tieCheckQuery = "SELECT COUNT(*) FROM (" +
                                   "SELECT empId FROM attendance " +
                                   "WHERE MONTH(date) = MONTH(CURRENT_DATE()) AND YEAR(date) = YEAR(CURRENT_DATE()) " +
                                   "GROUP BY empId HAVING COUNT(*) = " + maxAttendance +
                                   ") AS T";
            
            ResultSet rsTieCheck = c.s.executeQuery(tieCheckQuery);
            rsTieCheck.next();
            int tieCount = rsTieCheck.getInt(1);

            if (tieCount > 1) {
                // Step 2: If there's a tie, find the employee with the most total time spent at the office
                String timeQuery = "SELECT empId, SUM(TIMESTAMPDIFF(MINUTE, checkin_time, checkout_time)) AS total_minutes FROM attendance " +
                                   "WHERE MONTH(date) = MONTH(CURRENT_DATE()) AND YEAR(date) = YEAR(CURRENT_DATE()) " +
                                   "AND empId IN (SELECT empId FROM attendance WHERE MONTH(date) = MONTH(CURRENT_DATE()) GROUP BY empId HAVING COUNT(*) = " + maxAttendance + ") " +
                                   "AND checkout_time IS NOT NULL GROUP BY empId ORDER BY total_minutes DESC LIMIT 1";
                
                ResultSet rsTime = c.s.executeQuery(timeQuery);
                if (rsTime.next()) {
                    winnerEmpId = rsTime.getString("empId");
                }
            }
            
            // Step 3: Get the winner's details from the employee table
            String employeeDetailsQuery = "SELECT name, designation FROM employee WHERE empId = ?";
            PreparedStatement pstmt = c.c.prepareStatement(employeeDetailsQuery);
            pstmt.setString(1, winnerEmpId);
            ResultSet rsDetails = pstmt.executeQuery();

            if (rsDetails.next()) {
                String name = rsDetails.getString("name");
                String designation = rsDetails.getString("designation");
                
                String message = String.format("<html><b><font size='+1'>Staff of the Month</font></b><br><br>" +
                                               "<b>Name:</b> %s<br>" +
                                               "<b>Employee ID:</b> %s<br>" +
                                               "<b>Designation:</b> %s</html>", name, winnerEmpId, designation);

                JOptionPane.showMessageDialog(this, message, "Staff of the Month", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Employee details not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while fetching data.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();

        if (source == btnSearchDate) {
            Date date = dateChooser.getDate();
            if (date != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String selectedDate = sdf.format(date);
                refreshCounts(selectedDate);
            }
        } else if (source == staffOfTheMonth) {
            showStaffOfTheMonth();
        } else if (source == changePassword) {
            new ChangePasswordAdmin(this);
        } else if (source == add) {
            setVisible(false);
            new AddEmployee();
        } else if (source == view) {
            setVisible(false);
            new ViewEmployee();
        } else if (source == update) {
            setVisible(false);
            new UpdateEmployee(null);  
        } else if (source == remove) {
            setVisible(false);
            new RemoveEmployee();
        } else if (source == attendance) {
            setVisible(false);
            new AttendanceChecker(this);
        } else if (source == leaves) {
            setVisible(false);
            new ManageLeaves(this);
        } else if (source == salary) {
            setVisible(false);
            new ManageSalaryAdmin();
        } else if (source == notice) {
            setVisible(false);
            new AdminNoticeBoard();
        } else if (source == loanRequest) {
            setVisible(false);
            new LoanRequestAdmin();
        } else if (source == logout) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new AdminLogin();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Home::new);
    }
}*/
package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import com.toedter.calendar.JDateChooser;

public class Home extends JFrame implements ActionListener {

    JButton add, view, update, remove, attendance, leaves, salary, notice, logout, changePassword, loanRequest, btnSearchDate, staffOfTheMonth;
    JLabel lblTotalEmployees, lblTodayAttendance;
    JLabel lblPendingLeaves, lblApprovedLeaves, lblRejectedLeaves;
    JLabel lblPendingLoans, lblApprovedLoans, lblRejectedLoans;
    JDateChooser dateChooser;

    public Home() {
        setTitle("Admin Panel - Digital Staff Record Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel heading = new JLabel("Admin Panel", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 40));
        heading.setForeground(new Color(33, 64, 95));
        heading.setOpaque(true);
        heading.setBackground(new Color(230, 230, 250));
        heading.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        add(heading, BorderLayout.NORTH);

        // Left Navigation Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(300, getHeight()));
        leftPanel.setBackground(new Color(34, 45, 65));
        leftPanel.setLayout(new GridLayout(12, 1, 15, 15));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        changePassword = createRoundedButton("Change Password", new Color(52, 152, 219));
        add = createRoundedButton("Add Employee", new Color(39, 174, 96));
        view = createRoundedButton("View Employees", new Color(52, 152, 219));
        update = createRoundedButton("Update Employee", new Color(241, 196, 15));
        remove = createRoundedButton("Remove Employee", new Color(231, 76, 60));
        attendance = createRoundedButton("Check Attendance", new Color(155, 89, 182));
        leaves = createRoundedButton("Manage Leaves", new Color(46, 204, 113));
        salary = createRoundedButton("Manage Salaries", new Color(26, 188, 156));
        notice = createRoundedButton("Notice Board", new Color(52, 73, 94));
        loanRequest = createRoundedButton("Loan Requests", new Color(142, 68, 173));
        staffOfTheMonth = createRoundedButton("Staff of the Month", new Color(255, 165, 0));
        logout = createRoundedButton("Logout", new Color(231, 76, 60));

        leftPanel.add(changePassword);
        leftPanel.add(add);
        leftPanel.add(view);
        leftPanel.add(update);
        leftPanel.add(remove);
        leftPanel.add(attendance);
        leftPanel.add(leaves);
        leftPanel.add(salary);
        leftPanel.add(notice);
        leftPanel.add(loanRequest);
        leftPanel.add(staffOfTheMonth);
        leftPanel.add(logout);

        add(leftPanel, BorderLayout.WEST);

        // Center Panel with Stats
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));
        centerPanel.setBackground(new Color(245, 250, 255));

        // 1. Employee Stats
        JPanel employeeStatsPanel = new JPanel(new GridLayout(1, 2, 30, 20));
        employeeStatsPanel.setBackground(new Color(245, 250, 255));
        lblTotalEmployees = createStatLabel("Total Employees: 0", new Color(52, 152, 219));
        employeeStatsPanel.add(lblTotalEmployees);

        JPanel attendanceBox = new JPanel(new BorderLayout());
        attendanceBox.setBackground(Color.WHITE);
        attendanceBox.setBorder(BorderFactory.createLineBorder(new Color(241, 196, 15), 2));

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(Color.WHITE);
        JLabel lblDate = new JLabel("Select Date: ");
        dateChooser = new JDateChooser();
        dateChooser.setDate(new Date());
        btnSearchDate = new JButton("Search");
        btnSearchDate.addActionListener(this);
        topPanel.add(lblDate);
        topPanel.add(dateChooser);
        topPanel.add(btnSearchDate);

        attendanceBox.add(topPanel, BorderLayout.NORTH);

        lblTodayAttendance = new JLabel("Check-In: 0    |    Check-Out: 0", JLabel.CENTER);
        lblTodayAttendance.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTodayAttendance.setForeground(new Color(241, 196, 15));
        attendanceBox.add(lblTodayAttendance, BorderLayout.CENTER);

        employeeStatsPanel.add(attendanceBox);

        // 2. Leave Stats
        JPanel leaveStatsPanel = new JPanel(new GridLayout(1, 3, 30, 20));
        leaveStatsPanel.setBackground(new Color(245, 250, 255));
        lblPendingLeaves = createStatLabel("Requested Leaves: 0", new Color(243, 156, 18));
        lblApprovedLeaves = createStatLabel("Approved Leaves: 0", new Color(46, 204, 113));
        lblRejectedLeaves = createStatLabel("Rejected Leaves: 0", new Color(231, 76, 60));
        leaveStatsPanel.add(lblPendingLeaves);
        leaveStatsPanel.add(lblApprovedLeaves);
        leaveStatsPanel.add(lblRejectedLeaves);

        // 3. Loan Request Stats
        JPanel loanStatsPanel = new JPanel(new GridLayout(1, 3, 30, 20));
        loanStatsPanel.setBackground(new Color(245, 250, 255));
        lblPendingLoans = createStatLabel("Requested Loans: 0", new Color(243, 156, 18));
        lblApprovedLoans = createStatLabel("Approved Loans: 0", new Color(46, 204, 113));
        lblRejectedLoans = createStatLabel("Rejected Loans: 0", new Color(231, 76, 60));
        loanStatsPanel.add(lblPendingLoans);
        loanStatsPanel.add(lblApprovedLoans);
        loanStatsPanel.add(lblRejectedLoans);

        centerPanel.add(employeeStatsPanel);
        centerPanel.add(leaveStatsPanel);
        centerPanel.add(loanStatsPanel);

        add(centerPanel, BorderLayout.CENTER);

        refreshCounts(LocalDate.now().toString());

        setVisible(true);
    }

    private JLabel createStatLabel(String text, Color color) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 22));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setForeground(color);
        label.setBorder(BorderFactory.createLineBorder(color, 2));
        return label;
    }

    private JButton createRoundedButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                // No border
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(baseColor.darker());
                button.repaint();
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
                button.repaint();
            }
        });

        button.addActionListener(this);
        return button;
    }

    private void refreshCounts(String selectedDate) {
        try {
            Conn c = new Conn();

            ResultSet rsEmp = c.s.executeQuery("SELECT COUNT(*) FROM employee");
            if (rsEmp.next()) {
                lblTotalEmployees.setText("Total Employees: " + rsEmp.getInt(1));
            }

            ResultSet rsCheckIn = c.s.executeQuery("SELECT COUNT(*) FROM attendance WHERE date = '" + selectedDate + "' AND checkin_time IS NOT NULL");
            int checkIn = rsCheckIn.next() ? rsCheckIn.getInt(1) : 0;

            ResultSet rsCheckOut = c.s.executeQuery("SELECT COUNT(*) FROM attendance WHERE date = '" + selectedDate + "' AND checkout_time IS NOT NULL");
            int checkOut = rsCheckOut.next() ? rsCheckOut.getInt(1) : 0;

            lblTodayAttendance.setText("Check-In: " + checkIn + "    |    Check-Out: " + checkOut);

            ResultSet rsPending = c.s.executeQuery("SELECT COUNT(*) FROM leave_requests WHERE status='Pending'");
            if (rsPending.next()) {
                lblPendingLeaves.setText("Requested Leaves: " + rsPending.getInt(1));
            }

            ResultSet rsApproved = c.s.executeQuery("SELECT COUNT(*) FROM leave_requests WHERE status='Approved'");
            if (rsApproved.next()) {
                lblApprovedLeaves.setText("Approved Leaves: " + rsApproved.getInt(1));
            }

            ResultSet rsRejected = c.s.executeQuery("SELECT COUNT(*) FROM leave_requests WHERE status='Rejected'");
            if (rsRejected.next()) {
                lblRejectedLeaves.setText("Rejected Leaves: " + rsRejected.getInt(1));
            }

            ResultSet rsLoanPending = c.s.executeQuery("SELECT COUNT(*) FROM loan_requests WHERE status='Pending'");
            if (rsLoanPending.next()) {
                lblPendingLoans.setText("Requested Loans: " + rsLoanPending.getInt(1));
            }

            ResultSet rsLoanApproved = c.s.executeQuery("SELECT COUNT(*) FROM loan_requests WHERE status='Approved'");
            if (rsLoanApproved.next()) {
                lblApprovedLoans.setText("Approved Loans: " + rsLoanApproved.getInt(1));
            }

            ResultSet rsLoanRejected = c.s.executeQuery("SELECT COUNT(*) FROM loan_requests WHERE status='Rejected'");
            if (rsLoanRejected.next()) {
                lblRejectedLoans.setText("Rejected Loans: " + rsLoanRejected.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data counts from database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showStaffOfTheMonth() {
        try {
            Conn c = new Conn();

            // Step 1: Find the employee(s) with the highest attendance count for the current month.
            String attendanceQuery = "SELECT empId, COUNT(*) AS attendance_count FROM attendance " +
                                     "WHERE MONTH(date) = MONTH(CURRENT_DATE()) AND YEAR(date) = YEAR(CURRENT_DATE()) " +
                                     "GROUP BY empId ORDER BY attendance_count DESC LIMIT 1";

            ResultSet rsAttendance = c.s.executeQuery(attendanceQuery);
            
            String winnerEmpId = null;
            int maxAttendance = 0;
            
            if (rsAttendance.next()) {
                winnerEmpId = rsAttendance.getString("empId");
                maxAttendance = rsAttendance.getInt("attendance_count");
            }
            
            if (winnerEmpId == null) {
                JOptionPane.showMessageDialog(this, "No attendance data found for the current month.", "Staff of the Month", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Check for a tie in attendance
            String tieCheckQuery = "SELECT COUNT(*) FROM (" +
                                   "SELECT empId FROM attendance " +
                                   "WHERE MONTH(date) = MONTH(CURRENT_DATE()) AND YEAR(date) = YEAR(CURRENT_DATE()) " +
                                   "GROUP BY empId HAVING COUNT(*) = " + maxAttendance +
                                   ") AS T";
            
            ResultSet rsTieCheck = c.s.executeQuery(tieCheckQuery);
            rsTieCheck.next();
            int tieCount = rsTieCheck.getInt(1);

            if (tieCount > 1) {
                // Step 2: If there's a tie, find the employee with the most total time spent at the office
                String timeQuery = "SELECT empId, SUM(TIMESTAMPDIFF(MINUTE, checkin_time, checkout_time)) AS total_minutes FROM attendance " +
                                   "WHERE MONTH(date) = MONTH(CURRENT_DATE()) AND YEAR(date) = YEAR(CURRENT_DATE()) " +
                                   "AND empId IN (SELECT empId FROM attendance WHERE MONTH(date) = MONTH(CURRENT_DATE()) GROUP BY empId HAVING COUNT(*) = " + maxAttendance + ") " +
                                   "AND checkout_time IS NOT NULL GROUP BY empId ORDER BY total_minutes DESC LIMIT 1";
                
                ResultSet rsTime = c.s.executeQuery(timeQuery);
                if (rsTime.next()) {
                    winnerEmpId = rsTime.getString("empId");
                }
            }
            
            // Step 3: Get the winner's details from the employee table
            String employeeDetailsQuery = "SELECT name, designation FROM employee WHERE empId = ?";
            PreparedStatement pstmt = c.c.prepareStatement(employeeDetailsQuery);
            pstmt.setString(1, winnerEmpId);
            ResultSet rsDetails = pstmt.executeQuery();

            if (rsDetails.next()) {
                String name = rsDetails.getString("name");
                String designation = rsDetails.getString("designation");
                
                String message = String.format("<html><b><font size='+1'>Staff of the Month</font></b><br><br>" +
                                               "<b>Name:</b> %s<br>" +
                                               "<b>Employee ID:</b> %s<br>" +
                                               "<b>Designation:</b> %s</html>", name, winnerEmpId, designation);

                // Add to Notice Board Logic (Updated for 'notices' table)
                String noticeContent = "Congratulations to " + name + " for being the Staff of the Month!\n" +
                                    "Employee ID: " + winnerEmpId + "\n" +
                                    "Designation: " + designation + "\n\n" +
                                    "Your dedication and hard work are truly appreciated.";

                String noticeQuery = "INSERT INTO notices (notice, date) VALUES (?, ?)";
                PreparedStatement noticePstmt = c.c.prepareStatement(noticeQuery);
                noticePstmt.setString(1, noticeContent);
                noticePstmt.setString(2, LocalDate.now().toString());
                noticePstmt.executeUpdate();

                JOptionPane.showMessageDialog(this, message, "Staff of the Month", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Employee details not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while fetching or adding data.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();

        if (source == btnSearchDate) {
            Date date = dateChooser.getDate();
            if (date != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String selectedDate = sdf.format(date);
                refreshCounts(selectedDate);
            }
        } else if (source == staffOfTheMonth) {
            showStaffOfTheMonth();
        } else if (source == changePassword) {
            new ChangePasswordAdmin(this);
        } else if (source == add) {
            setVisible(false);
            new AddEmployee();
        } else if (source == view) {
            setVisible(false);
            new ViewEmployee();
        } else if (source == update) {
            setVisible(false);
            new UpdateEmployee(null);  
        } else if (source == remove) {
            setVisible(false);
            new RemoveEmployee();
        } else if (source == attendance) {
            setVisible(false);
            new AttendanceChecker(this);
        } else if (source == leaves) {
            setVisible(false);
            new ManageLeaves(this);
        } else if (source == salary) {
            setVisible(false);
            new ManageSalaryAdmin();
        } else if (source == notice) {
            setVisible(false);
            new AdminNoticeBoard();
        } else if (source == loanRequest) {
            setVisible(false);
            new LoanRequestAdmin();
        } else if (source == logout) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new AdminLogin();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Home::new);
    }
}
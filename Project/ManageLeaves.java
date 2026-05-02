/*package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ManageLeaves extends JFrame implements ActionListener {

    JTable table;
    DefaultTableModel model;
    JButton approveBtn, rejectBtn, backBtn, searchBtn, showAllBtn;
    JTextField searchField;
    Home homeRef;

    ManageLeaves(Home homeRef) {
        this.homeRef = homeRef;

        setTitle("Manage Leave Applications");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(new Color(240, 248, 255));
        setContentPane(contentPanel);

        JLabel heading = new JLabel("Leave Requests", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 36));
        heading.setBounds(500, 30, 400, 50);
        contentPanel.add(heading);

        // Search Label
        JLabel searchLabel = new JLabel("Search by Employee ID:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 16));
        searchLabel.setBounds(200, 100, 180, 30);
        contentPanel.add(searchLabel);

        // Search TextField
        searchField = new JTextField();
        searchField.setBounds(380, 100, 200, 30);
        contentPanel.add(searchField);

        // Search Button
        searchBtn = new JButton("Search");
        searchBtn.setBounds(600, 100, 100, 30);
        searchBtn.setBackground(new Color(52, 152, 219));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFont(new Font("Arial", Font.BOLD, 16));
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchBtn.addActionListener(this);
        contentPanel.add(searchBtn);

        // Show All Button
        showAllBtn = new JButton("Show All");
        showAllBtn.setBounds(710, 100, 100, 30);
        showAllBtn.setBackground(new Color(39, 174, 96));
        showAllBtn.setForeground(Color.WHITE);
        showAllBtn.setFont(new Font("Arial", Font.BOLD, 16));
        showAllBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showAllBtn.addActionListener(this);
        contentPanel.add(showAllBtn);

        model = new DefaultTableModel();
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);

        model.addColumn("Request ID");
        model.addColumn("Employee ID");
        model.addColumn("Leave Type");
        model.addColumn("Reason");
        model.addColumn("Leave Date");
        model.addColumn("Status");

        fetchLeaveRequests(null); // load all initially

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(200, 140, 1000, 400);
        contentPanel.add(scrollPane);

        approveBtn = new JButton("Approve");
        approveBtn.setBounds(400, 560, 150, 40);
        approveBtn.setBackground(new Color(0, 153, 76));
        approveBtn.setForeground(Color.WHITE);
        approveBtn.setFont(new Font("Arial", Font.BOLD, 16));
        approveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        approveBtn.addActionListener(this);
        contentPanel.add(approveBtn);

        rejectBtn = new JButton("Reject");
        rejectBtn.setBounds(600, 560, 150, 40);
        rejectBtn.setBackground(Color.RED);
        rejectBtn.setForeground(Color.WHITE);
        rejectBtn.setFont(new Font("Arial", Font.BOLD, 16));
        rejectBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rejectBtn.addActionListener(this);
        contentPanel.add(rejectBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(800, 560, 150, 40);
        backBtn.setBackground(Color.RED);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Arial", Font.BOLD, 16));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setOpaque(true);
        backBtn.addActionListener(this);
        backBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                backBtn.setBackground(Color.RED);
            }
            public void mouseExited(MouseEvent e) {
                backBtn.setBackground(Color.RED);
            }
        });
        contentPanel.add(backBtn);

        setVisible(true);
    }

    private void fetchLeaveRequests(String empIdFilter) {
        model.setRowCount(0); // clear table first
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM leave_requests";
            if (empIdFilter != null && !empIdFilter.trim().isEmpty()) {
                query += " WHERE empId = ?";
            }
            PreparedStatement ps = c.c.prepareStatement(query);
            if (empIdFilter != null && !empIdFilter.trim().isEmpty()) {
                ps.setString(1, empIdFilter.trim());
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("empId"),
                    rs.getString("leaveType"),
                    rs.getString("reason"),
                    rs.getString("leaveDate"),
                    rs.getString("status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching leave requests.");
        }
    }

    private void updateLeaveStatus(String status) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request to " + status.toLowerCase() + "!");
            return;
        }

        int requestId = (int) model.getValueAt(selectedRow, 0);

        try {
            Conn c = new Conn();
            PreparedStatement ps = c.c.prepareStatement("UPDATE leave_requests SET status = ? WHERE id = ?");
            ps.setString(1, status);
            ps.setInt(2, requestId);
            ps.executeUpdate();

            model.setValueAt(status, selectedRow, 5);
            JOptionPane.showMessageDialog(this, "Request " + status + " successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating status!");
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == approveBtn) {
            updateLeaveStatus("Approved");
        } else if (ae.getSource() == rejectBtn) {
            updateLeaveStatus("Rejected");
        } else if (ae.getSource() == backBtn) {
            dispose();
            homeRef.setVisible(true);
        } else if (ae.getSource() == searchBtn) {
            String empId = searchField.getText();
            fetchLeaveRequests(empId);
        } else if (ae.getSource() == showAllBtn) {
            searchField.setText("");
            fetchLeaveRequests(null);
        }
    }
}
*/
/*
package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.awt.geom.RoundRectangle2D;

public class ManageLeaves extends JFrame implements ActionListener {

    JTable table;
    DefaultTableModel model;
    JButton approveBtn, rejectBtn, backBtn, searchBtn, showAllBtn;
    JTextField searchField;
    Home homeRef;

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

    public ManageLeaves(Home homeRef) {
        this.homeRef = homeRef;

        setTitle("Manage Leave Applications");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(new Color(240, 248, 255));
        setContentPane(contentPanel);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;

        JLabel heading = new JLabel("Leave Requests", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 36));
        heading.setBounds((screenWidth - 400) / 2, 30, 400, 50);
        contentPanel.add(heading);

        // Search Label
        JLabel searchLabel = new JLabel("Search by Employee ID:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 16));
        searchLabel.setBounds((screenWidth - 800) / 2, 100, 180, 30);
        contentPanel.add(searchLabel);

        // Search TextField
        searchField = new JTextField();
        searchField.setBounds((screenWidth - 800) / 2 + 190, 100, 200, 30);
        contentPanel.add(searchField);

        // Search Button
        searchBtn = new RoundedButton("Search", new Color(52, 152, 219));
        searchBtn.setBounds((screenWidth - 800) / 2 + 400, 100, 100, 30);
        searchBtn.addActionListener(this);
        contentPanel.add(searchBtn);

        // Show All Button
        showAllBtn = new RoundedButton("Show All", new Color(39, 174, 96));
        showAllBtn.setBounds((screenWidth - 800) / 2 + 510, 100, 100, 30);
        showAllBtn.addActionListener(this);
        contentPanel.add(showAllBtn);

        model = new DefaultTableModel();
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);

        model.addColumn("Request ID");
        model.addColumn("Employee ID");
        model.addColumn("Leave Type");
        model.addColumn("Reason");
        model.addColumn("Leave Dates"); // Changed from "Leave Date" to "Leave Dates"
        model.addColumn("Status");

        fetchLeaveRequests(null); // load all initially

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds((screenWidth - 1000) / 2, 140, 1000, 400);
        contentPanel.add(scrollPane);

        approveBtn = new RoundedButton("Approve", new Color(0, 153, 76));
        approveBtn.setBounds((screenWidth - 600) / 2 - 10, 560, 150, 40);
        approveBtn.addActionListener(this);
        contentPanel.add(approveBtn);

        rejectBtn = new RoundedButton("Reject", new Color(204, 0, 0));
        rejectBtn.setBounds((screenWidth - 600) / 2 + 160, 560, 150, 40);
        rejectBtn.addActionListener(this);
        contentPanel.add(rejectBtn);

        backBtn = new RoundedButton("Back", new Color(128, 0, 128)); // Purple color for back button
        backBtn.setBounds((screenWidth - 600) / 2 + 330, 560, 150, 40);
        backBtn.addActionListener(this);
        contentPanel.add(backBtn);

        setVisible(true);
    }

    private void fetchLeaveRequests(String empIdFilter) {
        model.setRowCount(0); // clear table first
        try {
            Conn c = new Conn();
            String query = "SELECT id, empId, leaveType, reason, startleaveDate, endleaveDate, status FROM leave_requests";
            if (empIdFilter != null && !empIdFilter.trim().isEmpty()) {
                query += " WHERE empId = ?";
            }
            PreparedStatement ps = c.c.prepareStatement(query);
            if (empIdFilter != null && !empIdFilter.trim().isEmpty()) {
                ps.setString(1, empIdFilter.trim());
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String startDate = rs.getString("startleaveDate");
                String endDate = rs.getString("endleaveDate");
                String leaveDates = startDate + " to " + endDate;
                
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("empId"),
                    rs.getString("leaveType"),
                    rs.getString("reason"),
                    leaveDates,
                    rs.getString("status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching leave requests.");
        }
    }

    private void updateLeaveStatus(String status) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request to " + status.toLowerCase() + "!");
            return;
        }
        
        // Ensure status is updatable (e.g., only update Pending leaves)
        String currentStatus = (String) model.getValueAt(selectedRow, 5);
        if (!currentStatus.equals("Pending")) {
            JOptionPane.showMessageDialog(this, "Cannot change status of a non-pending request.");
            return;
        }

        int requestId = (int) model.getValueAt(selectedRow, 0);

        try {
            Conn c = new Conn();
            PreparedStatement ps = c.c.prepareStatement("UPDATE leave_requests SET status = ? WHERE id = ?");
            ps.setString(1, status);
            ps.setInt(2, requestId);
            ps.executeUpdate();

            model.setValueAt(status, selectedRow, 5);
            JOptionPane.showMessageDialog(this, "Request " + status + " successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating status!");
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == approveBtn) {
            updateLeaveStatus("Approved");
        } else if (ae.getSource() == rejectBtn) {
            updateLeaveStatus("Rejected");
        } else if (ae.getSource() == backBtn) {
            dispose();
            homeRef.setVisible(true);
        } else if (ae.getSource() == searchBtn) {
            String empId = searchField.getText();
            fetchLeaveRequests(empId);
        } else if (ae.getSource() == showAllBtn) {
            searchField.setText("");
            fetchLeaveRequests(null);
        }
    }
}
*/
/*
package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.awt.geom.RoundRectangle2D;

public class ManageLeaves extends JFrame implements ActionListener {

    JTable table;
    DefaultTableModel model;
    JButton approveBtn, rejectBtn, backBtn, searchBtn, showAllBtn;
    JTextField searchField;
    Home homeRef;

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

    public ManageLeaves(Home homeRef) {
        this.homeRef = homeRef;

        setTitle("Manage Leave Applications");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(240, 248, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        JLabel heading = new JLabel("Leave Requests", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 40));
        heading.setForeground(new Color(34, 45, 65)); // Heading color changed
        headerPanel.add(heading);
        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel for centering
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel, BorderLayout.CENTER);

        // Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        searchPanel.setBackground(new Color(240, 248, 255));
        
        JLabel searchLabel = new JLabel("Search by Employee ID:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 16));
        searchField = new JTextField(15);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchBtn = new RoundedButton("Search", new Color(52, 152, 219));
        searchBtn.addActionListener(this);
        showAllBtn = new RoundedButton("Show All", new Color(39, 174, 96));
        showAllBtn.addActionListener(this);
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(showAllBtn);
        
        mainPanel.add(searchPanel);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        tablePanel.setMaximumSize(new Dimension(1000, 400));
        
        model = new DefaultTableModel();
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(220, 220, 220));
        
        model.addColumn("Request ID");
        model.addColumn("Employee ID");
        model.addColumn("Leave Type");
        model.addColumn("Reason");
        model.addColumn("Leave Dates");
        model.addColumn("Status");

        fetchLeaveRequests(null);

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(tablePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));
        
        approveBtn = new RoundedButton("Approve", new Color(0, 153, 76));
        approveBtn.addActionListener(this);
        
        rejectBtn = new RoundedButton("Reject", new Color(204, 0, 0));
        rejectBtn.addActionListener(this);
        
        backBtn = new RoundedButton("Back", new Color(128, 0, 128));
        backBtn.addActionListener(this);
        
        buttonPanel.add(approveBtn);
        buttonPanel.add(rejectBtn);
        buttonPanel.add(backBtn);
        
        mainPanel.add(buttonPanel);
        
        // Final layout adjustment to center the main panel
        JPanel containerPanel = new JPanel(new GridBagLayout());
        containerPanel.setBackground(new Color(240, 248, 255));
        containerPanel.add(mainPanel);
        add(containerPanel, BorderLayout.CENTER);
        
        setVisible(true);
    }

    private void fetchLeaveRequests(String empIdFilter) {
        model.setRowCount(0);
        try {
            Conn c = new Conn();
            String query = "SELECT id, empId, leaveType, reason, startleaveDate, endleaveDate, status FROM leave_requests";
            if (empIdFilter != null && !empIdFilter.trim().isEmpty()) {
                query += " WHERE empId = ?";
            }
            PreparedStatement ps = c.c.prepareStatement(query);
            if (empIdFilter != null && !empIdFilter.trim().isEmpty()) {
                ps.setString(1, empIdFilter.trim());
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String startDate = rs.getString("startleaveDate");
                String endDate = rs.getString("endleaveDate");
                String leaveDates = startDate + " to " + endDate;
                
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("empId"),
                    rs.getString("leaveType"),
                    rs.getString("reason"),
                    leaveDates,
                    rs.getString("status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching leave requests.");
        }
    }

    private void updateLeaveStatus(String status) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request to " + status.toLowerCase() + "!");
            return;
        }
        
        String currentStatus = (String) model.getValueAt(selectedRow, 5);
        if (!currentStatus.equals("Pending")) {
            JOptionPane.showMessageDialog(this, "Cannot change status of a non-pending request.");
            return;
        }

        int requestId = (int) model.getValueAt(selectedRow, 0);

        try {
            Conn c = new Conn();
            PreparedStatement ps = c.c.prepareStatement("UPDATE leave_requests SET status = ? WHERE id = ?");
            ps.setString(1, status);
            ps.setInt(2, requestId);
            ps.executeUpdate();

            model.setValueAt(status, selectedRow, 5);
            JOptionPane.showMessageDialog(this, "Request " + status + " successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating status!");
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == approveBtn) {
            updateLeaveStatus("Approved");
        } else if (ae.getSource() == rejectBtn) {
            updateLeaveStatus("Rejected");
        } else if (ae.getSource() == backBtn) {
            dispose();
            homeRef.setVisible(true);
        } else if (ae.getSource() == searchBtn) {
            String empId = searchField.getText();
            fetchLeaveRequests(empId);
        } else if (ae.getSource() == showAllBtn) {
            searchField.setText("");
            fetchLeaveRequests(null);
        }
    }
}*/

package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.awt.geom.RoundRectangle2D;

public class ManageLeaves extends JFrame implements ActionListener {

    JTable table;
    DefaultTableModel model;
    JButton approveBtn, rejectBtn, backBtn, searchBtn, showAllBtn;
    JTextField searchField;
    Home homeRef;

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

    public ManageLeaves(Home homeRef) {
        this.homeRef = homeRef;

        setTitle("Manage Leave Applications");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Header Panel (similar to AddEmployee.java)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;

        JLabel title = new JLabel("Leave Requests", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(0, 102, 204));
        title.setBounds(0, 0, screenWidth, 80);
        
        JPanel headerContainer = new JPanel(null);
        headerContainer.setPreferredSize(new Dimension(screenWidth, 80));
        headerContainer.setBackground(new Color(0, 102, 204));
        headerContainer.add(title);
        add(headerContainer, BorderLayout.NORTH);

        // Main Content Panel for centering
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        searchPanel.setBackground(new Color(240, 248, 255));
        
        JLabel searchLabel = new JLabel("Search by Employee ID:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 16));
        searchField = new JTextField(15);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchBtn = new RoundedButton("Search", new Color(52, 152, 219));
        searchBtn.addActionListener(this);
        showAllBtn = new RoundedButton("Show All", new Color(39, 174, 96));
        showAllBtn.addActionListener(this);
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(showAllBtn);
        
        mainPanel.add(searchPanel);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        tablePanel.setMaximumSize(new Dimension(1000, 400));
        
        model = new DefaultTableModel();
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(220, 220, 220));
        
        model.addColumn("Request ID");
        model.addColumn("Employee ID");
        model.addColumn("Leave Type");
        model.addColumn("Reason");
        model.addColumn("Leave Dates");
        model.addColumn("Status");

        fetchLeaveRequests(null);

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(tablePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));
        
        approveBtn = new RoundedButton("Approve", new Color(0, 153, 76));
        approveBtn.addActionListener(this);
        
        rejectBtn = new RoundedButton("Reject", new Color(204, 0, 0));
        rejectBtn.addActionListener(this);
        
        backBtn = new RoundedButton("Back", new Color(128, 0, 128));
        backBtn.addActionListener(this);
        
        buttonPanel.add(approveBtn);
        buttonPanel.add(rejectBtn);
        buttonPanel.add(backBtn);
        
        mainPanel.add(buttonPanel);
        
        // Final layout adjustment to center the main panel
        JPanel containerPanel = new JPanel(new GridBagLayout());
        containerPanel.setBackground(new Color(240, 248, 255));
        containerPanel.add(mainPanel);
        add(containerPanel, BorderLayout.CENTER);
        
        setVisible(true);
    }

    private void fetchLeaveRequests(String empIdFilter) {
        model.setRowCount(0);
        try {
            Conn c = new Conn();
            String query = "SELECT id, empId, leaveType, reason, startleaveDate, endleaveDate, status FROM leave_requests";
            if (empIdFilter != null && !empIdFilter.trim().isEmpty()) {
                query += " WHERE empId = ?";
            }
            PreparedStatement ps = c.c.prepareStatement(query);
            if (empIdFilter != null && !empIdFilter.trim().isEmpty()) {
                ps.setString(1, empIdFilter.trim());
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String startDate = rs.getString("startleaveDate");
                String endDate = rs.getString("endleaveDate");
                String leaveDates = startDate + " to " + endDate;
                
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("empId"),
                    rs.getString("leaveType"),
                    rs.getString("reason"),
                    leaveDates,
                    rs.getString("status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching leave requests.");
        }
    }

    private void updateLeaveStatus(String status) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request to " + status.toLowerCase() + "!");
            return;
        }
        
        String currentStatus = (String) model.getValueAt(selectedRow, 5);
        if (!currentStatus.equals("Pending")) {
            JOptionPane.showMessageDialog(this, "Cannot change status of a non-pending request.");
            return;
        }

        int requestId = (int) model.getValueAt(selectedRow, 0);

        try {
            Conn c = new Conn();
            PreparedStatement ps = c.c.prepareStatement("UPDATE leave_requests SET status = ? WHERE id = ?");
            ps.setString(1, status);
            ps.setInt(2, requestId);
            ps.executeUpdate();

            model.setValueAt(status, selectedRow, 5);
            JOptionPane.showMessageDialog(this, "Request " + status + " successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating status!");
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == approveBtn) {
            updateLeaveStatus("Approved");
        } else if (ae.getSource() == rejectBtn) {
            updateLeaveStatus("Rejected");
        } else if (ae.getSource() == backBtn) {
            dispose();
            homeRef.setVisible(true);
        } else if (ae.getSource() == searchBtn) {
            String empId = searchField.getText();
            fetchLeaveRequests(empId);
        } else if (ae.getSource() == showAllBtn) {
            searchField.setText("");
            fetchLeaveRequests(null);
        }
    }
}

package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EmployeePanel extends JFrame implements ActionListener {

    JButton btnViewUpdateProfile, btnApplyLeave, btnSalaryDetails, btnLogout, btnAttendance, btnAttendanceRecord, btnNoticeBoard, btnChangePassword, btnApplyLoan;
    JPanel infoPanel;
    String employeeId;

    public EmployeePanel(String employeeId) {
        this.employeeId = employeeId;

        setTitle("Staff Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        
        JLabel heading = new JLabel("Welcome to Staff Dashboard", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 38));
        heading.setForeground(new Color(33, 64, 95));
        heading.setOpaque(true);
        heading.setBackground(new Color(230, 230, 250));
        heading.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        add(heading, BorderLayout.NORTH);

        // LEFT PANEL - Styled Buttons
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(300, getHeight()));
        leftPanel.setBackground(new Color(34, 45, 65));
        leftPanel.setLayout(new GridLayout(9, 1, 15, 15));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(50, 30, 50, 30));

        btnViewUpdateProfile = createButton("View & Update Profile", new Color(52, 152, 219));
        btnApplyLeave = createButton("Apply for Leave", new Color(46, 204, 113));
        btnSalaryDetails = createButton("Salary Details", new Color(155, 89, 182));
        btnAttendance = createButton("Attendance", new Color(241, 196, 15));
        btnAttendanceRecord = createButton("Attendance Record", new Color(255, 165, 0));
        btnNoticeBoard = createButton("Notice Board", new Color(230, 126, 34));
        btnChangePassword = createButton("Change Password", new Color(41, 128, 185));
        btnApplyLoan = createButton("Apply for Loan", new Color(26, 188, 156));
        btnLogout = createButton("Logout", new Color(231, 76, 60));

        leftPanel.add(btnViewUpdateProfile);
        leftPanel.add(btnApplyLeave);
        leftPanel.add(btnSalaryDetails);
        leftPanel.add(btnAttendance);
        leftPanel.add(btnAttendanceRecord);
        leftPanel.add(btnNoticeBoard);
        leftPanel.add(btnChangePassword);
        leftPanel.add(btnApplyLoan);
        leftPanel.add(btnLogout);

        add(leftPanel, BorderLayout.WEST);

        // RIGHT PANEL - Employee Info
        infoPanel = new JPanel();
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        infoPanel.setLayout(new BorderLayout());

        add(infoPanel, BorderLayout.CENTER);
        loadEmployeeData();
        setVisible(true);
    }

    private void loadEmployeeData() {
        try {
            Conn c = new Conn();
            String sql = "SELECT * FROM employee WHERE empId = ?";
            PreparedStatement ps = c.c.prepareStatement(sql);
            ps.setString(1, employeeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                infoPanel.removeAll();
                JPanel container = new JPanel();
                container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
                container.setBackground(Color.WHITE);

                container.add(createGroupPanel(new String[]{
                        "🆔 Employee ID: " + rs.getString("empId"),
                        "👤 Name: " + rs.getString("name"),
                        "🏢 Designation: " + rs.getString("designation")
                }, new Color(224, 243, 250)));

                container.add(createGroupPanel(new String[]{
                        "📞 Phone: " + rs.getString("phone"),
                        "📧 Email: " + rs.getString("email"),
                        "📍 Address: " + rs.getString("address")
                }, new Color(235, 247, 235)));

                container.add(createGroupPanel(new String[]{
                        "📅 Join Date: " + rs.getString("job_start_date"),
                        "💰 Net Salary: " + rs.getString("salary"),
                        "🎁 Bonus This Month: " + rs.getDouble("bonus")
                }, new Color(255, 243, 205)));

                JPanel motivationPanel = new JPanel(new BorderLayout());
                motivationPanel.setBackground(new Color(232, 228, 241));
                motivationPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
                JLabel motivationLabel = new JLabel("<html><i>\"Success usually comes to those who are too busy to be looking for it.\"</i></html>");
                motivationLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                motivationLabel.setForeground(new Color(52, 73, 94));
                motivationPanel.add(motivationLabel, BorderLayout.CENTER);

                container.add(Box.createVerticalStrut(15));
                container.add(motivationPanel);

                infoPanel.add(container, BorderLayout.NORTH);
                infoPanel.revalidate();
                infoPanel.repaint();
            }

            rs.close();
            ps.close();
            c.c.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load employee data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createGroupPanel(String[] texts, Color bgColor) {
        JPanel panel = new JPanel(new GridLayout(texts.length, 1));
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        for (String text : texts) {
            JLabel label = new JLabel(text);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            label.setForeground(new Color(44, 62, 80));
            label.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            label.setOpaque(true);
            label.setBackground(bgColor);
            panel.add(label);
        }

        return panel;
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text) {
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

        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.darker());
                btn.repaint();
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
                btn.repaint();
            }
        });

        btn.addActionListener(this);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object src = ae.getSource();
        if (src == btnViewUpdateProfile) {
            setVisible(false);
            new ViewProfile(employeeId, this);
        } else if (src == btnApplyLeave) {
            setVisible(false);
            new ApplyLeave(employeeId, this);
        } else if (src == btnSalaryDetails) {
            setVisible(false);
            new SalaryDetails(employeeId, this);
        } else if (src == btnAttendance) {
            setVisible(false);
            new Attendance(employeeId, this);
        } else if (src == btnAttendanceRecord) {
            setVisible(false);
            new AttendanceRecord(employeeId, this);
        } else if (src == btnNoticeBoard) {
            setVisible(false);
            new Notice(employeeId, this);
        } else if (src == btnChangePassword) {
            new ChangePasswordEmployee(employeeId);
        } else if (src == btnApplyLoan) {
            setVisible(false);
            new ApplyLoan(employeeId, this);
        } else if (src == btnLogout) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new EmployeeLogin();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeePanel("1001")); // Replace with real empId
    }
}

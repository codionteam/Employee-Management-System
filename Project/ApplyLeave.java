package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.awt.geom.RoundRectangle2D; // Import for drawing rounded rectangle
import com.toedter.calendar.JDateChooser;

public class ApplyLeave extends JFrame implements ActionListener {

    Choice leaveType;
    JTextArea reasonArea;
    JDateChooser startDateChooser, endDateChooser;
    JButton apply, back;
    String empId;
    EmployeePanel parentPanel;

    ApplyLeave(String empId, EmployeePanel parentPanel) {
        this.empId = empId;
        this.parentPanel = parentPanel;

        setTitle("Apply Leave");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Make window full screen

        // Header Panel
        JPanel header = new JPanel();
        header.setBackground(new Color(60, 90, 153));
        header.setPreferredSize(new Dimension(getWidth(), 80));
        JLabel heading = new JLabel("Apply for Leave");
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 32));
        header.setLayout(new FlowLayout(FlowLayout.CENTER));
        header.add(heading);

        // Content Panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblType = new JLabel("Leave Type:");
        lblType.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(lblType, gbc);

        leaveType = new Choice();
        leaveType.add("Pain Leave");
        leaveType.add("Unpaid Leave");
        leaveType.add("Casual Leave");
        leaveType.add("Study Leave");
        leaveType.add("Marriage Leave");
        leaveType.add("Emergency Leave");
        leaveType.add("Medical Leave");
        leaveType.add("Death of Family");
        leaveType.add("Tour Leave");
        leaveType.add("Long Journey Leave");
        leaveType.add("Maternity Leave");
        leaveType.add("Paternity Leave");
        leaveType.add("Government Duty");
        leaveType.add("Exam Leave");
        leaveType.add("Other");
        leaveType.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridx = 1;
        contentPanel.add(leaveType, gbc);

        JLabel lblStart = new JLabel("Start Date:");
        lblStart.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(lblStart, gbc);

        startDateChooser = new JDateChooser();
        startDateChooser.setDateFormatString("yyyy-MM-dd");
        startDateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        startDateChooser.setPreferredSize(new Dimension(200, 35));
        gbc.gridx = 1;
        contentPanel.add(startDateChooser, gbc);

        JLabel lblEnd = new JLabel("End Date:");
        lblEnd.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(lblEnd, gbc);

        endDateChooser = new JDateChooser();
        endDateChooser.setDateFormatString("yyyy-MM-dd");
        endDateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        endDateChooser.setPreferredSize(new Dimension(200, 35));
        gbc.gridx = 1;
        contentPanel.add(endDateChooser, gbc);

        JLabel lblReason = new JLabel("Reason:");
        lblReason.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(lblReason, gbc);

        reasonArea = new JTextArea(4, 20);
        reasonArea.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(reasonArea);
        gbc.gridx = 1;
        contentPanel.add(scroll, gbc);

        // Buttons
        apply = new RoundedButton("Apply", new Color(46, 204, 113), Color.WHITE, new Font("Segoe UI", Font.BOLD, 18), new Dimension(120, 40));
        back = new RoundedButton("Back", new Color(231, 76, 60), Color.WHITE, new Font("Segoe UI", Font.BOLD, 18), new Dimension(120, 40));

        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPanel.add(apply, gbc);

        gbc.gridx = 1;
        contentPanel.add(back, gbc);

        // Add panels to frame
        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        apply.addActionListener(this);
        back.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == apply) {
            String type = leaveType.getSelectedItem();
            String reason = reasonArea.getText();
            java.util.Date startDate = startDateChooser.getDate();
            java.util.Date endDate = endDateChooser.getDate();

            if (startDate == null || endDate == null || reason.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields are required.");
                return;
            }
            
            // Validation: End date cannot be before start date
            if (endDate.before(startDate)) {
                JOptionPane.showMessageDialog(this, "Leave finish date cannot be before leave begin date!", "Invalid Date", JOptionPane.WARNING_MESSAGE);
                return;
            }

            java.sql.Date sqlStart = new java.sql.Date(startDate.getTime());
            java.sql.Date sqlEnd = new java.sql.Date(endDate.getTime());

            try {
                Conn c = new Conn();
                String query = "INSERT INTO leave_requests (empId, leaveType, reason, startLeaveDate, endLeaveDate) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = c.c.prepareStatement(query);
                ps.setString(1, empId);
                ps.setString(2, type);
                ps.setString(3, reason);
                ps.setDate(4, sqlStart);
                ps.setDate(5, sqlEnd);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Leave applied successfully.");
                setVisible(false);
                parentPanel.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == back) {
            setVisible(false);
            parentPanel.setVisible(true);
        }
    }

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

            // Add hover effect
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
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 25, 25));

            super.paintComponent(g);
            g2.dispose();
        }
    }
}
/*package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ChangePasswordEmployee extends JFrame implements ActionListener {

    String empId;
    JPasswordField currentPassField, newPassField, confirmPassField;
    JButton changeBtn, backBtn;

    public ChangePasswordEmployee(String empId) {
        this.empId = empId;

        setTitle("Employee - Change Password");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 255, 240)); // Light green background
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Change Your Password", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(new Color(0, 102, 51));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(title, gbc);

        // Current password
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        mainPanel.add(createLabel("Current Password:"), gbc);
        gbc.gridx = 1;
        currentPassField = new JPasswordField(20);
        mainPanel.add(currentPassField, gbc);

        // New password
        gbc.gridy++;
        gbc.gridx = 0;
        mainPanel.add(createLabel("New Password:"), gbc);
        gbc.gridx = 1;
        newPassField = new JPasswordField(20);
        mainPanel.add(newPassField, gbc);

        // Confirm password
        gbc.gridy++;
        gbc.gridx = 0;
        mainPanel.add(createLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        confirmPassField = new JPasswordField(20);
        mainPanel.add(confirmPassField, gbc);

        // Buttons
        gbc.gridy++;
        gbc.gridx = 0;
        changeBtn = new JButton("Change Password");
        changeBtn.setBackground(new Color(0, 153, 76));
        changeBtn.setForeground(Color.WHITE);
        changeBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        changeBtn.addActionListener(this);
        mainPanel.add(changeBtn, gbc);

        gbc.gridx = 1;
        backBtn = new JButton("Back");
        backBtn.setBackground(new Color(204, 0, 0));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backBtn.addActionListener(this);
        mainPanel.add(backBtn, gbc);

        add(mainPanel);
        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        label.setForeground(new Color(0, 51, 0));
        return label;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            dispose(); // Just close this window
            return;
        }

        String current = new String(currentPassField.getPassword());
        String newPass = new String(newPassField.getPassword());
        String confirmPass = new String(confirmPassField.getPassword());

        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "New password and confirm password do not match.", "Mismatch", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Conn c = new Conn();
            String q = "SELECT * FROM employeelogin WHERE username='" + empId + "' AND password='" + current + "'";
            ResultSet rs = c.s.executeQuery(q);

            if (rs.next()) {
                String update = "UPDATE employeelogin SET password='" + newPass + "' WHERE username='" + empId + "'";
                c.s.executeUpdate(update);
                JOptionPane.showMessageDialog(this, "Password changed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect current password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while changing password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new ChangePasswordEmployee("empId"); // Demo employee ID
    }
}
*/

package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;

public class ChangePasswordEmployee extends JFrame implements ActionListener {

    String empId;
    JPasswordField currentPassField, newPassField, confirmPassField;
    JButton changeBtn, backBtn;

    // Custom JButton with rounded corners and hover effect
    private static class RoundedButton extends JButton {
        private Color baseColor;

        public RoundedButton(String text, Color bgColor) {
            super(text);
            this.baseColor = bgColor;
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setBackground(baseColor);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(baseColor.darker());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(baseColor);
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

    public ChangePasswordEmployee(String empId) {
        this.empId = empId;

        setTitle("Staff - Change Password");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout()); // Use BorderLayout for the frame

        // Header Panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        headerPanel.setBackground(new Color(60, 90, 153));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 100));
        JLabel title = new JLabel("Change Your Password", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        headerPanel.add(title);
        add(headerPanel, BorderLayout.NORTH);

        // Main content Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 248, 255)); // Light blue-ish background
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Current password
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(createLabel("Current Password:"), gbc);
        gbc.gridx = 1;
        currentPassField = createPasswordField();
        mainPanel.add(currentPassField, gbc);

        // New password
        gbc.gridy++;
        gbc.gridx = 0;
        mainPanel.add(createLabel("New Password:"), gbc);
        gbc.gridx = 1;
        newPassField = createPasswordField();
        mainPanel.add(newPassField, gbc);

        // Confirm password
        gbc.gridy++;
        gbc.gridx = 0;
        mainPanel.add(createLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        confirmPassField = createPasswordField();
        mainPanel.add(confirmPassField, gbc);

        // Buttons
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        changeBtn = new RoundedButton("Change Password", new Color(46, 204, 113));
        changeBtn.addActionListener(this);
        mainPanel.add(changeBtn, gbc);

        gbc.gridx = 1;
        backBtn = new RoundedButton("Back", new Color(231, 76, 60));
        backBtn.addActionListener(this);
        mainPanel.add(backBtn, gbc);

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        label.setForeground(new Color(33, 64, 95));
        return label;
    }

    private JPasswordField createPasswordField() {
        JPasswordField pf = new JPasswordField(20);
        pf.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        pf.setPreferredSize(new Dimension(250, 40));
        return pf;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            dispose(); // Just close this window
            return;
        }

        String current = new String(currentPassField.getPassword());
        String newPass = new String(newPassField.getPassword());
        String confirmPass = new String(confirmPassField.getPassword());

        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "New password and confirm password do not match.", "Mismatch", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Conn c = new Conn();
            String q = "SELECT * FROM employeelogin WHERE username='" + empId + "' AND password='" + current + "'";
            ResultSet rs = c.s.executeQuery(q);

            if (rs.next()) {
                String update = "UPDATE employeelogin SET password='" + newPass + "' WHERE username='" + empId + "'";
                c.s.executeUpdate(update);
                JOptionPane.showMessageDialog(this, "Password changed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect current password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while changing password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new ChangePasswordEmployee("empId"); // Demo employee ID
    }
}

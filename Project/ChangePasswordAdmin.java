/*package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ChangePasswordAdmin extends JFrame implements ActionListener {

    JTextField usernameField;
    JPasswordField currentPassField, newPassField, confirmPassField;
    JButton changeBtn, backBtn;
    Conn conn;
    JFrame parent; // Home window reference

    public ChangePasswordAdmin(JFrame parent) {
        this.parent = parent;

        setTitle("Admin - Change Password");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        conn = new Conn();

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 255, 255)); // Light aqua blue
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("Admin Password Change Panel", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(new Color(0, 102, 102));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        // Username
        gbc.gridx = 0;
        mainPanel.add(createLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        mainPanel.add(usernameField, gbc);

        // Current Password
        gbc.gridy++;
        gbc.gridx = 0;
        mainPanel.add(createLabel("Current Password:"), gbc);
        gbc.gridx = 1;
        currentPassField = new JPasswordField(20);
        mainPanel.add(currentPassField, gbc);

        // New Password
        gbc.gridy++;
        gbc.gridx = 0;
        mainPanel.add(createLabel("New Password:"), gbc);
        gbc.gridx = 1;
        newPassField = new JPasswordField(20);
        mainPanel.add(newPassField, gbc);

        // Confirm Password
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
        changeBtn.setBackground(new Color(39, 174, 96)); // Green
        changeBtn.setForeground(Color.WHITE);
        changeBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        changeBtn.addActionListener(this);
        mainPanel.add(changeBtn, gbc);

        gbc.gridx = 1;
        backBtn = new JButton("Back");
        backBtn.setBackground(new Color(192, 57, 43)); // Red
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backBtn.addActionListener(e -> dispose());
        mainPanel.add(backBtn, gbc);

        add(mainPanel);
        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        label.setForeground(new Color(0, 51, 102));
        return label;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText().trim();
        String currentPass = new String(currentPassField.getPassword());
        String newPass = new String(newPassField.getPassword());
        String confirmPass = new String(confirmPassField.getPassword());

        if (username.isEmpty() || currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "New password and Confirm password do not match.");
            return;
        }

        try {
            String checkSql = "SELECT * FROM adminlogin WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.c.prepareStatement(checkSql);
            ps.setString(1, username);
            ps.setString(2, currentPass);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String updateSql = "UPDATE adminlogin SET password = ? WHERE username = ?";
                PreparedStatement psUpdate = conn.c.prepareStatement(updateSql);
                psUpdate.setString(1, newPass);
                psUpdate.setString(2, username);

                int rowsUpdated = psUpdate.executeUpdate();

                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Password changed successfully.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Password update failed.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect username or current password.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error occurred. See console.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChangePasswordAdmin(null));
    }
}
*/

package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.awt.geom.RoundRectangle2D;

public class ChangePasswordAdmin extends JFrame implements ActionListener {

    JTextField usernameField;
    JPasswordField currentPassField, newPassField, confirmPassField;
    JButton changeBtn, backBtn;
    Conn conn;
    JFrame parent; // Home window reference

    // RoundedButton class
    private static class RoundedButton extends JButton {
        private Color backgroundColor;
        private Color foregroundColor;

        public RoundedButton(String text, Color bg, Color fg) {
            super(text);
            backgroundColor = bg;
            foregroundColor = fg;
            setForeground(foregroundColor);
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(150, 40));
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

    public ChangePasswordAdmin(JFrame parent) {
        this.parent = parent;

        setTitle("Admin - Change Password");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        conn = new Conn();

        // Main Panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204)); // Blue header
        headerPanel.setPreferredSize(new Dimension(getWidth(), 100));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 35));
        JLabel titleLabel = new JLabel("Admin Password Change");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Input Fields Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(240, 255, 255)); // Light aqua blue
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(createLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        inputPanel.add(usernameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        inputPanel.add(createLabel("Current Password:"), gbc);
        gbc.gridx = 1;
        currentPassField = new JPasswordField(20);
        inputPanel.add(currentPassField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        inputPanel.add(createLabel("New Password:"), gbc);
        gbc.gridx = 1;
        newPassField = new JPasswordField(20);
        inputPanel.add(newPassField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        inputPanel.add(createLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        confirmPassField = new JPasswordField(20);
        inputPanel.add(confirmPassField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        changeBtn = new RoundedButton("Change Password", new Color(39, 174, 96), Color.WHITE); // Green
        changeBtn.addActionListener(this);
        inputPanel.add(changeBtn, gbc);

        gbc.gridx = 1;
        backBtn = new RoundedButton("Back", new Color(192, 57, 43), Color.WHITE); // Red
        backBtn.addActionListener(e -> dispose());
        inputPanel.add(backBtn, gbc);

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        label.setForeground(new Color(0, 51, 102));
        return label;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText().trim();
        String currentPass = new String(currentPassField.getPassword());
        String newPass = new String(newPassField.getPassword());
        String confirmPass = new String(confirmPassField.getPassword());

        if (username.isEmpty() || currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "New password and Confirm password do not match.");
            return;
        }

        try {
            String checkSql = "SELECT * FROM adminlogin WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.c.prepareStatement(checkSql);
            ps.setString(1, username);
            ps.setString(2, currentPass);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String updateSql = "UPDATE adminlogin SET password = ? WHERE username = ?";
                PreparedStatement psUpdate = conn.c.prepareStatement(updateSql);
                psUpdate.setString(1, newPass);
                psUpdate.setString(2, username);

                int rowsUpdated = psUpdate.executeUpdate();

                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Password changed successfully.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Password update failed.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect username or current password.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error occurred. See console.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChangePasswordAdmin(null));
    }
}

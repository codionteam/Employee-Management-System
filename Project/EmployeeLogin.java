package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EmployeeLogin extends JFrame implements ActionListener {
    private JTextField empIdField;
    private JPasswordField passwordField;
    private JButton loginBtn, backBtn, forgetPasswordBtn;

    EmployeeLogin() {
        setTitle("Staff Login - Digital Staff Record Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 250, 255));

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(30, 144, 255));
        topPanel.setPreferredSize(new Dimension(getWidth(), 100));
        JLabel heading = new JLabel("Staff Login");
        heading.setFont(new Font("SansSerif", Font.BOLD, 42));
        heading.setForeground(Color.WHITE);
        topPanel.add(heading);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(245, 250, 255));
        centerPanel.setLayout(null);
        add(centerPanel, BorderLayout.CENTER);

        int xLabel = 600;
        int xField = 750;
        int y = 150;

        JLabel lblEmpId = new JLabel("Employee ID:");
        lblEmpId.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        lblEmpId.setBounds(xLabel, y, 160, 30);
        centerPanel.add(lblEmpId);

        empIdField = new JTextField();
        empIdField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        empIdField.setBounds(xField, y, 250, 35);
        empIdField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        centerPanel.add(empIdField);

        y += 80;
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        lblPassword.setBounds(xLabel, y, 160, 30);
        centerPanel.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        passwordField.setBounds(xField, y, 250, 35);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        centerPanel.add(passwordField);

        y += 80;
        loginBtn = createButton("LOGIN", new Color(0, 128, 0));
        loginBtn.setBounds(xField, y, 120, 45);
        centerPanel.add(loginBtn);

        backBtn = createButton("BACK", new Color(220, 20, 60));
        backBtn.setBounds(xField + 140, y, 120, 45);
        centerPanel.add(backBtn);

        // Forget Password Button
        forgetPasswordBtn = createButton("Forget Password", new Color(255, 165, 0)); // Orange color
        forgetPasswordBtn.setBounds(xField, y + 70, 260, 40);
        centerPanel.add(forgetPasswordBtn);

        passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginAction();
                }
            }
        });

        setVisible(true);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        btn.addActionListener(this);
        return btn;
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == loginBtn) {
            loginAction();
        } else if (ae.getSource() == backBtn) {
            setVisible(false);
            new Splash();
        } else if (ae.getSource() == forgetPasswordBtn) {
            setVisible(false);
            new EmployeeForgetPassword(); // You must implement this class
        }
    }

    private void loginAction() {
        try {
            String empId = empIdField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (empId.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Employee ID and Password", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ConnEmployee conn = new ConnEmployee();
            String query = "SELECT * FROM employeelogin WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.c.prepareStatement(query);
            ps.setString(1, empId);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);
                new EmployeePanel(empId).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Employee ID or Password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new EmployeeLogin();
    }
}

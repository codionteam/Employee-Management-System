package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminLogin extends JFrame implements ActionListener {

    private JTextField tfusername;
    private JPasswordField tfpassword;
    private JButton login, back, forgetPassword;

    public AdminLogin() {
        setTitle("Admin Login - Digital Staff Record Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 250, 255));

        // ====== Top Title ======
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(30, 144, 255));
        topPanel.setPreferredSize(new Dimension(getWidth(), 100));
        JLabel heading = new JLabel("Admin Login");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 42));
        heading.setForeground(Color.WHITE);
        topPanel.add(heading);
        add(topPanel, BorderLayout.NORTH);

        // ====== Center Panel ======
        JPanel centerPanel = new JPanel(null);
        centerPanel.setBackground(new Color(245, 250, 255));
        add(centerPanel, BorderLayout.CENTER);

        int xLabel = 600;
        int xField = 750;
        int y = 150;

        JLabel lblusername = new JLabel("Username:");
        lblusername.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        lblusername.setBounds(xLabel, y, 150, 30);
        centerPanel.add(lblusername);

        tfusername = new JTextField();
        tfusername.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tfusername.setBounds(xField, y, 250, 35);
        tfusername.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        centerPanel.add(tfusername);

        y += 80;
        JLabel lblpassword = new JLabel("Password:");
        lblpassword.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        lblpassword.setBounds(xLabel, y, 150, 30);
        centerPanel.add(lblpassword);

        tfpassword = new JPasswordField();
        tfpassword.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tfpassword.setBounds(xField, y, 250, 35);
        tfpassword.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        centerPanel.add(tfpassword);

        y += 80;
        login = createButton("LOGIN", new Color(0, 128, 0));
        login.setBounds(xField, y, 120, 45);
        centerPanel.add(login);

        back = createButton("BACK", new Color(220, 20, 60));
        back.setBounds(xField + 140, y, 120, 45);
        centerPanel.add(back);

        // ====== Forget Password Button (Orange Background, White Text) ======
        y += 70;
        forgetPassword = new JButton("Forget Password?");
        forgetPassword.setFont(new Font("Segoe UI", Font.BOLD, 16));
        forgetPassword.setForeground(Color.WHITE); // White text
        forgetPassword.setBackground(new Color(255, 140, 0)); // Orange background
        forgetPassword.setBorder(BorderFactory.createLineBorder(new Color(255, 140, 0), 1));
        forgetPassword.setFocusPainted(false);
        forgetPassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgetPassword.setBounds(xField, y, 260, 40);
        forgetPassword.addActionListener(this);
        centerPanel.add(forgetPassword);

        tfpassword.addKeyListener(new KeyAdapter() {
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
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        btn.addActionListener(this);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == login) {
            loginAction();
        } else if (ae.getSource() == back) {
            setVisible(false);
            new Splash();
        } else if (ae.getSource() == forgetPassword) {
            setVisible(false);
            new AdminForgetPassword();
        }
    }

    private void loginAction() {
        try {
            String username = tfusername.getText().trim();
            String password = new String(tfpassword.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Username and Password", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Conn c = new Conn();
            String query = "SELECT * FROM adminlogin WHERE username = ? AND password = ?";
            PreparedStatement pstmt = c.c.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);
                new Home();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password", "Login Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new AdminLogin();
    }
}

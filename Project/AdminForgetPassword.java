/*package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;

public class AdminForgetPassword extends JFrame implements ActionListener {

    private JTextField tfUsername, tfGmail, tfOtp, tfNewPass;
    private JButton sendOtpBtn, resetBtn, backBtn;

    private String generatedOtp;

    public AdminForgetPassword() {
        setTitle("Reset Admin Password via OTP");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 250, 255));

        JLabel heading = new JLabel("Admin Password Reset");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 30));
        heading.setBounds(470, 50, 700, 40);
        add(heading);
        
        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblUser.setBounds(480, 130, 150, 30);
        add(lblUser);

        tfUsername = new JTextField();
        tfUsername.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tfUsername.setBounds(650, 130, 250, 35);
        add(tfUsername);

        JLabel lblGmail = new JLabel("Gmail:");
        lblGmail.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblGmail.setBounds(480, 190, 150, 30);
        add(lblGmail);

        tfGmail = new JTextField();
        tfGmail.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tfGmail.setBounds(650, 190, 250, 35);
        add(tfGmail);

        JLabel lblOtp = new JLabel("Enter OTP:");
        lblOtp.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblOtp.setBounds(480, 250, 150, 30);
        add(lblOtp);

        tfOtp = new JTextField();
        tfOtp.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tfOtp.setBounds(650, 250, 250, 35);
        add(tfOtp);

        sendOtpBtn = new JButton("Send OTP");
        sendOtpBtn.setBounds(920, 190, 130, 35);
        sendOtpBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sendOtpBtn.setBackground(new Color(135, 206, 235)); // Sky Blue
        sendOtpBtn.setForeground(Color.WHITE);
        sendOtpBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sendOtpBtn.addActionListener(this);
        add(sendOtpBtn);

        JLabel lblNewPass = new JLabel("New Password:");
        lblNewPass.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblNewPass.setBounds(480, 310, 150, 30);
        add(lblNewPass);

        tfNewPass = new JTextField();
        tfNewPass.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tfNewPass.setBounds(650, 310, 250, 35);
        add(tfNewPass);

        resetBtn = new JButton("Reset Password");
        resetBtn.setBounds(650, 370, 180, 40);
        resetBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        resetBtn.setBackground(new Color(0, 128, 0)); // Green
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        resetBtn.addActionListener(this);
        add(resetBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(840, 370, 110, 40);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        backBtn.setBackground(new Color(220, 20, 60));
        backBtn.setForeground(Color.WHITE);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(this);
        add(backBtn);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == sendOtpBtn) {
            String gmail = tfGmail.getText().trim();
            if (gmail.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your Gmail first!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            generatedOtp = String.valueOf(new Random().nextInt(899999) + 100000); // 6-digit OTP
            sendOtpToEmail(gmail, generatedOtp);

        } else if (ae.getSource() == resetBtn) {
            String username = tfUsername.getText().trim();
            String gmail = tfGmail.getText().trim();
            String otp = tfOtp.getText().trim();
            String newPass = tfNewPass.getText().trim();

            if (username.isEmpty() || gmail.isEmpty() || otp.isEmpty() || newPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!otp.equals(generatedOtp)) {
                JOptionPane.showMessageDialog(this, "Incorrect OTP!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Conn c = new Conn();
                String query = "UPDATE adminlogin SET password = ? WHERE username = ? AND email = ?";
                PreparedStatement pstmt = c.c.prepareStatement(query);
                pstmt.setString(1, newPass);
                pstmt.setString(2, username);
                pstmt.setString(3, gmail);

                int rows = pstmt.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Password Reset Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    setVisible(false);
                    new AdminLogin();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Username or Gmail!", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (ae.getSource() == backBtn) {
            setVisible(false);
            new AdminLogin();
        }
    }

    private void sendOtpToEmail(String toEmail, String otp) {
        String fromEmail = "foysalmahamudfahim507@gmail.com"; // Replace with sender Gmail
        String password = "ioxbcyvmiksrnhwc"; // Replace with Gmail App Password

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Your OTP Code");
            message.setText("Your OTP for Admin Password Reset is: " + otp);

            Transport.send(message);

            JOptionPane.showMessageDialog(this, "OTP sent to your Gmail!", "OTP Sent", JOptionPane.INFORMATION_MESSAGE);
        } catch (MessagingException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to send OTP: " + e.getMessage(), "Email Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new AdminForgetPassword();
    }
}
*/
package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;

public class AdminForgetPassword extends JFrame implements ActionListener {

    private JTextField tfUsername, tfGmail, tfOtp, tfNewPass;
    private JButton sendOtpBtn, resetBtn, backBtn;

    private String generatedOtp;

    public AdminForgetPassword() {
        setTitle("Reset Admin Password via OTP");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Using BorderLayout for better layout management
        getContentPane().setBackground(new Color(245, 250, 255));

        // Header Panel with background color
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(30, 144, 255)); // Blue color
        headerPanel.setPreferredSize(new Dimension(getWidth(), 100)); // Set preferred size
        
        JLabel heading = new JLabel("Admin Password Reset");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 40));
        heading.setForeground(Color.WHITE);
        headerPanel.add(heading);
        add(headerPanel, BorderLayout.NORTH);

        // Main content panel
        JPanel contentPanel = new JPanel(null); // Using null layout for manual positioning
        contentPanel.setBackground(new Color(245, 250, 255));

        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblUser.setBounds(480, 50, 150, 30);
        contentPanel.add(lblUser);

        tfUsername = new JTextField();
        tfUsername.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tfUsername.setBounds(650, 50, 250, 35);
        contentPanel.add(tfUsername);

        JLabel lblGmail = new JLabel("Gmail:");
        lblGmail.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblGmail.setBounds(480, 110, 150, 30);
        contentPanel.add(lblGmail);

        tfGmail = new JTextField();
        tfGmail.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tfGmail.setBounds(650, 110, 250, 35);
        contentPanel.add(tfGmail);

        JLabel lblOtp = new JLabel("Enter OTP:");
        lblOtp.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblOtp.setBounds(480, 170, 150, 30);
        contentPanel.add(lblOtp);

        tfOtp = new JTextField();
        tfOtp.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tfOtp.setBounds(650, 170, 250, 35);
        contentPanel.add(tfOtp);

        sendOtpBtn = new JButton("Send OTP");
        sendOtpBtn.setBounds(920, 110, 130, 35);
        sendOtpBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sendOtpBtn.setBackground(new Color(135, 206, 235)); // Sky Blue
        sendOtpBtn.setForeground(Color.WHITE);
        sendOtpBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sendOtpBtn.addActionListener(this);
        contentPanel.add(sendOtpBtn);

        JLabel lblNewPass = new JLabel("New Password:");
        lblNewPass.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblNewPass.setBounds(480, 230, 150, 30);
        contentPanel.add(lblNewPass);

        tfNewPass = new JTextField();
        tfNewPass.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tfNewPass.setBounds(650, 230, 250, 35);
        contentPanel.add(tfNewPass);

        resetBtn = new JButton("Reset Password");
        resetBtn.setBounds(650, 290, 180, 40);
        resetBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        resetBtn.setBackground(new Color(0, 128, 0)); // Green
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        resetBtn.addActionListener(this);
        contentPanel.add(resetBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(840, 290, 110, 40);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        backBtn.setBackground(new Color(220, 20, 60));
        backBtn.setForeground(Color.WHITE);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(this);
        contentPanel.add(backBtn);

        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == sendOtpBtn) {
            String gmail = tfGmail.getText().trim();
            if (gmail.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your Gmail first!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            generatedOtp = String.valueOf(new Random().nextInt(899999) + 100000); // 6-digit OTP
            sendOtpToEmail(gmail, generatedOtp);

        } else if (ae.getSource() == resetBtn) {
            String username = tfUsername.getText().trim();
            String gmail = tfGmail.getText().trim();
            String otp = tfOtp.getText().trim();
            String newPass = tfNewPass.getText().trim();

            if (username.isEmpty() || gmail.isEmpty() || otp.isEmpty() || newPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!otp.equals(generatedOtp)) {
                JOptionPane.showMessageDialog(this, "Incorrect OTP!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Conn c = new Conn();
                String query = "UPDATE adminlogin SET password = ? WHERE username = ? AND email = ?";
                PreparedStatement pstmt = c.c.prepareStatement(query);
                pstmt.setString(1, newPass);
                pstmt.setString(2, username);
                pstmt.setString(3, gmail);

                int rows = pstmt.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Password Reset Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    setVisible(false);
                    new AdminLogin();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Username or Gmail!", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (ae.getSource() == backBtn) {
            setVisible(false);
            new AdminLogin();
        }
    }

    private void sendOtpToEmail(String toEmail, String otp) {
        String fromEmail = "foysalmahamudfahim507@gmail.com";
        String password = "ioxbcyvmiksrnhwc";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Your OTP Code");
            message.setText("Your OTP for Admin Password Reset is: " + otp);

            Transport.send(message);

            JOptionPane.showMessageDialog(this, "OTP sent to your Gmail!", "OTP Sent", JOptionPane.INFORMATION_MESSAGE);
        } catch (MessagingException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to send OTP: " + e.getMessage(), "Email Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new AdminForgetPassword();
    }
}
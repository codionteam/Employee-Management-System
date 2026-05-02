/*package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmployeeForgetPassword extends JFrame implements ActionListener {

    private JTextField empIdField, emailField, otpField;
    private JPasswordField newPasswordField;
    private JButton sendOtpBtn, resetBtn, backBtn;
    private String generatedOtp;

    EmployeeForgetPassword() {
        setTitle("Reset Password with OTP");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JLabel heading = new JLabel("Reset Your Password", SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 36));
        heading.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        add(heading, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fieldWidth = 400;
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 20);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 18);

        // Employee ID
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel idLabel = new JLabel("Employee ID:");
        idLabel.setFont(labelFont);
        formPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        empIdField = new JTextField(20);
        empIdField.setFont(fieldFont);
        empIdField.setPreferredSize(new Dimension(fieldWidth, 30));
        formPanel.add(empIdField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy++;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        emailField.setFont(fieldFont);
        emailField.setPreferredSize(new Dimension(fieldWidth, 30));
        formPanel.add(emailField, gbc);

        // OTP
        gbc.gridx = 0; gbc.gridy++;
        JLabel otpLabel = new JLabel("Enter OTP:");
        otpLabel.setFont(labelFont);
        formPanel.add(otpLabel, gbc);

        gbc.gridx = 1;
        JPanel otpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        otpField = new JTextField(10);
        otpField.setFont(fieldFont);
        otpField.setPreferredSize(new Dimension(180, 30));
        sendOtpBtn = new JButton("Send OTP");
        sendOtpBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendOtpBtn.setBackground(new Color(30, 144, 255));
        sendOtpBtn.setForeground(Color.WHITE);
        sendOtpBtn.setPreferredSize(new Dimension(140, 30));
        sendOtpBtn.addActionListener(this);
        otpPanel.setBackground(Color.WHITE);
        otpPanel.add(otpField);
        otpPanel.add(Box.createHorizontalStrut(10));
        otpPanel.add(sendOtpBtn);
        formPanel.add(otpPanel, gbc);

        // New Password
        gbc.gridx = 0; gbc.gridy++;
        JLabel passLabel = new JLabel("New Password:");
        passLabel.setFont(labelFont);
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        newPasswordField = new JPasswordField(20);
        newPasswordField.setFont(fieldFont);
        newPasswordField.setPreferredSize(new Dimension(fieldWidth, 30));
        formPanel.add(newPasswordField, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);

        resetBtn = new JButton("Reset Password");
        resetBtn.setBackground(new Color(0, 128, 0));
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resetBtn.setPreferredSize(new Dimension(180, 40));
        resetBtn.addActionListener(this);
        btnPanel.add(resetBtn);

        backBtn = new JButton("Back");
        backBtn.setBackground(new Color(220, 20, 60));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backBtn.setPreferredSize(new Dimension(120, 40));
        backBtn.addActionListener(this);
        btnPanel.add(Box.createHorizontalStrut(20));
        btnPanel.add(backBtn);

        formPanel.add(btnPanel, gbc);

        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == sendOtpBtn) {
            sendOtpToEmail();
        } else if (ae.getSource() == resetBtn) {
            resetPassword();
        } else if (ae.getSource() == backBtn) {
            setVisible(false);
            new EmployeeLogin();
        }
    }

    private void sendOtpToEmail() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        generatedOtp = String.valueOf(new Random().nextInt(899999) + 100000); // 6-digit OTP

        final String senderEmail = "foysalmahamudfahim507@gmail.com";
        final String senderPassword = "ioxbcyvmiksrnhwc"; // App Password

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Your OTP Code");
            message.setText("Your OTP for password reset is: " + generatedOtp);
            Transport.send(message);
            JOptionPane.showMessageDialog(this, "OTP sent to your email!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to send OTP: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetPassword() {
        String empId = empIdField.getText().trim();
        String email = emailField.getText().trim();
        String newPassword = new String(newPasswordField.getPassword()).trim();
        String enteredOtp = otpField.getText().trim();

        if (empId.isEmpty() || email.isEmpty() || newPassword.isEmpty() || enteredOtp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!enteredOtp.equals(generatedOtp)) {
            JOptionPane.showMessageDialog(this, "Invalid OTP", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            ConnEmployee conn = new ConnEmployee();
            String checkQuery = "SELECT * FROM employeelogin WHERE username = ? AND email = ?";
            PreparedStatement checkStmt = conn.c.prepareStatement(checkQuery);
            checkStmt.setString(1, empId);
            checkStmt.setString(2, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                String updateQuery = "UPDATE employeelogin SET password = ? WHERE username = ?";
                PreparedStatement updateStmt = conn.c.prepareStatement(updateQuery);
                updateStmt.setString(1, newPassword);
                updateStmt.setString(2, empId);
                updateStmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Password reset successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);
                new EmployeeLogin();
            } else {
                JOptionPane.showMessageDialog(this, "No account matched! Check ID and Email.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new EmployeeForgetPassword();
    }
}
*/
package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmployeeForgetPassword extends JFrame implements ActionListener {

    private JTextField empIdField, emailField, otpField;
    private JPasswordField newPasswordField;
    private JButton sendOtpBtn, resetBtn, backBtn;
    private String generatedOtp;

    EmployeeForgetPassword() {
        setTitle("Reset Password with OTP");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Header Panel with background color
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(30, 144, 255)); // Blue color
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        JLabel heading = new JLabel("Reset Your Password", SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);
        headerPanel.add(heading);
        add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fieldWidth = 400;
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 20);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 18);

        // Employee ID
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel idLabel = new JLabel("Employee ID:");
        idLabel.setFont(labelFont);
        formPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        empIdField = new JTextField(20);
        empIdField.setFont(fieldFont);
        empIdField.setPreferredSize(new Dimension(fieldWidth, 30));
        formPanel.add(empIdField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy++;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        emailField.setFont(fieldFont);
        emailField.setPreferredSize(new Dimension(fieldWidth, 30));
        formPanel.add(emailField, gbc);

        // OTP
        gbc.gridx = 0; gbc.gridy++;
        JLabel otpLabel = new JLabel("Enter OTP:");
        otpLabel.setFont(labelFont);
        formPanel.add(otpLabel, gbc);

        gbc.gridx = 1;
        JPanel otpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        otpField = new JTextField(10);
        otpField.setFont(fieldFont);
        otpField.setPreferredSize(new Dimension(180, 30));
        sendOtpBtn = new JButton("Send OTP");
        sendOtpBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendOtpBtn.setBackground(new Color(30, 144, 255));
        sendOtpBtn.setForeground(Color.WHITE);
        sendOtpBtn.setPreferredSize(new Dimension(140, 30));
        sendOtpBtn.addActionListener(this);
        otpPanel.setBackground(Color.WHITE);
        otpPanel.add(otpField);
        otpPanel.add(Box.createHorizontalStrut(10));
        otpPanel.add(sendOtpBtn);
        formPanel.add(otpPanel, gbc);

        // New Password
        gbc.gridx = 0; gbc.gridy++;
        JLabel passLabel = new JLabel("New Password:");
        passLabel.setFont(labelFont);
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        newPasswordField = new JPasswordField(20);
        newPasswordField.setFont(fieldFont);
        newPasswordField.setPreferredSize(new Dimension(fieldWidth, 30));
        formPanel.add(newPasswordField, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);

        resetBtn = new JButton("Reset Password");
        resetBtn.setBackground(new Color(0, 128, 0));
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resetBtn.setPreferredSize(new Dimension(180, 40));
        resetBtn.addActionListener(this);
        btnPanel.add(resetBtn);

        backBtn = new JButton("Back");
        backBtn.setBackground(new Color(220, 20, 60));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backBtn.setPreferredSize(new Dimension(120, 40));
        backBtn.addActionListener(this);
        btnPanel.add(Box.createHorizontalStrut(20));
        btnPanel.add(backBtn);

        formPanel.add(btnPanel, gbc);

        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == sendOtpBtn) {
            sendOtpToEmail();
        } else if (ae.getSource() == resetBtn) {
            resetPassword();
        } else if (ae.getSource() == backBtn) {
            setVisible(false);
            new EmployeeLogin();
        }
    }

    private void sendOtpToEmail() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        generatedOtp = String.valueOf(new Random().nextInt(899999) + 100000); // 6-digit OTP

        final String senderEmail = "foysalmahamudfahim507@gmail.com";
        final String senderPassword = "ioxbcyvmiksrnhwc"; // App Password

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Your OTP Code");
            message.setText("Your OTP for password reset is: " + generatedOtp);
            Transport.send(message);
            JOptionPane.showMessageDialog(this, "OTP sent to your email!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to send OTP: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetPassword() {
        String empId = empIdField.getText().trim();
        String email = emailField.getText().trim();
        String newPassword = new String(newPasswordField.getPassword()).trim();
        String enteredOtp = otpField.getText().trim();

        if (empId.isEmpty() || email.isEmpty() || newPassword.isEmpty() || enteredOtp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!enteredOtp.equals(generatedOtp)) {
            JOptionPane.showMessageDialog(this, "Invalid OTP", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            ConnEmployee conn = new ConnEmployee();
            String checkQuery = "SELECT * FROM employeelogin WHERE username = ? AND email = ?";
            PreparedStatement checkStmt = conn.c.prepareStatement(checkQuery);
            checkStmt.setString(1, empId);
            checkStmt.setString(2, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                String updateQuery = "UPDATE employeelogin SET password = ? WHERE username = ?";
                PreparedStatement updateStmt = conn.c.prepareStatement(updateQuery);
                updateStmt.setString(1, newPassword);
                updateStmt.setString(2, empId);
                updateStmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Password reset successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);
                new EmployeeLogin();
            } else {
                JOptionPane.showMessageDialog(this, "No account matched! Check ID and Email.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new EmployeeForgetPassword();
    }
}
/*
package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Attendance extends JFrame implements ActionListener {
    private JButton toggleAttendance, back;
    private String empId;
    private EmployeePanel parentPanel;
    private boolean isClockedIn = false;

    public Attendance(String empId, EmployeePanel parentPanel) {
        this.empId = empId;
        this.parentPanel = parentPanel;

        setTitle("Employee Attendance");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(240, 248, 255));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        JLabel heading = new JLabel("Mark Your Attendance", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 32));
        heading.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        add(heading, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        // Create the custom-styled toggleAttendance button
        toggleAttendance = new JButton();
        toggleAttendance.setFont(new Font("Segoe UI", Font.BOLD, 18));
        toggleAttendance.setFocusPainted(false);
        toggleAttendance.setForeground(Color.WHITE);
        toggleAttendance.setVerticalTextPosition(SwingConstants.BOTTOM);
        toggleAttendance.setHorizontalTextPosition(SwingConstants.CENTER);
        toggleAttendance.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Setting a border for better visual separation
        toggleAttendance.setBorder(BorderFactory.createLineBorder(new Color(46, 204, 113), 2));
        toggleAttendance.setContentAreaFilled(true); // Ensure content area is filled with color
        
        // Add a mouse listener for hover effect
        toggleAttendance.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if (toggleAttendance.getText().equals("Clock In")) {
                    toggleAttendance.setBackground(new Color(39, 174, 96)); // Darker green on hover
                } else {
                    toggleAttendance.setBackground(new Color(41, 128, 185)); // Darker blue on hover
                }
            }
            public void mouseExited(MouseEvent evt) {
                if (toggleAttendance.getText().equals("Clock In")) {
                    toggleAttendance.setBackground(new Color(46, 204, 113)); // Original green
                } else {
                    toggleAttendance.setBackground(new Color(52, 152, 219)); // Original blue
                }
            }
        });
        
        toggleAttendance.addActionListener(this);

        // Create the back button
        back = new JButton("Back");
        back.setFont(new Font("Segoe UI", Font.BOLD, 18));
        back.setFocusPainted(false);
        back.setForeground(Color.WHITE);
        back.setBackground(Color.RED);
        back.setPreferredSize(new Dimension(420, 40));
        back.addActionListener(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(toggleAttendance, gbc);

        gbc.gridy = 2; // Increased the gridy to push the back button down
        centerPanel.add(back, gbc);

        add(centerPanel, BorderLayout.CENTER);
        setVisible(true);

        checkClockStatus();
    }

    private ImageIcon resizeIcon(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(path));
            if (icon.getIconWidth() == -1) {
                // If the icon is not found, handle it gracefully
                return null;
            }
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void checkClockStatus() {
        String today = LocalDate.now().toString();
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM attendance WHERE empId='" + empId + "' AND date='" + today + "'");
            
            ImageIcon attendanceIcon = resizeIcon("attendance-management-software.png", 200, 200);

            if (rs.next()) {
                Time checkin = rs.getTime("checkin_time");
                Time checkout = rs.getTime("checkout_time");

                if (checkin != null && checkout == null) {
                    toggleAttendance.setText("Clock Out");
                    toggleAttendance.setIcon(attendanceIcon);
                    toggleAttendance.setBackground(new Color(52, 152, 219)); // Blue for Clock Out
                    isClockedIn = true;
                } else {
                    toggleAttendance.setText("Clock In");
                    toggleAttendance.setIcon(attendanceIcon);
                    toggleAttendance.setBackground(new Color(46, 204, 113)); // Green for Clock In
                    isClockedIn = false;
                }
            } else {
                toggleAttendance.setText("Clock In");
                toggleAttendance.setIcon(attendanceIcon);
                toggleAttendance.setBackground(new Color(46, 204, 113)); // Green for Clock In
                isClockedIn = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        String today = LocalDate.now().toString();

        if (source == toggleAttendance) {
            try {
                Conn c = new Conn();
                ResultSet rs = c.s.executeQuery("SELECT * FROM attendance WHERE empId='" + empId + "' AND date='" + today + "'");
                
                if (!rs.next()) {
                    c.s.executeUpdate("INSERT INTO attendance(empId, date, checkin_time) VALUES('" + empId + "', '" + today + "', CURTIME())");
                    JOptionPane.showMessageDialog(this, "Clock-In Successful.");
                    toggleAttendance.setText("Clock Out");
                    toggleAttendance.setBackground(new Color(52, 152, 219)); // Blue for Clock Out
                    isClockedIn = true;
                } else {
                    Time checkin = rs.getTime("checkin_time");
                    Time checkout = rs.getTime("checkout_time");

                    if (checkin != null && checkout == null) {
                        c.s.executeUpdate("UPDATE attendance SET checkout_time=CURTIME() WHERE empId='" + empId + "' AND date='" + today + "'");
                        JOptionPane.showMessageDialog(this, "Clock-Out Successful.");
                        toggleAttendance.setText("Clock In");
                        toggleAttendance.setBackground(new Color(46, 204, 113)); // Green for Clock In
                        isClockedIn = false;
                    } else if (checkin == null) {
                        c.s.executeUpdate("UPDATE attendance SET checkin_time=CURTIME() WHERE empId='" + empId + "' AND date='" + today + "'");
                        JOptionPane.showMessageDialog(this, "Clock-In Successful.");
                        toggleAttendance.setText("Clock Out");
                        toggleAttendance.setBackground(new Color(52, 152, 219)); // Blue for Clock Out
                        isClockedIn = true;
                    } else {
                        JOptionPane.showMessageDialog(this, "Already Clocked In and Out today.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (source == back) {
            dispose();
            parentPanel.setVisible(true);
        }
    }
}*/
/*
package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Attendance extends JFrame implements ActionListener {
    private JButton toggleAttendance, back;
    private String empId;
    private EmployeePanel parentPanel;
    private boolean isClockedIn = false;

    // Define the base path for your icons
    private static final String ICON_BASE_PATH = "C:\\Users\\Foysa\\OneDrive\\Documents\\NetBeansProjects\\Employee Management System\\src\\icons\\";

    public Attendance(String empId, EmployeePanel parentPanel) {
        this.empId = empId;
        this.parentPanel = parentPanel;

        setTitle("Employee Attendance");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(240, 248, 255));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        // Heading Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Mark Your Attendance", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(new Color(60, 90, 153));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        headerPanel.add(heading, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        // Create the custom-styled toggleAttendance button
        toggleAttendance = new JButton();
        toggleAttendance.setFont(new Font("Segoe UI", Font.BOLD, 18));
        toggleAttendance.setFocusPainted(false);
        toggleAttendance.setForeground(Color.WHITE);
        toggleAttendance.setVerticalTextPosition(SwingConstants.BOTTOM);
        toggleAttendance.setHorizontalTextPosition(SwingConstants.CENTER);
        toggleAttendance.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggleAttendance.setBorder(BorderFactory.createLineBorder(new Color(46, 204, 113), 2));
        toggleAttendance.setContentAreaFilled(true);

        // Add a mouse listener for hover effect
        toggleAttendance.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if (isClockedIn) {
                    toggleAttendance.setBackground(new Color(41, 128, 185)); // Darker blue on hover for "Clock Out"
                } else {
                    toggleAttendance.setBackground(new Color(39, 174, 96)); // Darker green on hover for "Clock In"
                }
            }
            public void mouseExited(MouseEvent evt) {
                if (isClockedIn) {
                    toggleAttendance.setBackground(new Color(52, 152, 219)); // Original blue for "Clock Out"
                } else {
                    toggleAttendance.setBackground(new Color(46, 204, 113)); // Original green for "Clock In"
                }
            }
        });
        
        toggleAttendance.addActionListener(this);

        // Create the back button
        back = new JButton("Back");
        back.setFont(new Font("Segoe UI", Font.BOLD, 18));
        back.setFocusPainted(false);
        back.setForeground(Color.WHITE);
        back.setBackground(Color.RED);
        back.setPreferredSize(new Dimension(420, 40));
        back.addActionListener(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(toggleAttendance, gbc);

        gbc.gridy = 2;
        centerPanel.add(back, gbc);

        add(centerPanel, BorderLayout.CENTER);
        setVisible(true);

        checkClockStatus();
    }

    /**
     * Resizes an image icon from a given file path.
     * @param path The relative path of the image file.
     * @param width The desired width.
     * @param height The desired height.
     * @return The resized ImageIcon, or null if the image cannot be found.
     *//*
    private ImageIcon resizeIcon(String path, int width, int height) {
        try {
            // Use the absolute file path directly
            ImageIcon icon = new ImageIcon(ICON_BASE_PATH + path);
            if (icon.getIconWidth() == -1) {
                System.err.println("Image not found at path: " + ICON_BASE_PATH + path);
                return null;
            }
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void checkClockStatus() {
        String today = LocalDate.now().toString();
        try {
            Conn c = new Conn();
            String query = "SELECT checkin_time, checkout_time FROM attendance WHERE empId='" + empId + "' AND date='" + today + "'";
            ResultSet rs = c.s.executeQuery(query);
            
            ImageIcon attendanceIcon = resizeIcon("attendance-management-software.png", 200, 200);

            if (rs.next()) {
                Time checkin = rs.getTime("checkin_time");
                Time checkout = rs.getTime("checkout_time");

                if (checkin != null && checkout == null) {
                    toggleAttendance.setText("Clock Out");
                    toggleAttendance.setIcon(attendanceIcon);
                    toggleAttendance.setBackground(new Color(52, 152, 219)); // Blue for Clock Out
                    isClockedIn = true;
                } else {
                    toggleAttendance.setText("Clock In");
                    toggleAttendance.setIcon(attendanceIcon);
                    toggleAttendance.setBackground(new Color(46, 204, 113)); // Green for Clock In
                    isClockedIn = false;
                }
            } else {
                toggleAttendance.setText("Clock In");
                toggleAttendance.setIcon(attendanceIcon);
                toggleAttendance.setBackground(new Color(46, 204, 113)); // Green for Clock In
                isClockedIn = false;
            }
            rs.close();
            c.s.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to check attendance status.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        String today = LocalDate.now().toString();

        if (source == toggleAttendance) {
            try {
                Conn c = new Conn();
                String checkQuery = "SELECT checkin_time, checkout_time FROM attendance WHERE empId='" + empId + "' AND date='" + today + "'";
                ResultSet rs = c.s.executeQuery(checkQuery);
                
                String actionQuery;
                
                if (!rs.next()) {
                    // No entry for today, so this is a new clock-in
                    actionQuery = "INSERT INTO attendance(empId, date, checkin_time) VALUES('" + empId + "', '" + today + "', CURTIME())";
                    c.s.executeUpdate(actionQuery);
                    JOptionPane.showMessageDialog(this, "Clock-In Successful.");
                    toggleAttendance.setText("Clock Out");
                    toggleAttendance.setBackground(new Color(52, 152, 219));
                    isClockedIn = true;
                } else {
                    Time checkin = rs.getTime("checkin_time");
                    Time checkout = rs.getTime("checkout_time");

                    if (checkin != null && checkout == null) {
                        // Clocked in but not out, so perform clock-out
                        actionQuery = "UPDATE attendance SET checkout_time=CURTIME() WHERE empId='" + empId + "' AND date='" + today + "'";
                        c.s.executeUpdate(actionQuery);
                        JOptionPane.showMessageDialog(this, "Clock-Out Successful.");
                        toggleAttendance.setText("Clock In");
                        toggleAttendance.setBackground(new Color(46, 204, 113));
                        isClockedIn = false;
                    } else if (checkin == null) {
                        // This case is unlikely due to the first `if` but added for robustness
                        actionQuery = "UPDATE attendance SET checkin_time=CURTIME() WHERE empId='" + empId + "' AND date='" + today + "'";
                        c.s.executeUpdate(actionQuery);
                        JOptionPane.showMessageDialog(this, "Clock-In Successful.");
                        toggleAttendance.setText("Clock Out");
                        toggleAttendance.setBackground(new Color(52, 152, 219));
                        isClockedIn = true;
                    } else {
                        // Already clocked in and out
                        JOptionPane.showMessageDialog(this, "You have already clocked in and out today.");
                    }
                }
                rs.close();
                c.s.close();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (source == back) {
            dispose();
            parentPanel.setVisible(true);
        }
    }
}
*//*
package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Attendance extends JFrame implements ActionListener {
    private JButton toggleAttendance, back;
    private String empId;
    private EmployeePanel parentPanel;
    private boolean isClockedIn = false;

    public Attendance(String empId, EmployeePanel parentPanel) {
        this.empId = empId;
        this.parentPanel = parentPanel;

        setTitle("Employee Attendance");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(240, 248, 255));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        // Heading Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Mark Your Attendance", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36)); // Increased font size for prominence
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(new Color(60, 90, 153)); // Set a new background color
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // Added padding
        headerPanel.add(heading, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        // Create the custom-styled toggleAttendance button
        toggleAttendance = new JButton();
        toggleAttendance.setFont(new Font("Segoe UI", Font.BOLD, 18));
        toggleAttendance.setFocusPainted(false);
        toggleAttendance.setForeground(Color.WHITE);
        toggleAttendance.setVerticalTextPosition(SwingConstants.BOTTOM);
        toggleAttendance.setHorizontalTextPosition(SwingConstants.CENTER);
        toggleAttendance.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Setting a border for better visual separation
        toggleAttendance.setBorder(BorderFactory.createLineBorder(new Color(46, 204, 113), 2));
        toggleAttendance.setContentAreaFilled(true); // Ensure content area is filled with color
        
        // Add a mouse listener for hover effect
        toggleAttendance.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if (toggleAttendance.getText().equals("Clock In")) {
                    toggleAttendance.setBackground(new Color(39, 174, 96)); // Darker green on hover
                } else {
                    toggleAttendance.setBackground(new Color(41, 128, 185)); // Darker blue on hover
                }
            }
            public void mouseExited(MouseEvent evt) {
                if (toggleAttendance.getText().equals("Clock In")) {
                    toggleAttendance.setBackground(new Color(46, 204, 113)); // Original green
                } else {
                    toggleAttendance.setBackground(new Color(52, 152, 219)); // Original blue
                }
            }
        });
        
        toggleAttendance.addActionListener(this);

        // Create the back button
        back = new JButton("Back");
        back.setFont(new Font("Segoe UI", Font.BOLD, 18));
        back.setFocusPainted(false);
        back.setForeground(Color.WHITE);
        back.setBackground(Color.RED);
        back.setPreferredSize(new Dimension(420, 40));
        back.addActionListener(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(toggleAttendance, gbc);

        gbc.gridy = 2; // Increased the gridy to push the back button down
        centerPanel.add(back, gbc);

        add(centerPanel, BorderLayout.CENTER);
        setVisible(true);

        checkClockStatus();
    }

    private ImageIcon resizeIcon(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(path));
            if (icon.getIconWidth() == -1) {
                // If the icon is not found, handle it gracefully
                return null;
            }
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void checkClockStatus() {
        String today = LocalDate.now().toString();
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM attendance WHERE empId='" + empId + "' AND date='" + today + "'");
            
            ImageIcon attendanceIcon = resizeIcon("attendance-management-software.png", 200, 200);

            if (rs.next()) {
                Time checkin = rs.getTime("checkin_time");
                Time checkout = rs.getTime("checkout_time");

                if (checkin != null && checkout == null) {
                    toggleAttendance.setText("Clock Out");
                    toggleAttendance.setIcon(attendanceIcon);
                    toggleAttendance.setBackground(new Color(52, 152, 219)); // Blue for Clock Out
                    isClockedIn = true;
                } else {
                    toggleAttendance.setText("Clock In");
                    toggleAttendance.setIcon(attendanceIcon);
                    toggleAttendance.setBackground(new Color(46, 204, 113)); // Green for Clock In
                    isClockedIn = false;
                }
            } else {
                toggleAttendance.setText("Clock In");
                toggleAttendance.setIcon(attendanceIcon);
                toggleAttendance.setBackground(new Color(46, 204, 113)); // Green for Clock In
                isClockedIn = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        String today = LocalDate.now().toString();

        if (source == toggleAttendance) {
            try {
                Conn c = new Conn();
                ResultSet rs = c.s.executeQuery("SELECT * FROM attendance WHERE empId='" + empId + "' AND date='" + today + "'");
                
                if (!rs.next()) {
                    c.s.executeUpdate("INSERT INTO attendance(empId, date, checkin_time) VALUES('" + empId + "', '" + today + "', CURTIME())");
                    JOptionPane.showMessageDialog(this, "Clock-In Successful.");
                    toggleAttendance.setText("Clock Out");
                    toggleAttendance.setBackground(new Color(52, 152, 219)); // Blue for Clock Out
                    isClockedIn = true;
                } else {
                    Time checkin = rs.getTime("checkin_time");
                    Time checkout = rs.getTime("checkout_time");

                    if (checkin != null && checkout == null) {
                        c.s.executeUpdate("UPDATE attendance SET checkout_time=CURTIME() WHERE empId='" + empId + "' AND date='" + today + "'");
                        JOptionPane.showMessageDialog(this, "Clock-Out Successful.");
                        toggleAttendance.setText("Clock In");
                        toggleAttendance.setBackground(new Color(46, 204, 113)); // Green for Clock In
                        isClockedIn = false;
                    } else if (checkin == null) {
                        c.s.executeUpdate("UPDATE attendance SET checkin_time=CURTIME() WHERE empId='" + empId + "' AND date='" + today + "'");
                        JOptionPane.showMessageDialog(this, "Clock-In Successful.");
                        toggleAttendance.setText("Clock Out");
                        toggleAttendance.setBackground(new Color(52, 152, 219)); // Blue for Clock Out
                        isClockedIn = true;
                    } else {
                        JOptionPane.showMessageDialog(this, "Already Clocked In and Out today.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (source == back) {
            dispose();
            parentPanel.setVisible(true);
        }
    }
}*/
package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Attendance extends JFrame implements ActionListener {
    private JButton toggleAttendance, back;
    private String empId;
    private EmployeePanel parentPanel;
    private boolean isClockedIn = false;

    // Define the base path for your icons
    private static final String ICON_BASE_PATH = "C:\\Users\\Foysa\\OneDrive\\Documents\\NetBeansProjects\\Employee Management System\\src\\icons\\";

    public Attendance(String empId, EmployeePanel parentPanel) {
        this.empId = empId;
        this.parentPanel = parentPanel;

        setTitle("Employee Attendance");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(240, 248, 255));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        // Heading Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Mark Your Attendance", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(new Color(60, 90, 153));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        headerPanel.add(heading, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        // Create the custom-styled toggleAttendance button
        toggleAttendance = new JButton();
        toggleAttendance.setFont(new Font("Segoe UI", Font.BOLD, 18));
        toggleAttendance.setFocusPainted(false);
        toggleAttendance.setForeground(Color.WHITE);
        toggleAttendance.setVerticalTextPosition(SwingConstants.BOTTOM);
        toggleAttendance.setHorizontalTextPosition(SwingConstants.CENTER);
        toggleAttendance.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggleAttendance.setBorder(BorderFactory.createLineBorder(new Color(46, 204, 113), 2));
        toggleAttendance.setContentAreaFilled(true);

        // Add a mouse listener for hover effect
        toggleAttendance.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if (isClockedIn) {
                    toggleAttendance.setBackground(new Color(41, 128, 185)); // Darker blue on hover for "Clock Out"
                } else {
                    toggleAttendance.setBackground(new Color(39, 174, 96)); // Darker green on hover for "Clock In"
                }
            }
            public void mouseExited(MouseEvent evt) {
                if (isClockedIn) {
                    toggleAttendance.setBackground(new Color(52, 152, 219)); // Original blue for "Clock Out"
                } else {
                    toggleAttendance.setBackground(new Color(46, 204, 113)); // Original green for "Clock In"
                }
            }
        });
        
        toggleAttendance.addActionListener(this);

        // Create the back button
        back = new JButton("Back");
        back.setFont(new Font("Segoe UI", Font.BOLD, 18));
        back.setFocusPainted(false);
        back.setForeground(Color.WHITE);
        back.setBackground(Color.RED);
        back.setPreferredSize(new Dimension(420, 40));
        back.addActionListener(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(toggleAttendance, gbc);

        gbc.gridy = 2;
        centerPanel.add(back, gbc);

        add(centerPanel, BorderLayout.CENTER);
        setVisible(true);

        checkClockStatus();
    }

    /**
     * Resizes an image icon from a given file path.
     * @param path The relative path of the image file.
     * @param width The desired width.
     * @param height The desired height.
     * @return The resized ImageIcon, or null if the image cannot be found.
     */
    private ImageIcon resizeIcon(String path, int width, int height) {
        try {
            // Use the absolute file path directly
            ImageIcon icon = new ImageIcon(ICON_BASE_PATH + path);
            if (icon.getIconWidth() == -1) {
                System.err.println("Image not found at path: " + ICON_BASE_PATH + path);
                return null;
            }
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void checkClockStatus() {
        String today = LocalDate.now().toString();
        try {
            Conn c = new Conn();
            String query = "SELECT checkin_time, checkout_time FROM attendance WHERE empId='" + empId + "' AND date='" + today + "'";
            ResultSet rs = c.s.executeQuery(query);
            
            ImageIcon attendanceIcon = resizeIcon("attendance-management-software.png", 200, 200);

            if (rs.next()) {
                Time checkin = rs.getTime("checkin_time");
                Time checkout = rs.getTime("checkout_time");

                if (checkin != null && checkout == null) {
                    toggleAttendance.setText("Clock Out");
                    toggleAttendance.setIcon(attendanceIcon);
                    toggleAttendance.setBackground(new Color(52, 152, 219)); // Blue for Clock Out
                    isClockedIn = true;
                } else {
                    toggleAttendance.setText("Clock In");
                    toggleAttendance.setIcon(attendanceIcon);
                    toggleAttendance.setBackground(new Color(46, 204, 113)); // Green for Clock In
                    isClockedIn = false;
                }
            } else {
                toggleAttendance.setText("Clock In");
                toggleAttendance.setIcon(attendanceIcon);
                toggleAttendance.setBackground(new Color(46, 204, 113)); // Green for Clock In
                isClockedIn = false;
            }
            rs.close();
            c.s.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to check attendance status.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        String today = LocalDate.now().toString();

        if (source == toggleAttendance) {
            try {
                Conn c = new Conn();
                String checkQuery = "SELECT checkin_time, checkout_time FROM attendance WHERE empId='" + empId + "' AND date='" + today + "'";
                ResultSet rs = c.s.executeQuery(checkQuery);
                
                String actionQuery;
                
                if (!rs.next()) {
                    // No entry for today, so this is a new clock-in
                    actionQuery = "INSERT INTO attendance(empId, date, checkin_time) VALUES('" + empId + "', '" + today + "', CURTIME())";
                    c.s.executeUpdate(actionQuery);
                    JOptionPane.showMessageDialog(this, "Clock-In Successful.");
                    toggleAttendance.setText("Clock Out");
                    toggleAttendance.setBackground(new Color(52, 152, 219));
                    isClockedIn = true;
                } else {
                    Time checkin = rs.getTime("checkin_time");
                    Time checkout = rs.getTime("checkout_time");

                    if (checkin != null && checkout == null) {
                        // Clocked in but not out, so perform clock-out
                        actionQuery = "UPDATE attendance SET checkout_time=CURTIME() WHERE empId='" + empId + "' AND date='" + today + "'";
                        c.s.executeUpdate(actionQuery);
                        JOptionPane.showMessageDialog(this, "Clock-Out Successful.");
                        toggleAttendance.setText("Clock In");
                        toggleAttendance.setBackground(new Color(46, 204, 113));
                        isClockedIn = false;
                    } else if (checkin == null) {
                        // This case is unlikely due to the first `if` but added for robustness
                        actionQuery = "UPDATE attendance SET checkin_time=CURTIME() WHERE empId='" + empId + "' AND date='" + today + "'";
                        c.s.executeUpdate(actionQuery);
                        JOptionPane.showMessageDialog(this, "Clock-In Successful.");
                        toggleAttendance.setText("Clock Out");
                        toggleAttendance.setBackground(new Color(52, 152, 219));
                        isClockedIn = true;
                    } else {
                        // Already clocked in and out
                        JOptionPane.showMessageDialog(this, "You have already clocked in and out today.");
                    }
                }
                rs.close();
                c.s.close();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (source == back) {
            dispose();
            parentPanel.setVisible(true);
        }
    }
}
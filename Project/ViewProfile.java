/*package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewProfile extends JFrame implements ActionListener {
    JTextField tfname, tffname, tfaddress, tfphone, tfemail, tfeducation, tfdesignation, tfsalary;
    JLabel lblempId, lbldob;
    JButton update, back;
    String empId;
    EmployeePanel parentPanel;

    public ViewProfile(String empId, EmployeePanel parentPanel) {
        this.empId = empId;
        this.parentPanel = parentPanel;

        setTitle("View & Update Profile");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(null);

        JLabel heading = new JLabel("Your Profile", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 32));
        heading.setBounds(0, 30, getWidth(), 50);
        add(heading);

        int labelX = 100, labelW = 200, labelH = 30;
        int fieldX = 320, fieldW = 400, fieldH = 30;
        int gapY = 50;
        int y = 120;

        JLabel labelEmpId = new JLabel("Employee ID:");
        labelEmpId.setBounds(labelX, y, labelW, labelH);
        labelEmpId.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelEmpId);

        lblempId = new JLabel(empId);
        lblempId.setBounds(fieldX, y, fieldW, fieldH);
        lblempId.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblempId);
        y += gapY;

        JLabel labelName = new JLabel("Name:");
        labelName.setBounds(labelX, y, labelW, labelH);
        labelName.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelName);

        tfname = new JTextField();
        tfname.setBounds(fieldX, y, fieldW, fieldH);
        add(tfname);
        y += gapY;

        JLabel labelFname = new JLabel("Father's Name:");
        labelFname.setBounds(labelX, y, labelW, labelH);
        labelFname.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelFname);

        tffname = new JTextField();
        tffname.setBounds(fieldX, y, fieldW, fieldH);
        add(tffname);
        y += gapY;

        JLabel labelDob = new JLabel("Date of Birth:");
        labelDob.setBounds(labelX, y, labelW, labelH);
        labelDob.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelDob);

        lbldob = new JLabel();
        lbldob.setBounds(fieldX, y, fieldW, fieldH);
        lbldob.setFont(new Font("Arial", Font.BOLD, 18));
        add(lbldob);
        y += gapY;

        JLabel labelAddress = new JLabel("Address:");
        labelAddress.setBounds(labelX, y, labelW, labelH);
        labelAddress.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelAddress);

        tfaddress = new JTextField();
        tfaddress.setBounds(fieldX, y, fieldW, fieldH);
        add(tfaddress);
        y += gapY;

        JLabel labelPhone = new JLabel("Phone:");
        labelPhone.setBounds(labelX, y, labelW, labelH);
        labelPhone.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelPhone);

        tfphone = new JTextField();
        tfphone.setBounds(fieldX, y, fieldW, fieldH);
        add(tfphone);
        y += gapY;

        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setBounds(labelX, y, labelW, labelH);
        labelEmail.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelEmail);

        tfemail = new JTextField();
        tfemail.setBounds(fieldX, y, fieldW, fieldH);
        add(tfemail);
        y += gapY;

        JLabel labelEducation = new JLabel("Education:");
        labelEducation.setBounds(labelX, y, labelW, labelH);
        labelEducation.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelEducation);

        tfeducation = new JTextField();
        tfeducation.setBounds(fieldX, y, fieldW, fieldH);
        add(tfeducation);
        y += gapY;

        JLabel labelDesignation = new JLabel("Designation:");
        labelDesignation.setBounds(labelX, y, labelW, labelH);
        labelDesignation.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelDesignation);

        tfdesignation = new JTextField();
        tfdesignation.setBounds(fieldX, y, fieldW, fieldH);
        add(tfdesignation);
        y += gapY;

        JLabel labelSalary = new JLabel("Salary:");
        labelSalary.setBounds(labelX, y, labelW, labelH);
        labelSalary.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelSalary);

        tfsalary = new JTextField();
        tfsalary.setBounds(fieldX, y, fieldW, fieldH);
        tfsalary.setEditable(false); // Optional: salary may not be editable by employee
        add(tfsalary);
        y += gapY + 20;

        update = new JButton("Update");
        update.setBounds(800, 600, 150, 40);
        update.setFont(new Font("Arial", Font.BOLD, 16));
        update.addActionListener(this);
        add(update);

        back = new JButton("Back");
        back.setBounds(1000,600, 150, 40);
        back.setFont(new Font("Arial", Font.BOLD, 16));
        back.addActionListener(this);
        add(back);

        fetchEmployeeDetails();
        setVisible(true);
    }

    private void fetchEmployeeDetails() {
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM employee WHERE empId = '" + empId + "'";
            ResultSet rs = c.s.executeQuery(query);
            if (rs.next()) {
                tfname.setText(rs.getString("name"));
                tffname.setText(rs.getString("fname"));
                lbldob.setText(rs.getString("dob"));
                tfaddress.setText(rs.getString("address"));
                tfphone.setText(rs.getString("phone"));
                tfemail.setText(rs.getString("email"));
                tfeducation.setText(rs.getString("education"));
                tfdesignation.setText(rs.getString("designation"));
                tfsalary.setText(rs.getString("salary"));
            } else {
                JOptionPane.showMessageDialog(null, "Employee Not Found!");
                dispose();
                parentPanel.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == update) {
            String name = tfname.getText();
            String fname = tffname.getText();
            String address = tfaddress.getText();
            String phone = tfphone.getText();
            String email = tfemail.getText();
            String education = tfeducation.getText();
            String designation = tfdesignation.getText();

            try {
                Conn conn = new Conn();
                String query = "UPDATE employee SET name = '"+name+"', fname = '"+fname+"', address = '"+address+"', phone = '"+phone+"', email = '"+email+"', education = '"+education+"', designation = '"+designation+"' WHERE empId = '"+empId+"'";
                conn.s.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Profile Updated Successfully!");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error Updating Profile!");
            }
        } else if (ae.getSource() == back) {
            dispose();
            parentPanel.setVisible(true);
        }
    }

    public static void main(String[] args) {
        new ViewProfile("EMP123", new EmployeePanel("EMP123"));
    }
}
*/
/*
package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewProfile extends JFrame implements ActionListener {
    JTextField tfname, tffname, tfaddress, tfphone, tfemail, tfeducation, tfdesignation, tfsalary;
    JLabel lblempId, lbldob;
    JButton update, back;
    String empId;
    EmployeePanel parentPanel;

    public ViewProfile(String empId, EmployeePanel parentPanel) {
        this.empId = empId;
        this.parentPanel = parentPanel;

        setTitle("View & Update Profile");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(null);

        JLabel heading = new JLabel("Your Profile", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 32));
        heading.setBounds(0, 30, 1920, 50);
        add(heading);

        int labelX = 500, labelW = 200, labelH = 30;
        int fieldX = 720, fieldW = 400, fieldH = 30;
        int gapY = 50;
        int y = 120;

        JLabel labelEmpId = new JLabel("Employee ID:");
        labelEmpId.setBounds(labelX, y, labelW, labelH);
        labelEmpId.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelEmpId);

        lblempId = new JLabel(empId);
        lblempId.setBounds(fieldX, y, fieldW, fieldH);
        lblempId.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblempId);
        y += gapY;

        JLabel labelName = new JLabel("Name:");
        labelName.setBounds(labelX, y, labelW, labelH);
        labelName.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelName);

        tfname = new JTextField();
        tfname.setBounds(fieldX, y, fieldW, fieldH);
        add(tfname);
        y += gapY;

        JLabel labelFname = new JLabel("Father's Name:");
        labelFname.setBounds(labelX, y, labelW, labelH);
        labelFname.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelFname);

        tffname = new JTextField();
        tffname.setBounds(fieldX, y, fieldW, fieldH);
        add(tffname);
        y += gapY;

        JLabel labelDob = new JLabel("Date of Birth:");
        labelDob.setBounds(labelX, y, labelW, labelH);
        labelDob.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelDob);

        lbldob = new JLabel();
        lbldob.setBounds(fieldX, y, fieldW, fieldH);
        lbldob.setFont(new Font("Arial", Font.BOLD, 18));
        add(lbldob);
        y += gapY;

        JLabel labelAddress = new JLabel("Address:");
        labelAddress.setBounds(labelX, y, labelW, labelH);
        labelAddress.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelAddress);

        tfaddress = new JTextField();
        tfaddress.setBounds(fieldX, y, fieldW, fieldH);
        add(tfaddress);
        y += gapY;

        JLabel labelPhone = new JLabel("Phone:");
        labelPhone.setBounds(labelX, y, labelW, labelH);
        labelPhone.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelPhone);

        tfphone = new JTextField();
        tfphone.setBounds(fieldX, y, fieldW, fieldH);
        add(tfphone);
        y += gapY;

        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setBounds(labelX, y, labelW, labelH);
        labelEmail.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelEmail);

        tfemail = new JTextField();
        tfemail.setBounds(fieldX, y, fieldW, fieldH);
        add(tfemail);
        y += gapY;

        JLabel labelEducation = new JLabel("Education:");
        labelEducation.setBounds(labelX, y, labelW, labelH);
        labelEducation.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelEducation);

        tfeducation = new JTextField();
        tfeducation.setBounds(fieldX, y, fieldW, fieldH);
        add(tfeducation);
        y += gapY;

        JLabel labelDesignation = new JLabel("Designation:");
        labelDesignation.setBounds(labelX, y, labelW, labelH);
        labelDesignation.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelDesignation);

        tfdesignation = new JTextField();
        tfdesignation.setBounds(fieldX, y, fieldW, fieldH);
        add(tfdesignation);
        y += gapY;

        JLabel labelSalary = new JLabel("Salary:");
        labelSalary.setBounds(labelX, y, labelW, labelH);
        labelSalary.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelSalary);

        tfsalary = new JTextField();
        tfsalary.setBounds(fieldX, y, fieldW, fieldH);
        tfsalary.setEditable(false);
        add(tfsalary);

        // Buttons at the bottom center
        update = new JButton("Update");
        update.setBounds(650, 680, 150, 45);
        update.setFont(new Font("Arial", Font.BOLD, 18));
        update.setBackground(new Color(135, 206, 250)); // Sky Blue
        update.setForeground(Color.WHITE);
        update.setFocusPainted(false);
        update.addActionListener(this);
        add(update);

        back = new JButton("Back");
        back.setBounds(850, 680, 150, 45);
        back.setFont(new Font("Arial", Font.BOLD, 18));
        back.setBackground(new Color(220, 20, 60)); // Crimson Red
        back.setForeground(Color.WHITE);
        back.setFocusPainted(false);
        back.addActionListener(this);
        add(back);

        fetchEmployeeDetails();
        setVisible(true);
    }

    private void fetchEmployeeDetails() {
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM employee WHERE empId = '" + empId + "'";
            ResultSet rs = c.s.executeQuery(query);
            if (rs.next()) {
                tfname.setText(rs.getString("name"));
                tffname.setText(rs.getString("fname"));
                lbldob.setText(rs.getString("dob"));
                tfaddress.setText(rs.getString("address"));
                tfphone.setText(rs.getString("phone"));
                tfemail.setText(rs.getString("email"));
                tfeducation.setText(rs.getString("education"));
                tfdesignation.setText(rs.getString("designation"));
                tfsalary.setText(rs.getString("salary"));
            } else {
                JOptionPane.showMessageDialog(null, "Employee Not Found!");
                dispose();
                parentPanel.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == update) {
            String name = tfname.getText();
            String fname = tffname.getText();
            String address = tfaddress.getText();
            String phone = tfphone.getText();
            String email = tfemail.getText();
            String education = tfeducation.getText();
            String designation = tfdesignation.getText();

            try {
                Conn conn = new Conn();
                String query = "UPDATE employee SET name = '"+name+"', fname = '"+fname+"', address = '"+address+"', phone = '"+phone+"', email = '"+email+"', education = '"+education+"', designation = '"+designation+"' WHERE empId = '"+empId+"'";
                conn.s.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Profile Updated Successfully!");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error Updating Profile!");
            }
        } else if (ae.getSource() == back) {
            dispose();
            parentPanel.setVisible(true);
        }
    }

    public static void main(String[] args) {
        new ViewProfile("EMP123", new EmployeePanel("EMP123"));
    }
}
*/

package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;

public class ViewProfile extends JFrame implements ActionListener {
    JTextField tfname, tffname, tfaddress, tfphone, tfemail, tfeducation, tfdesignation, tfsalary;
    JLabel lblempId, lbldob;
    JButton update, back;
    String empId;
    EmployeePanel parentPanel;

    public ViewProfile(String empId, EmployeePanel parentPanel) {
        this.empId = empId;
        this.parentPanel = parentPanel;

        setTitle("View & Update Profile");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(null);

        // Header with background color
        JLabel heading = new JLabel("Details", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 32));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(new Color(60, 90, 153)); // Dark blue background

        // FIX: Use Toolkit to get the screen width for maximized frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        heading.setBounds(0, 0, screenSize.width, 80);
        add(heading);

        int labelX = 500, labelW = 200, labelH = 30;
        int fieldX = 720, fieldW = 400, fieldH = 30;
        int gapY = 50;
        int y = 120;

        JLabel labelEmpId = new JLabel("Employee ID:");
        labelEmpId.setBounds(labelX, y, labelW, labelH);
        labelEmpId.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelEmpId);

        lblempId = new JLabel(empId);
        lblempId.setBounds(fieldX, y, fieldW, fieldH);
        lblempId.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblempId);
        y += gapY;

        JLabel labelName = new JLabel("Name:");
        labelName.setBounds(labelX, y, labelW, labelH);
        labelName.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelName);

        tfname = new JTextField();
        tfname.setBounds(fieldX, y, fieldW, fieldH);
        tfname.setFont(new Font("Arial", Font.PLAIN, 16));
        add(tfname);
        y += gapY;

        JLabel labelFname = new JLabel("Father's Name:");
        labelFname.setBounds(labelX, y, labelW, labelH);
        labelFname.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelFname);

        tffname = new JTextField();
        tffname.setBounds(fieldX, y, fieldW, fieldH);
        tffname.setFont(new Font("Arial", Font.PLAIN, 16));
        add(tffname);
        y += gapY;

        JLabel labelDob = new JLabel("Date of Birth:");
        labelDob.setBounds(labelX, y, labelW, labelH);
        labelDob.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelDob);

        lbldob = new JLabel();
        lbldob.setBounds(fieldX, y, fieldW, fieldH);
        lbldob.setFont(new Font("Arial", Font.BOLD, 18));
        add(lbldob);
        y += gapY;

        JLabel labelAddress = new JLabel("Address:");
        labelAddress.setBounds(labelX, y, labelW, labelH);
        labelAddress.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelAddress);

        tfaddress = new JTextField();
        tfaddress.setBounds(fieldX, y, fieldW, fieldH);
        tfaddress.setFont(new Font("Arial", Font.PLAIN, 16));
        add(tfaddress);
        y += gapY;

        JLabel labelPhone = new JLabel("Phone:");
        labelPhone.setBounds(labelX, y, labelW, labelH);
        labelPhone.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelPhone);

        tfphone = new JTextField();
        tfphone.setBounds(fieldX, y, fieldW, fieldH);
        tfphone.setFont(new Font("Arial", Font.PLAIN, 16));
        add(tfphone);
        y += gapY;

        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setBounds(labelX, y, labelW, labelH);
        labelEmail.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelEmail);

        tfemail = new JTextField();
        tfemail.setBounds(fieldX, y, fieldW, fieldH);
        tfemail.setFont(new Font("Arial", Font.PLAIN, 16));
        add(tfemail);
        y += gapY;

        JLabel labelEducation = new JLabel("Education:");
        labelEducation.setBounds(labelX, y, labelW, labelH);
        labelEducation.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelEducation);

        tfeducation = new JTextField();
        tfeducation.setBounds(fieldX, y, fieldW, fieldH);
        tfeducation.setFont(new Font("Arial", Font.PLAIN, 16));
        add(tfeducation);
        y += gapY;

        JLabel labelDesignation = new JLabel("Designation:");
        labelDesignation.setBounds(labelX, y, labelW, labelH);
        labelDesignation.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelDesignation);

        tfdesignation = new JTextField();
        tfdesignation.setBounds(fieldX, y, fieldW, fieldH);
        tfdesignation.setFont(new Font("Arial", Font.PLAIN, 16));
        add(tfdesignation);
        y += gapY;

        JLabel labelSalary = new JLabel("Salary:");
        labelSalary.setBounds(labelX, y, labelW, labelH);
        labelSalary.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelSalary);

        tfsalary = new JTextField();
        tfsalary.setBounds(fieldX, y, fieldW, fieldH);
        tfsalary.setFont(new Font("Arial", Font.PLAIN, 16));
        tfsalary.setEditable(false);
        add(tfsalary);

        // Buttons at the bottom center
        // Updated "Update" button color to a standard blue
        update = createRoundedButton("Update", new Color(0, 102, 204)); 
        update.setBounds(650, 680, 150, 45);
        add(update);

        back = createRoundedButton("Back", new Color(220, 20, 60)); // Crimson Red
        back.setBounds(850, 680, 150, 45);
        add(back);

        fetchEmployeeDetails();
        setVisible(true);
    }
    
    private JButton createRoundedButton(String text, Color baseColor) {
        JButton button = new RoundedButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(this);
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(baseColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }
        });
        
        return button;
    }

    // Custom JButton with rounded corners
    private static class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
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

    private void fetchEmployeeDetails() {
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM employee WHERE empId = '" + empId + "'";
            ResultSet rs = c.s.executeQuery(query);
            if (rs.next()) {
                tfname.setText(rs.getString("name"));
                tffname.setText(rs.getString("fname"));
                lbldob.setText(rs.getString("dob"));
                tfaddress.setText(rs.getString("address"));
                tfphone.setText(rs.getString("phone"));
                tfemail.setText(rs.getString("email"));
                tfeducation.setText(rs.getString("education"));
                tfdesignation.setText(rs.getString("designation"));
                tfsalary.setText(rs.getString("salary"));
            } else {
                JOptionPane.showMessageDialog(null, "Employee Not Found!");
                dispose();
                parentPanel.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == update) {
            String name = tfname.getText();
            String fname = tffname.getText();
            String address = tfaddress.getText();
            String phone = tfphone.getText();
            String email = tfemail.getText();
            String education = tfeducation.getText();
            String designation = tfdesignation.getText();

            try {
                Conn conn = new Conn();
                String query = "UPDATE employee SET name = '"+name+"', fname = '"+fname+"', address = '"+address+"', phone = '"+phone+"', email = '"+email+"', education = '"+education+"', designation = '"+designation+"' WHERE empId = '"+empId+"'";
                conn.s.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Profile Updated Successfully!");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error Updating Profile!");
            }
        } else if (ae.getSource() == back) {
            dispose();
            parentPanel.setVisible(true);
        }
    }

    public static void main(String[] args) {
        new ViewProfile("1001", new EmployeePanel("1001"));
    }
}
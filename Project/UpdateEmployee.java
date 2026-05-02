/*package employee.management.system;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class UpdateEmployee extends JFrame implements ActionListener {

    JTextField tfeducation, tffname, tfaddress, tfphone, tfnid, tfemail, tfsalary, tfdesignation;
    JLabel lblname, lbldob, lblnid, lblempId;
    JButton updateBtn, backBtn;
    String empId;

    UpdateEmployee(String empId) {
        this.empId = empId;
        setTitle("Admin: Update Employee");

        setLayout(null);
        getContentPane().setBackground(new Color(245, 250, 255));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel heading = new JLabel("Update Employee Details");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setBounds(500, 30, 600, 50);
        heading.setForeground(new Color(44, 62, 80));
        add(heading);

        int x1 = 100, x2 = 350, x3 = 700, x4 = 950;
        int y = 120, hGap = 60;

        addLabel("Name:", x1, y);
        lblname = addDataLabel(x2, y);

        addLabel("Father's Name:", x3, y);
        tffname = addTextField(x4, y, "Enter father's name");

        y += hGap;
        addLabel("Date of Birth:", x1, y);
        lbldob = addDataLabel(x2, y);

        addLabel("Salary:", x3, y);
        tfsalary = addTextField(x4, y, "Enter salary");

        y += hGap;
        addLabel("Address:", x1, y);
        tfaddress = addTextField(x2, y, "Enter address");

        addLabel("Phone:", x3, y);
        tfphone = addTextField(x4, y, "Enter phone number");

        y += hGap;
        addLabel("Email:", x1, y);
        tfemail = addTextField(x2, y, "Enter email");

        addLabel("Highest Education:", x3, y);
        tfeducation = addTextField(x4, y, "Enter highest education");

        y += hGap;
        addLabel("Designation:", x1, y);
        tfdesignation = addTextField(x2, y, "Enter designation");

        addLabel("NID Number:", x3, y);
        lblnid = addDataLabel(x4, y);

        y += hGap;
        addLabel("Employee ID:", x1, y);
        lblempId = addDataLabel(x2, y);

        // Buttons
        updateBtn = new JButton("Update");
        styleButton(updateBtn, 500, y + 100, false);
        add(updateBtn);

        backBtn = new JButton("Back");
        styleButton(backBtn, 750, y + 100, true);
        add(backBtn);

        updateBtn.addActionListener(this);
        backBtn.addActionListener(this);

        loadDataFromDB();

        setVisible(true);
    }

    private void loadDataFromDB() {
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM employee WHERE empId = '" + empId + "'";
            ResultSet rs = c.s.executeQuery(query);

            if (rs.next()) {
                lblname.setText(rs.getString("name"));
                tffname.setText(rs.getString("fname"));
                lbldob.setText(rs.getString("dob"));
                tfaddress.setText(rs.getString("address"));
                tfsalary.setText(rs.getString("salary"));
                tfphone.setText(rs.getString("phone"));
                tfemail.setText(rs.getString("email"));
                tfeducation.setText(rs.getString("education"));
                lblnid.setText(rs.getString("nid"));
                lblempId.setText(rs.getString("empId"));
                tfdesignation.setText(rs.getString("designation"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to fetch data.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == updateBtn) {
            String fname = tffname.getText();
            String salary = tfsalary.getText();
            String address = tfaddress.getText();
            String phone = tfphone.getText();
            String email = tfemail.getText();
            String education = tfeducation.getText();
            String designation = tfdesignation.getText();

            try {
                Conn conn = new Conn();
                String query = "UPDATE employee SET fname='" + fname + "', salary='" + salary + "', address='" + address + "', phone='" + phone + "', email='" + email + "', education='" + education + "', designation='" + designation + "' WHERE empId='" + empId + "'";
                conn.s.executeUpdate(query);
                JOptionPane.showMessageDialog(this, "Employee details updated successfully.");
                setVisible(false);
                new Home();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Update failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            setVisible(false);
            new Home();
        }
    }

    private void addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 200, 30);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        add(label);
    }

    private JTextField addTextField(int x, int y, String tooltip) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, 200, 30);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tf.setToolTipText(tooltip);
        add(tf);
        return tf;
    }

    private JLabel addDataLabel(int x, int y) {
        JLabel label = new JLabel();
        label.setBounds(x, y, 200, 30);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setForeground(Color.DARK_GRAY);
        add(label);
        return label;
    }

    private void styleButton(JButton btn, int x, int y, boolean isRed) {
        btn.setBounds(x, y, 180, 40);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        if (isRed) {
            btn.setBackground(new Color(231, 76, 60)); // Red
        } else {
            btn.setBackground(new Color(52, 152, 219)); // Blue
        }
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        new UpdateEmployee("1001"); // test with dummy empId
    }
}
*/
/*
package employee.management.system;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class UpdateEmployee extends JFrame implements ActionListener {

    JTextField tfeducation, tffname, tfaddress, tfphone, tfnid, tfemail, tfsalary, tfdesignation, tfjoinDate;
    JLabel lblname, lbldob, lblnid, lblempId;
    JButton updateBtn, backBtn;
    String empId;

    UpdateEmployee(String empId) {
        this.empId = empId;
        setTitle("Admin: Update Employee");

        setLayout(null);
        getContentPane().setBackground(new Color(245, 250, 255));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel heading = new JLabel("Update Employee Details");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setBounds(500, 30, 600, 50);
        heading.setForeground(new Color(44, 62, 80));
        add(heading);

        int x1 = 100, x2 = 350, x3 = 700, x4 = 950;
        int y = 120, hGap = 60;

        addLabel("Name:", x1, y);
        lblname = addDataLabel(x2, y);

        addLabel("Father's Name:", x3, y);
        tffname = addTextField(x4, y, "Enter father's name");

        y += hGap;
        addLabel("Date of Birth:", x1, y);
        lbldob = addDataLabel(x2, y);

        addLabel("Salary:", x3, y);
        tfsalary = addTextField(x4, y, "Enter salary");

        y += hGap;
        addLabel("Address:", x1, y);
        tfaddress = addTextField(x2, y, "Enter address");

        addLabel("Phone:", x3, y);
        tfphone = addTextField(x4, y, "Enter phone number");

        y += hGap;
        addLabel("Email:", x1, y);
        tfemail = addTextField(x2, y, "Enter email");

        addLabel("Highest Education:", x3, y);
        tfeducation = addTextField(x4, y, "Enter highest education");

        y += hGap;
        addLabel("Designation:", x1, y);
        tfdesignation = addTextField(x2, y, "Enter designation");

        addLabel("Join Date:", x3, y);
        tfjoinDate = addTextField(x4, y, "YYYY-MM-DD");

        y += hGap;
        addLabel("NID Number:", x1, y);
        lblnid = addDataLabel(x2, y);

        addLabel("Employee ID:", x3, y);
        lblempId = addDataLabel(x4, y);

        // Buttons
        updateBtn = new JButton("Update");
        styleButton(updateBtn, 500, y + 100, false);
        add(updateBtn);

        backBtn = new JButton("Back");
        styleButton(backBtn, 750, y + 100, true);
        add(backBtn);

        updateBtn.addActionListener(this);
        backBtn.addActionListener(this);

        loadDataFromDB();

        setVisible(true);
    }

    private void loadDataFromDB() {
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM employee WHERE empId = '" + empId + "'";
            ResultSet rs = c.s.executeQuery(query);

            if (rs.next()) {
                lblname.setText(rs.getString("name"));
                tffname.setText(rs.getString("fname"));
                lbldob.setText(rs.getString("dob"));
                tfaddress.setText(rs.getString("address"));
                tfsalary.setText(rs.getString("salary"));
                tfphone.setText(rs.getString("phone"));
                tfemail.setText(rs.getString("email"));
                tfeducation.setText(rs.getString("education"));
                lblnid.setText(rs.getString("nid"));
                lblempId.setText(rs.getString("empId"));
                tfdesignation.setText(rs.getString("designation"));
                tfjoinDate.setText(rs.getString("job_start_date"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to fetch data.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == updateBtn) {
            String fname = tffname.getText();
            String salary = tfsalary.getText();
            String address = tfaddress.getText();
            String phone = tfphone.getText();
            String email = tfemail.getText();
            String education = tfeducation.getText();
            String designation = tfdesignation.getText();
            String joinDate = tfjoinDate.getText();

            try {
                Conn conn = new Conn();
                String query = "UPDATE employee SET fname='" + fname + "', salary='" + salary + "', address='" + address +
                        "', phone='" + phone + "', email='" + email + "', education='" + education +
                        "', designation='" + designation + "', job_start_date='" + joinDate + "' WHERE empId='" + empId + "'";
                conn.s.executeUpdate(query);
                JOptionPane.showMessageDialog(this, "Employee details updated successfully.");
                setVisible(false);
                new Home();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Update failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            setVisible(false);
            new Home();
        }
    }

    private void addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 200, 30);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        add(label);
    }

    private JTextField addTextField(int x, int y, String tooltip) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, 200, 30);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tf.setToolTipText(tooltip);
        add(tf);
        return tf;
    }

    private JLabel addDataLabel(int x, int y) {
        JLabel label = new JLabel();
        label.setBounds(x, y, 200, 30);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setForeground(Color.DARK_GRAY);
        add(label);
        return label;
    }

    private void styleButton(JButton btn, int x, int y, boolean isRed) {
        btn.setBounds(x, y, 180, 40);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(isRed ? new Color(231, 76, 60) : new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        new UpdateEmployee("1001"); // test with dummy empId
    }
}
*//*
package employee.management.system;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class UpdateEmployee extends JFrame implements ActionListener {

    JTextField tfname, tffname, tfaddress, tfphone, tfnid, tfemail, tfsalary, tfdesignation, tfeducation, tfdob, tfjoinDate;
    JLabel lblempId;
    JButton updateBtn, backBtn;
    String empId;

    UpdateEmployee(String empId) {
        this.empId = empId;
        setTitle("Admin: Update Employee");

        setLayout(null);
        getContentPane().setBackground(new Color(245, 250, 255));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel heading = new JLabel("Update Employee Details");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setBounds(500, 30, 600, 50);
        heading.setForeground(new Color(44, 62, 80));
        add(heading);

        int x1 = 100, x2 = 350, x3 = 700, x4 = 950;
        int y = 120, hGap = 60;

        addLabel("Name:", x1, y);
        tfname = addTextField(x2, y, "Enter name");

        addLabel("Father's Name:", x3, y);
        tffname = addTextField(x4, y, "Enter father's name");

        y += hGap;
        addLabel("Date of Birth:", x1, y);
        tfdob = addTextField(x2, y, "YYYY-MM-DD");

        addLabel("Salary:", x3, y);
        tfsalary = addTextField(x4, y, "Enter salary");

        y += hGap;
        addLabel("Address:", x1, y);
        tfaddress = addTextField(x2, y, "Enter address");

        addLabel("Phone:", x3, y);
        tfphone = addTextField(x4, y, "Enter phone number");

        y += hGap;
        addLabel("Email:", x1, y);
        tfemail = addTextField(x2, y, "Enter email");

        addLabel("Highest Education:", x3, y);
        tfeducation = addTextField(x4, y, "Enter highest education");

        y += hGap;
        addLabel("Designation:", x1, y);
        tfdesignation = addTextField(x2, y, "Enter designation");

        addLabel("Join Date:", x3, y);
        tfjoinDate = addTextField(x4, y, "YYYY-MM-DD");

        y += hGap;
        addLabel("NID Number:", x1, y);
        tfnid = addTextField(x2, y, "Enter NID number");

        addLabel("Employee ID:", x3, y);
        lblempId = addDataLabel(x4, y);

        // Buttons
        updateBtn = new JButton("Update");
        styleButton(updateBtn, 500, y + 100, false);
        add(updateBtn);

        backBtn = new JButton("Back");
        styleButton(backBtn, 750, y + 100, true);
        add(backBtn);

        updateBtn.addActionListener(this);
        backBtn.addActionListener(this);

        loadDataFromDB();

        setVisible(true);
    }

    private void loadDataFromDB() {
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM employee WHERE empId = '" + empId + "'";
            ResultSet rs = c.s.executeQuery(query);

            if (rs.next()) {
                tfname.setText(rs.getString("name"));
                tffname.setText(rs.getString("fname"));
                tfdob.setText(rs.getString("dob"));
                tfaddress.setText(rs.getString("address"));
                tfsalary.setText(rs.getString("salary"));
                tfphone.setText(rs.getString("phone"));
                tfemail.setText(rs.getString("email"));
                tfeducation.setText(rs.getString("education"));
                tfnid.setText(rs.getString("nid"));
                tfdesignation.setText(rs.getString("designation"));
                tfjoinDate.setText(rs.getString("job_start_date"));
                lblempId.setText(rs.getString("empId"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to fetch data.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == updateBtn) {
            String name = tfname.getText();
            String fname = tffname.getText();
            String dob = tfdob.getText();
            String salary = tfsalary.getText();
            String address = tfaddress.getText();
            String phone = tfphone.getText();
            String email = tfemail.getText();
            String education = tfeducation.getText();
            String designation = tfdesignation.getText();
            String joinDate = tfjoinDate.getText();
            String nid = tfnid.getText();

            try {
                Conn conn = new Conn();
                String query = "UPDATE employee SET name='" + name + "', fname='" + fname + "', dob='" + dob +
                        "', salary='" + salary + "', address='" + address + "', phone='" + phone +
                        "', email='" + email + "', education='" + education + "', designation='" + designation +
                        "', job_start_date='" + joinDate + "', nid='" + nid + "' WHERE empId='" + empId + "'";
                conn.s.executeUpdate(query);
                JOptionPane.showMessageDialog(this, "Employee details updated successfully.");
                setVisible(false);
                new Home();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Update failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            setVisible(false);
            new Home();
        }
    }

    private void addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 200, 30);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        add(label);
    }

    private JTextField addTextField(int x, int y, String tooltip) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, 200, 30);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tf.setToolTipText(tooltip);
        add(tf);
        return tf;
    }

    private JLabel addDataLabel(int x, int y) {
        JLabel label = new JLabel();
        label.setBounds(x, y, 200, 30);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setForeground(Color.DARK_GRAY);
        add(label);
        return label;
    }

    private void styleButton(JButton btn, int x, int y, boolean isRed) {
        btn.setBounds(x, y, 180, 40);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(isRed ? new Color(231, 76, 60) : new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        new UpdateEmployee("1001"); // test with dummy empId
    }
}
*/


package employee.management.system;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.awt.geom.RoundRectangle2D;

public class UpdateEmployee extends JFrame implements ActionListener {

    JTextField tfname, tffname, tfaddress, tfphone, tfnid, tfemail, tfsalary, tfdesignation, tfeducation, tfdob, tfjoinDate;
    JLabel lblempId;
    JButton updateBtn, backBtn, searchBtn; // 'searchBtn' নতুন যুক্ত করা হয়েছে
    Choice cemployeeId; // 'cemployeeId' নতুন যুক্ত করা হয়েছে
    String empId;

    // Custom RoundedButton class with hover effect
    private static class RoundedButton extends JButton {
        private Color backgroundColor;

        public RoundedButton(String text, Color bg) {
            super(text);
            backgroundColor = bg;
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(180, 40));
            setBackground(backgroundColor); // Set the initial background color

            // Add mouse listener for hover effect
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(backgroundColor.darker()); // Darken the color on hover
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(backgroundColor); // Restore the original color
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
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

    UpdateEmployee(String empId) {
        this.empId = empId;
        setTitle("Admin: Update Staff Details");

        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 250, 255));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ---------- Heading Panel ----------
        JPanel headingPanel = new JPanel();
        headingPanel.setBackground(new Color(0, 102, 204));
        JLabel mainHeading = new JLabel("Update Staff Details", SwingConstants.CENTER);
        mainHeading.setFont(new Font("Segoe UI", Font.BOLD, 42));
        mainHeading.setForeground(Color.WHITE);
        mainHeading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        headingPanel.add(mainHeading);
        add(headingPanel, BorderLayout.NORTH);

        // ---------- Search Panel at the top ----------
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        searchPanel.setBackground(new Color(245, 250, 255));

        JLabel searchLabel = new JLabel("Search by Employee ID:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        searchLabel.setForeground(new Color(0, 70, 140));
        searchPanel.add(searchLabel);

        cemployeeId = new Choice();
        cemployeeId.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT empId FROM employee ORDER BY empId ASC");
            while (rs.next()) {
                cemployeeId.add(rs.getString("empId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        searchPanel.add(cemployeeId);

        searchBtn = createButton("Search", new Color(0, 153, 255));
        searchPanel.add(searchBtn);

        // Combine heading and search panel
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(headingPanel, BorderLayout.NORTH);
        topContainer.add(searchPanel, BorderLayout.CENTER);
        add(topContainer, BorderLayout.NORTH);

        // ---------- Form Panel ----------
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 250, 255));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));
        add(formPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(createLabel("Name:"), gbc);
        gbc.gridx = 1; tfname = createTextField("Enter name", 25); formPanel.add(tfname, gbc);
        gbc.gridx = 2; formPanel.add(createLabel("Father's Name:"), gbc);
        gbc.gridx = 3; tffname = createTextField("Enter father's name", 25); formPanel.add(tffname, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(createLabel("Date of Birth:"), gbc);
        gbc.gridx = 1; tfdob = createTextField("YYYY-MM-DD", 25); formPanel.add(tfdob, gbc); tfdob.setEditable(false);
        gbc.gridx = 2; formPanel.add(createLabel("Salary:"), gbc);
        gbc.gridx = 3; tfsalary = createTextField("Enter salary", 25); formPanel.add(tfsalary, gbc);

        // Row 3
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(createLabel("Address:"), gbc);
        gbc.gridx = 1; tfaddress = createTextField("Enter address", 25); formPanel.add(tfaddress, gbc);
        gbc.gridx = 2; formPanel.add(createLabel("Phone:"), gbc);
        gbc.gridx = 3; tfphone = createTextField("Enter phone number", 25); formPanel.add(tfphone, gbc);

        // Row 4
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(createLabel("Email:"), gbc);
        gbc.gridx = 1; tfemail = createTextField("Enter email", 25); formPanel.add(tfemail, gbc);
        gbc.gridx = 2; formPanel.add(createLabel("Highest Education:"), gbc);
        gbc.gridx = 3; tfeducation = createTextField("Enter highest education", 25); formPanel.add(tfeducation, gbc);

        // Row 5
        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(createLabel("Designation:"), gbc);
        gbc.gridx = 1; tfdesignation = createTextField("Enter designation", 25); formPanel.add(tfdesignation, gbc);
        gbc.gridx = 2; formPanel.add(createLabel("Join Date:"), gbc);
        gbc.gridx = 3; tfjoinDate = createTextField("YYYY-MM-DD", 25); formPanel.add(tfjoinDate, gbc); tfjoinDate.setEditable(false);

        // Row 6
        gbc.gridx = 0; gbc.gridy = 5; formPanel.add(createLabel("NID Number:"), gbc);
        gbc.gridx = 1; tfnid = createTextField("Enter NID number", 25); formPanel.add(tfnid, gbc);
        gbc.gridx = 2; formPanel.add(createLabel("Employee ID:"), gbc);
        gbc.gridx = 3; lblempId = createDataLabel(); formPanel.add(lblempId, gbc);

        // ---------- Bottom Button Panel ----------
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
        buttonPanel.setBackground(new Color(245, 250, 255));
        updateBtn = new RoundedButton("Update Details", new Color(40, 167, 69));
        backBtn = new RoundedButton("Back", new Color(204, 0, 0));
        buttonPanel.add(updateBtn);
        buttonPanel.add(backBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        updateBtn.addActionListener(this);
        backBtn.addActionListener(this);
        searchBtn.addActionListener(this);

        // Load data if an empId is provided initially
        if (this.empId != null) {
             cemployeeId.select(this.empId);
             loadDataFromDB();
        }

        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(44, 62, 80));
        return label;
    }

    private JTextField createTextField(String tooltip, int columns) {
        JTextField tf = new JTextField(columns);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tf.setToolTipText(tooltip);
        return tf;
    }

    private JLabel createDataLabel() {
        JLabel label = new JLabel();
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setForeground(Color.DARK_GRAY);
        return label;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new RoundedButton(text, bgColor);
        btn.setPreferredSize(new Dimension(110, 40));
        return btn;
    }

    private void loadDataFromDB() {
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM employee WHERE empId = '" + empId + "'";
            ResultSet rs = c.s.executeQuery(query);

            if (rs.next()) {
                tfname.setText(rs.getString("name"));
                tffname.setText(rs.getString("fname"));
                tfdob.setText(rs.getString("dob"));
                tfaddress.setText(rs.getString("address"));
                tfsalary.setText(rs.getString("salary"));
                tfphone.setText(rs.getString("phone"));
                tfemail.setText(rs.getString("email"));
                tfeducation.setText(rs.getString("education"));
                tfnid.setText(rs.getString("nid"));
                tfdesignation.setText(rs.getString("designation"));
                tfjoinDate.setText(rs.getString("job_start_date"));
                lblempId.setText(rs.getString("empId"));
            } else {
                 clearFields();
                 JOptionPane.showMessageDialog(this, "Employee with ID " + empId + " not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to fetch data.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void clearFields() {
        tfname.setText("");
        tffname.setText("");
        tfdob.setText("");
        tfaddress.setText("");
        tfsalary.setText("");
        tfphone.setText("");
        tfemail.setText("");
        tfeducation.setText("");
        tfnid.setText("");
        tfdesignation.setText("");
        tfjoinDate.setText("");
        lblempId.setText("");
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == searchBtn) {
            empId = cemployeeId.getSelectedItem();
            if (empId != null && !empId.isEmpty()) {
                loadDataFromDB();
            } else {
                JOptionPane.showMessageDialog(this, "Please select an employee ID.", "No ID Selected", JOptionPane.WARNING_MESSAGE);
            }
        } else if (ae.getSource() == updateBtn) {
            String name = tfname.getText();
            String fname = tffname.getText();
            String dob = tfdob.getText();
            String salary = tfsalary.getText();
            String address = tfaddress.getText();
            String phone = tfphone.getText();
            String email = tfemail.getText();
            String education = tfeducation.getText();
            String designation = tfdesignation.getText();
            String joinDate = tfjoinDate.getText();
            String nid = tfnid.getText();

            try {
                Conn conn = new Conn();
                String query = "UPDATE employee SET name='" + name + "', fname='" + fname + "', dob='" + dob +
                        "', salary='" + salary + "', address='" + address + "', phone='" + phone +
                        "', email='" + email + "', education='" + education + "', designation='" + designation +
                        "', job_start_date='" + joinDate + "', nid='" + nid + "' WHERE empId='" + empId + "'";
                conn.s.executeUpdate(query);
                JOptionPane.showMessageDialog(this, "Employee details updated successfully.");
                setVisible(false);
                new Home();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Update failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (ae.getSource() == backBtn) {
            setVisible(false);
            new Home();
        }
    }

    public static void main(String[] args) {
        new UpdateEmployee("1001"); // test with dummy empId
    }
}

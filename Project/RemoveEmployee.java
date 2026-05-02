/*package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RemoveEmployee extends JFrame implements ActionListener {

    Choice cEmpId;
    JLabel lblname, lblphone, lblemail, lblFixedName, lblFixedPhone, lblFixedEmail;
    JButton delete, back;

    RemoveEmployee() {
        setTitle("Remove Employee");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // maximize full screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 250, 255));
        setLayout(null);

        // Get screen dimensions for centering
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int frameWidth = screenSize.width;
        int frameHeight = screenSize.height;

        JLabel title = new JLabel("🗑️ Remove Employee");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(0, 102, 204));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(0, 0, frameWidth, 80);
        add(title);

        // Positioning variables
        int labelWidth = 150;
        int valueWidth = 400;
        int compHeight = 30;
        int verticalSpacing = 60;

        // Center starting X positions
        int totalWidth = labelWidth + valueWidth + 20; // 20 px space between label and value
        int startX = (frameWidth - totalWidth) / 2;

        // Starting Y below title bar with some padding
        int startY = 150;

        // Select Employee ID label and choice
        JLabel labelId = new JLabel("Select Employee ID:");
        labelId.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelId.setBounds(startX, startY, labelWidth, compHeight);
        add(labelId);

        cEmpId = new Choice();
        cEmpId.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        cEmpId.setBounds(startX + labelWidth + 20, startY, valueWidth, compHeight);
        add(cEmpId);

        // Name label and value
        lblFixedName = new JLabel("Name:");
        lblFixedName.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblFixedName.setHorizontalAlignment(SwingConstants.RIGHT);
        lblFixedName.setBounds(startX, startY + verticalSpacing, labelWidth, compHeight);
        add(lblFixedName);

        lblname = new JLabel();
        lblname.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblname.setBounds(startX + labelWidth + 20, startY + verticalSpacing, valueWidth, compHeight);
        add(lblname);

        // Phone label and value
        lblFixedPhone = new JLabel("Phone:");
        lblFixedPhone.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblFixedPhone.setHorizontalAlignment(SwingConstants.RIGHT);
        lblFixedPhone.setBounds(startX, startY + verticalSpacing * 2, labelWidth, compHeight);
        add(lblFixedPhone);

        lblphone = new JLabel();
        lblphone.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblphone.setBounds(startX + labelWidth + 20, startY + verticalSpacing * 2, valueWidth, compHeight);
        add(lblphone);

        // Email label and value
        lblFixedEmail = new JLabel("Email:");
        lblFixedEmail.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblFixedEmail.setHorizontalAlignment(SwingConstants.RIGHT);
        lblFixedEmail.setBounds(startX, startY + verticalSpacing * 3, labelWidth, compHeight);
        add(lblFixedEmail);

        lblemail = new JLabel();
        lblemail.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblemail.setBounds(startX + labelWidth + 20, startY + verticalSpacing * 3, valueWidth, compHeight);
        add(lblemail);

        // Buttons centered below labels
        int buttonWidth = 150;
        int buttonHeight = 40;
        int buttonsTotalWidth = buttonWidth * 2 + 40; // 40 px gap between buttons
        int buttonsStartX = (frameWidth - buttonsTotalWidth) / 2;
        int buttonsY = startY + verticalSpacing * 4 + 20;

        delete = new JButton("Delete");
        delete.setFont(new Font("Segoe UI", Font.BOLD, 18));
        delete.setBackground(new Color(204, 0, 0));
        delete.setForeground(Color.WHITE);
        delete.setBounds(buttonsStartX, buttonsY, buttonWidth, buttonHeight);
        delete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        delete.addActionListener(this);
        add(delete);

        back = new JButton("Back");
        back.setFont(new Font("Segoe UI", Font.BOLD, 18));
        back.setBackground(new Color(0, 102, 204));
        back.setForeground(Color.WHITE);
        back.setBounds(buttonsStartX + buttonWidth + 40, buttonsY, buttonWidth, buttonHeight);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        back.addActionListener(this);
        add(back);

        loadEmpIds();

        if (cEmpId.getItemCount() > 0) {
            loadEmployeeInfo(cEmpId.getSelectedItem());
        }

        cEmpId.addItemListener(e -> loadEmployeeInfo(cEmpId.getSelectedItem()));

        setVisible(true);
    }

    private void loadEmpIds() {
        cEmpId.removeAll();
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT empId FROM employee ORDER BY empId ASC");
            while (rs.next()) {
                cEmpId.add(rs.getString("empId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadEmployeeInfo(String empId) {
        try {
            Conn c = new Conn();
            String query = "SELECT name, phone, email FROM employee WHERE empId = '" + empId + "'";
            ResultSet rs = c.s.executeQuery(query);
            if (rs.next()) {
                lblname.setText(rs.getString("name"));
                lblphone.setText(rs.getString("phone"));
                lblemail.setText(rs.getString("email"));
            } else {
                lblname.setText("");
                lblphone.setText("");
                lblemail.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == delete) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this employee?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Conn c = new Conn();
                    String empId = cEmpId.getSelectedItem();
                    String query = "DELETE FROM employee WHERE empId = '" + empId + "'";
                    c.s.executeUpdate(query);
                    JOptionPane.showMessageDialog(this, "Employee deleted successfully.");
                    loadEmpIds();

                    if (cEmpId.getItemCount() > 0) {
                        loadEmployeeInfo(cEmpId.getSelectedItem());
                    } else {
                        lblname.setText("");
                        lblphone.setText("");
                        lblemail.setText("");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (ae.getSource() == back) {
            setVisible(false);
            new Home();
        }
    }

    public static void main(String[] args) {
        new RemoveEmployee();
    }
}
*/


package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.awt.geom.RoundRectangle2D;

public class RemoveEmployee extends JFrame implements ActionListener {

    Choice cEmpId;
    JLabel lblname, lbfname, lbdob, lblsalary, lbladdress, lblphone, lblemail, lbleducation, lbldesig, lblnid, lbljoinDate;
    JButton delete, back;

    // Custom RoundedButton class
    private static class RoundedButton extends JButton {
        private Color backgroundColor;
        private Color originalColor;
        private Color hoverColor;

        public RoundedButton(String text, Color bg) {
            super(text);
            this.backgroundColor = bg;
            this.originalColor = bg;
            this.hoverColor = bg.darker();
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 18));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(150, 40));
            setBackground(backgroundColor);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(originalColor);
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
    }

    RemoveEmployee() {
        setTitle("Remove Staff");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // maximize full screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 250, 255));
        setLayout(null);

        // Get screen dimensions for centering
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int frameWidth = screenSize.width;

        JLabel title = new JLabel("Remove Staff");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(0, 102, 204));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(0, 0, frameWidth, 80);
        add(title);

        // Positioning variables
        int labelWidth = 200;
        int valueWidth = 400;
        int compHeight = 30;
        int verticalSpacing = 40;

        // Center starting X positions
        int totalWidth = labelWidth + valueWidth + 20; // 20 px space between label and value
        int startX = (frameWidth - totalWidth) / 2;

        // Starting Y below title bar with some padding
        int startY = 150;

        // Left side labels and values
        JLabel labelId = createLabel("Select Employee ID:");
        labelId.setBounds(startX, startY, labelWidth, compHeight);
        add(labelId);

        cEmpId = new Choice();
        cEmpId.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        cEmpId.setBounds(startX + labelWidth + 20, startY, valueWidth, compHeight);
        add(cEmpId);

        JLabel lblFixedName = createLabel("Name:");
        lblFixedName.setBounds(startX, startY + verticalSpacing, labelWidth, compHeight);
        add(lblFixedName);
        lblname = createValueLabel();
        lblname.setBounds(startX + labelWidth + 20, startY + verticalSpacing, valueWidth, compHeight);
        add(lblname);

        JLabel lblFixedFname = createLabel("Father's Name:");
        lblFixedFname.setBounds(startX, startY + verticalSpacing * 2, labelWidth, compHeight);
        add(lblFixedFname);
        lbfname = createValueLabel();
        lbfname.setBounds(startX + labelWidth + 20, startY + verticalSpacing * 2, valueWidth, compHeight);
        add(lbfname);

        JLabel lblFixedDOB = createLabel("Date of Birth:");
        lblFixedDOB.setBounds(startX, startY + verticalSpacing * 3, labelWidth, compHeight);
        add(lblFixedDOB);
        lbdob = createValueLabel();
        lbdob.setBounds(startX + labelWidth + 20, startY + verticalSpacing * 3, valueWidth, compHeight);
        add(lbdob);

        JLabel lblFixedSalary = createLabel("Salary:");
        lblFixedSalary.setBounds(startX, startY + verticalSpacing * 4, labelWidth, compHeight);
        add(lblFixedSalary);
        lblsalary = createValueLabel();
        lblsalary.setBounds(startX + labelWidth + 20, startY + verticalSpacing * 4, valueWidth, compHeight);
        add(lblsalary);

        JLabel lblFixedAddress = createLabel("Address:");
        lblFixedAddress.setBounds(startX, startY + verticalSpacing * 5, labelWidth, compHeight);
        add(lblFixedAddress);
        lbladdress = createValueLabel();
        lbladdress.setBounds(startX + labelWidth + 20, startY + verticalSpacing * 5, valueWidth, compHeight);
        add(lbladdress);

        JLabel lblFixedPhone = createLabel("Phone:");
        lblFixedPhone.setBounds(startX, startY + verticalSpacing * 6, labelWidth, compHeight);
        add(lblFixedPhone);
        lblphone = createValueLabel();
        lblphone.setBounds(startX + labelWidth + 20, startY + verticalSpacing * 6, valueWidth, compHeight);
        add(lblphone);
        
        JLabel lblFixedEmail = createLabel("Email:");
        lblFixedEmail.setBounds(startX, startY + verticalSpacing * 7, labelWidth, compHeight);
        add(lblFixedEmail);
        lblemail = createValueLabel();
        lblemail.setBounds(startX + labelWidth + 20, startY + verticalSpacing * 7, valueWidth, compHeight);
        add(lblemail);

        JLabel lblFixedEducation = createLabel("Highest Education:");
        lblFixedEducation.setBounds(startX, startY + verticalSpacing * 8, labelWidth, compHeight);
        add(lblFixedEducation);
        lbleducation = createValueLabel();
        lbleducation.setBounds(startX + labelWidth + 20, startY + verticalSpacing * 8, valueWidth, compHeight);
        add(lbleducation);

        JLabel lblFixedDesig = createLabel("Designation:");
        lblFixedDesig.setBounds(startX, startY + verticalSpacing * 9, labelWidth, compHeight);
        add(lblFixedDesig);
        lbldesig = createValueLabel();
        lbldesig.setBounds(startX + labelWidth + 20, startY + verticalSpacing * 9, valueWidth, compHeight);
        add(lbldesig);

        JLabel lblFixedNid = createLabel("NID Number:");
        lblFixedNid.setBounds(startX, startY + verticalSpacing * 10, labelWidth, compHeight);
        add(lblFixedNid);
        lblnid = createValueLabel();
        lblnid.setBounds(startX + labelWidth + 20, startY + verticalSpacing * 10, valueWidth, compHeight);
        add(lblnid);

        JLabel lblFixedJoinDate = createLabel("Join Date:");
        lblFixedJoinDate.setBounds(startX, startY + verticalSpacing * 11, labelWidth, compHeight);
        add(lblFixedJoinDate);
        lbljoinDate = createValueLabel();
        lbljoinDate.setBounds(startX + labelWidth + 20, startY + verticalSpacing * 11, valueWidth, compHeight);
        add(lbljoinDate);


        // Buttons centered below labels
        int buttonWidth = 150;
        int buttonHeight = 40;
        int buttonsTotalWidth = buttonWidth * 2 + 40; // 40 px gap between buttons
        int buttonsStartX = (frameWidth - buttonsTotalWidth) / 2;
        int buttonsY = startY + verticalSpacing * 12 + 40;

        delete = new RoundedButton("Delete", new Color(204, 0, 0));
        delete.setBounds(buttonsStartX, buttonsY, buttonWidth, buttonHeight);
        delete.addActionListener(this);
        add(delete);

        back = new RoundedButton("Back", new Color(128, 0, 128)); // Purple color for back button
        back.setBounds(buttonsStartX + buttonWidth + 40, buttonsY, buttonWidth, buttonHeight);
        back.addActionListener(this);
        add(back);

        loadEmpIds();

        if (cEmpId.getItemCount() > 0) {
            loadEmployeeInfo(cEmpId.getSelectedItem());
        }

        cEmpId.addItemListener(e -> loadEmployeeInfo(cEmpId.getSelectedItem()));

        setVisible(true);
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setForeground(new Color(44, 62, 80));
        return label;
    }

    private JLabel createValueLabel() {
        JLabel label = new JLabel();
        label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        label.setForeground(Color.DARK_GRAY);
        return label;
    }


    private void loadEmpIds() {
        cEmpId.removeAll();
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT empId FROM employee ORDER BY empId ASC");
            while (rs.next()) {
                cEmpId.add(rs.getString("empId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadEmployeeInfo(String empId) {
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM employee WHERE empId = '" + empId + "'";
            ResultSet rs = c.s.executeQuery(query);
            if (rs.next()) {
                lblname.setText(rs.getString("name"));
                lbfname.setText(rs.getString("fname"));
                lbdob.setText(rs.getString("dob"));
                lblsalary.setText(rs.getString("salary"));
                lbladdress.setText(rs.getString("address"));
                lblphone.setText(rs.getString("phone"));
                lblemail.setText(rs.getString("email"));
                lbleducation.setText(rs.getString("education"));
                lbldesig.setText(rs.getString("designation"));
                lblnid.setText(rs.getString("nid"));
                lbljoinDate.setText(rs.getString("job_start_date"));
            } else {
                clearLabels();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void clearLabels() {
        lblname.setText("");
        lbfname.setText("");
        lbdob.setText("");
        lblsalary.setText("");
        lbladdress.setText("");
        lblphone.setText("");
        lblemail.setText("");
        lbleducation.setText("");
        lbldesig.setText("");
        lblnid.setText("");
        lbljoinDate.setText("");
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == delete) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this employee?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Conn c = new Conn();
                    String empId = cEmpId.getSelectedItem();
                    String query = "DELETE FROM employee WHERE empId = '" + empId + "'";
                    c.s.executeUpdate(query);
                    JOptionPane.showMessageDialog(this, "Employee deleted successfully.");
                    loadEmpIds();

                    if (cEmpId.getItemCount() > 0) {
                        loadEmployeeInfo(cEmpId.getSelectedItem());
                    } else {
                        clearLabels();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (ae.getSource() == back) {
            setVisible(false);
            new Home();
        }
    }

    public static void main(String[] args) {
        new RemoveEmployee();
    }
}
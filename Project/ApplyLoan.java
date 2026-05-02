/*package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class ApplyLoan extends JFrame implements ActionListener {
    JTextField amountField, purposeField;
    JButton submit, back;
    String empId;
    JFrame parent;

    public ApplyLoan(String empId, JFrame parent) {
        this.empId = empId;
        this.parent = parent;

        setTitle("Apply for Loan");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Top heading panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(60, 90, 153));
        topPanel.setPreferredSize(new Dimension(getWidth(), 100));
        JLabel heading = new JLabel("Apply Loan");
        heading.setFont(new Font("SansSerif", Font.BOLD, 42));
        heading.setForeground(Color.WHITE);
        topPanel.add(heading);
        add(topPanel, BorderLayout.NORTH);

        // Center form panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblAmount = new JLabel("Loan Amount:");
        lblAmount.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        centerPanel.add(lblAmount, gbc);

        gbc.gridx = 1;
        amountField = new JTextField(20);
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        centerPanel.add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblPurpose = new JLabel("Purpose:");
        lblPurpose.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        centerPanel.add(lblPurpose, gbc);

        gbc.gridx = 1;
        purposeField = new JTextField(20);
        purposeField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        centerPanel.add(purposeField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        submit = createButton("Submit", new Color(46, 204, 113));
        submit.addActionListener(this);
        centerPanel.add(submit, gbc);

        gbc.gridx = 1;
        back = createButton("Back", new Color(231, 76, 60));
        back.addActionListener(e -> {
            this.setVisible(false);
            if (parent != null) parent.setVisible(true);
        });
        centerPanel.add(back, gbc);

        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(150, 45));
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        submitLoan();
    }

    private void submitLoan() {
        try {
            String amountText = amountField.getText().trim();
            String purpose = purposeField.getText().trim();

            if (amountText.isEmpty() || purpose.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter valid amount and purpose.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Conn c = new Conn();
            String checkSql = "SELECT job_start_date FROM employee WHERE empid = ?";
            PreparedStatement psCheck = c.c.prepareStatement(checkSql);
            psCheck.setString(1, empId);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                Date startDate = rs.getDate("job_start_date");
                if (startDate == null || startDate.toLocalDate().plusYears(2).isAfter(LocalDate.now())) {
                    JOptionPane.showMessageDialog(this, "You must have at least 2 years of job duration to apply for a loan.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String insertSql = "INSERT INTO loan_requests (emp_id, amount, purpose, request_date) VALUES (?, ?, ?, ?)";
            PreparedStatement psInsert = c.c.prepareStatement(insertSql);
            psInsert.setString(1, empId);
            psInsert.setDouble(2, amount);
            psInsert.setString(3, purpose);
            psInsert.setDate(4, Date.valueOf(LocalDate.now()));

            int result = psInsert.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Loan request submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.setVisible(false);
                if (parent != null) parent.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Loan request failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for amount.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new ApplyLoan("1001", null);
    }
}
*/

package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.awt.geom.RoundRectangle2D;

public class ApplyLoan extends JFrame implements ActionListener {
    JTextField amountField, purposeField;
    JButton submit, back;
    String empId;
    JFrame parent;

    // Custom JButton with rounded corners
    private static class RoundedButton extends JButton {
        private Color baseColor;

        public RoundedButton(String text, Color bgColor, Color fgColor, Font font, Dimension size) {
            super(text);
            this.baseColor = bgColor;
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setBackground(baseColor);
            setForeground(fgColor);
            setFont(font);
            setPreferredSize(size);
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

    public ApplyLoan(String empId, JFrame parent) {
        this.empId = empId;
        this.parent = parent;

        setTitle("Apply for Loan");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Top heading panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(60, 90, 153));
        topPanel.setPreferredSize(new Dimension(getWidth(), 100));
        JLabel heading = new JLabel("Apply Loan");
        heading.setFont(new Font("SansSerif", Font.BOLD, 42));
        heading.setForeground(Color.WHITE);
        topPanel.add(heading);
        add(topPanel, BorderLayout.NORTH);

        // Center form panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblAmount = new JLabel("Loan Amount:");
        lblAmount.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        centerPanel.add(lblAmount, gbc);

        gbc.gridx = 1;
        amountField = new JTextField(20);
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        centerPanel.add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblPurpose = new JLabel("Purpose:");
        lblPurpose.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        centerPanel.add(lblPurpose, gbc);

        gbc.gridx = 1;
        purposeField = new JTextField(20);
        purposeField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        centerPanel.add(purposeField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        submit = createRoundedButton("Submit", new Color(46, 204, 113));
        submit.addActionListener(this);
        centerPanel.add(submit, gbc);

        gbc.gridx = 1;
        back = createRoundedButton("Back", new Color(231, 76, 60));
        back.addActionListener(e -> {
            this.setVisible(false);
            if (parent != null) parent.setVisible(true);
        });
        centerPanel.add(back, gbc);

        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createRoundedButton(String text, Color bgColor) {
        return new RoundedButton(text, bgColor, Color.WHITE, new Font("Segoe UI", Font.BOLD, 20), new Dimension(150, 45));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        submitLoan();
    }

    private void submitLoan() {
        try {
            String amountText = amountField.getText().trim();
            String purpose = purposeField.getText().trim();

            if (amountText.isEmpty() || purpose.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter valid amount and purpose.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Conn c = new Conn();
            String checkSql = "SELECT job_start_date FROM employee WHERE empid = ?";
            PreparedStatement psCheck = c.c.prepareStatement(checkSql);
            psCheck.setString(1, empId);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                Date startDate = rs.getDate("job_start_date");
                if (startDate == null || startDate.toLocalDate().plusYears(2).isAfter(LocalDate.now())) {
                    JOptionPane.showMessageDialog(this, "You must have at least 2 years of job duration to apply for a loan.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String insertSql = "INSERT INTO loan_requests (emp_id, amount, purpose, request_date) VALUES (?, ?, ?, ?)";
            PreparedStatement psInsert = c.c.prepareStatement(insertSql);
            psInsert.setString(1, empId);
            psInsert.setDouble(2, amount);
            psInsert.setString(3, purpose);
            psInsert.setDate(4, Date.valueOf(LocalDate.now()));

            int result = psInsert.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Loan request submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.setVisible(false);
                if (parent != null) parent.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Loan request failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for amount.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new ApplyLoan("1001", null);
    }
}
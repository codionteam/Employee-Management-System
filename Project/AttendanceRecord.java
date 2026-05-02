package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.toedter.calendar.JDateChooser;
import net.proteanit.sql.DbUtils; // This import requires rs2xml.jar

public class AttendanceRecord extends JFrame implements ActionListener {

    JTable table;
    JButton back, btnSearch, btnShowAll;
    String employeeId;
    EmployeePanel parentPanel;
    JDateChooser dateChooser;

    public AttendanceRecord(String employeeId, EmployeePanel parentPanel) {
        this.employeeId = employeeId;
        this.parentPanel = parentPanel;

        setTitle("Attendance Record for Employee ID: " + employeeId);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        getContentPane().setBackground(new Color(240, 248, 255));

        // Top Panel for Heading and Search Controls
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(new Color(240, 248, 255));
        
        // Heading
        JLabel heading = new JLabel("Attendance Record", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(new Color(60, 90, 153)); // Changed heading background color
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        topContainer.add(heading, BorderLayout.NORTH);
        
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        searchPanel.setBackground(new Color(240, 248, 255));
        
        JLabel searchLabel = new JLabel("Select Date:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        
        dateChooser = new JDateChooser();
        dateChooser.setPreferredSize(new Dimension(150, 35));
        dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        btnSearch = createButton("Search", new Color(52, 152, 219));
        btnShowAll = createButton("Show All", new Color(46, 204, 113));
        
        searchPanel.add(searchLabel);
        searchPanel.add(dateChooser);
        searchPanel.add(btnSearch);
        searchPanel.add(btnShowAll);
        
        topContainer.add(searchPanel, BorderLayout.SOUTH);
        add(topContainer, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        table = new JTable();
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(52, 152, 219));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // Back Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 30));
        buttonPanel.setBackground(new Color(240, 248, 255));

        back = createButton("Back", new Color(231, 76, 60));

        buttonPanel.add(back);
        add(buttonPanel, BorderLayout.SOUTH);

        loadAttendanceData(null); // Load all data initially

        setVisible(true);
    }

    private void loadAttendanceData(String date) {
        try {
            Conn c = new Conn();
            String query;

            if (date != null && !date.isEmpty()) {
                query = "SELECT date, checkin_time, checkout_time FROM attendance WHERE empId = '" + employeeId + "' AND date = '" + date + "' ORDER BY date DESC";
            } else {
                query = "SELECT date, checkin_time, checkout_time FROM attendance WHERE empId = '" + employeeId + "' ORDER BY date DESC";
            }
            
            ResultSet rs = c.s.executeQuery(query);
            table.setModel(DbUtils.resultSetToTableModel(rs));

            rs.close();
            c.s.close();
            c.c.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading attendance data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Helper method to create a rounded button with a specific style
    private JButton createButton(String text, Color baseColor) {
        JButton button = new RoundedButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 40));
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

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == back) {
            dispose();
            if (parentPanel != null) {
                parentPanel.setVisible(true);
            }
        } else if (ae.getSource() == btnSearch) {
            Date selectedDate = dateChooser.getDate();
            if (selectedDate != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = sdf.format(selectedDate);
                loadAttendanceData(dateString);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a date to search.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else if (ae.getSource() == btnShowAll) {
            loadAttendanceData(null);
        }
    }

    // Custom JButton with rounded corners
    private static class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
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

    // Main method for testing (optional)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AttendanceRecord("1001", null));
    }
}
/*package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.time.LocalDate;
import java.util.Vector;

public class Notice extends JFrame implements ActionListener {
    private String empId;
    private EmployeePanel parentPanel;
    private JTable noticeTable;
    private DefaultTableModel tableModel;
    private JButton backButton;

    // Custom JButton with rounded corners
    private static class RoundedButton extends JButton {
        public RoundedButton(String text, Color bgColor, Color fgColor, Font font, Dimension size) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setBackground(bgColor);
            setForeground(fgColor);
            setFont(font);
            setPreferredSize(size);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(bgColor.darker());
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(bgColor);
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

    public Notice(String empId, EmployeePanel parentPanel) {
        this.empId = empId;
        this.parentPanel = parentPanel;

        setTitle("Notice Board");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        JLabel heading = new JLabel(" Notice Board", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(new Color(60, 90, 153));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        headerPanel.add(heading, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        String[] columns = {"Notice No", "Date", "Notice"};
        tableModel = new DefaultTableModel(columns, 0);
        noticeTable = new JTable(tableModel);
        
        // Style the table for a modern look
        noticeTable.setFont(new Font("Tahoma", Font.PLAIN, 18));
        noticeTable.setRowHeight(30);
        noticeTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 18));
        noticeTable.getTableHeader().setBackground(new Color(60, 90, 153));
        noticeTable.getTableHeader().setForeground(Color.WHITE);
        noticeTable.setSelectionBackground(new Color(173, 216, 230));
        noticeTable.setGridColor(Color.LIGHT_GRAY);
        noticeTable.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(noticeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 20)); 

        Dimension buttonSize = new Dimension(250, 45); 

        backButton = new RoundedButton("Back", new Color(231, 76, 60), Color.WHITE, new Font("Arial", Font.BOLD, 18), buttonSize);
        backButton.addActionListener(this);
        
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadNotices();
        setVisible(true);
    }
    
    private void loadNotices() {
        try {
            tableModel.setRowCount(0);
            Conn c = new Conn();
            String query = "SELECT notice_no, date, notice FROM notices ORDER BY notice_no DESC";
            ResultSet rs = c.s.executeQuery(query);

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                int id = rs.getInt("notice_no");
                String date = rs.getString("date");
                String notice = rs.getString("notice");
                tableModel.addRow(new Object[]{id, date, notice});
            }

            if (!hasData) {
                tableModel.addRow(new Object[]{"-", "-", "No notices available."});
            }

        } catch (Exception e) {
            e.printStackTrace();
            tableModel.setRowCount(0);
            tableModel.addRow(new Object[]{"-", "-", "Error fetching notices."});
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            dispose();
            if (parentPanel != null) {
                parentPanel.setVisible(true);
            }
        }
    }

    public static void main(String[] args) {
        // For testing purposes
        new Notice("1001", new EmployeePanel("1001"));
    }
}
*/
package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.time.LocalDate;
import java.util.Vector;

public class Notice extends JFrame implements ActionListener {
    private String empId;
    private EmployeePanel parentPanel;
    private JTable noticeTable;
    private DefaultTableModel tableModel;
    private JButton backButton;

    // Custom JButton with rounded corners
    private static class RoundedButton extends JButton {
        public RoundedButton(String text, Color bgColor, Color fgColor, Font font, Dimension size) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setBackground(bgColor);
            setForeground(fgColor);
            setFont(font);
            setPreferredSize(size);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(bgColor.darker());
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(bgColor);
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

    public Notice(String empId, EmployeePanel parentPanel) {
        this.empId = empId;
        this.parentPanel = parentPanel;

        setTitle("Notice Board");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        JLabel heading = new JLabel(" Notice Board", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(new Color(60, 90, 153));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        headerPanel.add(heading, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        String[] columns = {"Notice No", "Date", "Notice"};
        tableModel = new DefaultTableModel(columns, 0);
        noticeTable = new JTable(tableModel);

        // Style the table for a modern look
        noticeTable.setFont(new Font("Tahoma", Font.PLAIN, 18));
        noticeTable.setRowHeight(30);
        noticeTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 18));
        noticeTable.getTableHeader().setBackground(new Color(60, 90, 153));
        noticeTable.getTableHeader().setForeground(Color.WHITE);
        noticeTable.setSelectionBackground(new Color(173, 216, 230));
        noticeTable.setGridColor(Color.LIGHT_GRAY);
        noticeTable.setEnabled(false);
        
        // --- START: Updated Code for column width ---
        // Create a custom cell renderer for the "Notice" column to handle multi-line text
        noticeTable.getColumnModel().getColumn(2).setCellRenderer(new MyTableCellRenderer());
        
        // Get the TableColumnModel
        TableColumnModel columnModel = noticeTable.getColumnModel();
        
        // Set preferred widths for each column
        columnModel.getColumn(0).setPreferredWidth(100); // Notice No
        columnModel.getColumn(1).setPreferredWidth(150); // Date
        columnModel.getColumn(2).setPreferredWidth(800); // Notice (Make it much wider)

        // Set minimum width to prevent columns from becoming too small
        columnModel.getColumn(0).setMinWidth(80);
        columnModel.getColumn(1).setMinWidth(120);
        columnModel.getColumn(2).setMinWidth(400);

        // Allow "Notice" column to resize automatically, but other columns can also be resized by user
        columnModel.getColumn(2).setResizable(true);
        // --- END: Updated Code ---

        JScrollPane scrollPane = new JScrollPane(noticeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 20));

        Dimension buttonSize = new Dimension(250, 45);

        backButton = new RoundedButton("Back", new Color(231, 76, 60), Color.WHITE, new Font("Arial", Font.BOLD, 18), buttonSize);
        backButton.addActionListener(this);

        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadNotices();
        setVisible(true);
    }

    private void loadNotices() {
        try {
            tableModel.setRowCount(0);
            Conn c = new Conn();
            String query = "SELECT notice_no, date, notice FROM notices ORDER BY notice_no DESC";
            ResultSet rs = c.s.executeQuery(query);

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                int id = rs.getInt("notice_no");
                String date = rs.getString("date");
                String notice = rs.getString("notice");
                
                // Add new rows to the table model
                tableModel.addRow(new Object[]{id, date, notice});
            }

            if (!hasData) {
                tableModel.addRow(new Object[]{"-", "-", "No notices available."});
            }

        } catch (Exception e) {
            e.printStackTrace();
            tableModel.setRowCount(0);
            tableModel.addRow(new Object[]{"-", "-", "Error fetching notices."});
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            dispose();
            if (parentPanel != null) {
                parentPanel.setVisible(true);
            }
        }
    }

    public static void main(String[] args) {
        // For testing purposes
        new Notice("1001", new EmployeePanel("1001"));
    }
    
    // Custom Cell Renderer to handle multi-line text in JTable
    class MyTableCellRenderer extends JTextArea implements javax.swing.table.TableCellRenderer {
        public MyTableCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setFont(new Font("Tahoma", Font.PLAIN, 18));
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText((String) value);
            
            // Set background color
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            
            // Adjust row height to fit content
            int fontHeight = getFontMetrics(getFont()).getHeight();
            int lineCount = getLineCount();
            int newHeight = (lineCount * fontHeight) + 10; // 10 for padding
            if (newHeight > table.getRowHeight(row)) {
                table.setRowHeight(row, newHeight);
            }
            
            return this;
        }
    }
}
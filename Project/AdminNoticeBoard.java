/*package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Vector;

public class AdminNoticeBoard extends JFrame {
    private JTextArea noticeInputArea;
    private JTable noticeTable;
    private JButton addBtn, updateBtn, deleteBtn, backBtn;
    private int selectedNoticeNo = -1;

    private JTextField dateSearchField;
    private JButton dateSearchBtn, showAllBtn;

    public AdminNoticeBoard() {
        setTitle("Admin Notice Board");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        JLabel heading = new JLabel("Admin Notice Board", JLabel.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 32));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(heading, BorderLayout.NORTH);

        // Top Panel for Search + Notice Input + Buttons
        JPanel mainTopPanel = new JPanel(new BorderLayout());
        mainTopPanel.setOpaque(false);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        searchPanel.setOpaque(false);

        JLabel searchLabel = new JLabel("Search by Date (yyyy-mm-dd):");
        searchLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        searchPanel.add(searchLabel);

        dateSearchField = new JTextField(12);
        dateSearchField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        searchPanel.add(dateSearchField);

        dateSearchBtn = styledButton("Search", new Color(52, 152, 219));
        dateSearchBtn.addActionListener(e -> {
            String dateStr = dateSearchField.getText().trim();
            if (dateStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a date in yyyy-mm-dd format.");
                return;
            }
            loadNoticesByDate(dateStr);
        });
        searchPanel.add(dateSearchBtn);

        showAllBtn = styledButton("Show All", new Color(39, 174, 96));
        showAllBtn.addActionListener(e -> {
            dateSearchField.setText("");
            loadNotices();
        });
        searchPanel.add(showAllBtn);

        mainTopPanel.add(searchPanel, BorderLayout.NORTH);

        // TextArea and Buttons panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        noticeInputArea = new JTextArea(5, 50);
        noticeInputArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        noticeInputArea.setBorder(BorderFactory.createTitledBorder("Write or Edit Notice"));
        JScrollPane scroll = new JScrollPane(noticeInputArea);
        topPanel.add(scroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        addBtn = styledButton("Add Notice", new Color(52, 152, 219));
        updateBtn = styledButton("Update Notice", new Color(241, 196, 15));
        deleteBtn = styledButton("Delete Notice", new Color(231, 76, 60));
        backBtn = styledButton("Back", new Color(192, 57, 43));

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(backBtn);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainTopPanel.add(topPanel, BorderLayout.CENTER);

        add(mainTopPanel, BorderLayout.NORTH);

        // Table to display notices
        noticeTable = new JTable();
        noticeTable.setRowHeight(30);
        noticeTable.setFont(new Font("SansSerif", Font.PLAIN, 16));
        noticeTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        noticeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane tableScroll = new JScrollPane(noticeTable);
        tableScroll.setPreferredSize(new Dimension(1000, 400));
        tableScroll.setBorder(BorderFactory.createTitledBorder("All Notices"));
        add(tableScroll, BorderLayout.CENTER);

        // Event Listeners
        addBtn.addActionListener(e -> addNotice());
        updateBtn.addActionListener(e -> updateNotice());
        deleteBtn.addActionListener(e -> deleteNotice());
        backBtn.addActionListener(e -> {
            setVisible(false);
            new Home();  // আপনার Home ক্লাস এখানে কল করবেন
        });

        noticeTable.getSelectionModel().addListSelectionListener(e -> {
            int row = noticeTable.getSelectedRow();
            if (row != -1) {
                selectedNoticeNo = (int) noticeTable.getValueAt(row, 0); // notice_no
                String selectedNotice = (String) noticeTable.getValueAt(row, 2); // notice text
                noticeInputArea.setText(selectedNotice);
            }
        });

        loadNotices();
        setVisible(true);
    }

    private JButton styledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void addNotice() {
        String noticeText = noticeInputArea.getText().trim();
        if (noticeText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Notice cannot be empty!");
            return;
        }

        try {
            Conn c = new Conn();
            String query = "INSERT INTO notices (notice, date) VALUES (?, ?)";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, noticeText);
            ps.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Notice added successfully!");
            noticeInputArea.setText("");
            loadNotices();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateNotice() {
        if (selectedNoticeNo == -1) {
            JOptionPane.showMessageDialog(this, "Please select a notice to update!");
            return;
        }

        String updatedText = noticeInputArea.getText().trim();
        if (updatedText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Updated notice cannot be empty!");
            return;
        }

        try {
            Conn c = new Conn();
            String query = "UPDATE notices SET notice=? WHERE notice_no=?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, updatedText);
            ps.setInt(2, selectedNoticeNo);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Notice updated successfully!");
            noticeInputArea.setText("");
            selectedNoticeNo = -1;
            loadNotices();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteNotice() {
        if (selectedNoticeNo == -1) {
            JOptionPane.showMessageDialog(this, "Please select a notice to delete!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this notice?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Conn c = new Conn();
                String query = "DELETE FROM notices WHERE notice_no=?";
                PreparedStatement ps = c.c.prepareStatement(query);
                ps.setInt(1, selectedNoticeNo);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Notice deleted successfully!");
                noticeInputArea.setText("");
                selectedNoticeNo = -1;
                loadNotices();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void loadNotices() {
        try {
            Conn c = new Conn();
            String query = "SELECT notice_no, date, notice FROM notices ORDER BY notice_no DESC";
            ResultSet rs = c.s.executeQuery(query);

            Vector<String> columnNames = new Vector<>();
            columnNames.add("Notice No");
            columnNames.add("Date");
            columnNames.add("Notice");

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("notice_no"));
                row.add(rs.getDate("date").toString());
                row.add(rs.getString("notice"));
                data.add(row);
            }

            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            noticeTable.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading notices: " + e.getMessage());
        }
    }

    private void loadNoticesByDate(String date) {
        try {
            Conn c = new Conn();
            String query = "SELECT notice_no, date, notice FROM notices WHERE date = ? ORDER BY notice_no DESC";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setDate(1, java.sql.Date.valueOf(date));
            ResultSet rs = ps.executeQuery();

            Vector<String> columnNames = new Vector<>();
            columnNames.add("Notice No");
            columnNames.add("Date");
            columnNames.add("Notice");

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("notice_no"));
                row.add(rs.getDate("date").toString());
                row.add(rs.getString("notice"));
                data.add(row);
            }

            if (data.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No notices found for date: " + date);
            }

            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            noticeTable.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading notices for date: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new AdminNoticeBoard();
    }
}
*/

package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.time.LocalDate;
import java.util.Vector;

public class AdminNoticeBoard extends JFrame {
    private JTextArea noticeInputArea;
    private JTable noticeTable;
    private JButton addBtn, updateBtn, deleteBtn, backBtn;
    private int selectedNoticeNo = -1;

    private JTextField dateSearchField;
    private JButton dateSearchBtn, showAllBtn;

    public AdminNoticeBoard() {
        setTitle("Admin Notice Board");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Heading with color and center alignment
        JLabel heading = new JLabel("Admin Notice Board", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(new Color(60, 90, 153));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(heading, BorderLayout.NORTH);

        // --- Search Panel (Right below the heading) ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel searchLabel = new JLabel("Search by Date (yyyy-mm-dd):");
        searchLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        searchPanel.add(searchLabel);

        dateSearchField = new JTextField(12);
        dateSearchField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        searchPanel.add(dateSearchField);

        dateSearchBtn = createRoundedButton("Search", new Color(52, 152, 219));
        showAllBtn = createRoundedButton("Show All", new Color(39, 174, 96));
        
        searchPanel.add(dateSearchBtn);
        searchPanel.add(showAllBtn);

        // --- Main Center Panel (Contains input area and table) ---
        JPanel mainContentPanel = new JPanel(new BorderLayout(10, 10));
        mainContentPanel.setOpaque(false);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Notice Input and Buttons panel (top of mainContentPanel)
        JPanel noticeInputPanel = new JPanel(new BorderLayout(10, 10));
        noticeInputPanel.setOpaque(false);

        noticeInputArea = new JTextArea(5, 50);
        noticeInputArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        // Changed "Write or Edit Notice" to "Create Notice"
        noticeInputArea.setBorder(BorderFactory.createTitledBorder("Create Notice")); 
        JScrollPane scroll = new JScrollPane(noticeInputArea);
        noticeInputPanel.add(scroll, BorderLayout.CENTER);

        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        actionButtonPanel.setOpaque(false);

        addBtn = createRoundedButton("Add Notice", new Color(52, 152, 219));
        updateBtn = createRoundedButton("Update Notice", new Color(241, 196, 15));
        deleteBtn = createRoundedButton("Delete Notice", new Color(231, 76, 60));
        
        actionButtonPanel.add(addBtn);
        actionButtonPanel.add(updateBtn);
        actionButtonPanel.add(deleteBtn);
        
        noticeInputPanel.add(actionButtonPanel, BorderLayout.SOUTH);
        
        mainContentPanel.add(noticeInputPanel, BorderLayout.NORTH);


        // Table to display notices (center of mainContentPanel)
        noticeTable = new JTable();
        noticeTable.setRowHeight(30);
        noticeTable.setFont(new Font("SansSerif", Font.PLAIN, 16));
        noticeTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        noticeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        noticeTable.getTableHeader().setBackground(new Color(60, 90, 153));
        noticeTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane tableScroll = new JScrollPane(noticeTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("All Notices"));
        mainContentPanel.add(tableScroll, BorderLayout.CENTER);

        // --- Back button panel (at the very bottom) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        backBtn = createRoundedButton("Back", new Color(192, 57, 43));
        bottomPanel.add(backBtn);

        // Add panels to the frame
        JPanel mainLayoutPanel = new JPanel(new BorderLayout());
        mainLayoutPanel.add(searchPanel, BorderLayout.NORTH);
        mainLayoutPanel.add(mainContentPanel, BorderLayout.CENTER);
        add(mainLayoutPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);


        // Event Listeners
        addBtn.addActionListener(e -> addNotice());
        updateBtn.addActionListener(e -> updateNotice());
        deleteBtn.addActionListener(e -> deleteNotice());
        backBtn.addActionListener(e -> {
            setVisible(false);
            new Home();
        });
        dateSearchBtn.addActionListener(e -> {
            String dateStr = dateSearchField.getText().trim();
            if (dateStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a date in yyyy-mm-dd format.");
                return;
            }
            loadNoticesByDate(dateStr);
        });
        showAllBtn.addActionListener(e -> {
            dateSearchField.setText("");
            loadNotices();
        });

        noticeTable.getSelectionModel().addListSelectionListener(e -> {
            int row = noticeTable.getSelectedRow();
            if (row != -1) {
                selectedNoticeNo = (int) noticeTable.getValueAt(row, 0); // notice_no
                String selectedNotice = (String) noticeTable.getValueAt(row, 2); // notice text
                noticeInputArea.setText(selectedNotice);
            }
        });

        loadNotices();
        setVisible(true);
    }
    
    // Custom rounded button class
    private static class RoundedButton extends JButton {
        private Color bgColor;

        public RoundedButton(String text, Color bgColor, Font font, Dimension size) {
            super(text);
            this.bgColor = bgColor;
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(font);
            setPreferredSize(size);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBackground(bgColor.darker());
                }
                public void mouseExited(MouseEvent e) {
                    setBackground(bgColor);
                }
            });
        }
        
        @Override
        public void setBackground(Color bg) {
            this.bgColor = bg;
            super.setBackground(bg);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
            super.paintComponent(g);
            g2.dispose();
        }
    }
    
    private JButton createRoundedButton(String text, Color bgColor) {
        JButton btn = new RoundedButton(text, bgColor, new Font("SansSerif", Font.BOLD, 15), new Dimension(150, 40));
        return btn;
    }


    private void addNotice() {
        String noticeText = noticeInputArea.getText().trim();
        if (noticeText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Notice cannot be empty!");
            return;
        }

        try {
            Conn c = new Conn();
            String query = "INSERT INTO notices (notice, date) VALUES (?, ?)";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, noticeText);
            ps.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Notice added successfully!");
            noticeInputArea.setText("");
            loadNotices();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateNotice() {
        if (selectedNoticeNo == -1) {
            JOptionPane.showMessageDialog(this, "Please select a notice to update!");
            return;
        }

        String updatedText = noticeInputArea.getText().trim();
        if (updatedText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Updated notice cannot be empty!");
            return;
        }

        try {
            Conn c = new Conn();
            String query = "UPDATE notices SET notice=? WHERE notice_no=?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, updatedText);
            ps.setInt(2, selectedNoticeNo);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Notice updated successfully!");
            noticeInputArea.setText("");
            selectedNoticeNo = -1;
            loadNotices();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteNotice() {
        if (selectedNoticeNo == -1) {
            JOptionPane.showMessageDialog(this, "Please select a notice to delete!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this notice?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Conn c = new Conn();
                String query = "DELETE FROM notices WHERE notice_no=?";
                PreparedStatement ps = c.c.prepareStatement(query);
                ps.setInt(1, selectedNoticeNo);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Notice deleted successfully!");
                noticeInputArea.setText("");
                selectedNoticeNo = -1;
                loadNotices();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void loadNotices() {
        try {
            Conn c = new Conn();
            String query = "SELECT notice_no, date, notice FROM notices ORDER BY notice_no DESC";
            ResultSet rs = c.s.executeQuery(query);

            Vector<String> columnNames = new Vector<>();
            columnNames.add("Notice No");
            columnNames.add("Date");
            columnNames.add("Notice");

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("notice_no"));
                row.add(rs.getDate("date").toString());
                row.add(rs.getString("notice"));
                data.add(row);
            }

            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            noticeTable.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading notices: " + e.getMessage());
        }
    }

    private void loadNoticesByDate(String date) {
        try {
            Conn c = new Conn();
            String query = "SELECT notice_no, date, notice FROM notices WHERE date = ? ORDER BY notice_no DESC";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setDate(1, java.sql.Date.valueOf(date));
            ResultSet rs = ps.executeQuery();

            Vector<String> columnNames = new Vector<>();
            columnNames.add("Notice No");
            columnNames.add("Date");
            columnNames.add("Notice");

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("notice_no"));
                row.add(rs.getDate("date").toString());
                row.add(rs.getString("notice"));
                data.add(row);
            }

            if (data.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No notices found for date: " + date);
            }

            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            noticeTable.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading notices for date: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new AdminNoticeBoard();
    }
}
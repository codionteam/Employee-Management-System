/*package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.sql.*;

public class LoanStatusDialog extends JFrame {
    String empId;
    JTable table;
    DefaultTableModel model;
    JTextField dateField;
    JLabel totalLabel;
    Conn c;
    boolean isUpdating = false;

    public LoanStatusDialog(String empId) {
        this.empId = empId;
        setTitle("Loan Request Status");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(230, 245, 255));
        setLayout(new BorderLayout());

        JLabel heading = new JLabel("Loan Request Status", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(new Color(0, 102, 204));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(heading, BorderLayout.NORTH);

        // Top panel with Date, Search, Show All, Back buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBackground(new Color(230, 245, 255));

        topPanel.add(new JLabel("Date:"));

        dateField = new JTextField(10);
        dateField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        dateField.setToolTipText("Format: YYYY-MM-DD");
        topPanel.add(dateField);

        JButton searchBtn = new JButton("Search by Date");
        searchBtn.setBackground(new Color(0, 153, 255));
        searchBtn.setForeground(Color.WHITE);
        topPanel.add(searchBtn);

        JButton showAllBtn = new JButton("Show All");
        showAllBtn.setBackground(new Color(40, 167, 69));
        showAllBtn.setForeground(Color.WHITE);
        topPanel.add(showAllBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
        backBtn.setBackground(new Color(220, 20, 60)); // crimson red
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> this.dispose());
        topPanel.add(backBtn);  // Show All এর পরে back button

        add(topPanel, BorderLayout.BEFORE_FIRST_LINE);

        String[] columns = {"ID", "Amount", "Purpose", "Status", "Request Date", "Withdraw Status"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                String status = (String) getValueAt(row, 3);
                return column == 5 && !"Rejected".equalsIgnoreCase(status);
            }
        };

        table = new JTable(model);
        table.setFont(new Font("Tahoma", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 16));
        table.setGridColor(Color.LIGHT_GRAY);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        totalLabel = new JLabel("Total Approved Withdraw Amount: 0.0", JLabel.CENTER);
        totalLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        totalLabel.setOpaque(true);
        totalLabel.setBackground(new Color(30, 144, 255)); // dodger blue
        totalLabel.setForeground(Color.WHITE);
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(totalLabel, BorderLayout.SOUTH);

        setupEditor();

        searchBtn.addActionListener(e -> loadData(dateField.getText().trim()));
        showAllBtn.addActionListener(e -> loadData(null));

        loadData(null);

        setVisible(true);
    }

    private void loadData(String date) {
        try {
            model.setRowCount(0);
            c = new Conn();

            PreparedStatement autoUpdateRejected = c.c.prepareStatement(
                "UPDATE loan_requests SET withdraw_status = 'No' WHERE emp_id = ? AND status = 'Rejected'"
            );
            autoUpdateRejected.setString(1, empId);
            autoUpdateRejected.executeUpdate();

            String query = "SELECT id, amount, purpose, status, request_date, withdraw_status FROM loan_requests WHERE emp_id = ?";
            if (date != null && !date.isEmpty()) {
                query += " AND request_date = ?";
            }

            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, empId);
            if (date != null && !date.isEmpty()) {
                ps.setString(2, date);
            }

            ResultSet rs = ps.executeQuery();
            double totalApproved = 0.0;
            while (rs.next()) {
                String status = rs.getString("status");
                String withdraw = rs.getString("withdraw_status");
                double amount = rs.getDouble("amount");

                if ("Approved".equalsIgnoreCase(status) && "Yes".equalsIgnoreCase(withdraw)) {
                    totalApproved += amount;
                }

                model.addRow(new Object[]{
                    rs.getInt("id"),
                    String.format("%.2f", amount),
                    rs.getString("purpose"),
                    status,
                    rs.getDate("request_date").toString(),
                    withdraw != null ? withdraw : "Pending"
                });
            }
            totalLabel.setText("Total Approved Withdraw Amount: " + String.format("%.2f", totalApproved));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    private void setupEditor() {
        JComboBox<String> combo = new JComboBox<>(new String[]{"Yes", "No", "Cancel"});
        table.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(combo));

        model.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (isUpdating) return;

                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 5) {
                    isUpdating = true;
                    int row = e.getFirstRow();
                    int id = (int) model.getValueAt(row, 0);
                    String newStatus = (String) model.getValueAt(row, 5);
                    String approvalStatus = (String) model.getValueAt(row, 3);

                    try {
                        PreparedStatement check = c.c.prepareStatement(
                            "SELECT withdraw_status FROM loan_requests WHERE id = ?"
                        );
                        check.setInt(1, id);
                        ResultSet r = check.executeQuery();

                        if (r.next()) {
                            String oldStatus = r.getString("withdraw_status");

                            if ("Rejected".equalsIgnoreCase(approvalStatus)) {
                                JOptionPane.showMessageDialog(null, "Rejected loans cannot be changed. Withdraw Status is 'No'.");
                                if (!"No".equalsIgnoreCase(oldStatus)) {
                                    model.setValueAt("No", row, 5);
                                }
                            } else if ("Pending".equalsIgnoreCase(approvalStatus)) {
                                JOptionPane.showMessageDialog(null, "Pending loans cannot be updated.");
                                if (!oldStatus.equals(newStatus)) {
                                    model.setValueAt(oldStatus, row, 5);
                                }
                            } else if ("Approved".equalsIgnoreCase(approvalStatus)) {
                                if (!"Pending".equalsIgnoreCase(oldStatus)) {
                                    JOptionPane.showMessageDialog(null, "Withdraw status can only be changed once.");
                                    if (!oldStatus.equals(newStatus)) {
                                        model.setValueAt(oldStatus, row, 5);
                                    }
                                } else {
                                    PreparedStatement update = c.c.prepareStatement(
                                        "UPDATE loan_requests SET withdraw_status = ? WHERE id = ?"
                                    );
                                    update.setString(1, newStatus);
                                    update.setInt(2, id);
                                    update.executeUpdate();
                                }
                            } else {
                                PreparedStatement update = c.c.prepareStatement(
                                    "UPDATE loan_requests SET withdraw_status = ? WHERE id = ?"
                                );
                                update.setString(1, newStatus);
                                update.setInt(2, id);
                                update.executeUpdate();
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        isUpdating = false;
                        loadData(null);
                    }
                }
            }
        });
    }
}
*//*
package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.sql.*;

public class LoanStatusDialog extends JFrame {
    String empId;
    JTable table;
    DefaultTableModel model;
    JTextField dateField;
    JLabel totalLabel;
    Conn c;
    boolean isUpdating = false;

    public LoanStatusDialog(String empId) {
        this.empId = empId;
        setTitle("Loan Request Status");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(230, 245, 255));
        setLayout(new BorderLayout());

        // Header Panel with heading
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Loan Request Status", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(new Color(60, 90, 153));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        headerPanel.add(heading, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Top panel with Date, Search, Show All, Back buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBackground(new Color(230, 245, 255));

        topPanel.add(new JLabel("Date:"));

        dateField = new JTextField(10);
        dateField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        dateField.setToolTipText("Format: YYYY-MM-DD");
        topPanel.add(dateField);

        JButton searchBtn = new JButton("Search by Date");
        searchBtn.setBackground(new Color(0, 153, 255));
        searchBtn.setForeground(Color.WHITE);
        topPanel.add(searchBtn);

        JButton showAllBtn = new JButton("Show All");
        showAllBtn.setBackground(new Color(40, 167, 69));
        showAllBtn.setForeground(Color.WHITE);
        topPanel.add(showAllBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
        backBtn.setBackground(new Color(220, 20, 60)); // crimson red
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> this.dispose());
        topPanel.add(backBtn); // Show All এর পরে back button

        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Amount", "Purpose", "Status", "Request Date", "Withdraw Status"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                String status = (String) getValueAt(row, 3);
                return column == 5 && !"Rejected".equalsIgnoreCase(status);
            }
        };

        table = new JTable(model);
        table.setFont(new Font("Tahoma", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(52, 152, 219)); // Table header color
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(Color.LIGHT_GRAY);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        totalLabel = new JLabel("Total Approved Withdraw Amount: 0.0", JLabel.CENTER);
        totalLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        totalLabel.setOpaque(true);
        totalLabel.setBackground(new Color(30, 144, 255)); // dodger blue
        totalLabel.setForeground(Color.WHITE);
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(totalLabel, BorderLayout.SOUTH);

        setupEditor();

        searchBtn.addActionListener(e -> loadData(dateField.getText().trim()));
        showAllBtn.addActionListener(e -> loadData(null));

        loadData(null);

        setVisible(true);
    }

    private void loadData(String date) {
        try {
            model.setRowCount(0);
            c = new Conn();

            PreparedStatement autoUpdateRejected = c.c.prepareStatement(
                "UPDATE loan_requests SET withdraw_status = 'No' WHERE emp_id = ? AND status = 'Rejected'"
            );
            autoUpdateRejected.setString(1, empId);
            autoUpdateRejected.executeUpdate();

            String query = "SELECT id, amount, purpose, status, request_date, withdraw_status FROM loan_requests WHERE emp_id = ?";
            if (date != null && !date.isEmpty()) {
                query += " AND request_date = ?";
            }

            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, empId);
            if (date != null && !date.isEmpty()) {
                ps.setString(2, date);
            }

            ResultSet rs = ps.executeQuery();
            double totalApproved = 0.0;
            while (rs.next()) {
                String status = rs.getString("status");
                String withdraw = rs.getString("withdraw_status");
                double amount = rs.getDouble("amount");

                if ("Approved".equalsIgnoreCase(status) && "Yes".equalsIgnoreCase(withdraw)) {
                    totalApproved += amount;
                }

                model.addRow(new Object[]{
                    rs.getInt("id"),
                    String.format("%.2f", amount),
                    rs.getString("purpose"),
                    status,
                    rs.getDate("request_date").toString(),
                    withdraw != null ? withdraw : "Pending"
                });
            }
            totalLabel.setText("Total Approved Withdraw Amount: " + String.format("%.2f", totalApproved));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    private void setupEditor() {
        JComboBox<String> combo = new JComboBox<>(new String[]{"Yes", "No", "Cancel"});
        table.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(combo));

        model.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (isUpdating) return;

                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 5) {
                    isUpdating = true;
                    int row = e.getFirstRow();
                    int id = (int) model.getValueAt(row, 0);
                    String newStatus = (String) model.getValueAt(row, 5);
                    String approvalStatus = (String) model.getValueAt(row, 3);

                    try {
                        PreparedStatement check = c.c.prepareStatement(
                            "SELECT withdraw_status FROM loan_requests WHERE id = ?"
                        );
                        check.setInt(1, id);
                        ResultSet r = check.executeQuery();

                        if (r.next()) {
                            String oldStatus = r.getString("withdraw_status");

                            if ("Rejected".equalsIgnoreCase(approvalStatus)) {
                                JOptionPane.showMessageDialog(null, "Rejected loans cannot be changed. Withdraw Status is 'No'.");
                                if (!"No".equalsIgnoreCase(oldStatus)) {
                                    model.setValueAt("No", row, 5);
                                }
                            } else if ("Pending".equalsIgnoreCase(approvalStatus)) {
                                JOptionPane.showMessageDialog(null, "Pending loans cannot be updated.");
                                if (!oldStatus.equals(newStatus)) {
                                    model.setValueAt(oldStatus, row, 5);
                                }
                            } else if ("Approved".equalsIgnoreCase(approvalStatus)) {
                                if (!"Pending".equalsIgnoreCase(oldStatus)) {
                                    JOptionPane.showMessageDialog(null, "Withdraw status can only be changed once.");
                                    if (!oldStatus.equals(newStatus)) {
                                        model.setValueAt(oldStatus, row, 5);
                                    }
                                } else {
                                    PreparedStatement update = c.c.prepareStatement(
                                        "UPDATE loan_requests SET withdraw_status = ? WHERE id = ?"
                                    );
                                    update.setString(1, newStatus);
                                    update.setInt(2, id);
                                    update.executeUpdate();
                                }
                            } else {
                                PreparedStatement update = c.c.prepareStatement(
                                    "UPDATE loan_requests SET withdraw_status = ? WHERE id = ?"
                                );
                                update.setString(1, newStatus);
                                update.setInt(2, id);
                                update.executeUpdate();
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        isUpdating = false;
                        loadData(null);
                    }
                }
            }
        });
    }
}*/

package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class LoanStatusDialog extends JFrame {
    String empId;
    JTable table;
    DefaultTableModel model;
    JTextField dateField;
    JLabel totalLabel;
    Conn c;
    boolean isUpdating = false;

    // Custom JButton with rounded corners and hover effect
    private static class RoundedButton extends JButton {
        private Color baseColor;
        private final Dimension preferredSize = new Dimension(150, 40);

        public RoundedButton(String text, Color bgColor) {
            super(text);
            this.baseColor = bgColor;
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setBackground(baseColor);
            setForeground(Color.WHITE);
            setFont(new Font("Tahoma", Font.BOLD, 16));
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
        public Dimension getPreferredSize() {
            return preferredSize;
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

    public LoanStatusDialog(String empId) {
        this.empId = empId;
        setTitle("Loan Request Status");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(230, 245, 255));
        setLayout(new BorderLayout());

        // Header Panel with heading and button controls
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setOpaque(false);

        JLabel heading = new JLabel("Loan Request Status", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(new Color(60, 90, 153));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        topContainer.add(heading, BorderLayout.NORTH);

        // Top panel with Date, Search, Show All, Back buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBackground(new Color(230, 245, 255));

        topPanel.add(new JLabel("Date:"));

        dateField = new JTextField(10);
        dateField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        dateField.setToolTipText("Format: YYYY-MM-DD");
        topPanel.add(dateField);

        JButton searchBtn = createRoundedButton("Search by Date", new Color(0, 153, 255));
        topPanel.add(searchBtn);

        JButton showAllBtn = createRoundedButton("Show All", new Color(40, 167, 69));
        topPanel.add(showAllBtn);

        JButton backBtn = createRoundedButton("Back", new Color(220, 20, 60)); // crimson red
        backBtn.addActionListener(e -> this.dispose());
        topPanel.add(backBtn);

        topContainer.add(topPanel, BorderLayout.CENTER);
        add(topContainer, BorderLayout.NORTH);

        String[] columns = {"ID", "Amount", "Purpose", "Status", "Request Date", "Withdraw Status"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                String status = (String) getValueAt(row, 3);
                return column == 5 && !"Rejected".equalsIgnoreCase(status);
            }
        };

        table = new JTable(model);
        table.setFont(new Font("Tahoma", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(52, 152, 219)); // Table header color
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(Color.LIGHT_GRAY);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        totalLabel = new JLabel("Total Approved Withdraw Amount: 0.0", JLabel.CENTER);
        totalLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        totalLabel.setOpaque(true);
        totalLabel.setBackground(new Color(30, 144, 255)); // dodger blue
        totalLabel.setForeground(Color.WHITE);
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(totalLabel, BorderLayout.SOUTH);

        setupEditor();

        searchBtn.addActionListener(e -> loadData(dateField.getText().trim()));
        showAllBtn.addActionListener(e -> loadData(null));

        loadData(null);

        setVisible(true);
    }
    
    private JButton createRoundedButton(String text, Color bgColor) {
        return new RoundedButton(text, bgColor);
    }

    private void loadData(String date) {
        try {
            model.setRowCount(0);
            c = new Conn();

            PreparedStatement autoUpdateRejected = c.c.prepareStatement(
                "UPDATE loan_requests SET withdraw_status = 'No' WHERE emp_id = ? AND status = 'Rejected'"
            );
            autoUpdateRejected.setString(1, empId);
            autoUpdateRejected.executeUpdate();

            String query = "SELECT id, amount, purpose, status, request_date, withdraw_status FROM loan_requests WHERE emp_id = ?";
            if (date != null && !date.isEmpty()) {
                query += " AND request_date = ?";
            }

            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, empId);
            if (date != null && !date.isEmpty()) {
                ps.setString(2, date);
            }

            ResultSet rs = ps.executeQuery();
            double totalApproved = 0.0;
            while (rs.next()) {
                String status = rs.getString("status");
                String withdraw = rs.getString("withdraw_status");
                double amount = rs.getDouble("amount");

                if ("Approved".equalsIgnoreCase(status) && "Yes".equalsIgnoreCase(withdraw)) {
                    totalApproved += amount;
                }

                model.addRow(new Object[]{
                    rs.getInt("id"),
                    String.format("%.2f", amount),
                    rs.getString("purpose"),
                    status,
                    rs.getDate("request_date").toString(),
                    withdraw != null ? withdraw : "Pending"
                });
            }
            totalLabel.setText("Total Approved Withdraw Amount: " + String.format("%.2f", totalApproved));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    private void setupEditor() {
        JComboBox<String> combo = new JComboBox<>(new String[]{"Yes", "No", "Cancel"});
        table.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(combo));

        model.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (isUpdating) return;

                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 5) {
                    isUpdating = true;
                    int row = e.getFirstRow();
                    int id = (int) model.getValueAt(row, 0);
                    String newStatus = (String) model.getValueAt(row, 5);
                    String approvalStatus = (String) model.getValueAt(row, 3);

                    try {
                        PreparedStatement check = c.c.prepareStatement(
                            "SELECT withdraw_status FROM loan_requests WHERE id = ?"
                        );
                        check.setInt(1, id);
                        ResultSet r = check.executeQuery();

                        if (r.next()) {
                            String oldStatus = r.getString("withdraw_status");

                            if ("Rejected".equalsIgnoreCase(approvalStatus)) {
                                JOptionPane.showMessageDialog(null, "Rejected loans cannot be changed. Withdraw Status is 'No'.");
                                if (!"No".equalsIgnoreCase(oldStatus)) {
                                    model.setValueAt("No", row, 5);
                                }
                            } else if ("Pending".equalsIgnoreCase(approvalStatus)) {
                                JOptionPane.showMessageDialog(null, "Pending loans cannot be updated.");
                                if (!oldStatus.equals(newStatus)) {
                                    model.setValueAt(oldStatus, row, 5);
                                }
                            } else if ("Approved".equalsIgnoreCase(approvalStatus)) {
                                if (!"Pending".equalsIgnoreCase(oldStatus)) {
                                    JOptionPane.showMessageDialog(null, "Withdraw status can only be changed once.");
                                    if (!oldStatus.equals(newStatus)) {
                                        model.setValueAt(oldStatus, row, 5);
                                    }
                                } else {
                                    PreparedStatement update = c.c.prepareStatement(
                                        "UPDATE loan_requests SET withdraw_status = ? WHERE id = ?"
                                    );
                                    update.setString(1, newStatus);
                                    update.setInt(2, id);
                                    update.executeUpdate();
                                }
                            } else {
                                PreparedStatement update = c.c.prepareStatement(
                                    "UPDATE loan_requests SET withdraw_status = ? WHERE id = ?"
                                );
                                update.setString(1, newStatus);
                                update.setInt(2, id);
                                update.executeUpdate();
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        isUpdating = false;
                        loadData(null);
                    }
                }
            }
        });
    }
}
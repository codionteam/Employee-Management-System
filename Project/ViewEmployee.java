/*package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;

public class ViewEmployee extends JFrame implements ActionListener {

    JTable table;
    Choice cemployeeId;
    JButton search, showAll, print, update, back;

    ViewEmployee() {
        setTitle("View Employee");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(15, 15));

        // ---------- Top Panel ----------
        
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(new Color(245, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);

        JLabel heading = new JLabel("🔍 Search by Employee ID:");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        heading.setForeground(new Color(0, 70, 140));
        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(heading, gbc);

        cemployeeId = new Choice();
        cemployeeId.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT empId FROM employee ORDER BY empId ASC"); // Sorted
            while (rs.next()) {
                cemployeeId.add(rs.getString("empId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        gbc.gridx = 1;
        topPanel.add(cemployeeId, gbc);

        search = createButton("Search", new Color(0, 102, 204));
        gbc.gridx = 2;
        topPanel.add(search, gbc);

        showAll = createButton("Show All", new Color(102, 0, 204));
        gbc.gridx = 3;
        topPanel.add(showAll, gbc);

        print = createButton("Print", new Color(0, 153, 76));
        gbc.gridx = 4;
        topPanel.add(print, gbc);

        update = createButton("Update", new Color(0, 102, 204));
        gbc.gridx = 5;
        topPanel.add(update, gbc);

        back = createButton("Back", new Color(204, 0, 0));
        gbc.gridx = 6;
        topPanel.add(back, gbc);

        add(topPanel, BorderLayout.NORTH);

        // ---------- Table Panel ----------
        table = new JTable();
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240), 1),
                "Employee Details", 0, 0,
                new Font("Segoe UI", Font.BOLD, 16), new Color(0, 102, 204)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Load all data initially
        loadTable();

        setVisible(true);
    }

    private void loadTable() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM employee");
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(110, 35));
        btn.addActionListener(this);
        return btn;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(220, 235, 255));
        table.getTableHeader().setForeground(new Color(0, 102, 204));
        table.setSelectionBackground(new Color(204, 229, 255));
        table.setGridColor(new Color(220, 220, 220));
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        String selectedEmpId = cemployeeId.getSelectedItem();

        if (source == search) {
            try {
                Conn c = new Conn();
                ResultSet rs = c.s.executeQuery("SELECT * FROM employee WHERE empId = '" + selectedEmpId + "'");
                table.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (source == showAll) {
            loadTable();
        } else if (source == print) {
            try {
                table.print();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (source == update) {
            setVisible(false);
            new UpdateEmployee(selectedEmpId);
        } else if (source == back) {
            setVisible(false);
            new Home();
        }
    }

    public static void main(String[] args) {
        new ViewEmployee();
    }
}
*/
/*
package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class ViewEmployee extends JFrame implements ActionListener {

    JTable table;
    Choice cemployeeId;
    JButton search, showAll, print, back;

    // Custom RoundedButton class
    private static class RoundedButton extends JButton {
        private Color backgroundColor;
        private Color foregroundColor;

        public RoundedButton(String text, Color bg, Color fg) {
            super(text);
            backgroundColor = bg;
            foregroundColor = fg;
            setForeground(foregroundColor);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(110, 35));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(backgroundColor.darker());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(backgroundColor);
                }
            });
        }
        
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(110, 40);
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

    ViewEmployee() {
        setTitle("View Staff Details");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 250, 255));
        setLayout(new BorderLayout(15, 15));

        // ---------- Heading Panel ----------
        JPanel headingPanel = new JPanel();
        headingPanel.setBackground(new Color(0, 102, 204));
        JLabel mainHeading = new JLabel("Staff Details", SwingConstants.CENTER);
        mainHeading.setFont(new Font("Segoe UI", Font.BOLD, 42));
        mainHeading.setForeground(Color.WHITE);
        mainHeading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        headingPanel.add(mainHeading);
        add(headingPanel, BorderLayout.NORTH);
        
        // ---------- Control Panel (Search, Show All) ----------
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(new Color(245, 250, 255));

        JLabel searchLabel = new JLabel("Search by Employee ID:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchLabel.setForeground(new Color(0, 70, 140));
        controlPanel.add(searchLabel);

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
        controlPanel.add(cemployeeId);

        search = createButton("Search", new Color(0, 102, 204));
        controlPanel.add(search);

        showAll = createButton("Show All", new Color(102, 0, 204));
        controlPanel.add(showAll);

        add(controlPanel, BorderLayout.NORTH);

        // This is a temporary panel to place the search and showAll buttons below the main heading.
        // It will be replaced by the code below which correctly places the heading and buttons.
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(headingPanel, BorderLayout.NORTH);
        topContainer.add(controlPanel, BorderLayout.CENTER);
        add(topContainer, BorderLayout.NORTH);

        // ---------- Table Panel ----------
        table = new JTable();
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // ---------- Bottom Panel (Print, Back) ----------
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(new Color(245, 250, 255));
        print = createButton("Print", new Color(0, 153, 76));
        back = createButton("Back", new Color(204, 0, 0));
        bottomPanel.add(print);
        bottomPanel.add(back);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load all data initially
        loadTable();

        setVisible(true);
    }

    private void loadTable() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM employee");
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new RoundedButton(text, bgColor, Color.WHITE);
        btn.addActionListener(this);
        return btn;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(220, 235, 255));
        table.getTableHeader().setForeground(new Color(0, 102, 204));
        table.setSelectionBackground(new Color(204, 229, 255));
        table.setGridColor(new Color(220, 220, 220));
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        String selectedEmpId = cemployeeId.getSelectedItem();

        if (source == search) {
            try {
                Conn c = new Conn();
                ResultSet rs = c.s.executeQuery("SELECT * FROM employee WHERE empId = '" + selectedEmpId + "'");
                table.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (source == showAll) {
            loadTable();
        } else if (source == print) {
            try {
                table.print();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (source == back) {
            setVisible(false);
            new Home();
        }
    }

    public static void main(String[] args) {
        new ViewEmployee();
    }
}
*/
/*
package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class ViewEmployee extends JFrame implements ActionListener {

    JTable table;
    Choice cemployeeId;
    JButton search, showAll, print, back;

    // Custom RoundedButton class
    private static class RoundedButton extends JButton {
        private Color backgroundColor;

        public RoundedButton(String text, Color bg) {
            super(text);
            backgroundColor = bg;
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(110, 40));
            setBackground(backgroundColor); // Set the background color
        }
        
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(110, 40);
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

    ViewEmployee() {
        setTitle("View Staff Details");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 250, 255));
        setLayout(new BorderLayout(15, 15));

        // ---------- Heading Panel ----------
        JPanel headingPanel = new JPanel();
        headingPanel.setBackground(new Color(0, 102, 204));
        JLabel mainHeading = new JLabel("Staff Details", SwingConstants.CENTER);
        mainHeading.setFont(new Font("Segoe UI", Font.BOLD, 42));
        mainHeading.setForeground(Color.WHITE);
        mainHeading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        headingPanel.add(mainHeading);
        
        // ---------- Control Panel (Search, Show All) ----------
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(new Color(245, 250, 255));

        JLabel searchLabel = new JLabel("Search by Employee ID:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchLabel.setForeground(new Color(0, 70, 140));
        controlPanel.add(searchLabel);

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
        controlPanel.add(cemployeeId);

        search = createButton("Search", new Color(40, 167, 69)); // Different color for search
        controlPanel.add(search);

        showAll = createButton("Show All", new Color(0, 153, 255)); // Different color for show all
        controlPanel.add(showAll);
        
        // Combine heading and control panel
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(headingPanel, BorderLayout.NORTH);
        topContainer.add(controlPanel, BorderLayout.CENTER);
        add(topContainer, BorderLayout.NORTH);

        // ---------- Table Panel ----------
        table = new JTable();
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // ---------- Bottom Panel (Print, Back) ----------
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(new Color(245, 250, 255));
        print = createButton("Print", new Color(102, 0, 204));
        back = createButton("Back", new Color(204, 0, 0));
        bottomPanel.add(print);
        bottomPanel.add(back);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load all data initially
        loadTable();

        setVisible(true);
    }

    private void loadTable() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM employee");
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new RoundedButton(text, bgColor);
        btn.addActionListener(this);
        return btn;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(220, 235, 255));
        table.getTableHeader().setForeground(new Color(0, 102, 204));
        table.setSelectionBackground(new Color(204, 229, 255));
        table.setGridColor(new Color(220, 220, 220));
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        String selectedEmpId = cemployeeId.getSelectedItem();

        if (source == search) {
            try {
                Conn c = new Conn();
                ResultSet rs = c.s.executeQuery("SELECT * FROM employee WHERE empId = '" + selectedEmpId + "'");
                table.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (source == showAll) {
            loadTable();
        } else if (source == print) {
            try {
                table.print();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (source == back) {
            setVisible(false);
            new Home();
        }
    }

    public static void main(String[] args) {
        new ViewEmployee();
    }
}*/

package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class ViewEmployee extends JFrame implements ActionListener {

    JTable table;
    Choice cemployeeId;
    JButton search, showAll, print, back;

    // Custom RoundedButton class
    private static class RoundedButton extends JButton {
        private Color backgroundColor;

        public RoundedButton(String text, Color bg) {
            super(text);
            backgroundColor = bg;
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(110, 40));
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
        public Dimension getPreferredSize() {
            return new Dimension(110, 40);
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

    ViewEmployee() {
        setTitle("View Staff Details");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 250, 255));
        setLayout(new BorderLayout(15, 15));

        // ---------- Heading Panel ----------
        JPanel headingPanel = new JPanel();
        headingPanel.setBackground(new Color(0, 102, 204));
        JLabel mainHeading = new JLabel("Staff Details", SwingConstants.CENTER);
        mainHeading.setFont(new Font("Segoe UI", Font.BOLD, 42));
        mainHeading.setForeground(Color.WHITE);
        mainHeading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        headingPanel.add(mainHeading);

        // ---------- Control Panel (Search, Show All) ----------
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(new Color(245, 250, 255));

        JLabel searchLabel = new JLabel("Search by Employee ID:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchLabel.setForeground(new Color(0, 70, 140));
        controlPanel.add(searchLabel);

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
        controlPanel.add(cemployeeId);

        search = createButton("Search", new Color(40, 167, 69)); // Different color for search
        controlPanel.add(search);

        showAll = createButton("Show All", new Color(0, 153, 255)); // Different color for show all
        controlPanel.add(showAll);

        // Combine heading and control panel
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(headingPanel, BorderLayout.NORTH);
        topContainer.add(controlPanel, BorderLayout.CENTER);
        add(topContainer, BorderLayout.NORTH);

        // ---------- Table Panel ----------
        table = new JTable();
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // ---------- Bottom Panel (Print, Back) ----------
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(new Color(245, 250, 255));
        print = createButton("Print", new Color(102, 0, 204));
        back = createButton("Back", new Color(204, 0, 0));
        bottomPanel.add(print);
        bottomPanel.add(back);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load all data initially
        loadTable();

        setVisible(true);
    }

    private void loadTable() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM employee");
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new RoundedButton(text, bgColor);
        btn.addActionListener(this);
        return btn;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(220, 235, 255));
        table.getTableHeader().setForeground(new Color(0, 102, 204));
        table.setSelectionBackground(new Color(204, 229, 255));
        table.setGridColor(new Color(220, 220, 220));
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        String selectedEmpId = cemployeeId.getSelectedItem();

        if (source == search) {
            try {
                Conn c = new Conn();
                ResultSet rs = c.s.executeQuery("SELECT * FROM employee WHERE empId = '" + selectedEmpId + "'");
                table.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (source == showAll) {
            loadTable();
        } else if (source == print) {
            try {
                table.print();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (source == back) {
            setVisible(false);
            new Home();
        }
    }

    public static void main(String[] args) {
        new ViewEmployee();
    }
}

/*
package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class ViewEmployee extends JFrame implements ActionListener {

    JTable table;
    Choice cemployeeId;
    JButton search, showAll, print, update, back; // 'update' বাটন যুক্ত করা হয়েছে

    // Custom RoundedButton class
    private static class RoundedButton extends JButton {
        private Color backgroundColor;

        public RoundedButton(String text, Color bg) {
            super(text);
            backgroundColor = bg;
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(110, 40));
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
        public Dimension getPreferredSize() {
            return new Dimension(110, 40);
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

    ViewEmployee() {
        setTitle("View Staff Details");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 250, 255));
        setLayout(new BorderLayout(15, 15));

        // ---------- Heading Panel ----------
        JPanel headingPanel = new JPanel();
        headingPanel.setBackground(new Color(0, 102, 204));
        JLabel mainHeading = new JLabel("Staff Details", SwingConstants.CENTER);
        mainHeading.setFont(new Font("Segoe UI", Font.BOLD, 42));
        mainHeading.setForeground(Color.WHITE);
        mainHeading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        headingPanel.add(mainHeading);

        // ---------- Control Panel (Search, Show All, Update) ----------
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(new Color(245, 250, 255));

        JLabel searchLabel = new JLabel("Search by Employee ID:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchLabel.setForeground(new Color(0, 70, 140));
        controlPanel.add(searchLabel);

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
        controlPanel.add(cemployeeId);

        search = createButton("Search", new Color(40, 167, 69));
        controlPanel.add(search);

        showAll = createButton("Show All", new Color(0, 153, 255));
        controlPanel.add(showAll);

        update = createButton("Update", new Color(255, 153, 0)); // 'Update' বাটন যুক্ত করা হয়েছে
        controlPanel.add(update);

        // Combine heading and control panel
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(headingPanel, BorderLayout.NORTH);
        topContainer.add(controlPanel, BorderLayout.CENTER);
        add(topContainer, BorderLayout.NORTH);

        // ---------- Table Panel ----------
        table = new JTable();
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // ---------- Bottom Panel (Print, Back) ----------
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(new Color(245, 250, 255));
        print = createButton("Print", new Color(102, 0, 204));
        back = createButton("Back", new Color(204, 0, 0));
        bottomPanel.add(print);
        bottomPanel.add(back);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load all data initially
        loadTable();

        setVisible(true);
    }

    private void loadTable() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM employee");
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new RoundedButton(text, bgColor);
        btn.addActionListener(this);
        return btn;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(220, 235, 255));
        table.getTableHeader().setForeground(new Color(0, 102, 204));
        table.setSelectionBackground(new Color(204, 229, 255));
        table.setGridColor(new Color(220, 220, 220));
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        String selectedEmpId = cemployeeId.getSelectedItem();

        if (source == search) {
            try {
                Conn c = new Conn();
                ResultSet rs = c.s.executeQuery("SELECT * FROM employee WHERE empId = '" + selectedEmpId + "'");
                table.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (source == showAll) {
            loadTable();
        } else if (source == print) {
            try {
                table.print();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (source == update) { // 'update' বাটনের অ্যাকশন
            if (selectedEmpId != null && !selectedEmpId.isEmpty()) {
                setVisible(false);
                new UpdateEmployee(selectedEmpId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an employee to update.", "No Employee Selected", JOptionPane.WARNING_MESSAGE);
            }
        } else if (source == back) {
            setVisible(false);
            new Home();
        }
    }

    public static void main(String[] args) {
        new ViewEmployee();
    }
}
*/
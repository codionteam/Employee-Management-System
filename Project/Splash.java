/*package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Splash extends JFrame implements ActionListener {

    private JButton btnAdmin, btnEmployee, btnExit;

    public Splash() {
        setTitle("Digital Staff Record Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // ====== Top Title Panel ======
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(30, 144, 255)); // Blue background
        titlePanel.setPreferredSize(new Dimension(getWidth(), 120));

        JLabel title = new JLabel("Digital Staff Record Management System", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);

        // ====== Center Panel for Admin and Employee Buttons ======
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 60, 30, 60);

        btnAdmin = createStyledButton("Admin", new Color(52, 152, 219));
        btnEmployee = createStyledButton("Staff", new Color(39, 174, 96));

        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(btnAdmin, gbc);

        gbc.gridx = 1;
        centerPanel.add(btnEmployee, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // ====== Bottom Exit Button ======
        JPanel exitPanel = new JPanel();
        exitPanel.setBackground(Color.WHITE);
        exitPanel.setBorder(BorderFactory.createEmptyBorder(30, 200, 60, 200));

        btnExit = new JButton("Exit");
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 26));
        btnExit.setBackground(new Color(192, 57, 43));
        btnExit.setForeground(Color.WHITE);
        btnExit.setFocusPainted(false);
        btnExit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnExit.setPreferredSize(new Dimension(2000, 60));
        btnExit.addActionListener(this);

        // Hover effect
        btnExit.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnExit.setBackground(new Color(155, 44, 32));
            }

            public void mouseExited(MouseEvent e) {
                btnExit.setBackground(new Color(192, 57, 43));
            }
        });

        exitPanel.add(btnExit);
        add(exitPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ====== Reusable Styled Button Method ======
    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 26));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(300, 80));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(baseColor.darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }
        });

        button.addActionListener(this);
        return button;
    }

    // ====== Button Actions ======
    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        if (e.getSource() == btnAdmin) {
            new AdminLogin().setVisible(true);
        } else if (e.getSource() == btnEmployee) {
            new EmployeeLogin().setVisible(true);
        } else if (e.getSource() == btnExit) {
            System.exit(0);
        }
    }

    // ====== Main Method ======
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Splash::new);
    }
}*/
/*
package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Splash extends JFrame implements ActionListener {

    private JButton btnAdmin, btnEmployee, btnExit;

    public Splash() {
        setTitle("Digital Staff Record Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // ====== Top Title Panel ======
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(30, 144, 255)); // Blue background
        titlePanel.setPreferredSize(new Dimension(getWidth(), 120));

        JLabel title = new JLabel("Digital Staff Record Management System", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);

        // ====== Center Panel for Admin and Employee Buttons ======
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 60, 30, 60);

        btnAdmin = createStyledIconButton("Admin Login.png", new Color(52, 152, 219), "Admin");
        btnEmployee = createStyledIconButton("Staff login.png", new Color(39, 174, 96), "Staff");

        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(btnAdmin, gbc);

        gbc.gridx = 1;
        centerPanel.add(btnEmployee, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // ====== Bottom Exit Button ======
        JPanel exitPanel = new JPanel();
        exitPanel.setBackground(Color.WHITE);
        exitPanel.setBorder(BorderFactory.createEmptyBorder(30, 200, 60, 200));

        btnExit = new JButton("Exit");
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 26));
        btnExit.setBackground(new Color(192, 57, 43));
        btnExit.setForeground(Color.WHITE);
        btnExit.setFocusPainted(false);
        btnExit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnExit.setPreferredSize(new Dimension(2000, 60));
        btnExit.addActionListener(this);

        // Hover effect
        btnExit.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnExit.setBackground(new Color(155, 44, 32));
            }

            public void mouseExited(MouseEvent e) {
                btnExit.setBackground(new Color(192, 57, 43));
            }
        });

        exitPanel.add(btnExit);
        add(exitPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ====== Reusable Styled Icon Button Method ======
    private JButton createStyledIconButton(String imagePath, Color baseColor, String labelText) {
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(imagePath));
        Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT);
        ImageIcon scaledIcon = new ImageIcon(img);

        JButton button = new JButton(labelText, scaledIcon);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setFont(new Font("Segoe UI", Font.BOLD, 26));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(300, 250)); // Adjusted size for icon and text
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(baseColor.darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }
        });

        button.addActionListener(this);
        return button;
    }

    // ====== Button Actions ======
    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        if (e.getSource() == btnAdmin) {
            new AdminLogin().setVisible(true);
        } else if (e.getSource() == btnEmployee) {
            new EmployeeLogin().setVisible(true);
        } else if (e.getSource() == btnExit) {
            System.exit(0);
        }
    }

    // ====== Main Method ======
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Splash::new);
    }
}
*/
/*
package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Splash extends JFrame implements ActionListener {

    private JButton btnAdmin, btnEmployee, btnExit;

    public Splash() {
        setTitle("Digital Staff Record Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // ====== Top Title Panel ======
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(30, 144, 255)); // Blue background
        titlePanel.setPreferredSize(new Dimension(getWidth(), 120));

        JLabel title = new JLabel("Digital Staff Record Management System", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);

        // ====== Center Panel for Admin and Employee Buttons ======
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 60, 30, 60);

        btnAdmin = createStyledIconButton("Admin.jpeg");
        btnEmployee = createStyledIconButton("Staff.jpeg");

        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(btnAdmin, gbc);

        gbc.gridx = 1;
        centerPanel.add(btnEmployee, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // ====== Bottom Exit Button ======
        JPanel exitPanel = new JPanel();
        exitPanel.setBackground(Color.WHITE);
        exitPanel.setBorder(BorderFactory.createEmptyBorder(30, 200, 60, 200));

        btnExit = new JButton("Exit");
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 26));
        btnExit.setBackground(new Color(192, 57, 43));
        btnExit.setForeground(Color.WHITE);
        btnExit.setFocusPainted(false);
        btnExit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnExit.setPreferredSize(new Dimension(2000, 60));
        btnExit.addActionListener(this);

        // Hover effect
        btnExit.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnExit.setBackground(new Color(155, 44, 32));
            }

            public void mouseExited(MouseEvent e) {
                btnExit.setBackground(new Color(192, 57, 43));
            }
        });

        exitPanel.add(btnExit);
        add(exitPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ====== Reusable Styled Icon Button Method ======
    private JButton createStyledIconButton(String imagePath) {
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(imagePath));
        Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH); // Larger icon
        ImageIcon scaledIcon = new ImageIcon(img);

        JButton button = new JButton(scaledIcon);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(250, 250)); // Adjusted size for icon
        button.setContentAreaFilled(false); // Make button transparent
        button.setBorder(BorderFactory.createLineBorder(Color.GREEN, 5)); // Add green border

        button.addActionListener(this);
        return button;
    }

    // ====== Button Actions ======
    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        if (e.getSource() == btnAdmin) {
            new AdminLogin().setVisible(true);
        } else if (e.getSource() == btnEmployee) {
            new EmployeeLogin().setVisible(true);
        } else if (e.getSource() == btnExit) {
            System.exit(0);
        }
    }

    // ====== Main Method ======
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Splash::new);
    }
}*/

package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Splash extends JFrame implements ActionListener {

    private JButton btnAdmin, btnEmployee, btnExit;

    public Splash() {
        setTitle("Digital Staff Record Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // ====== Top Title Panel ======
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(30, 144, 255)); // Blue background
        titlePanel.setPreferredSize(new Dimension(getWidth(), 120));

        JLabel title = new JLabel("Financial Staff Record & Payroll Management System", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);

        // ====== Center Panel for Admin and Employee Buttons ======
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 60, 30, 60);

        btnAdmin = createStyledIconButton("Admin.jpeg");
        btnEmployee = createStyledIconButton("Staff.jpeg");

        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(btnAdmin, gbc);

        gbc.gridx = 1;
        centerPanel.add(btnEmployee, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // ====== Bottom Exit Button ======
        JPanel exitPanel = new JPanel();
        exitPanel.setBackground(Color.WHITE);
        exitPanel.setBorder(BorderFactory.createEmptyBorder(30, 200, 60, 200));

        btnExit = new JButton("Exit");
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 26));
        btnExit.setBackground(new Color(192, 57, 43));
        btnExit.setForeground(Color.WHITE);
        btnExit.setFocusPainted(false);
        btnExit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnExit.setPreferredSize(new Dimension(2000, 60));
        btnExit.addActionListener(this);

        // Hover effect
        btnExit.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnExit.setBackground(new Color(155, 44, 32));
            }

            public void mouseExited(MouseEvent e) {
                btnExit.setBackground(new Color(192, 57, 43));
            }
        });

        exitPanel.add(btnExit);
        add(exitPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ====== Reusable Styled Icon Button Method ======
    private JButton createStyledIconButton(String imagePath) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/icons/" + imagePath));
        Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH); // Larger icon
        ImageIcon scaledIcon = new ImageIcon(img);

        JButton button = new JButton(scaledIcon);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(250, 250)); // Adjusted size for icon
        button.setContentAreaFilled(false); // Make button transparent
        button.setBorder(BorderFactory.createLineBorder(Color.GREEN, 5)); // Add green border

        button.addActionListener(this);
        return button;
    }

    // ====== Button Actions ======
    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        if (e.getSource() == btnAdmin) {
            new AdminLogin().setVisible(true);
        } else if (e.getSource() == btnEmployee) {
            new EmployeeLogin().setVisible(true);
        } else if (e.getSource() == btnExit) {
            System.exit(0);
        }
    }

    // ====== Main Method ======
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Splash::new);
    }
}

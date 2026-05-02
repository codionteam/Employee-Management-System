package employee.management.system;

import java.sql.*;

public class ConnEmployee {
    Connection c;
    Statement s;

    public ConnEmployee() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/employeemanagementsystem", "root", "admin");
            s = c.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

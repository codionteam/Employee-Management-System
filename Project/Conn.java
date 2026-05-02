package employee.management.system;
import java.sql.*;

public class Conn {
    public Connection c;
    public Statement s;

    public Conn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/employeemanagementsystem", "root", "admin"); // password might be "allah" if you set one
            s = c.createStatement();
            System.out.println("Connected to database!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


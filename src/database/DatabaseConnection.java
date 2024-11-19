package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    static final String DB_URL = "jdbc:mysql://localhost:3306/shopee_pink";
    static final String USER = "root";
    static final String PASS = "140705";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            // System.out.println("Terkoneksi");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void tes() {
        System.out.println();
    }
}

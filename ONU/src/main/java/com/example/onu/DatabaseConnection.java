package com.example.onu;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Registrado correctamente");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL no encontrado:");
            e.printStackTrace();
        }
    }
    // -----------------------------------------------------------------------
    public static Connection getConnection() throws SQLException {

        String url = "jdbc:mysql://localhost:3306/iweb_proy?serverTimezone=UTC";
        String user = "root";
        String password = "root";
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(" Conexión exitosa. ");
        return conn;
    }
}

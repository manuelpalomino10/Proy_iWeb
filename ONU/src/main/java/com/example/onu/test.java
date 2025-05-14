package com.example.onu;

import java.sql.Connection;
import java.sql.SQLException;

public class test {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("¡Conexión exitosa!");
        } catch (SQLException e) {
            System.err.println("Error al conectar:");
            e.printStackTrace();
        }
    }
}

package com.example.onumujeres.daos;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

public class BaseDAO {

    public Connection getConnection() throws SQLException {

        try {
            Class.forName("com.mysql.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String URL = "jdbc:mysql://localhost:3306/bd_proy_onumujeres";
        String USER = "root";
        String PASSWORD = "root";
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
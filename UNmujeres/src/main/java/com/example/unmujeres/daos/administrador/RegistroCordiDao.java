package com.example.unmujeres.daos.administrador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistroCordiDao extends BaseDao {
    public void nuevoCordi(String nombres, String apellidos, int dni, String correo, int idZona) {

        String sql = "INSERT INTO usuario (nombres, apellidos, DNI, correo, contraseña, estado, idroles, fecha_incorporacion, idzona) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombres);
            ps.setString(2, apellidos);
            ps.setInt(3, dni);
            ps.setString(4, correo);

            ps.setString(5, "wenaswenas");
            ps.setInt(6, 1);
            ps.setInt(7, 2);
            ps.setDate(8, new java.sql.Date(System.currentTimeMillis()));
            ps.setInt(9, idZona);


            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package com.example.unmujeres.daos;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistroCordiDao extends BaseDAO {
    public void nuevoCordi(String nombres, String apellidos, int dni, String correo, int idZona) {

        String sql = "INSERT INTO usuario (nombres, apellidos, DNI, correo, contraseña, estado, idroles, fecha_incorporacion, idzona) " +
                "VALUES (?, ?, ?, ?, SHA2(?,256), ?, ?, ?, ?)";

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

    // Verificar si un correo ya está registrado
    public boolean existeCorreo(String correo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE correo = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    // Verificar si un DNI ya está registrado
    public boolean existeDni(int dni) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE dni = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dni);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }
}

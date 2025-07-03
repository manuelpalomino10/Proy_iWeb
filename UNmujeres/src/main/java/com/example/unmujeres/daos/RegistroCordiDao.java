package com.example.unmujeres.daos;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistroCordiDao extends BaseDAO {
    public void nuevoCordi(String nombres, String apellidos, int dni, String correo, int idZona) {

        String sql = "INSERT INTO usuario (nombres, apellidos, DNI, correo, estado, idroles, fecha_incorporacion, idzona) " +
                "VALUES (?, ?, ?, ?, SHA2(?,256), ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombres);
            ps.setString(2, apellidos);
            ps.setInt(3, dni);
            ps.setString(4, correo);
            ps.setInt(5, 1);
            ps.setInt(6, 2);
            ps.setDate(7, new java.sql.Date(System.currentTimeMillis()));
            ps.setInt(8, idZona);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Genera un código único para la activación de coordinadores.
     */
    public String generarCodigoUnico() {
        return "COR-" + System.currentTimeMillis();
    }

    /**
     * Inserta un coordinador sin contraseña para que pueda activarla luego.
     * El estado se establece en 0 (pendiente) y se almacena el código de verificación.
     */
    public void insertarPendiente(String nombres, String apellidos, int dni,
                                  String correo, int idZona, String codigo) throws SQLException {
        String sql = "INSERT INTO usuario " +
                "(nombres, apellidos, DNI, correo, contraseña, estado, idroles, " +
                "fecha_incorporacion, idzona, token, token_expires) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombres);
            ps.setString(2, apellidos);
            ps.setInt(3, dni);
            ps.setString(4, correo);
            ps.setString(5, "");           // contraseña vacía inicialmente
            ps.setInt(6, 0);                // 0 = pendiente de activación
            ps.setInt(7, 2);                // 2 = rol coordinador
            ps.setDate(8, new java.sql.Date(System.currentTimeMillis()));
            ps.setInt(9, idZona);
            ps.setString(10, codigo);
            ps.setTimestamp(11, new java.sql.Timestamp(System.currentTimeMillis() + 24L * 60 * 60 * 1000));

            ps.executeUpdate();
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

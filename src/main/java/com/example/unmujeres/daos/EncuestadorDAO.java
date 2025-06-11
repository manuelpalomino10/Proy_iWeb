package com.example.unmujeres.daos;


import com.example.unmujeres.daos.Usuario;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import java.sql.*;

public class EncuestadorDAO extends BaseDAO {

    /** 1) Chequear si ya existe un DNI */
    public boolean existeDni(int dni) throws SQLException {
        String sql = "SELECT 1 FROM usuario WHERE DNI = ?";

        try (Connection con = this.getConnection();
             PreparedStatement p = con.prepareStatement(sql)) {
            p.setInt(1, dni);
            try (ResultSet rs = p.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** 2) Chequear si ya existe un correo */
    public boolean existeEmail(String correo) throws SQLException {
        String sql = "SELECT 1 FROM usuario WHERE correo = ?";
        try (Connection con = this.getConnection();
             PreparedStatement p = con.prepareStatement(sql)) {
            p.setString(1, correo);
            try (ResultSet rs = p.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** 3) Generar código único */
    public String generarCodigoUnico() {
        return "ENC-" + System.currentTimeMillis();
    }

    /** 4) Hash de contraseña (SHA-256) */
    public String hashPassword(String plain) {
        return DigestUtils.sha256Hex(plain);
    }

    /** 5) Insertar nuevo encuestador en la tabla usuario (sin contraseña) */
    public void insert(Usuario u) throws SQLException {
        String sql =
                "INSERT INTO usuario " +
                        "(nombres, apellidos, contraseña, DNI, correo, direccion, estado, " +
                        " idroles, idzona, iddistritos, fecha_incorporacion, foto, cod_enc) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE(), ?, ?)";
        try (Connection con = this.getConnection();
             PreparedStatement p = con.prepareStatement(sql)) {

            p.setString(1, u.getNombres());
            p.setString(2, u.getApellidos());
            p.setString(3, u.getContrasena());                    // contraseña vacía al registro inicial
            p.setInt(4, u.getDNI());
            p.setString(5, u.getCorreo());
            p.setString(6, u.getDireccion());
            p.setByte(7, u.getEstado());              // 0 = pendiente
            p.setInt(8, u.getRoles_idroles());        // 3 = Encuestador
            if (u.getZona_idzona() != null) p.setInt(9, u.getZona_idzona());
            else                           p.setNull(9, Types.INTEGER);
            if (u.getDistritos_iddistritos() != null) p.setInt(10, u.getDistritos_iddistritos());
            else                                      p.setNull(10, Types.INTEGER);
            if (u.getFoto() != null) p.setBytes(11, u.getFoto());
            else                     p.setNull(11, Types.BLOB);
            p.setString(12, u.getCod_enc());

            p.executeUpdate();
        }
    }

    /** 6) Activar usuario: guardar la contraseña, set estado=1 y limpiar el token */
    public void activate(String code, String hashedPwd) throws SQLException {
        String sql =
                "UPDATE usuario " +
                        "SET contraseña = ?, estado = 1, cod_enc = NULL " +
                        "WHERE cod_enc = ?";
        try (Connection con = this.getConnection();
             PreparedStatement p = con.prepareStatement(sql)) {
            p.setString(1, hashedPwd);
            p.setString(2, code);
            p.executeUpdate();
        }
    }
}

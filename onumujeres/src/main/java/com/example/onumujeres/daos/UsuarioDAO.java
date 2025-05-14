package com.example.onumujeres.daos;

import com.example.onumujeres.beans.Usuario;
import com.example.onumujeres.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // Obtener Usuario por ID
    public Usuario obtenerUsuarioPorId(int idUsuario) throws SQLException {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuario WHERE idusuario = ?"; // Changed id_usuario to idusuario

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdusuario(rs.getInt("idusuario")); // Changed id_usuario to idusuario
                usuario.setNombres(rs.getString("nombres"));
                usuario.setApellidos(rs.getString("apellidos"));
                usuario.setContraseña(rs.getString("contraseña")); // Changed clave to contraseña
                usuario.setDni(rs.getInt("DNI"));              // Changed dni to DNI and got as Int
                usuario.setCorreo(rs.getString("correo"));
                usuario.setDireccion(rs.getString("direccion"));
                usuario.setEstado(rs.getByte("estado"));       // Changed to getByte
                usuario.setRoles_idroles(rs.getInt("roles_idroles"));
                usuario.setZona_idzona(rs.getObject("zona_idzona", Integer.class)); // Changed to getObject to handle nulls
                usuario.setDistritos_iddistritos(rs.getObject("distritos_iddistritos", Integer.class)); // Changed to getObject to handle nulls
                usuario.setFecha_incorporacion(rs.getDate("fecha_incorporacion"));
                usuario.setFoto(rs.getBlob("foto"));
                usuario.setCod_enc(rs.getString("cod_enc"));
            }

        }
        return usuario;
    }

    // Actualizar Usuario (Dirección, Distrito, Contraseña)
    public boolean actualizarUsuario(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuario SET direccion = ?, distritos_iddistritos = ?, contraseña = ? WHERE idusuario = ?"; // Updated query

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario.getDireccion());
            // Check if distritos_iddistritos is null
            if (usuario.getDistritos_iddistritos() != null) {
                ps.setInt(2, usuario.getDistritos_iddistritos());
            } else {
                ps.setNull(2, Types.INTEGER); // Set to null if it is
            }
            ps.setString(3, usuario.getContraseña());
            ps.setInt(4, usuario.getIdusuario());

            int filasActualizadas = ps.executeUpdate();
            return filasActualizadas > 0;

        }
    }

    // Actualizar Foto de Perfil
    public boolean actualizarFoto(int idUsuario, Blob foto) throws SQLException { // Changed method name
        String sql = "UPDATE usuario SET foto = ? WHERE idusuario = ?";  // Updated query

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setBlob(1, foto);
            ps.setInt(2, idUsuario);

            int filasActualizadas = ps.executeUpdate();
            return filasActualizadas > 0;

        }
    }
}
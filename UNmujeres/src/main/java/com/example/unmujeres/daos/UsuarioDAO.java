package com.example.unmujeres.daos;

import com.example.unmujeres.beans.Distritos;
import com.example.unmujeres.beans.Usuario;
import java.sql.*;
import java.util.Arrays;

public class UsuarioDAO extends BaseDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/iweb_proy?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public Usuario obtenerUsuarioConDistrito(int idUsuario) throws SQLException {
        Usuario usuario = null;
        // Añade u.idroles a la consulta
        String sql = "SELECT u.*, d.nombre as nombre_distrito " +
                "FROM usuario u " +
                "LEFT JOIN distritos d ON u.iddistritos = d.iddistritos " +
                "WHERE u.idusuario = ?";

        try (Connection con = this.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("idusuario"));
                    usuario.setNombres(rs.getString("nombres"));
                    usuario.setApellidos(rs.getString("apellidos"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setDNI(rs.getInt("dni"));
                    usuario.setDireccion(rs.getString("direccion"));
                    usuario.setFotoBytes(rs.getBytes("foto"));

                    // Campos adicionales críticos
                    usuario.setIdroles(rs.getInt("idroles")); // AÑADE ESTA LÍNEA
                    usuario.setEstado(rs.getBoolean("estado")); // Recomendado
                    usuario.setIddistritos(rs.getInt("iddistritos")); // Recomendado

                    // Set Distrito
                    Distritos distrito = new Distritos();
                    distrito.setIddistritos(rs.getInt("iddistritos"));
                    distrito.setNombre(rs.getString("nombre_distrito"));
                    usuario.setDistrito(distrito);
                }
            }
        }
        return usuario;
    }

    public Usuario obtenerUsuarioPorId(int id) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuario WHERE idusuario = ?";

        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("idusuario"));
                usuario.setNombres(rs.getString("nombres"));
                usuario.setApellidos(rs.getString("apellidos"));
                usuario.setContraseña(rs.getString("contraseña"));
                usuario.setDNI(rs.getInt("dni"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setDireccion(rs.getString("direccion"));
                usuario.setEstado(rs.getBoolean("estado"));
                usuario.setIdroles(rs.getInt("idroles"));
                usuario.setIdzona((Integer) rs.getObject("idzona"));
                usuario.setIddistritos((Integer) rs.getObject("iddistritos"));
                usuario.setFechaIncorporacion(rs.getDate("fecha_incorporacion"));
                usuario.setCodEnc(rs.getString("cod_enc"));
                usuario.setIddistritos(rs.getInt("iddistritos")); // Añade esta línea

                Blob fotoBlob = rs.getBlob("foto");
                if (fotoBlob != null) {
                    byte[] fotoBytes = fotoBlob.getBytes(1, (int) fotoBlob.length());
                    usuario.setFotoBytes(fotoBytes);
                    System.out.println("[DEBUG] Foto cargada con tamaño: " + fotoBytes.length);
                } else {
                    System.out.println("[DEBUG] Usuario sin foto.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuario;
    }


    public boolean actualizarUsuario(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuario SET direccion = ?, iddistritos = ?, contraseña = ? WHERE idusuario = ?";

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getDireccion());

            // Manejar iddistritos como nulo si es necesario
            if (usuario.getIddistritos() != null) {
                ps.setInt(2, usuario.getIddistritos());
            } else {
                ps.setNull(2, Types.INTEGER); // Usar setNull para valores nulos
            }

            ps.setString(3, usuario.getContraseña());
            ps.setInt(4, usuario.getIdUsuario());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarFoto(int idUsuario, byte[] fotoBytes) throws SQLException {
        String sql = "UPDATE usuario SET foto = ? WHERE idusuario = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBytes(1, fotoBytes);
            pstmt.setInt(2, idUsuario);

            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("[DEBUG] Filas afectadas: " + filasAfectadas); // Debe ser 1

            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("[ERROR] SQL al actualizar foto: " + e.getMessage());
            throw e;
        }
    }

    public Usuario getById(int id) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuario WHERE idusuario = ?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("idusuario"));
                    usuario.setNombres(rs.getString("nombres"));
                    usuario.setApellidos(rs.getString("apellidos"));
                    usuario.setFechaIncorporacion(rs.getDate("fecha_incorporacion"));
                    usuario.setEstado(rs.getBoolean("estado"));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public Usuario validarUsuario(String correo, String contraseñaHasheada) {
        Usuario usuario = null;
        String sql = "SELECT idUsuario, nombres, apellidos, contraseña, DNI, correo, direccion, estado, " +
                "idroles, idzona, iddistritos, fecha_incorporacion, cod_Enc FROM usuario " +
                "WHERE correo = ? AND contraseña = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, correo);
            pstmt.setString(2, contraseñaHasheada);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    usuario = mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error SQL al validar usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return usuario;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("idUsuario"));
        usuario.setNombres(rs.getString("nombres"));
        usuario.setApellidos(rs.getString("apellidos"));
        usuario.setContraseña(rs.getString("contraseña"));
        usuario.setDNI(rs.getInt("DNI"));
        usuario.setCorreo(rs.getString("correo"));
        usuario.setDireccion(rs.getString("direccion"));
        usuario.setEstado(rs.getBoolean("estado"));
        usuario.setIdroles(rs.getInt("idroles"));
        usuario.setIdzona(rs.getInt("idzona"));
        usuario.setIddistritos(rs.getInt("iddistritos"));
        usuario.setFechaIncorporacion(rs.getDate("fecha_incorporacion"));
        usuario.setCodEnc(rs.getString("cod_Enc"));
        return usuario;
    }

    public boolean actualizarContraseña(int idUsuario, String contraseñaPlana) throws SQLException {
        String sql = "UPDATE usuario SET contraseña = SHA2(?, 256) WHERE idusuario = ?";

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, contraseñaPlana); // Se hashea en la BD con SHA2
            ps.setInt(2, idUsuario);

            return ps.executeUpdate() > 0;
        }
    }

}


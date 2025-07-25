package com.example.unmujeres.daos.administrador;

import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.daos.BaseDAO;
import com.example.unmujeres.dtos.UsuarioDto;

import java.sql.*;
import java.util.ArrayList;

public class UsuarioDao extends BaseDAO {

    public ArrayList<UsuarioDto> listar() {
        ArrayList<UsuarioDto> lista = new ArrayList<>();

        String sql = "SELECT u.idUsuario, concat(u.nombres, ' ',u.apellidos) as Nombre_Usuario, u.correo, u.DNI, r.nombre AS nombreRol," +
                "z.nombre AS nombreZona, u.estado AS estado " +
                "FROM usuario u " +
                "JOIN roles r ON u.idroles = r.idroles " +
                "JOIN zona z ON u.idzona = z.idzona " +
                "WHERE u.idroles IN (2, 3) " +
                "ORDER BY u.idUsuario DESC";

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UsuarioDto usuariodto = new UsuarioDto();

                usuariodto.setId(rs.getInt("idUsuario"));
                usuariodto.setDni(rs.getString("DNI"));

                usuariodto.setNombreCompleto(rs.getString("Nombre_Usuario"));
                usuariodto.setCorreo(rs.getString("correo"));
                usuariodto.setNombreRol(rs.getString("nombreRol"));
                usuariodto.setNombreZona(rs.getString("nombreZona"));
                usuariodto.setEstado(rs.getBoolean("estado"));

                lista.add(usuariodto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean cambiarEstado(int idUsuario, boolean estado) {
        boolean actualizado = false;
        String sql = "UPDATE usuario SET estado = ? WHERE idUsuario = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, estado);
            ps.setInt(2, idUsuario);

            actualizado = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actualizado;
    }

    public void nuevoCordi(String nombre, String apellido, int dni, String correo) {

        String sql = "INSERT INTO usuario (nombres, apellidos, DNI, correo) VALUES (?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setInt(3, dni);
            ps.setString(4, correo);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

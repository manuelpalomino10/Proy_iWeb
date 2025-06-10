package com.example.unmujeres.daos;

import com.example.unmujeres.beans.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoordiGestionEncDAO extends BaseDAO {

    /**
     * Lista todos los encuestadores (usuarios con rol 3) asociados a las zonas del coordinador
     * @param coordiId ID del coordinador
     * @return Lista de usuarios encuestadores
     * @throws SQLException
     */
    public List<Usuario> listarPorZona(int coordiId) throws SQLException {
        String sql = "SELECT u.idusuario, u.nombres, u.apellidos, u.correo, u.estado, " +
                "u.dni, u.direccion, u.idroles, u.idzona, u.iddistritos, " +
                "u.fechaIncorporacion, u.codEnc " +
                "FROM usuario u " +
                "INNER JOIN zona_has_usuario zu ON u.idusuario = zu.usuario_idusuario " +
                "WHERE zu.zona_idzona IN (SELECT zona_idzona FROM zona_has_usuario WHERE usuario_idusuario = ?) " +
                "AND u.idroles = 3"; // 3 = rol encuestador

        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, coordiId);

            List<Usuario> encuestadores = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("idusuario"));
                    usuario.setNombres(rs.getString("nombres"));
                    usuario.setApellidos(rs.getString("apellidos"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setEstado(rs.getBoolean("estado"));
                    usuario.setDNI(rs.getInt("dni"));
                    usuario.setDireccion(rs.getString("direccion"));
                    usuario.setIdroles(rs.getInt("idroles"));
                    usuario.setIdzona(rs.getInt("idzona"));
                    usuario.setIddistritos(rs.getInt("iddistritos"));
                    usuario.setFechaIncorporacion(rs.getDate("fechaIncorporacion"));
                    usuario.setCodEnc(rs.getString("codEnc"));

                    encuestadores.add(usuario);
                }
            }
            return encuestadores;
        }
    }

    /**
     * Actualiza el estado de un encuestador (banear/reactivar)
     * @param idUsuario ID del encuestador
     * @param nuevoEstado false = baneado, true = activo
     * @throws SQLException
     */
    public void actualizarEstado(int idUsuario, boolean nuevoEstado) throws SQLException {
        String sql = "UPDATE usuario SET estado = ? WHERE idusuario = ? AND idroles = 3";

        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, nuevoEstado);
            ps.setInt(2, idUsuario);
            ps.executeUpdate();
        }
    }

    /**
     * Asigna formularios a un encuestador (operación transaccional)
     * @param idEncuestador ID del encuestador
     * @param idsFormularios Lista de IDs de formularios a asignar
     * @param idCoordinador ID del coordinador que realiza la asignación
     * @throws SQLException
     */
    public void asignarFormularios(int idEncuestador, List<Integer> idsFormularios, int idCoordinador)
            throws SQLException {

        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);

            // 1. Eliminar asignaciones anteriores
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM usuario_has_formulario WHERE usuario_idusuario = ?")) {
                ps.setInt(1, idEncuestador);
                ps.executeUpdate();
            }

            // 2. Insertar nuevas asignaciones si la lista no está vacía
            if (idsFormularios != null && !idsFormularios.isEmpty()) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO usuario_has_formulario(usuario_idusuario, formulario_idformulario, codigo, asignado_por) " +
                                "VALUES (?, ?, UUID(), ?)")) {

                    for (int idForm : idsFormularios) {
                        ps.setInt(1, idEncuestador);
                        ps.setInt(2, idForm);
                        ps.setInt(3, idCoordinador);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}
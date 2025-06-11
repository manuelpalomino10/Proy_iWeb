package com.example.unmujeres.daos;

import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.beans.Formulario;
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
                "u.fecha_incorporacion, u.cod_enc  " +
                "FROM usuario u " +
                "WHERE u.idzona = (SELECT idzona FROM usuario WHERE idusuario = ?) " +
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
                    usuario.setFechaIncorporacion(rs.getDate("fecha_incorporacion"));
                    usuario.setCodEnc(rs.getString("cod_enc"));

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
                    "DELETE FROM enc_has_formulario  WHERE enc_idusuario  = ?")) {
                ps.setInt(1, idEncuestador);
                ps.executeUpdate();
            }

            // 2. Insertar nuevas asignaciones si la lista no está vacía
            if (idsFormularios != null && !idsFormularios.isEmpty()) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO enc_has_formulario(enc_idusuario, idformulario, codigo, fecha_asignacion) " +
                                "VALUES (?, ?, UUID(), ?)")) {

                    for (int idForm : idsFormularios) {
                        ps.setInt(1, idEncuestador);
                        ps.setInt(2, idForm);
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

    /**
     * Obtiene los formularios activos disponibles para asignar a un encuestador.
     * Un formulario está disponible si está activo y no se ha asignado previamente
     * al encuestador indicado. La lógica de zona se maneja a nivel de coordinador
     * y queda supeditada a la configuración de la base de datos.
     *
     * @param coordiId id del coordinador que realiza la consulta
     * @param encId id del encuestador al que se asignarán formularios
     * @return lista de formularios disponibles
     * @throws SQLException en caso de error de consulta
     */
    public List<Formulario> obtenerFormulariosDisponibles(int coordiId, int encId) throws SQLException {
        String sql = "SELECT f.idformulario, f.nombre, f.fecha_creacion, f.fecha_limite, " +
                "f.estado, f.registros_esperados, f.idcategoria " +
                "FROM formulario f " +
                "WHERE f.estado = 1 AND f.idformulario NOT IN (" +
                "SELECT idformulario FROM enc_has_formulario WHERE enc_idusuario = ?)";

        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, encId);

            List<Formulario> formularios = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Formulario f = new Formulario();
                    f.setIdFormulario(rs.getInt("idformulario"));
                    f.setNombre(rs.getString("nombre"));
                    f.setFechaCreacion(rs.getDate("fecha_creacion"));
                    f.setFechaLimite(rs.getDate("fecha_limite"));
                    f.setEstado(rs.getBoolean("estado"));
                    f.setRegistrosEsperados(rs.getInt("registros_esperados"));
                    formularios.add(f);
                }
            }
            return formularios;
        }
    }

    /**
     * Obtiene los formularios ya asignados a un encuestador.
     *
     * @param encId id del encuestador
     * @return lista de formularios asignados
     * @throws SQLException en caso de error de consulta
     */
    public List<Formulario> obtenerFormulariosAsignados(int encId) throws SQLException {
        String sql = "SELECT f.idformulario, f.nombre, f.fecha_creacion, f.fecha_limite, " +
                "f.estado, f.registros_esperados, f.idcategoria " +
                "FROM formulario f " +
                "JOIN enc_has_formulario ehf ON f.idformulario = ehf.idformulario " +
                "WHERE ehf.enc_idusuario = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, encId);

            List<Formulario> formularios = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Formulario f = new Formulario();
                    f.setIdFormulario(rs.getInt("idformulario"));
                    f.setNombre(rs.getString("nombre"));
                    f.setFechaCreacion(rs.getDate("fecha_creacion"));
                    f.setFechaLimite(rs.getDate("fecha_limite"));
                    f.setEstado(rs.getBoolean("estado"));
                    f.setRegistrosEsperados(rs.getInt("registros_esperados"));
                    formularios.add(f);
                }
            }
            return formularios;
        }
    }
}
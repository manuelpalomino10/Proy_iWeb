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
     * Asigna un formulario específico a un encuestador.
     */
    public void asignarFormulario(int encId, int idFormulario) throws SQLException {
        String sql = "INSERT INTO enc_has_formulario(enc_idusuario, idformulario, codigo, fecha_asignacion) " +
                "VALUES (?, ?, UUID(), ?)";
        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, encId);
            ps.setInt(2, idFormulario);
            ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            ps.executeUpdate();
        }
    }

    /**
     * Desasigna un formulario de un encuestador. Devuelve false si el
     * encuestador ya registró respuestas para dicho formulario.
     */
    public boolean desasignarFormulario(int encId, int idFormulario) throws SQLException {
        if (contarRespuestasPorEncuestadorYFormulario(encId, idFormulario) > 0) {
            return false;
        }
        String sql = "DELETE FROM enc_has_formulario WHERE enc_idusuario = ? AND idformulario = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, encId);
            ps.setInt(2, idFormulario);
            ps.executeUpdate();
        }
        return true;
    }

    /**
     * Asigna formularios a un encuestador (operación transaccional)
     * @param idEncuestador ID del encuestador
     * @param idsFormularios Lista de IDs de formularios a asignar
     * @throws SQLException
     */
    public List<Integer> asignarFormularios(int idEncuestador, List<Integer> idsFormularios)
            throws SQLException {

        Connection conn = null;
        List<Integer> noDesasignados = new ArrayList<>();
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);

            // Obtener asignaciones actuales del encuestador
            String query = "SELECT idenc_has_formulario, idformulario FROM enc_has_formulario WHERE enc_idusuario = ?";
            List<Integer> asignadosPersistentes = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, idEncuestador);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int idEhf = rs.getInt("idenc_has_formulario");
                        int idForm = rs.getInt("idformulario");
                        int count = contarRespuestasPorEncuestadorYFormulario(idEncuestador, idForm);
                        if (count > 0) {
                            // no eliminar si el encuestador ya registró respuestas
                            asignadosPersistentes.add(idForm);
                            noDesasignados.add(idForm);
                        } else {
                            try (PreparedStatement del = conn.prepareStatement(
                                    "DELETE FROM enc_has_formulario WHERE idenc_has_formulario = ?")) {
                                del.setInt(1, idEhf);
                                del.executeUpdate();
                            }
                        }
                    }
                }
            }

            // Insertar nuevas asignaciones
            if (idsFormularios != null && !idsFormularios.isEmpty()) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO enc_has_formulario(enc_idusuario, idformulario, codigo, fecha_asignacion) " +
                                "VALUES (?, ?, UUID(), ?)")) {
                    for (int idForm : idsFormularios) {
                        if (!asignadosPersistentes.contains(idForm)) {
                            ps.setInt(1, idEncuestador);
                            ps.setInt(2, idForm);
                            ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                            ps.addBatch();
                            asignadosPersistentes.add(idForm);
                        }
                    }
                    ps.executeBatch();
                }
            }

            conn.commit();
            return noDesasignados;
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
        String sql = "SELECT DISTINCT f.idformulario, f.nombre, f.fecha_creacion, f.fecha_limite, " +
                "f.estado, f.registros_esperados, f.idcategoria " +
                "FROM formulario f " +
                "WHERE f.estado = 1 " +
                "AND NOT EXISTS (SELECT 1 FROM enc_has_formulario ehf WHERE ehf.enc_idusuario = ? AND ehf.idformulario = f.idformulario)";
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
                "f.estado, f.registros_esperados, f.idcategoria, " +
                "COUNT(rr.idregistro_respuestas) AS respuestas_count " +
                "FROM formulario f " +
                "JOIN enc_has_formulario ehf ON f.idformulario = ehf.idformulario " +
                "LEFT JOIN registro_respuestas rr ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "WHERE ehf.enc_idusuario = ? " +
                "GROUP BY f.idformulario, f.nombre, f.fecha_creacion, f.fecha_limite, f.estado, f.registros_esperados, f.idcategoria";

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
                    f.setRespuestasCount(rs.getInt("respuestas_count"));
                    formularios.add(f);
                }
            }
            return formularios;
        }
    }
    public int contarRespuestasPorEncuestadorYFormulario(int encId, int idFormulario) throws SQLException {
        String sql = "SELECT COUNT(*) FROM registro_respuestas rr " +
                "JOIN enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "WHERE ehf.enc_idusuario = ? AND ehf.idformulario = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, encId);
            ps.setInt(2, idFormulario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        }
    }

    /**
     * Verifica que el encuestador pertenezca a la zona del coordinador.
     *
     * @param encId    id del encuestador
     * @param coordiId id del coordinador
     * @return {@code true} si ambos están en la misma zona y el usuario tiene rol de encuestador
     * @throws SQLException en caso de error en la consulta
     */
    public boolean perteneceACoordinador(int encId, int coordiId) throws SQLException {
        String sql = "SELECT 1 FROM usuario WHERE idusuario=? AND idroles=3 " +
                "AND idzona=(SELECT idzona FROM usuario WHERE idusuario=?)";
        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, encId);
            ps.setInt(2, coordiId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}

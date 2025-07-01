package com.example.unmujeres.daos;
import com.example.unmujeres.dtos.FormularioDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GestionFormDao extends BaseDAO {
    public ArrayList<FormularioDto> listar() {
        return listar(0);
    }

    public ArrayList<FormularioDto> listar(int idCategoria) {
        ArrayList<FormularioDto> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder("select idFormulario, f.nombre as NombreForm, fecha_creacion as creacion, estado, c.nombre as NombreCat\n" +
                "from formulario f\n" +
                "join categoria c on f.idcategoria = c.idcategoria");
        if (idCategoria != 0) {
            sql.append(" WHERE f.idcategoria = ?");
        }

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            if (idCategoria != 0) {
                ps.setInt(1, idCategoria);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    FormularioDto formularioDto = new FormularioDto();
                    formularioDto.setId(rs.getInt("idFormulario"));
                    formularioDto.setNombreForm(rs.getString("NombreForm"));
                    formularioDto.setFechaCreacion(rs.getDate("creacion"));
                    formularioDto.setEstado(rs.getBoolean("estado"));
                    formularioDto.setNombreCat(rs.getString("NombreCat"));
                    lista.add(formularioDto);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }


    public ArrayList<FormularioDto> listar(int idCoordi,int idCategoria) {
        StringBuilder sql1 = new StringBuilder();
        String sql = "SELECT " +
                "    f.idformulario, f.nombre, ehf_principal.idenc_has_formulario, COUNT(rr.idregistro_respuestas) AS total_registros_completados, " +
                "    f.fecha_creacion, f.fecha_limite, f.estado " +
                "FROM formulario f " +
                "JOIN usuario u_principal ON u_principal.idusuario = ? " + // Param 1: ID usuario principal
                "JOIN usuario u_zona ON u_zona.idzona = u_principal.idzona " +
                "LEFT JOIN enc_has_formulario ehf_principal " +
                "    ON ehf_principal.idformulario = f.idformulario " +
                "    AND ehf_principal.enc_idusuario = u_principal.idusuario " +
                "LEFT JOIN enc_has_formulario ehf_zona " +
                "    ON ehf_zona.idformulario = f.idformulario " +
                "    AND ehf_zona.enc_idusuario = u_zona.idusuario " +
                "LEFT JOIN registro_respuestas rr " +
                "    ON rr.idenc_has_formulario = ehf_zona.idenc_has_formulario " +
                "    AND rr.estado = 'C' ";
        sql1.append(sql);
        if (idCategoria != 0) {
            sql1.append(" WHERE f.idcategoria = ? ");    // Param 2: ID categoria
        }
        sql1.append(" GROUP BY f.idformulario, f.nombre, ehf_principal.idenc_has_formulario");

        ArrayList<FormularioDto> formularios = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql1.toString())) {

            ps.setInt(1, idCoordi);
            if (idCategoria != 0) {
                ps.setInt(2, idCategoria);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    FormularioDto formularioDto = new FormularioDto();
                    formularioDto.setId(rs.getInt("idformulario"));
                    formularioDto.setNombreForm(rs.getString("nombre"));
                    formularioDto.setNRegCompletados(rs.getInt("total_registros_completados"));
                    formularioDto.setFechaCreacion(rs.getDate("fecha_creacion"));
                    formularioDto.setFechaLimite(rs.getDate("fecha_limite"));
                    formularioDto.setEstado(rs.getBoolean("estado"));
                    formularioDto.setIdEncHasFormulario(rs.getInt("idenc_has_formulario"));

                    formularios.add(formularioDto);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return formularios;
    }


    public boolean cambiarEstado(int idFormulario, boolean estado) {
        boolean actualizado = false;
        String sql = "UPDATE formulario SET estado = ? WHERE idFormulario = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, estado);
            ps.setInt(2, idFormulario);

            actualizado = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actualizado;
    }

    
    public boolean eliminarFormularioSiMenorA12(int idFormulario) throws SQLException {
        boolean eliminado = false;

        String contarSql = "SELECT COUNT(*) FROM registro_respuestas rr " +
                "JOIN enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "WHERE ehf.idformulario = ?";

        String eliminarRespuestasSql = "DELETE FROM respuesta WHERE idpregunta IN " +
                "(SELECT idpregunta FROM pregunta WHERE idseccion IN " +
                "(SELECT idseccion FROM seccion WHERE idformulario = ?))";

        String eliminarOpcionesSql = "DELETE FROM opcion_pregunta WHERE idpregunta IN " +
                "(SELECT idpregunta FROM pregunta WHERE idseccion IN " +
                "(SELECT idseccion FROM seccion WHERE idformulario = ?))";

        String eliminarPreguntasSql = "DELETE FROM pregunta WHERE idseccion IN " +
                "(SELECT idseccion FROM seccion WHERE idformulario = ?)";

        String eliminarSeccionesSql = "DELETE FROM seccion WHERE idformulario = ?";

        String eliminarRegistroRespuestasSql = "DELETE FROM registro_respuestas WHERE idenc_has_formulario IN " +
                "(SELECT idenc_has_formulario FROM enc_has_formulario WHERE idformulario = ?)";

        String eliminarAsignacionesSql = "DELETE FROM enc_has_formulario WHERE idformulario = ?";

        String eliminarFormularioSql = "DELETE FROM formulario WHERE idformulario = ?";

        try (Connection con = getConnection();
             PreparedStatement psContar = con.prepareStatement(contarSql)) {

            psContar.setInt(1, idFormulario);
            ResultSet rs = psContar.executeQuery();

            if (rs.next()) {
                int totalRespuestas = rs.getInt(1);

                if (totalRespuestas < 12) {
                    try {
                        con.setAutoCommit(false); // Inicia la transacción

                        // 1. Eliminar respuestas
                        try (PreparedStatement ps = con.prepareStatement(eliminarRespuestasSql)) {
                            ps.setInt(1, idFormulario);
                            ps.executeUpdate();
                        }

                        // 2. Eliminar opciones
                        try (PreparedStatement ps = con.prepareStatement(eliminarOpcionesSql)) {
                            ps.setInt(1, idFormulario);
                            ps.executeUpdate();
                        }

                        // 3. Eliminar preguntas
                        try (PreparedStatement ps = con.prepareStatement(eliminarPreguntasSql)) {
                            ps.setInt(1, idFormulario);
                            ps.executeUpdate();
                        }

                        // 4. Eliminar secciones
                        try (PreparedStatement ps = con.prepareStatement(eliminarSeccionesSql)) {
                            ps.setInt(1, idFormulario);
                            ps.executeUpdate();
                        }

                        // 5. Eliminar registro_respuestas
                        try (PreparedStatement ps = con.prepareStatement(eliminarRegistroRespuestasSql)) {
                            ps.setInt(1, idFormulario);
                            ps.executeUpdate();
                        }

                        // 6. Eliminar enc_has_formulario
                        try (PreparedStatement ps = con.prepareStatement(eliminarAsignacionesSql)) {
                            ps.setInt(1, idFormulario);
                            ps.executeUpdate();
                        }

                        // 7. Finalmente, eliminar el formulario
                        try (PreparedStatement ps = con.prepareStatement(eliminarFormularioSql)) {
                            ps.setInt(1, idFormulario);
                            int filasAfectadas = ps.executeUpdate();
                            eliminado = filasAfectadas > 0;
                        }

                        con.commit(); // Confirma los cambios

                    } catch (SQLException e) {
                        con.rollback(); // Revierte todo si hay error
                        e.printStackTrace();
                        throw e;
                    } finally {
                        con.setAutoCommit(true); // Restaura el autocommit
                    }
                } else {
                    System.out.println("No se puede eliminar: el formulario tiene " + totalRespuestas + " respuestas.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return eliminado;
    }
}

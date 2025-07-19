package com.example.unmujeres.daos;

import java.sql.*;
import java.util.ArrayList;

import com.example.unmujeres.beans.EncHasFormulario;
import com.example.unmujeres.beans.Formulario;
import com.example.unmujeres.beans.RegistroRespuestas;

public class RegistroRespuestasDAO extends BaseDAO {

    public RegistroRespuestas findEncDraftById(int idReg, int idEnc) {      //Devuelve registro borrador(B) solo si existe y pertenece al encuestador

        RegistroRespuestas reg = null;
        String sql = "SELECT reg.*, ehf.* FROM registro_respuestas reg " +
                "INNER JOIN enc_has_formulario ehf ON ehf.idenc_has_formulario=reg.idenc_has_formulario AND ehf.enc_idusuario=? " +
                "INNER JOIN formulario f ON f.idformulario=ehf.idformulario " +
                "WHERE reg.idregistro_respuestas=? AND reg.estado='B' AND f.estado=1 ; ";

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, idEnc);
            ps.setInt(2, idReg);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    reg = new RegistroRespuestas();

                    reg.setIdRegistroRespuestas(rs.getInt("idregistro_respuestas"));
                    reg.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
                    reg.setEstado(rs.getString("estado"));

                    Formulario form = new Formulario();
                    form.setIdFormulario(rs.getInt("idformulario"));

//                    Usuario usuario = new Usuario();
//                    usuario.setIdUsuario(rs.getInt("enc_idusuario"));

                    EncHasFormulario ehf = new EncHasFormulario();
                    ehf.setIdEncHasFormulario(rs.getInt("idenc_has_formulario"));
                    ehf.setFormulario(form);
//                    ehf.setUsuario(usuario);

                    reg.setEncHasFormulario(ehf);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reg;
    }


    public ArrayList<RegistroRespuestas> getByUser(int idUsuario) {
        ArrayList<RegistroRespuestas> registros = new ArrayList<>();
        String sql = "SELECT reg.idregistro_respuestas, reg.estado, f.idformulario, f.nombre, f.estado AS fEstado, reg.fecha_registro, f.fecha_limite " +
                "FROM formulario f " +
                "INNER JOIN enc_has_formulario ehf ON f.idformulario=ehf.idformulario " +
                "INNER JOIN registro_respuestas reg ON ehf.idenc_has_formulario=reg.idenc_has_formulario " +
                "WHERE ehf.enc_idusuario = ? AND f.estado=1 " +
                "GROUP BY reg.idregistro_respuestas ORDER BY reg.fecha_registro DESC, reg.idregistro_respuestas DESC;";
        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RegistroRespuestas reg = new RegistroRespuestas();
                    reg.setIdRegistroRespuestas(rs.getInt("idregistro_respuestas"));
                    reg.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
                    reg.setEstado(rs.getString("estado"));

                    EncHasFormulario ehf = new EncHasFormulario();
                        Formulario form = new Formulario();
                        form.setIdFormulario(rs.getInt("idformulario"));
                        form.setNombre(rs.getString("nombre"));
                        form.setEstado(rs.getBoolean("fEstado"));
                        form.setFechaLimite(rs.getDate("fecha_limite"));
                    ehf.setFormulario(form);
                    reg.setEncHasFormulario(ehf);

                    registros.add(reg);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return registros;

    }


    public void delete(int id) {
        String sql = "DELETE FROM registro_respuestas WHERE idregistro_respuestas = ?";

        String delRespSQL = "DELETE FROM respuesta WHERE idregistro_respuestas = ?";
        String delRegSQL = "DELETE FROM registro_respuestas WHERE idregistro_respuestas = ?";

        try (Connection con = this.getConnection();){

            try (PreparedStatement ps = con.prepareStatement(delRespSQL)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = con.prepareStatement(delRegSQL)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateState(int id, String estado) {
        String sql = "UPDATE registro_respuestas SET estado = ? WHERE idregistro_respuestas = ?";

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);){

            ps.setString(1, estado);
            ps.setInt(2, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int crearRegistroRespuestas(RegistroRespuestas registro) throws SQLException {
        String sql = "INSERT INTO registro_respuestas (fecha_registro, estado, idenc_has_formulario) " +
                "VALUES (?, ?, ?)";

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, Timestamp.valueOf(registro.getFechaRegistro()));
            ps.setString(2, registro.getEstado());
            ps.setInt(3, registro.getEncHasFormulario().getIdEncHasFormulario());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("No se pudo crear el registro de respuestas");
    }

    public int crearRegTransaccion(Connection conn, RegistroRespuestas registro) throws SQLException {
        String sql = "INSERT INTO registro_respuestas (fecha_registro, estado, idenc_has_formulario) " +
                "VALUES (?, ?, ?)";

        try (Connection con = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, Timestamp.valueOf(registro.getFechaRegistro()));
            ps.setString(2, registro.getEstado());
            ps.setInt(3, registro.getEncHasFormulario().getIdEncHasFormulario());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("No se pudo crear el registro de respuestas");
    }
    
}

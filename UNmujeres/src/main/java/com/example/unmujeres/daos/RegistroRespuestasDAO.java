package com.example.unmujeres.daos;

import java.sql.*;
import java.util.ArrayList;

import com.example.unmujeres.beans.EncHasFormulario;
import com.example.unmujeres.beans.Formulario;
import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.beans.RegistroRespuestas;

public class RegistroRespuestasDAO extends BaseDAO {

    public ArrayList<RegistroRespuestas> getByEhf(int idEhf) {
        ArrayList<RegistroRespuestas> registros = new ArrayList<>();

        String sql = "SELECT " +
                "    reg.idregistro_respuestas, " +
                "    reg.fecha_registro, " +
                "    reg.estado, " +
                "    ehf.idenc_has_formulario, " +
                "    ehf.idformulario " +
                "FROM " +
                "    registro_respuestas reg " +
                "INNER JOIN " +
                "    enc_has_formulario ehf ON reg.idenc_has_formulario = ehf.idenc_has_formulario " +
                "WHERE " +
                "    ehf.idenc_has_formulario = ?";

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, idEhf);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    RegistroRespuestas reg = new RegistroRespuestas();

                    reg.setEstado(rs.getString("estado"));
                    reg.setIdRegistroRespuestas(rs.getInt("idregistro_respuestas"));
                    reg.setFechaRegistro(rs.getDate("fecha_registro"));

                    // Obtener asignacion por id
                    EncHasFormularioDAO encHasFormularioDAO = new EncHasFormularioDAO();
                    reg.setEncHasFormulario(encHasFormularioDAO.getById(idEhf));


                    registros.add(reg);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registros;
    }


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
                    reg.setFechaRegistro(rs.getDate("fecha_registro"));
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







    public void save(RegistroRespuestas reg) {}

    public ArrayList<Integer> getIDsByUsuario(int idUsuario) {
        ArrayList<Integer> ids = new ArrayList<>();
        String sql = "SELECT reg.idregistro_respuestas " +
                "FROM registro_respuestas reg " +
                "INNER JOIN enc_has_formulario ehf " +
                "    ON reg.idenc_has_formulario = ehf.idenc_has_formulario " +
                "WHERE ehf.enc_idusuario = ?";
        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Integer id = rs.getInt(1);
                    ids.add(id);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;

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

            ps.setDate(1, new Date(registro.getFechaRegistro().getTime()));
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


    public ArrayList<Integer> countRegByForm(int idUser) {
        String sql = "SELECT " +
                "   f.idformulario, " +
                "   f.nombre AS nombre_formulario, " +
                "   COUNT(rr.idregistro_respuestas) AS total_registros_completados " +
                "FROM enc_has_formulario ehf_principal " +
                "JOIN usuario u_principal " +
                "   ON ehf_principal.enc_idusuario = u_principal.idusuario " +
                "JOIN formulario f " +
                "   ON ehf_principal.idformulario = f.idformulario " +
                "JOIN usuario u_zona " +
                "   ON u_principal.idzona = u_zona.idzona " +
                "JOIN enc_has_formulario ehf_zona " +
                "   ON u_zona.idusuario = ehf_zona.enc_idusuario " +
                "   AND ehf_principal.idformulario = ehf_zona.idformulario " +
                "LEFT JOIN registro_respuestas rr " +
                "   ON ehf_zona.idenc_has_formulario = rr.idenc_has_formulario " +
                "   AND rr.estado = 'C' " +
                "WHERE " +
                "   u_principal.idusuario = ? " +  // Parámetro para el idusuario
                "GROUP BY " +
                "   f.idformulario, f.nombre";
        ArrayList<Integer> totales = new ArrayList<>();

        try (Connection con = this.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUser);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                totales.add(rs.getInt(3));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totales;
    }

}

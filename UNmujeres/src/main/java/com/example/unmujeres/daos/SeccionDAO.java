package com.example.unmujeres.daos;

import com.example.unmujeres.beans.Seccion;

import java.sql.*;

public class SeccionDAO extends BaseDAO {

    public int crearSeccion(Connection conn, Seccion seccion) throws SQLException {

        String sql = "INSERT INTO seccion (nombre_sec, idformulario) VALUES (?, ?)";

        try (Connection con = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, seccion.getNombreSec());
            ps.setInt(2, seccion.getFormulario().getIdFormulario());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("No se pudo crear la nueva seccion");
    }

}

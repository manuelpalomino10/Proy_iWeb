package com.example.unmujeres.daos;

import com.example.unmujeres.beans.Roles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RolesDAO extends BaseDAO {

    public List<Roles> listarRoles() throws SQLException {
        String sql = "SELECT idroles, nombre FROM roles where idroles != 1 ORDER BY idroles";
        List<Roles> zonas = new ArrayList<>();

        try (Connection con = this.getConnection();
             PreparedStatement p = con.prepareStatement(sql);
             ResultSet rs = p.executeQuery()) {

            while (rs.next()) {
                Roles rol = new Roles();
                rol.setIdRoles(rs.getInt("idroles"));
                rol.setNombre(rs.getString("nombre"));
                zonas.add(rol);
            }
        }

        return zonas;
    }

}

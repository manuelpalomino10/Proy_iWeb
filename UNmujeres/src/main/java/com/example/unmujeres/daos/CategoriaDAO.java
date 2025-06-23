package com.example.unmujeres.daos;

import com.example.unmujeres.beans.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoriaDAO extends BaseDAO {

    public ArrayList<Categoria> getCategorias() {
        ArrayList<Categoria> categorias = new ArrayList<Categoria>();

        String sql = "select * from categoria";

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria c = new Categoria();
                c.setIdCategoria(rs.getInt("idcategoria"));
                c.setNombre(rs.getString("nombre"));

                categorias.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categorias;
    }


}

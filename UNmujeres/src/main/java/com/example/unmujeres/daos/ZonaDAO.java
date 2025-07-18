package com.example.unmujeres.daos;



import com.example.unmujeres.beans.Zona;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ZonaDAO extends BaseDAO {
    /**
     * Lee todas las zonas de la tabla `zona` y las devuelve como lista de beans Zona.
     */
    public List<Zona> listarZonas() throws SQLException {
        String sql = "SELECT idzona, nombre FROM zona ORDER BY idzona";
        List<Zona> zonas = new ArrayList<>();

        try (Connection con = this.getConnection();
             PreparedStatement p = con.prepareStatement(sql);
             ResultSet rs = p.executeQuery()) {

            while (rs.next()) {
                Zona z = new Zona(
                        rs.getInt("idzona"),
                        rs.getString("nombre")
                );
                zonas.add(z);
            }
        }

        return zonas;
    }

    public boolean existeZona(int idZona) throws SQLException {
        String sql = "SELECT 1 FROM zona WHERE idzona = ?";
        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idZona);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();  // true si encontró un resultado
            }
        }
    }

    public boolean existeDistritoEnZona(int idDistritos, int idZona) throws SQLException {
        String sql = "SELECT 1 FROM distrito WHERE iddistritos = ? AND idzona = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idDistritos);
            stmt.setInt(2, idZona);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

}

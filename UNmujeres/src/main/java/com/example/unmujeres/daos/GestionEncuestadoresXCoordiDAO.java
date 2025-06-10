package com.example.unmujeres.daos;

public class GestionEncuestadorXCoordiDAO extends BaseDAO{
    private final DataSource ds;
    public GestionEncuestadorXCoordiDAO(DataSource ds){ this.ds = ds; }

    // 1. Listar
    public List<Encuestador> listarPorZona(int coordiId) throws SQLException {
        String sql = /* tu SELECT con ? para coordiId */;
        try(var con=ds.getConnection();
            var ps=con.prepareStatement(sql)) {
            ps.setInt(1, coordiId);
            try(var rs=ps.executeQuery()) {
                var list = new ArrayList<Encuestador>();
                while(rs.next()) {
                    list.add(new Encuestador(
                            rs.getInt("idusuario"),
                            rs.getString("Nombre")+" "+rs.getString("Apellido"),
                            rs.getString("Email"),
                            rs.getString("Estado")
                    ));
                }
                return list;
            }
        }
    }

    // 2. Banear / Reactivar
    public void actualizarEstado(int idusuario, int nuevoEstado) throws SQLException {
        String sql = "UPDATE usuario SET estado=? WHERE idusuario=? AND roles_idroles=3";
        try(var con=ds.getConnection();
            var ps=con.prepareStatement(sql)) {
            ps.setInt(1, nuevoEstado);
            ps.setInt(2, idusuario);
            ps.executeUpdate();
        }
    }

    // 3. Asignar (borra y vuelve a insertar)
    public void asignarFormularios(int idusuario, List<Integer> formularios, int coordiId) throws SQLException {
        try(var con=ds.getConnection()) {
            con.setAutoCommit(false);
            try(var del=con.prepareStatement("DELETE FROM usuario_has_formulario WHERE usuario_idusuario=?")) {
                del.setInt(1, idusuario);
                del.executeUpdate();
            }
            try(var ins=con.prepareStatement(
                    "INSERT INTO usuario_has_formulario(usuario_idusuario,formulario_idformulario,codigo,asignado_por) VALUES(?,?,UUID(),?)")) {
                for(int fid: formularios) {
                    ins.setInt(1, idusuario);
                    ins.setInt(2, fid);
                    ins.setInt(3, coordiId);
                    ins.addBatch();
                }
                ins.executeBatch();
            }
            con.commit();
        }
    }
}

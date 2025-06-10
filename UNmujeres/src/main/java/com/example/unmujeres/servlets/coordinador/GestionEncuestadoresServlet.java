package com.example.unmujeres.servlets.coordinador;

import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.daos.CoordiGestionEncDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/gestion_encuestadores")
public class GestionEncuestadoresServlet extends HttpServlet {

    private CoordiGestionEncDAO dao;

    @Override
    public void init() throws ServletException {
        try {
            // Obtener DataSource del contexto de la aplicación
            DataSource ds = (DataSource) getServletContext().getAttribute("dataSource");
            dao = new CoordiGestionEncDAO(ds);
        } catch (Exception e) {
            throw new ServletException("Error inicializando el DAO", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            HttpSession session = req.getSession();
            Integer coordiId = (Integer) session.getAttribute("usuarioId");

            if (coordiId == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }

            List<Usuario> lista = dao.listarPorZona(coordiId);
            req.setAttribute("listaEncuestadores", lista);
            req.getRequestDispatcher("/gestion_encuestadores.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error al listar encuestadores", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer coordiId = (Integer) session.getAttribute("usuarioId");

        if (coordiId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        int id = Integer.parseInt(req.getParameter("idusuario"));

        try {
            switch(action) {
                case "banear":
                    // false = baneado (adaptado al boolean de Usuario)
                    dao.actualizarEstado(id, false);
                    break;
                case "asignar":
                    String[] fids = req.getParameterValues("formularios");
                    List<Integer> lista = fids == null ? List.of() :
                            Arrays.stream(fids).map(Integer::parseInt).toList();
                    dao.asignarFormularios(id, lista, coordiId);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción inválida");
                    return;
            }
            resp.sendRedirect(req.getContextPath() + "/gestion_encuestadores");
        } catch (SQLException e) {
            throw new ServletException("Error en operación con encuestador", e);
        }
    }
}
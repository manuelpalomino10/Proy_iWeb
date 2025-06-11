package com.example.unmujeres.servlets.coordinador;

import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.beans.Formulario;
import com.example.unmujeres.daos.CoordiGestionEncDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
            dao = new CoordiGestionEncDAO();
        } catch (Exception e) {
            throw new ServletException("Error inicializando el DAO", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer coordiId = (Integer) session.getAttribute("idUsuario");
        if (coordiId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        if ("get_formularios".equals(action)) {
            String idStr = req.getParameter("idusuario");
            int encId;
            try {
                encId = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Par\u00E1metro idusuario inv\u00E1lido");
                return;
            }
            try {
                List<Formulario> disponibles = dao.obtenerFormulariosDisponibles(coordiId, encId);
                List<Formulario> asignados = dao.obtenerFormulariosAsignados(encId);
                resp.setContentType("application/json");
                resp.getWriter().print(toJson(disponibles, asignados));
            } catch (SQLException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Error al obtener formularios");
                return;
            }
            return;
        }

        try {
            List<Usuario> lista = dao.listarPorZona(coordiId);
            req.setAttribute("listaEncuestadores", lista);
            req.getRequestDispatcher("/coordinador/gestion_encuestadores.jsp")
                    .forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error al listar encuestadores");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer coordiId = (Integer) session.getAttribute("idUsuario");

        if (coordiId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");

        String idParam = req.getParameter("idusuario");
        if (idParam == null || idParam.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Par\u00E1metro idusuario inv\u00E1lido");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Par\u00E1metro idusuario inv\u00E1lido");
            return;
        }

        try {
            switch(action) {
                case "banear":
                    // false = baneado (adaptado al boolean de Usuario)
                    dao.actualizarEstado(id, false);
                    break;
                case "desbanear":
                    dao.actualizarEstado(id, true);
                    break;
                case "asignar":
                    String[] fids = req.getParameterValues("formularios");
                    List<Integer> lista = (fids == null) ? List.of()
                            : Arrays.stream(fids)
                            .filter(s -> s != null && !s.isBlank())
                            .map(Integer::parseInt)
                            .toList();
                    dao.asignarFormularios(id, lista);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción inválida");
                    return;
            }
            resp.sendRedirect(req.getContextPath() + "/gestion_encuestadores");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error en operación con encuestador");
        }
    }
    private String toJson(List<Formulario> disponibles, List<Formulario> asignados) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("\"disponibles\":");
        sb.append('[');
        for (int i = 0; i < disponibles.size(); i++) {
            Formulario f = disponibles.get(i);
            sb.append('{')
                    .append("\"id\":").append(f.getIdFormulario()).append(',')
                    .append("\"nombre\":\"").append(escape(f.getNombre())).append("\"}");
            if (i < disponibles.size() - 1) sb.append(',');
        }
        sb.append(']');
        sb.append(',');
        sb.append("\"asignados\":");
        sb.append('[');
        for (int i = 0; i < asignados.size(); i++) {
            Formulario f = asignados.get(i);
            sb.append('{')
                    .append("\"id\":").append(f.getIdFormulario()).append(',')
                    .append("\"nombre\":\"").append(escape(f.getNombre())).append("\"}");
            if (i < asignados.size() - 1) sb.append(',');
        }
        sb.append(']');
        sb.append('}');
        return sb.toString();
    }

    private String escape(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
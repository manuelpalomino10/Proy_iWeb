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
        Integer openId = null;
        try {
            if (req.getParameter("idusuario") != null) {
                openId = Integer.parseInt(req.getParameter("idusuario"));

            }
        } catch (NumberFormatException ignored) { }

        try {
            List<Usuario> lista = dao.listarPorZona(coordiId);
            req.setAttribute("listaEncuestadores", lista);
            if (openId != null) {
                List<Formulario> disp = dao.obtenerFormulariosDisponibles(coordiId, openId);
                List<Formulario> asign = dao.obtenerFormulariosAsignados(openId);
                req.setAttribute("dispFormularios", disp);
                req.setAttribute("asigFormularios", asign);
                req.setAttribute("assignId", openId);
                for (Usuario u : lista) {
                    if (u.getIdUsuario() == openId) {
                        req.setAttribute("assignName", u.getNombres() + " " + u.getApellidos());
                        break;
                    }
                }
                req.setAttribute("showAssignModal", true);
            }
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
                    List<Integer> noDes = dao.asignarFormularios(id, lista);
                    String url = req.getContextPath() + "/gestion_encuestadores";
                    if (!noDes.isEmpty()) {
                        url += "?warn=No+se+desasignaron+formularios+porque+ya+tienen+respuestas";
                    } else {
                        url += "?success=Asignaciones+actualizadas";
                    }
                    resp.sendRedirect(url);
                    return;
                case "asignar_form":
                    int idFormAdd = Integer.parseInt(req.getParameter("idformulario"));
                    dao.asignarFormulario(id, idFormAdd);
                    resp.sendRedirect(req.getContextPath() + "/gestion_encuestadores?success=Formulario+asignado");
                    return;
                case "desasignar_form":
                    int idFormDel = Integer.parseInt(req.getParameter("idformulario"));
                    boolean ok = dao.desasignarFormulario(id, idFormDel);
                    String rurl = req.getContextPath() + "/gestion_encuestadores";
                    if (ok) {
                        rurl += "?success=Asignaciones+actualizadas";
                    } else {
                        rurl += "?warn=No+se+puede+desasignar+este+formulario+porque+ya+tiene+respuestas";
                    }
                    resp.sendRedirect(rurl);
                    return;
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
}
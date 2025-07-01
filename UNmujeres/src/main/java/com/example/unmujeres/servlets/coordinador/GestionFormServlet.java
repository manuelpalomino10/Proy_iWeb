package com.example.unmujeres.servlets.coordinador;

import com.example.unmujeres.beans.Categoria;
import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.daos.CategoriaDAO;
import com.example.unmujeres.daos.GestionFormDao;
import com.example.unmujeres.dtos.FormularioDto;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@WebServlet(name = "GestionFormServlet", value = "/coordinador/GestionFormServlet")
public class GestionFormServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        Usuario user = (Usuario) session.getAttribute("usuario");
        int idUser = user.getIdUsuario();

        GestionFormDao formularioDao = new GestionFormDao();
        CategoriaDAO categoriaDAO = new CategoriaDAO();

        ArrayList<Categoria> categorias = categoriaDAO.getCategorias();
        request.setAttribute("categorias", categorias);

        String catParam = request.getParameter("idCategoria");
        int idCategoria = 0;
        if (catParam != null && !catParam.isEmpty()) {
            try {
                idCategoria = Integer.parseInt(catParam);
            } catch (NumberFormatException ignored) {
            }
        }

        ArrayList<FormularioDto> list = formularioDao.listar(idUser,idCategoria);

        request.setAttribute("selectedCategoria", idCategoria);
        request.setAttribute("lista", list);
        RequestDispatcher rd = request.getRequestDispatcher("/coordinador/gestionFormularios.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GestionFormDao gestionFormDao = new GestionFormDao();

        String idParam = request.getParameter("id");
        String action = request.getParameter("action"); // <-- Obtener el parámetro 'action'

        String message = "";
        String type = "error"; // Por defecto, si algo sale mal

        if (idParam == null || idParam.isEmpty() || action == null || action.isEmpty()) {
            message = "Parámetros incompletos para cambiar el estado del formulario.";

        } else {
            try {
                int idFormulario = Integer.parseInt(idParam);

                switch (action) {
                    case "activar":
                        if (gestionFormDao.cambiarEstado(idFormulario, true)) {
                            message = "Formulario activado exitosamente.";
                            type = "success";
                        } else {
                            message = "Error al activar el formulario.";
                        }
                        break;

                    case "desactivar":
                        if (gestionFormDao.cambiarEstado(idFormulario, false)) {
                            message = "Formulario desactivado exitosamente.";
                            type = "success";
                        } else {
                            message = "Error al desactivar el formulario.";
                        }
                        break;

                    case "eliminar":
                        if (gestionFormDao.eliminarFormularioSiMenorA12(idFormulario)) {
                            message = "Formulario eliminado correctamente.";
                            type = "success";
                        } else {
                            message = "No se puede eliminar. El formulario tiene 12 o más respuestas.";
                        }
                        break;

                    default:
                        message = "Acción no reconocida.";
                }

            } catch (NumberFormatException e) {
                message = "ID de formulario inválido.";
            } catch (Exception e) {
                message = "Error inesperado: " + e.getMessage();
                e.printStackTrace();
            }
        }

        // Redirigir de vuelta a la página de gestión de formularios con el mensaje
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);

        if ("success".equals(type)) {
            request.getSession().setAttribute("success", message);
            response.sendRedirect(request.getContextPath() + "/coordinador/GestionFormServlet");

        } else {
            request.getSession().setAttribute("error", message);
            response.sendRedirect(request.getContextPath() + "/coordinador/GestionFormServlet");

        }
    }
}

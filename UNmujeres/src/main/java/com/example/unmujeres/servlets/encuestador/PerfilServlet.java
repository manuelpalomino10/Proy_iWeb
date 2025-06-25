package com.example.unmujeres.servlets.encuestador;

import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.daos.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;

@WebServlet("/encuestador/perfil")
public class PerfilServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

        //
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Usuario usuario = usuarioDAO.obtenerUsuarioConDistrito(usuarioSesion.getIdUsuario());

            if (usuario == null) {
                request.setAttribute("error", "Usuario no encontrado");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            //
            byte[] fotoBytes = usuario.getFotoBytes();
            if (fotoBytes != null && fotoBytes.length > 0) {
                String base64Image = Base64.getEncoder().encodeToString(fotoBytes);
                request.setAttribute("fotoBase64", base64Image);
                session.setAttribute("fotoBase64", base64Image);
            } else {
                request.setAttribute("fotoVacia", true);
                session.removeAttribute("fotoBase64");
            }

            request.setAttribute("usuario", usuario);
            request.getRequestDispatcher("/encuestador/perfil.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error de base de datos: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}

//fin del doGet







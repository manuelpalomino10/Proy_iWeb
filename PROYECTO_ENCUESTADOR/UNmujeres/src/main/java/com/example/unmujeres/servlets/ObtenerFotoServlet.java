package com.example.unmujeres.servlets;


import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.daos.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/obtenerFoto")
public class ObtenerFotoServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer idUsuario = Integer.parseInt(request.getParameter("id"));

        Usuario usuario = usuarioDAO.obtenerUsuarioPorId(idUsuario);
        if (usuario != null && usuario.getFotoBytes() != null) {
            response.setContentType("image/jpeg");
            response.getOutputStream().write(usuario.getFotoBytes());
        }
    }
}
package com.example.unmujeres.servlets.administrador;

import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.daos.UsuarioDAO;
import com.example.unmujeres.dtos.UsuarioDto;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "UsuarioServlet", value = "/UsuarioServlet")
public class UsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        ArrayList<UsuarioDto> list = usuarioDAO.listar();

       //mandar lista a la vista -> listaUsuarios
        request.setAttribute("lista", list);
        RequestDispatcher rd = request.getRequestDispatcher("administrador/listaUsuarios.jsp");
        rd.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UsuarioDao usuarioDAO = new UsuarioDao();
        int id = Integer.parseInt(request.getParameter("id"));
        boolean estadoActual = Boolean.parseBoolean(request.getParameter("estado"));
        boolean nuevoEstado = !estadoActual;

        usuarioDAO.cambiarEstado(id, nuevoEstado);

        response.sendRedirect("UsuarioServlet");
    }
}

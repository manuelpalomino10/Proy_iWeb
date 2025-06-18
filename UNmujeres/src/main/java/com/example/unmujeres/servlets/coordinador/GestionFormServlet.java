package com.example.unmujeres.servlets.coordinador;

import com.example.unmujeres.daos.GestionFormDao;
import com.example.unmujeres.dtos.FormularioDto;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "GestionFormServlet", value = "/coordinador/GestionFormServlet")
public class GestionFormServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        GestionFormDao formularioDao = new GestionFormDao();
        ArrayList<FormularioDto> list = formularioDao.listar();

        //mandar lista a la vista -> listaUsuarios
        request.setAttribute("lista", list);
        RequestDispatcher rd = request.getRequestDispatcher("/coordinador/GestionFormularios.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GestionFormDao gestionFormDao = new GestionFormDao();
        int id = Integer.parseInt(request.getParameter("id"));
        boolean estadoActual = Boolean.parseBoolean(request.getParameter("estado"));
        boolean nuevoEstado = !estadoActual;

        gestionFormDao.cambiarEstado(id, nuevoEstado);

        response.sendRedirect("/coordinador/GestionFormServlet");
    }
}

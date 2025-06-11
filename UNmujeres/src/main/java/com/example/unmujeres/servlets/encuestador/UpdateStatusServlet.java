package com.example.unmujeres.servlets.encuestador;

import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.daos.RespuestaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/updateStatus")
public class UpdateStatusServlet extends HttpServlet {
    private RespuestaDAO respuestaDAO;

    @Override
    public void init() throws ServletException {
        respuestaDAO = new RespuestaDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Validar usuario autenticado
        if (usuario == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("{\"status\":\"error\",\"message\":\"Usuario no autenticado\"}");
            out.flush();
            return;
        }

        try {
            int registroId = Integer.parseInt(request.getParameter("registroId"));
            String estado = request.getParameter("estado");
            if (!estado.equals("C") && !estado.equals("B")) {
                throw new IllegalArgumentException("Estado inválido: debe ser 'C' o 'B'");
            }

            respuestaDAO.updateResponse(registroId, registroId, estado);
            request.setAttribute("mensaje", "Estado actualizado correctamente");
            response.sendRedirect("/Sistema/success.jsp");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Datos inválidos: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}

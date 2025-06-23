package com.example.unmujeres.servlets.administrador;

import com.example.unmujeres.daos.EstadisticasCuidadoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/administrador/DashboardCuidado")
public class DashboardServiceServlet extends HttpServlet {

    private final EstadisticasCuidadoDAO estadisticasDAO = new EstadisticasCuidadoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            Map<Integer, Map<String, Integer>> respuestasSiNo = estadisticasDAO.obtenerTodasRespuestasSiNo();

            // Puedes hacer esto si sabes los ID y quieres pasarlos por separado
            request.setAttribute("respuesta9", respuestasSiNo.get(9));
            request.setAttribute("respuesta11", respuestasSiNo.get(11));
            request.setAttribute("respuesta12", respuestasSiNo.get(12));
            request.setAttribute("respuesta13", respuestasSiNo.get(13));
            request.setAttribute("respuesta16", respuestasSiNo.get(16));
            request.setAttribute("respuesta17", respuestasSiNo.get(17));
            request.setAttribute("respuesta20", respuestasSiNo.get(20));
            request.setAttribute("respuesta21", respuestasSiNo.get(21));
            request.setAttribute("respuesta23", respuestasSiNo.get(23));
            request.setAttribute("respuesta24", respuestasSiNo.get(24));
            request.setAttribute("respuesta26", respuestasSiNo.get(26));
            request.setAttribute("respuesta27", respuestasSiNo.get(27));
            request.setAttribute("respuesta28", respuestasSiNo.get(28));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/DashboardCuidado.jsp").forward(request, response);
    }
}




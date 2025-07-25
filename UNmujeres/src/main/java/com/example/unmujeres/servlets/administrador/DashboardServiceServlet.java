package com.example.unmujeres.servlets.administrador;

import com.example.unmujeres.beans.EstadisticasFormulario;
import com.example.unmujeres.beans.Formulario;
import com.example.unmujeres.daos.EstadisticasCuidadoDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
@WebServlet("/administrador/DashboardCuidado")
public class DashboardServiceServlet extends HttpServlet {
    private final EstadisticasCuidadoDAO estadisticasDAO = new EstadisticasCuidadoDAO();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            List<Formulario> formularios = estadisticasDAO.listarFormulariosActivos();

            int idFormularioSeleccionado = obtenerIdFormulario(request);
            int idF = idFormularioSeleccionado;
            if (formularios.stream().noneMatch(f -> f.getIdFormulario() == idF)) {
                idFormularioSeleccionado=1;
            }

            EstadisticasFormulario estadisticas = estadisticasDAO.obtenerEstadisticasPorFormulario(idFormularioSeleccionado);


            request.setAttribute("formularios", formularios);
            request.setAttribute("formularioSeleccionado", idFormularioSeleccionado);
            request.setAttribute("estadisticas", estadisticas);

        } catch (SQLException e) {
            request.setAttribute("error", "Error al cargar datos: " + e.getMessage());
            e.printStackTrace();
        }

        request.getRequestDispatcher("/administrador/DashboardCuidado.jsp").forward(request, response);
    }

    private int obtenerIdFormulario(HttpServletRequest request) {
        String param = request.getParameter("formularioId");
        if (param != null && !param.isEmpty()) {
            try {
                return Integer.parseInt(param);
            } catch (NumberFormatException e) {
                return 1;
            }
        } else {return 1;}
//        return (param != null && !param.isEmpty()) ? Integer.parseInt(param) : 1;
    }
}
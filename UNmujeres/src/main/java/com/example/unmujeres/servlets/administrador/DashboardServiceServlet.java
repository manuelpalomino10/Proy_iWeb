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
            // 1. Asistencia a guarderías
            Map<String, Integer> asistenciaGuarderia = estadisticasDAO.obtenerAsistenciaGuarderia();
            request.setAttribute("asistenciaGuarderia", asistenciaGuarderia);

            // 2. Motivos por los que no usan guarderías
            Map<String, Integer> motivosNoGuarderia = estadisticasDAO.obtenerMotivosNoGuarderia();
            request.setAttribute("motivosNoGuarderia", motivosNoGuarderia);

            // 3. Enfermedades en adultos mayores
            Map<String, Integer> adultosMayoresEnfermos = estadisticasDAO.obtenerAdultosMayoresEnfermos();
            request.setAttribute("adultosMayoresEnfermos", adultosMayoresEnfermos);

            // 4. Hogares con niños y adultos mayores
            Map<String, Integer> hogaresNinosAncianos = estadisticasDAO.obtenerHogaresConNinosYAncianos();
            request.setAttribute("hogaresNinosAncianos", hogaresNinosAncianos);

            // 5. Tiempo promedio de cuidado
            double tiempoPromedio = estadisticasDAO.obtenerTiempoPromedioCuidado();
            request.setAttribute("tiempoPromedioCuidado", tiempoPromedio);

            // jsp
            request.getRequestDispatcher("/DashboardCuidado.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al cargar");
        }
    }
}


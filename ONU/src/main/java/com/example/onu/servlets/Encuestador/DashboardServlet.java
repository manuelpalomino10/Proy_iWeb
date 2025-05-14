package com.example.onu.servlets;


import com.example.onu.daos.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // -------------------------------------------------
            EstadisticasDAO estadisticasDAO = new EstadisticasDAO();

            HttpSession session = request.getSession();

            Integer idusuario = (Integer) session.getAttribute("idusuario");
            if (idusuario == null) {
                idusuario = 5; // Valor por defecto para pruebas
            }
            //-------------------------------------------------

            Map<String, Object> avance = estadisticasDAO.calcularAvance(idusuario);
            int borradores = estadisticasDAO.contarBorradores();
            int completados = estadisticasDAO.contarFormulariosCompletados();
            int completadosUltimos7Dias = estadisticasDAO.contarCompletadosUltimos7Dias();
            int formulariosAsignados = estadisticasDAO.contarFormulariosAsignados(idusuario);
            Map<String, Integer> formulariosPorCategoria = estadisticasDAO.obtenerRespuestasCompletadasPorFormulario(idusuario);
            List<Map.Entry<String, Integer>> listaFormularios = new ArrayList<>(formulariosPorCategoria.entrySet());

            //-------------------------------------------------
            request.setAttribute("listaFormularios", listaFormularios);
            request.setAttribute("avanceResultados", avance);
            request.setAttribute("borradores", borradores);
            request.setAttribute("completados", completados);
            request.setAttribute("completadosUltimos7Dias", completadosUltimos7Dias);
            request.setAttribute("formulariosAsignados", formulariosAsignados);
            request.setAttribute("formulariosPorCategoria", formulariosPorCategoria);
            request.getRequestDispatcher("index.jsp").forward(request, response);


        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al obtener los datos: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
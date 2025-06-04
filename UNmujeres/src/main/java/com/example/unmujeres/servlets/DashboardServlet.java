package com.example.unmujeres.servlets;

import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.daos.EstadisticasDAO;
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

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        //
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // -------------------------------------------------
            EstadisticasDAO estadisticasDAO = new EstadisticasDAO();

            int idUsuario = usuario.getIdUsuario();
            //-------------------------------------------------

            Map<String, Object> avance = estadisticasDAO.calcularAvance(idUsuario);
            int borradores = estadisticasDAO.contarBorradores(idUsuario);
            int completados = estadisticasDAO.contarFormulariosCompletados(idUsuario);
            int completadosUltimos7Dias = estadisticasDAO.contarCompletadosUltimos7Dias();
            int formulariosAsignados = estadisticasDAO.contarFormulariosAsignados(idUsuario);
            Map<String, Integer> formulariosPorCategoria = estadisticasDAO.obtenerRespuestasCompletadasPorFormulario(idUsuario);
            List<Map.Entry<String, Integer>> listaFormularios = new ArrayList<>(formulariosPorCategoria.entrySet());

            // --------- NUEVAS estadísticas ---------
            String ultimoFormularioRegistrado = estadisticasDAO.obtenerUltimoFormularioRegistrado();
            int formulariosPorVencer = estadisticasDAO.contarFormulariosPorVencerPronto();
            int totalRespuestas = estadisticasDAO.contarTotalRespuestasRegistradas(idUsuario);
            int formulariosAsignadosHoy = estadisticasDAO.contarFormulariosAsignadosHoy();

            //-------------------------------------------------
            request.setAttribute("listaFormularios", listaFormularios);
            request.setAttribute("avanceResultados", avance);
            request.setAttribute("borradores", borradores);
            request.setAttribute("completados", completados);
            request.setAttribute("completadosUltimos7Dias", completadosUltimos7Dias);
            request.setAttribute("formulariosAsignados", formulariosAsignados);
            request.setAttribute("formulariosPorCategoria", formulariosPorCategoria);

            // Nuevos atributos
            request.setAttribute("ultimoFormularioRegistrado", ultimoFormularioRegistrado);
            request.setAttribute("formulariosPorVencer", formulariosPorVencer);
            request.setAttribute("totalRespuestas", totalRespuestas);
            request.setAttribute("formulariosAsignadosHoy", formulariosAsignadosHoy);
            request.getRequestDispatcher("index.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al obtener los datos: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
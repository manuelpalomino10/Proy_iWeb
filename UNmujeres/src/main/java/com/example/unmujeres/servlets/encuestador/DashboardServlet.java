package com.example.unmujeres.servlets.encuestador;

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
import java.util.List;
import java.util.Map;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {

    private final EstadisticasDAO estadisticasDAO = new EstadisticasDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        System.out.println("ID Usuario en sesión: " + usuario.getIdUsuario());

        int rol = usuario.getIdroles();

        // DEBUG
        System.out.println("[DEBUG] DashboardServlet - ID Usuario: " + usuario.getIdUsuario()
                + ", Rol: " + rol);
        int idUsuario = usuario.getIdUsuario();
        try {
            switch (rol) {
                case 1: // Administrador
                    cargarDatosAdmin(request);
                    request.getRequestDispatcher("/administrador/indexAdmin.jsp").forward(request, response);
                    break;

                case 3: // Encuestador
                    cargarDatosEncuestador(request, response, usuario.getIdUsuario());
                    request.getRequestDispatcher("/encuestador/index.jsp").forward(request, response);
                    break;

                case 2: // Coordinador
                    System.out.println("[DEBUG] doGet - Iniciando carga de datos para coordinador con ID: " + idUsuario );
                    cargarDatosCoordinador(request, response, usuario.getIdUsuario());
                    request.getRequestDispatcher("/coordinador/indexCoordi.jsp").forward(request, response);
                    break;

                default:
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Rol no autorizado: " + rol);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error en base de datos");
        }
    }

    private void cargarDatosAdmin(HttpServletRequest request) throws SQLException {
        // Estadísticas para dashboard de administrador
        request.setAttribute("totalUsuarios", estadisticasDAO.getTotalUsuarios());
        request.setAttribute("coordinadoresActivos", estadisticasDAO.getCoordinadoresActivos());
        request.setAttribute("encuestadoresActivos", estadisticasDAO.getEncuestadoresActivos());
        request.setAttribute("respuestasPorZona", estadisticasDAO.getRespuestasPorZona());
        request.setAttribute("distribucionUsuarios", estadisticasDAO.getDistribucionUsuarios());
        request.setAttribute("formulariosActivos", estadisticasDAO.getFormulariosActivos());
        request.setAttribute("promedioRespuestas", estadisticasDAO.getPromedioRespuestasPorEncuestador());
        request.setAttribute("usuariosDesactivados", estadisticasDAO.getUsuariosDesactivados());
        request.setAttribute("zonaMasActiva", estadisticasDAO.getZonaConMasRespuestas());
        request.setAttribute("respuestas30Dias", estadisticasDAO.getRespuestasPorZona30Dias());
        request.setAttribute("respuestasUltimaSemana", estadisticasDAO.getRespuestasUltimaSemana());
        request.setAttribute("progresoFormularios", estadisticasDAO.getProgresoFormulariosPorZona());
        request.setAttribute("totalRespuestas", estadisticasDAO.getTotalRespuestas());
    }

    private void cargarDatosEncuestador(HttpServletRequest request, HttpServletResponse response, int idUsuario) throws SQLException, ServletException, IOException {
        // Estadísticas para dashboard de encuestador
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

        request.setAttribute("ultimoFormularioRegistrado", ultimoFormularioRegistrado);
        request.setAttribute("formulariosPorVencer", formulariosPorVencer);
        request.setAttribute("totalRespuestas", totalRespuestas);
        request.setAttribute("formulariosAsignadosHoy", formulariosAsignadosHoy);




    }



    private void cargarDatosCoordinador(HttpServletRequest request, HttpServletResponse response, int idCoordinador) throws SQLException {
        // Estadísticas para dashboard de coordinador
        System.out.println("[DEBUG] cargarDatosCoordinador - Iniciando para idCoordinador: " + idCoordinador);
        int totalEncuestadores = estadisticasDAO.getTotalEncuestadoresCoordinador(idCoordinador);
        int encuestadoresActivos = estadisticasDAO.getEncuestadoresActivosCoordinador(idCoordinador);
        int encuestadoresBaneados = estadisticasDAO.getEncuestadoresBaneadosCoordinador(idCoordinador);
        int formulariosAsignados = estadisticasDAO.getFormulariosAsignadosCoordinador(idCoordinador);
        Map<String, Integer> respuestasPorZona = estadisticasDAO.getRespuestasPorZonaCoordinador(idCoordinador);
        Map<String, Double> porcentajeActivosVsBaneados = estadisticasDAO.getPorcentajeEncuestadoresActivosVsBaneadosCoordinador(idCoordinador);


        // En cargarDatosCoordinador
        System.out.println("[DEBUG] idCoordinador: " + idCoordinador);
        System.out.println("[DEBUG] totalEncuestadores: " + totalEncuestadores);


        // Logs
        System.out.println("[DEBUG] cargarDatosCoordinador - idCoordinador: " + idCoordinador);
        System.out.println("[DEBUG] totalEncuestadores: " + totalEncuestadores);
        System.out.println("[DEBUG] encuestadoresActivos: " + encuestadoresActivos);
        System.out.println("[DEBUG] encuestadoresBaneados: " + encuestadoresBaneados);
        System.out.println("[DEBUG] formulariosAsignados: " + formulariosAsignados);
        System.out.println("[DEBUG] respuestasPorZona: " + respuestasPorZona);
        System.out.println("[DEBUG] porcentajeActivosVsBaneados: " + porcentajeActivosVsBaneados);

        // Atributos para las métricas del dashboard
        request.setAttribute("totalEncuestadores", totalEncuestadores);
        request.setAttribute("encuestadoresActivos", encuestadoresActivos);
        request.setAttribute("encuestadoresBaneados", encuestadoresBaneados);
        request.setAttribute("formulariosAsignados", formulariosAsignados);

        // Atributos para los gráficos
        request.setAttribute("respuestasPorZona", respuestasPorZona);
        request.setAttribute("porcentajeActivosVsBaneados", porcentajeActivosVsBaneados);
    }
}


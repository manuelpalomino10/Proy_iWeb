package com.example.unmujeres.servlets.administrador;

import com.example.unmujeres.beans.Formulario;
import com.example.unmujeres.beans.Zona;
import com.example.unmujeres.daos.CoordiGestionEncDAO;
import com.example.unmujeres.daos.FormularioDAO;
import com.example.unmujeres.daos.RegistroCordiDao;
import com.example.unmujeres.daos.administrador.ZonaDao;
import com.example.unmujeres.utils.EmailUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException; // Mantener la importación
import java.util.ArrayList;

@WebServlet(name = "CrearCordiServlet", value = "/administrador/CrearCordiServlet")
public class CrearCordiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ZonaDao zonaDao = new ZonaDao();
        try {
            ArrayList<Zona> listaZonas = zonaDao.obtenerZonas();
            request.setAttribute("listaZonas", listaZonas);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar las zonas. Intente nuevamente.");
            request.setAttribute("listaZonas", new ArrayList<Zona>());
        }

        String exito = request.getParameter("exito");
        request.setAttribute("registroExitoso", "true".equals(exito));

        request.getRequestDispatcher("/administrador/registrarCordi.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String dniStr = request.getParameter("DNI");
        String correo = request.getParameter("correo");
        String idZonaStr = request.getParameter("idZona");

        // --- 1. Validación de campos vacíos ---
        if (nombres == null || nombres.trim().isEmpty() ||
                apellidos == null || apellidos.trim().isEmpty() ||
                dniStr == null || dniStr.trim().isEmpty() ||
                correo == null || correo.trim().isEmpty() ||
                idZonaStr == null || idZonaStr.trim().isEmpty()) {

            request.setAttribute("error", "Todos los campos son obligatorios.");
            cargarZonasYValores(request, nombres, apellidos, dniStr, correo, idZonaStr);
            request.getRequestDispatcher("/administrador/registrarCordi.jsp").forward(request, response);
            return;
        }

        nombres = nombres.trim();
        apellidos = apellidos.trim();
        dniStr = dniStr.trim();
        correo = correo.trim();
        idZonaStr = idZonaStr.trim();

        // --- 2. Validación de formato de DNI: exactamente 8 dígitos numéricos ---
        if (!dniStr.matches("\\d{8}")) {
            request.setAttribute("error", "El DNI debe tener exactamente 8 dígitos numéricos.");
            cargarZonasYValores(request, nombres, apellidos, dniStr, correo, idZonaStr);
            request.getRequestDispatcher("/administrador/registrarCordi.jsp").forward(request, response);
            return;
        }

        // --- 3. Validación de formato de correo ---
        if (!correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            request.setAttribute("error", "El correo ingresado no tiene un formato válido.");
            cargarZonasYValores(request, nombres, apellidos, dniStr, correo, idZonaStr);
            request.getRequestDispatcher("/administrador/registrarCordi.jsp").forward(request, response);
            return;
        }

        int dni;
        try {
            dni = Integer.parseInt(dniStr);
        } catch (NumberFormatException e) {
            // Este catch es para el DNI, aunque el regex `\d{8}` ya debería prevenirlo.
            request.setAttribute("error", "El DNI debe ser un número válido.");
            cargarZonasYValores(request, nombres, apellidos, dniStr, correo, idZonaStr);
            request.getRequestDispatcher("/administrador/registrarCordi.jsp").forward(request, response);
            return;
        }

        int idZona;
        try {
            idZona = Integer.parseInt(request.getParameter("idZona"));
        } catch (NumberFormatException e) {
            ZonaDao zonaDao = new ZonaDao();
            try {
                request.setAttribute("listaZonas", zonaDao.obtenerZonas());
            } catch (SQLException ex) {
                request.setAttribute("error", "Error al obtener zonas: " + ex.getMessage());
            }
            request.setAttribute("error", "La zona seleccionada no es válida o no existe.");

            // Recupera los datos ingresados y vuelve a colocarlos como atributos
            request.setAttribute("nombres", request.getParameter("nombres"));
            request.setAttribute("apellidos", request.getParameter("apellidos"));
            request.setAttribute("DNI", request.getParameter("DNI"));
            request.setAttribute("correo", request.getParameter("correo"));

            request.getRequestDispatcher("/administrador/registrarCordi.jsp").forward(request, response);
            return;
        }


        // A partir de aquí, idZona es un número válido. Procedemos a las interacciones con la BD.
        ZonaDao zonaDao = new ZonaDao();
        RegistroCordiDao registroCordiDao = new RegistroCordiDao();

        // --- BLOQUE PARA OPERACIONES DE BASE DE DATOS Y LÓGICA DE NEGOCIO ---
        try {
            // 4. Validación de existencia de zona en la base de datos
            // Este método puede lanzar SQLException.
            boolean zonaExiste = zonaDao.existeZona(idZona); // Asegúrate que este método exista y funcione.
            if (!zonaExiste) { // Si la zona NO existe en la BD, entonces hay un error.
                request.setAttribute("error", "La zona seleccionada no es válida o no existe.");
                cargarZonasYValores(request, nombres, apellidos, dniStr, correo, idZonaStr);
                request.getRequestDispatcher("/administrador/registrarCordi.jsp").forward(request, response);
                return;
            }

            // 5. Validación de existencia de correo y DNI
            // Estos métodos pueden lanzar SQLException.
            boolean correoExiste = registroCordiDao.existeCorreo(correo);
            boolean dniExiste = registroCordiDao.existeDni(dni);

            if (correoExiste || dniExiste) {
                if (correoExiste && dniExiste) {
                    request.setAttribute("error", "El DNI y el correo ya están registrados.");
                } else if (correoExiste) {
                    request.setAttribute("error", "El correo ya está registrado.");
                } else { // dniExiste
                    request.setAttribute("error", "El DNI ya está registrado.");
                }
                cargarZonasYValores(request, nombres, apellidos, dniStr, correo, idZonaStr);
                request.getRequestDispatcher("/administrador/registrarCordi.jsp").forward(request, response);
                return;
            }

            // --- Si todas las validaciones pasan, procede con el registro ---
            String codigo = registroCordiDao.generarCodigoUnico(); // Puede lanzar SQLException
            int idNuevoCordi = registroCordiDao.insertarPendiente(nombres, apellidos, dni, correo, idZona, codigo); // Puede lanzar SQLException

            // Este método asignarNuevoCordi lanza RuntimeException en caso de SQLException interna.
            // Por eso es buena práctica tener un catch para RuntimeException en el doPost.
            asignarNuevoCordi(idNuevoCordi);

            // Enviar correo de verificación (no lanza SQLException)
            String base = request.getRequestURL().toString()
                    .replace(request.getRequestURI(), request.getContextPath());
            String link = base + "/verify?code=" + codigo;
            String cuerpo = "Para activar su cuenta haga clic en el siguiente enlace: " +
                    "<a href='" + link + "'>Verificar correo</a>";
            EmailUtil.sendEmail(correo, "Verificación de cuenta", cuerpo);

            // Redirige al doGet() para mostrar mensaje de éxito y limpiar el formulario
            response.sendRedirect(request.getContextPath() + "/administrador/CrearCordiServlet?exito=true");

        } catch (SQLException e) {
            // Captura cualquier error SQL que ocurra durante las operaciones de BD en este bloque.
            e.printStackTrace(); // Imprime la traza para depuración.
            request.setAttribute("error", "Error en la base de datos al registrar el coordinador. Intente nuevamente.");
            cargarZonasYValores(request, nombres, apellidos, dniStr, correo, idZonaStr);
            request.getRequestDispatcher("/administrador/registrarCordi.jsp").forward(request, response);
        } catch (RuntimeException e) {
            // Captura la RuntimeException que asignaNuevoCordi() podría lanzar.
            e.printStackTrace();
            request.setAttribute("error", "Error interno al asignar formularios. Contacte al administrador.");
            cargarZonasYValores(request, nombres, apellidos, dniStr, correo, idZonaStr);
            request.getRequestDispatcher("/administrador/registrarCordi.jsp").forward(request, response);
        }
    }

    // --- Método de apoyo para evitar duplicación de código ---
    private void cargarZonasYValores(HttpServletRequest request, String nombres, String apellidos,
                                     String dniStr, String correo, String idZonaStr) {
        try {
            ZonaDao zonaDao = new ZonaDao();
            ArrayList<Zona> listaZonas = zonaDao.obtenerZonas();
            request.setAttribute("listaZonas", listaZonas);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("listaZonas", new ArrayList<Zona>());
            request.setAttribute("error", (request.getAttribute("error") != null ? request.getAttribute("error") + " " : "") + "No se pudieron cargar las zonas.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("listaZonas", new ArrayList<Zona>());
            request.setAttribute("error", (request.getAttribute("error") != null ? request.getAttribute("error") + " " : "") + "Error inesperado al cargar las zonas.");
        }

        request.setAttribute("nombres", nombres);
        request.setAttribute("apellidos", apellidos);
        request.setAttribute("DNI", dniStr);
        request.setAttribute("correo", correo);
        request.setAttribute("idZonaSeleccionada", idZonaStr);
    }

    // --- Método auxiliar para asignar formularios ---
    private void asignarNuevoCordi(int idCordi) {
        FormularioDAO formularioDAO = new FormularioDAO();
        ArrayList<Formulario> formularios = formularioDAO.getFormularios();

        for (Formulario form : formularios) {
            try {
                CoordiGestionEncDAO asigDAO = new CoordiGestionEncDAO();
                asigDAO.asignarFormulario(idCordi, form.getIdFormulario());
            } catch (SQLException e) {
                // Aquí relanzamos como RuntimeException para que el doPost pueda capturarlo.
                // Si quieres un manejo más granular, podrías hacer que asignarNuevoCordi lance SQLException.
                System.err.println("Error al asignar formulario " + form.getIdFormulario() + " al coordinador " + idCordi + ": " + e.getMessage());
                throw new RuntimeException("Error al asignar formularios al nuevo coordinador.", e);
            }
        }
    }
}

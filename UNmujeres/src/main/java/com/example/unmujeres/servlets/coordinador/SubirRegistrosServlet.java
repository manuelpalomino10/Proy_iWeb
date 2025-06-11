package com.example.unmujeres.servlets.coordinador;

import com.example.unmujeres.beans.EncHasFormulario;
import com.example.unmujeres.beans.RegistroRespuestas;
import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.daos.EncHasFormularioDAO;
import com.example.unmujeres.daos.FormularioDAO;
import com.example.unmujeres.daos.RegistroRespuestasDAO;
import com.example.unmujeres.daos.RespuestaDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@MultipartConfig
@WebServlet(name = "SubirRegistrosServlet", value = "/SubirRegistrosServlet")
public class SubirRegistrosServlet extends HttpServlet {

    EncHasFormularioDAO ehfDAO = new EncHasFormularioDAO();
    FormularioDAO formularioDAO = new FormularioDAO();
    RegistroRespuestasDAO registroDAO = new RegistroRespuestasDAO();
    RespuestaDAO respuestaDAO = new RespuestaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        // Obtener sesión sin crear una nueva
        HttpSession session = request.getSession(false);
        if (session == null) {
            System.out.println("No hay session");
            response.sendRedirect(request.getContextPath() + "/Sistema/login.jsp");
            return;
        }
        Usuario user = (Usuario) session.getAttribute("usuario");
        if (user == null || user.getIdUsuario()==0 || user.getIdroles()==0) {
            System.out.println("No hay usuario en session");
            session.setAttribute("error", "Sesión inválida o usuario no autenticado.");
            response.sendRedirect(request.getContextPath() + "/Sistema/login.jsp");
            return;
        }
        int idUser = user.getIdUsuario();
        int userRole = user.getIdroles();
        //int idUser = 7;

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        RequestDispatcher view;

        switch (action) {
            case "lista":
                try {
                    System.out.println("Se consulto lista de asignados de coordi");

                    ArrayList<EncHasFormulario> asignaciones = ehfDAO.getByUser(idUser);
                    ArrayList<Integer> totales = registroDAO.countRegByForm();
                    // 9. Enviar a vista
                    request.setAttribute("asignaciones", asignaciones);
                    request.setAttribute("totalesRegistros", totales);
                    view = request.getRequestDispatcher("/coordinador/listaFormularios.jsp");
                    view.forward(request, response);

                } catch (Exception e) {
                    e.printStackTrace();
                    //request.getRequestDispatcher("/WEB-INF/vistas/error.jsp").forward(request, response);
                }
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);    // Obtener la sesión sin crear una nueva

        if (session == null) {
            System.out.println("No hay session");
            response.sendRedirect(request.getContextPath() + "/Sistema/login.jsp");
            return;
        }
        Usuario user = (Usuario) session.getAttribute("usuario");
        if (user == null || user.getIdUsuario()==0 || user.getIdroles()==0) {
            System.out.println("No hay usuario en session");
            session.setAttribute("error", "Sesión inválida o usuario no autenticado.");
            response.sendRedirect(request.getContextPath() + "/Sistema/login.jsp");
            return;
        }


        System.out.println("Iniciando proceso de subir registros");
        //Validación del parámetro idEhf.
        String idEhfParam = request.getParameter("idEhf");
        System.out.println("Parametro del asig es: " + idEhfParam);
        if (idEhfParam == null || idEhfParam.trim().isEmpty()) {
            //request.setAttribute("error", "Imposible encontrar un formulario nulo, es requerido elegir uno.");
            session.setAttribute("error", "Imposible encontrar un formulario nulo, es requerido elegir uno.");
            request.getRequestDispatcher("/SubirRegistrosServlet").forward(request, response);
            return;
        }

        int idEhf;
        try {
            idEhf = Integer.parseInt(idEhfParam);
        } catch (NumberFormatException e) {
            System.out.println("Error en el parametro idEhf");
            //.getSession().setAttribute("error", e.getMessage());
            session.setAttribute("error", "Imposible encontrar ese formulario.");
            response.sendRedirect(request.getContextPath() + "/SubirRegistrosServlet");
            return;
        }

        //Obtención del archivo CSV subido (parámetro"csvFile").
        Part filePart = request.getPart("csvFile");
        if (filePart == null || filePart.getSize() == 0) {  //Validar que exista y no esté vacío
            //request.setAttribute("error", "Debe seleccionar un archivo.");
            session.setAttribute("error", "Debe seleccionar un archivo.");
            response.sendRedirect(request.getContextPath() + "/SubirRegistrosServlet");
            return;
        }
        String fileName = filePart.getSubmittedFileName();
        if (fileName == null || !fileName.toLowerCase().endsWith(".csv")) {    //Validar extensión .csv
            //request.setAttribute("error", "El archivo debe ser de tipo CSV (.csv)");
            session.setAttribute("error", "El archivo debe ser de tipo CSV (.csv).");
            response.sendRedirect(request.getContextPath() + "/SubirRegistrosServlet");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(filePart.getInputStream(), "UTF-8"))) {

            // 3. Saltar las primeras 6 líneas de cabecera
            for (int i = 0; i < 7; i++) {
                reader.readLine();
            }

            String line;
            String delimiter = ";";

            while ((line = reader.readLine()) != null) {
                Map<Integer, String> respuestasTexto = new HashMap<>();
                // Para cada línea, se crea un nuevo registro en la tabla Registro que asocia el idEhf.
                RegistroRespuestas nuevoRegistro = new RegistroRespuestas();
                nuevoRegistro.setFechaRegistro(new Date(System.currentTimeMillis()));
                nuevoRegistro.setEstado("C"); // Siempre en estado C
                nuevoRegistro.setEncHasFormulario(ehfDAO.getById(idEhf));
                int idRegistro = registroDAO.crearRegistroRespuestas(nuevoRegistro);
                System.out.println("Nuevo Registro id es: "+idRegistro);

                // Se separa la línea en celdas según el delimitador.
                String[] cells = line.split(delimiter);
                int preguntaIndex = 1;
                for (String cell : cells) {
                    String respuesta = cell.trim();
                    // Manejar valores vacíos y placeholder
                    if (respuesta.isEmpty() || respuesta.equals("-")) {
                        respuestasTexto.put(preguntaIndex, null);
                    } else {
                        respuestasTexto.put(preguntaIndex, respuesta);
                    }
                    preguntaIndex++;
                }
                respuestaDAO.guardarRespuestas(idRegistro, respuestasTexto);
            }
            System.out.println("Exito al subir registros por csv");
            session.setAttribute("success", "Registros importados correctamente");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            session.setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/SubirRegistrosServlet");
            return;
        } finally {
            response.sendRedirect(request.getContextPath() + "/SubirRegistrosServlet");
        }
    }

    @Override
    public void init() throws ServletException {
        // Código de inicialización (opcional)
    }

}
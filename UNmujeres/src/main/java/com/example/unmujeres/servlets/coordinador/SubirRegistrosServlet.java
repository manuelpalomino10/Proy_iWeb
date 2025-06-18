package com.example.unmujeres.servlets.coordinador;

import com.example.unmujeres.beans.EncHasFormulario;
import com.example.unmujeres.beans.OpcionPregunta;
import com.example.unmujeres.beans.Pregunta;
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
import java.util.*;

@MultipartConfig
@WebServlet(name = "SubirRegistrosServlet", value = "/SubirRegistrosServlet")
public class SubirRegistrosServlet extends HttpServlet {

    EncHasFormularioDAO ehfDAO = new EncHasFormularioDAO();
    FormularioDAO formularioDAO = new FormularioDAO();
    RegistroRespuestasDAO registroDAO = new RegistroRespuestasDAO();
    RespuestaDAO respuestaDAO = new RespuestaDAO();
    // otro servlet creo        OpcionPreguntaDAO opcionDAO = new OpcionPreguntaDAO();

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
        if (userRole != 2) {
            System.out.println("rol incorrecto: "+userRole);
            request.setAttribute("error", "Acceso no permitido.");
            response.sendRedirect(request.getContextPath() + "/Sistema/login.jsp");
            return;
        }

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        RequestDispatcher view;

        switch (action) {
            case "lista":
                try {
                    System.out.println("Se consulto lista de asignados de coordi");

                    ArrayList<EncHasFormulario> asignaciones = ehfDAO.getByUser(idUser);
                    ArrayList<Integer> totales = registroDAO.countRegByForm(idUser);
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

//        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
//        System.out.println("accion de dopost es: " + action);

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


        // desde aqui en otro servelt creo
//        switch (action){
//            case "crear":
//                System.out.println("se hace en dopost: " + action);
//                String acto = request.getParameter("acto");
//
//                String nuevoEstado = "B";
//                if (Objects.equals(acto, "borrador")) {
//                    nuevoEstado = "B";
//                } else if (Objects.equals(acto, "completado")) {
//                    nuevoEstado = "C";
//                } else {
//                    // Valor por defecto o generar error
//                    nuevoEstado = "B";
//                }
//                System.out.println("se hace acto para: " +nuevoEstado);
//                try {
//                    int idEncHasFormulario = Integer.parseInt(request.getParameter("idasignacion"));
//                    System.out.println("el id de asignacion es: " +idEncHasFormulario);
//                    // 1. Crear registro principal
//                    RegistroRespuestas nuevoRegistro = new RegistroRespuestas();
//                    nuevoRegistro.setFechaRegistro(new Date(System.currentTimeMillis()));
//                    nuevoRegistro.setEstado(nuevoEstado); // Borrador
//                    nuevoRegistro.setEncHasFormulario(ehfDAO.getById(idEncHasFormulario));
//
//                    int idRegistro = registroDAO.crearRegistroRespuestas(nuevoRegistro);
//
//                    System.out.println("Nuevo Registro id es: "+idRegistro);
//
//                    // 2. Procesar respuestas
//                    Map<String, String[]> parametros = request.getParameterMap();
//                    Map<Integer, String> respuestasTexto = new HashMap<>();
//                    Map<Integer, List<Integer>> respuestasOpciones = new HashMap<>();
//
//                    for (String paramName : parametros.keySet()) {
//                        if (paramName.startsWith("pregunta_")) {
//
//                            String[] parts = paramName.split("_");
//                            int idPregunta = Integer.parseInt(parts[1]);
//
//                            System.out.println("Id pregunta es " + idPregunta);
//
//                            // Manejar checkbox (pueden tener múltiples valores)
//                            if (parts.length > 2) {
//                                String[] valores = request.getParameterValues(paramName);
//                                if (valores != null) {
//                                    for (String valor : valores) {
//                                        respuestasOpciones.computeIfAbsent(idPregunta, k -> new ArrayList<>())
//                                                .add(Integer.parseInt(valor));
//                                    }
//                                }
//                            } else {
//                                String valor = request.getParameter(paramName);
////                                respuestasTexto.put(idPregunta, (valor != null) ? valor.trim() : " ");
//                                if (valor != null && !valor.trim().isEmpty()) {
//                                    respuestasTexto.put(idPregunta, valor.trim());
//                                } else {respuestasTexto.put(idPregunta, null);}
//                            }
//                        }
//                    }
//
//                    // 3. Guardar respuestas
//                    if (!respuestasTexto.isEmpty()) {
//                        respuestaDAO.guardarRespuestas(idRegistro, respuestasTexto);
//                    }
//
//                    if (!respuestasOpciones.isEmpty()) {
//                        respuestaDAO.guardarRespuestasOpciones(idRegistro, respuestasOpciones);
//                    }
//
//                    // Redirigir a edición para permitir guardar como borrador o completar
//                    response.sendRedirect( "/SubirRegistrosServlet");
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    request.setAttribute("error", "Error al guardar las respuestas");
//                    request.getRequestDispatcher("/error.jsp").forward(request, response);
//                }
//                break;
//        }
        // hasta aqui en tro servlet


    }

    @Override
    public void init() throws ServletException {
        // Código de inicialización (opcional)
    }

}
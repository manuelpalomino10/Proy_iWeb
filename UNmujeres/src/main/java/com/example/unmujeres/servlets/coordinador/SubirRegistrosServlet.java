package com.example.unmujeres.servlets.coordinador;

import com.example.unmujeres.beans.EncHasFormulario;
import com.example.unmujeres.beans.OpcionPregunta;
import com.example.unmujeres.beans.Pregunta;
import com.example.unmujeres.beans.RegistroRespuestas;
import com.example.unmujeres.daos.*;
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
    OpcionPreguntaDAO opcionDAO = new OpcionPreguntaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

//        // Obtener ID
//        HttpSession session = request.getSession();
//        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
//        // Verificar
//        if (idUsuario == null) {
//            response.sendRedirect(request.getContextPath() + "/login.jsp");
//            return;
//        }

        int idUser = 7;

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        RequestDispatcher view;


        switch (action) {
            case "crear":
                System.out.println("Se crea nueva respuesta");
                int idFormulario = Integer.parseInt(request.getParameter("id_form"));
                PreguntaDAO preguntaDAO = new PreguntaDAO();

                // Obtener preguntas con sus opciones
                ArrayList<Pregunta> preguntas = preguntaDAO.getPreguntasConOpcionesPorFormulario(idFormulario);
                ArrayList<OpcionPregunta> opciones1 = opcionDAO.getByForm(idFormulario);

                request.setAttribute("preguntas", preguntas);
                request.setAttribute("opciones", opciones1);
                request.setAttribute("idformulario", idFormulario);
                request.getRequestDispatcher("coordinador/RegistrarRespuesta.jsp").forward(request, response);
                break;

            case "lista":
                try {
                    System.out.println("Se consulto lista de asignados de coordi");

                    ArrayList<EncHasFormulario> asignaciones = ehfDAO.getByUser(idUser); // ID hardcodeado

                    // 9. Enviar a vista
                    request.setAttribute("asignaciones", asignaciones);
                    view = request.getRequestDispatcher("coordinador/listaFormularios.jsp");
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

        System.out.println("Iniciando proceso de subir registros");
        //Validación del parámetro idEhf.
        String idEhfParam = request.getParameter("idEhf");
        System.out.println("Parametro del asig es: " + idEhfParam);
        if (idEhfParam == null || idEhfParam.trim().isEmpty()) {
            request.setAttribute("error", "Imposible encontrar un formulario nulo, es requerido elegir uno.");
            //request.getRequestDispatcher("resultado.jsp").forward(request, response);
            request.getRequestDispatcher("coordinador/listaFormularios.jsp").forward(request, response);
            return;
        }

        int idEhf;
        try {
            idEhf = Integer.parseInt(idEhfParam);
        } catch (NumberFormatException ex) {
            System.out.println("Error en el parametro idEhf");
            request.setAttribute("error", "Imposible encontrar un formulario con ese valor");
            request.getRequestDispatcher("/SubirRegistrosServlet").forward(request, response);
            return;
        }

        //Obtención del archivo CSV subido (parámetro"csvFile").
        Part filePart = request.getPart("csvFile");
        if (filePart == null || filePart.getSize() == 0) {
            request.setAttribute("error", "Debe seleccionar un archivo CSV.");
            request.getRequestDispatcher("/SubirRegistrosServlet").forward(request, response);
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(filePart.getInputStream(), "UTF-8"));

            // 3. Saltar las primeras 6 líneas (por ejemplo, cabecera o información no relevante)
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
            reader.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        System.out.println("accion de dopost es: " + action);
        RequestDispatcher view;
        switch (action){
            case "crear":
                System.out.println("se hace en dopost: " + action);
                String acto = request.getParameter("acto");

                String nuevoEstado = "B";
                if (Objects.equals(acto, "borrador")) {
                    nuevoEstado = "B";
                } else if (Objects.equals(acto, "completado")) {
                    nuevoEstado = "C";
                } else {
                    // Valor por defecto o generar error
                    nuevoEstado = "B";
                }
                System.out.println("se hace acto para: " +nuevoEstado);
                try {
                    int idEncHasFormulario = Integer.parseInt(request.getParameter("idasignacion"));
                    System.out.println("el id de asignacion es: " +idEncHasFormulario);
                    // 1. Crear registro principal
                    RegistroRespuestas nuevoRegistro = new RegistroRespuestas();
                    nuevoRegistro.setFechaRegistro(new Date(System.currentTimeMillis()));
                    nuevoRegistro.setEstado(nuevoEstado); // Borrador
                    nuevoRegistro.setEncHasFormulario(ehfDAO.getById(idEncHasFormulario));

                    int idRegistro = registroDAO.crearRegistroRespuestas(nuevoRegistro);

                    System.out.println("Nuevo Registro id es: "+idRegistro);

                    // 2. Procesar respuestas
                    Map<String, String[]> parametros = request.getParameterMap();
                    Map<Integer, String> respuestasTexto = new HashMap<>();
                    Map<Integer, List<Integer>> respuestasOpciones = new HashMap<>();

                    for (String paramName : parametros.keySet()) {
                        if (paramName.startsWith("pregunta_")) {

                            String[] parts = paramName.split("_");
                            int idPregunta = Integer.parseInt(parts[1]);

                            System.out.println("Id pregunta es " + idPregunta);

                            // Manejar checkbox (pueden tener múltiples valores)
                            if (parts.length > 2) {
                                String[] valores = request.getParameterValues(paramName);
                                if (valores != null) {
                                    for (String valor : valores) {
                                        respuestasOpciones.computeIfAbsent(idPregunta, k -> new ArrayList<>())
                                                .add(Integer.parseInt(valor));
                                    }
                                }
                            } else {
                                String valor = request.getParameter(paramName);
//                                respuestasTexto.put(idPregunta, (valor != null) ? valor.trim() : " ");
                                if (valor != null && !valor.trim().isEmpty()) {
                                    respuestasTexto.put(idPregunta, valor.trim());
                                } else {respuestasTexto.put(idPregunta, null);}
                            }
                        }
                    }

                    // 3. Guardar respuestas
                    if (!respuestasTexto.isEmpty()) {
                        respuestaDAO.guardarRespuestas(idRegistro, respuestasTexto);
                    }

                    if (!respuestasOpciones.isEmpty()) {
                        respuestaDAO.guardarRespuestasOpciones(idRegistro, respuestasOpciones);
                    }

                    // Redirigir a edición para permitir guardar como borrador o completar
                    response.sendRedirect( "/SubirRegistrosServlet");

                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("error", "Error al guardar las respuestas");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                }
                break;
        }


        response.sendRedirect(request.getContextPath() + "/SubirRegistrosServlet");

    }

    @Override
    public void init() throws ServletException {
        // Código de inicialización (opcional)
    }

}
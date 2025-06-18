package com.example.unmujeres.servlets.encuestador;
import com.example.unmujeres.beans.OpcionPregunta;
import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.daos.OpcionPreguntaDAO;
import com.example.unmujeres.daos.PreguntaDAO;
import com.example.unmujeres.daos.RegistroRespuestasDAO;


import com.example.unmujeres.beans.*;
import com.example.unmujeres.daos.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.*;

@WebServlet(
        name = "VerFormulariosServlet",
        value = {"/shared/VerFormulariosServlet", "/encuestador/ServletA", "/coordinador/VerFormulariosServlet", "/encuestador/VerFormulariosServlet"}, // Múltiples rutas de acceso
        initParams = {
                @WebInitParam(name = "config", value = "default") // Parámetro de configuración
        }
)
public class VerFormulariosServlet extends HttpServlet {

    private String configValue;

    @Override
    public void init() throws ServletException {
        // Cargar parámetro de configuración
        configValue = getServletConfig().getInitParameter("config");
    }

    EncHasFormularioDAO ehfDAO = new EncHasFormularioDAO();
    RegistroRespuestasDAO registroDAO = new RegistroRespuestasDAO();
    FormularioDAO formularioDAO = new FormularioDAO();
    RespuestaDAO respuestaDAO = new RespuestaDAO();
    PreguntaDAO preguntaDAO = new PreguntaDAO();
    OpcionPreguntaDAO opcionDAO = new OpcionPreguntaDAO();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
//        if (session == null) {
//            response.sendRedirect(request.getContextPath() + "/Sistema/login.jsp");
//            return;
//        }
        Usuario user = (Usuario) session.getAttribute("usuario");
//        if (user == null || user.getIdUsuario()==0 || user.getIdroles()==0) {
//            session.setAttribute("error", "Sesión inválida o usuario no autenticado.");
//            response.sendRedirect(request.getContextPath() + "/Sistema/login.jsp");
//            return;
//        }
        int idUser = user.getIdUsuario();
        int userRole = user.getIdroles();
//        if (userRole != 3 || userRole != 2) {
//            System.out.println("rol incorrecto: "+userRole);
//            request.setAttribute("error", "Acceso no permitido.");
//            response.sendRedirect(request.getContextPath() + "/Sistema/login.jsp");
//            return;
//        }

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        RequestDispatcher view;

        switch (action) {
            case "lista":
                try {
                    System.out.println("Se consulto lista de asignados");

                    // 2. Inicializa arreglo de datos para vista
                    ArrayList<Map<String, Object>> datos = new ArrayList<>();

                    // 3. Obtener arreglo de asignaciones
                    ArrayList<EncHasFormulario> asignaciones = ehfDAO.getByUser(idUser);
                    List<Integer> IDsAsignaciones = new ArrayList<>();
                    //  para cada asignacion
                    for (EncHasFormulario asignacion : asignaciones) {
                        IDsAsignaciones.add(asignacion.getIdEncHasFormulario());
                        //System.out.println("\n1. Asignacion extraída: " + asignacion.getIdEncHasFormulario());
                        // 4. Obtener formulario relacionado por el id, obtenido de la asignacion
                        Formulario formulario = formularioDAO.getById(asignacion.getFormulario().getIdFormulario());
                        // si existe formulario y esta activo (formulario.estado=1),
                        if (formulario != null && formulario.isEstado()) {
                            //System.out.println("2. Formulario de asignacion existe y activo: " + formulario.getIdFormulario());
                            // inicializa un item para agregar a datos
                            Map<String, Object> item = new LinkedHashMap<>();
                            // 5. Informacion de formulario
                            item.put("id_formulario", formulario.getIdFormulario());
                            item.put("nombre_formulario", formulario.getNombre());
                            item.put("fecha_limite", formulario.getFechaLimite());
                            item.put("registros_esperados", formulario.getRegistrosEsperados());

                            // 7. Datos asignacion ehf
                            item.put("id_asignacion", asignacion.getIdEncHasFormulario());
                            item.put("fecha_asignacion", asignacion.getFechaAsignacion());
                            //System.out.println("3. Fecha de asignacion: " + asignacion.getFechaAsignacion());

                            // 8. Datos registro, arreglo de registros en asignacion
                            ArrayList<RegistroRespuestas> registros = registroDAO.getByEhf(asignacion.getIdEncHasFormulario());
                            item.put("registros_completados", registros.size());
                            //System.out.println("4. Cantidad registros completados: " + registros.size());

                            datos.add(item);
                        }
                    }
                    session.setAttribute("IDsAsignaciones", IDsAsignaciones);

                    // 9. Enviar a vista
                    request.setAttribute("datos", datos);
                    view = request.getRequestDispatcher("/encuestador/showAssignedForms.jsp");
                    view.forward(request, response);

                } catch (Exception e) {
                    e.printStackTrace();
                    //request.getRequestDispatcher("/WEB-INF/vistas/error.jsp").forward(request, response);
                }
                break;
            case "guardar":
                System.out.println("Se crea nueva respuesta");
                int idFormulario = Integer.parseInt(request.getParameter("id_form"));

                // Convertir a estructura para la vista
                ArrayList<Pregunta> preguntas = preguntaDAO.getPreguntasConOpcionesPorFormulario(idFormulario);
                ArrayList<OpcionPregunta> opciones1 = opcionDAO.getByForm(idFormulario);

                request.setAttribute("preguntas", preguntas);
                request.setAttribute("opciones", opciones1);
                request.setAttribute("idformulario", idFormulario);
                request.getRequestDispatcher("/encuestador/crearRespuesta.jsp").forward(request, response);

                break;

            case "historial":
                System.out.println("Se consulto historial");
                try {

                    // 2. Inicializar arreglos de datos (borradores y completados)
                    ArrayList<Map<String, Object>> datos1 = new ArrayList<>();
                    ArrayList<Map<String, Object>> datos2 = new ArrayList<>();

                    // 3. lista de borradores
                    ArrayList<EncHasFormulario> asignaciones = ehfDAO.getByUser(idUser);
                    for (EncHasFormulario asignacion : asignaciones) {
                        System.out.println("Asignacion id es: "+asignacion.getIdEncHasFormulario());

                        // 3.1. Informacion de registro, arreglo de registros en asignacion
                        ArrayList<RegistroRespuestas> registros = registroDAO.getByEhf(asignacion.getIdEncHasFormulario());
                        // 3.2. Obtener formulario relacionado por el id, obtenido de la asignacion
                        Formulario formulario = formularioDAO.getById(asignacion.getFormulario().getIdFormulario());
                        System.out.println("Form id es: "+formulario.getIdFormulario());

                        // para cada registro
                        for (RegistroRespuestas registro : registros) {
                            // 3.3. Validar estado de formulario y estado borrador de registro
                            if (registro != null && registro.getEstado().charAt(0)=='B' && formulario != null && formulario.isEstado()) {

                                // inicializa un item para agregar a datos
                                Map<String, Object> item1 = new LinkedHashMap<>();
                                // 3.5 Informacion de registro
                                item1.put("fecha_registro", registro.getFechaRegistro());
                                item1.put("id_registro", registro.getIdRegistroRespuestas()); // no se mostrara en vista, pero se usara para editar o descartar

                                // 3.4. Informacion de formulario
                                item1.put("id_formulario", formulario.getIdFormulario());
                                item1.put("nombre_formulario", formulario.getNombre());
                                item1.put("fecha_limite", formulario.getFechaLimite());

                                datos1.add(item1);
                            } else if (registro != null && registro.getEstado().charAt(0)=='C' && formulario != null && formulario.isEstado()) {

                                // inicializa un item para agregar a datos
                                Map<String, Object> item2 = new LinkedHashMap<>();
                                // 3.5 Informacion de registro
                                item2.put("fecha_registro", registro.getFechaRegistro());

                                Date fecha = registro.getFechaRegistro();

                                item2.put("id_registro", registro.getIdRegistroRespuestas()); // se deberia mstrar?

                                // 3.4. Informacion de formulario
                                item2.put("id_formulario", formulario.getIdFormulario());
                                item2.put("nombre_formulario", formulario.getNombre());
                                datos2.add(item2);
                            }
                        }
                        // 9. Pasar a vista
                        request.setAttribute("drafts", datos1);
                        request.setAttribute("records", datos2);
                    }
                    view = request.getRequestDispatcher("/encuestador/responsesHistory.jsp");
                    view.forward(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "editar":

                String idFormParam = request.getParameter("id_form");
                String idRegParam = request.getParameter("id");

                //validar parametros
                int idForm;
                int idReg;
                if (idFormParam != null && idRegParam != null) {
                    try {
                        idForm = Integer.parseUnsignedInt(idFormParam);
                        idReg = Integer.parseUnsignedInt(idRegParam);

                    } catch (NumberFormatException e) {
                        System.out.println("Parámetros de form o registro inválidos");
                        session.setAttribute("error", "Parámetros de form o registro inválidos");
                        response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet");
                        return;

//                    } catch (IllegalArgumentException e) {
//                    session.setAttribute("error", "Validar que pertenezca al usuario");
//                    response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet");
//                    return;
                    }
                } else {
                    session.setAttribute("error", "Los parámetros de formulario o registro no pueden ser nulos");
                    response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet");
                    return;
                }


                System.out.println("Se consulto el editor del registro id: " + idReg+" para form "+idForm);

                RegistroRespuestas registro = registroDAO.getById(idReg);

                if (registro == null) {
                    response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet");

                } else {
                    request.setAttribute("registro", registro);

                    ArrayList<OpcionPregunta> opciones = opcionDAO.getByForm(idForm);
                    System.out.println("opciones: "+opciones);
                    request.setAttribute("opciones", opciones);
                    for (OpcionPregunta opcion : opciones) {System.out.println("Opcion es: "+opcion.getIdOpcionPregunta()+" con idpregunta: "+opcion.getIdOpcionPregunta());}

                    ArrayList<Respuesta> respuestas = respuestaDAO.listaRespuestas(idReg);
                    request.setAttribute("respuestas", respuestas);
                    view = request.getRequestDispatcher("/encuestador/editResponse.jsp");
                    view.forward(request, response);
                }
                break;

            case "descartar":
                String idRegParam1 = request.getParameter("id");

                //validar parametro
                int IdReg1;
                if (idRegParam1 != null) {
                    try {
                        IdReg1 = Integer.parseUnsignedInt(idRegParam1);
                    } catch (NumberFormatException e) {
                        System.out.println("Parámetro de registro inválidos");
                        session.setAttribute("error", "Parámetros de form o registro inválidos");
                        response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet?action=historial");
                        return;

//                    } catch (IllegalArgumentException e) {
//                        session.setAttribute("error", "Validar que pertenezca al usuario");
//                        response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet?action=historial");
//                        return;
                    }
                } else {
                    session.setAttribute("error", "El parámetro de registro no puede ser nulo");
                    response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet?action=historial");
                    return;
                }

                try {

                    if(registroDAO.getById(IdReg1) != null){
                        System.out.println("Se eliminara el registro id: " + IdReg1);
                        registroDAO.delete(IdReg1);
                    }
                    response.sendRedirect(request.getContextPath()+"/encuestador/VerFormulariosServlet?action=historial");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        Usuario user = (Usuario) session.getAttribute("usuario");
        int idUser = user.getIdUsuario();
        int userRole = user.getIdroles();

        List<Integer> IDsAsignaciones = (List<Integer>) session.getAttribute("IDsAsignaciones");
        System.out.println("IDsAsignaciones: "+IDsAsignaciones);

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        System.out.println("accion de dopost es: " + action);
        RequestDispatcher view;

        switch (action) {

            case "editar":
                String acto1 = request.getParameter("acto");

                String nuevoEstado1;
                if (Objects.equals(acto1, "borrador")) {
                    nuevoEstado1 = "B";
                } else if (Objects.equals(acto1, "completado")) {
                    nuevoEstado1 = "C";
                } else {
                    // Valor por defecto o generar error
                    session.setAttribute("error", "No es un acto válido");
                    response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet?action=historial");
                    return;
                }

                String idRegParam = request.getParameter("id");

                //validar parametro
                int idReg;
                if (idRegParam != null) {
                    try {
                        idReg = Integer.parseUnsignedInt(idRegParam);

                    } catch (NumberFormatException e) {
                        System.out.println("Parámetro de registro inválidos");
                        session.setAttribute("error", "Parámetros de form o registro inválidos");
                        response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet?action=historial");
                        return;

//                    } catch (IllegalArgumentException e) {
//                        session.setAttribute("error", "Validar que pertenezca al usuario");
//                        response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet?action=historial");
//                        return;
                    }
                } else {
                    session.setAttribute("error", "El parámetro de registro no puede ser nulo");
                    response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet?action=historial");
                    return;
                }

                registroDAO.updateState(idReg,nuevoEstado1);
                System.out.println("Se ha actualizado el registro con id: " + idReg + "al estado: " + nuevoEstado1 + " en" + nuevoEstado1.getClass().getSimpleName());

                Map<String, String[]> parametroMap = request.getParameterMap();
                // Itera sobre los parámetros para identificar elementos que comiencen con "respuesta_"
                for (String paramName : parametroMap.keySet()) {
                    if (paramName.startsWith("respuesta_")) {
                        // Extrae el id de la pregunta del nombre del input (por ejemplo, "respuesta_45")
                        String idStr = paramName.substring("respuesta_".length());
                        try {
                            int idPregunta = Integer.parseInt(idStr);
                            // Obtiene el valor del input
                            String nuevaRespuesta = request.getParameter(paramName);
                            if (nuevaRespuesta != null) {
                                nuevaRespuesta = nuevaRespuesta.trim();
                            }
                            // Actualiza la respuesta para esa pregunta en el registro de respuestas
                            respuestaDAO.updateResponse(idReg, idPregunta, nuevaRespuesta);

                            System.out.println("Se actualizo una respuesta con id: " + idPregunta + "con el texto: " + nuevaRespuesta);

                        } catch (NumberFormatException e) {
                            System.err.println("ID de pregunta inválido en el parámetro: " + paramName);
                        }
                    }
                }

                    response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet?action=historial");

                break;

            case "guardar":

                String acto = request.getParameter("acto");
                System.out.println("se hace acto en dopost: " +acto);

                String nuevoEstado = "B";
                if (Objects.equals(acto, "borrador")) {
                    nuevoEstado = "B";
                } else if (Objects.equals(acto, "completado")) {
                    nuevoEstado = "C";
                } else {
                    session.setAttribute("error", "No es un acto válido");
                    response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet");
                    return;
                }
                System.out.println("se hace para: " +nuevoEstado);

                //String idFormParam = request.getParameter("id_form");
                String idEncHasFormularioParam = request.getParameter("idasignacion");

                //validar parametros
                //int idForm;
                int idEncHasFormulario;
                if (idEncHasFormularioParam != null) {
                    try {
                        idEncHasFormulario = Integer.parseUnsignedInt(idEncHasFormularioParam);

                        if (!IDsAsignaciones.contains(idEncHasFormulario)) {
                            System.out.println("parametro de idAsig no pertenece a usuario");
                            throw new IllegalArgumentException("No puedes generar respuestas para formularios no asignados");
                        } else {
                            int idForm = ehfDAO.getById(idEncHasFormulario).getFormulario().getIdFormulario();
                            System.out.println("id form es " + idForm);

                            ArrayList<Pregunta> preguntas = preguntaDAO.getPreguntasConOpcionesPorFormulario(idForm);
                            ArrayList<OpcionPregunta> opciones = opcionDAO.getByForm(idForm);
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Parámetros de form o asignacion inválidos");
                        session.setAttribute("error", "Parámetro de asignación inválido");
                        response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet");
                        return;

                    } catch (IllegalArgumentException e) {
                        session.setAttribute("error", e.getMessage());
                        response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet");
                        return;
                    }
                } else {
                    session.setAttribute("error", "El parámetro de asignación no puede ser nulo");
                    response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet");
                    return;
                }


                try {
                    System.out.println("el id de asignacion es: " +idEncHasFormulario);
                    // 1. Crear registro principal
                    RegistroRespuestas nuevoRegistro = new RegistroRespuestas();
                    nuevoRegistro.setFechaRegistro(new Date(System.currentTimeMillis()));
                    nuevoRegistro.setEstado(nuevoEstado);
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

                    if (userRole==3) {
                        response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet");

                    } else if (userRole==2) {
                        response.sendRedirect(request.getContextPath() + "/coordinador/SubirRegistrosServlet");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("error", "Error al guardar las respuestas");
                    session.setAttribute("error", "Error al guardar las respuestas");
                    if (userRole==3) {
                        response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet");

                    } else if (userRole==2) {
                        response.sendRedirect(request.getContextPath() + "/coordinador/SubirRegistrosServlet");
                    }
                }
            break;

        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Ejemplo básico para otros métodos HTTP
        response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
    }
}


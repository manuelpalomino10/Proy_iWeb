package com.example.unmujeres.servlets.encuestador;
import com.example.unmujeres.beans.OpcionPregunta;
import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.daos.OpcionPreguntaDAO;
import com.example.unmujeres.daos.PreguntaDAO;
import com.example.unmujeres.daos.RegistroRespuestasDAO;


import com.example.unmujeres.beans.*;
import com.example.unmujeres.daos.*;
import com.example.unmujeres.dtos.AsignacionDTO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import javassist.NotFoundException;
import javassist.tools.web.BadHttpRequest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.*;

@WebServlet(
        name = "VerFormulariosServlet",
        value = {"/shared/VerFormulariosServlet", "/encuestador/ServletA", "/coordinador/ServletA","/coordinador/VerFormulariosServlet", "/encuestador/VerFormulariosServlet"}, // Múltiples rutas de acceso
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

        Usuario user = (Usuario) session.getAttribute("usuario");
        int idUser = user.getIdUsuario();
        int userRole = user.getIdroles();
        String codEnc = user.getCodEnc();

        ArrayList<AsignacionDTO> asignaciones = ehfDAO.getByUser(idUser);

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        RequestDispatcher view;

        switch (action) {
            case "lista":
                if (userRole!=3) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acción inválida");
                    return;
                }
                try {
                    System.out.println("Se consulto lista de asignados");

                    // 1. Inicializa arreglo de datos para vista
                    ArrayList<Map<String, Object>> datos = new ArrayList<>();

                    // 2. De lo extraido. Para cada asignacion:
                    for (AsignacionDTO asignacion : asignaciones) {
                        //System.out.println("1. Asignacion extraída: " + asignacion.getIdAsignacion());
                        Formulario formulario = asignacion.getFormulario();
                        if (formulario != null) {
                            //System.out.println("2. Formulario de asignacion existe y activo: " + formulario.getIdFormulario());
                            // inicializa un item para agregar a datos
                            Map<String, Object> item = new LinkedHashMap<>();
                            // 3. Informacion de formulario
                            item.put("id_formulario", formulario.getIdFormulario());
                            item.put("nombre_formulario", formulario.getNombre());
                            item.put("fecha_limite", formulario.getFechaLimite());
                            item.put("registros_esperados", formulario.getRegistrosEsperados());

                            // 4. Datos asignacion ehf
                            item.put("id_asignacion", asignacion.getIdAsignacion());
                            item.put("fecha_asignacion", asignacion.getFechaAsignacion());
                            //System.out.println("3. Fecha de asignacion: " + asignacion.getFechaAsignacion());

                            // 5. Datos registro: esperados y completados para enc
                            item.put("registros_completados", asignacion.getTotalRegistros());
                            item.put("reg_esperados_enc", asignacion.getEsperadosEnc());

                            datos.add(item);
                        }
                    }

                    // 9. Enviar a vista
                    request.setAttribute("datos", datos);
                    view = request.getRequestDispatcher("/encuestador/showAssignedForms.jsp");
                    view.forward(request, response);

                } catch (Exception e) {
                    response.sendRedirect(getRedirectUrl(userRole));
                }
                break;
            case "guardar":
                System.out.println("Se crea nueva respuesta");

                String idFormParam1 = request.getParameter("id_form");

                int idFormulario;
                if (idFormParam1 != null) {
                    try {
                        idFormulario = Integer.parseUnsignedInt(idFormParam1);

                        Formulario formAsig = ehfDAO.getAsigByForm(idFormulario,idUser);
                        if (formAsig==null) {
//                            throw new IllegalArgumentException("No puede crear registros de formularios no asignados");
                            throw new IllegalArgumentException();
                        } else {
                            idFormulario = formAsig.getIdFormulario();
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Parámetro de form inválido");
                        session.setAttribute("error", "Parámetro de formulario inválido");
                        response.sendRedirect(getRedirectUrl(userRole));
                        return;

                    } catch (IllegalArgumentException e) {
                    session.setAttribute("error", e.getMessage());
                    response.sendRedirect(getRedirectUrl(userRole));
                    return;
                    }
                } else {
                    session.setAttribute("error", "El parámetro de formulario no puede ser nulo");
                    response.sendRedirect(getRedirectUrl(userRole));
                    return;
                }

                // Convertir a estructura para la vista
                ArrayList<Pregunta> preguntas = preguntaDAO.getPreguntasConOpcionesPorFormulario(idFormulario);
                ArrayList<OpcionPregunta> opciones1 = opcionDAO.getByForm(idFormulario);

                request.setAttribute("preguntas", preguntas);
                request.setAttribute("opciones", opciones1);
                request.getRequestDispatcher("/encuestador/crearRespuesta.jsp").forward(request, response);

                break;

            case "historial":
                if (userRole!=3) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acción inválida");
                    return;
                }
                System.out.println("Se consulto historial");
                try {
                    //1. Obtener registros de Usuario, de formularios activos
                    ArrayList<RegistroRespuestas> registros = registroDAO.getByUser(idUser);

                    // 2. Inicializar arreglos de datos (borradores y completados)
                    ArrayList<Map<String, Object>> datos1 = new ArrayList<>();
                    ArrayList<Map<String, Object>> datos2 = new ArrayList<>();

                    // 3. Separar B y C. Para cada registro:
                    for (RegistroRespuestas registro : registros) {
                        Formulario formulario = registro.getEncHasFormulario().getFormulario();
                        // 3.1. Lista de borradores: validar estado borrador de registro
                        if ("B".equals(registro.getEstado())) {
                            // inicializa un item para agregar a datos
                            Map<String, Object> item1 = new LinkedHashMap<>();
                            // 3.1.1 Informacion de registro
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                            String fRegFormat = registro.getFechaRegistro().format(formatter);
                            item1.put("fecha_registro", fRegFormat);
                            item1.put("id_registro", registro.getIdRegistroRespuestas()); // no se mostrara en vista, pero se usara para editar o descartar

                            // 3.1.2 Informacion de formulario
                            item1.put("id_formulario", formulario.getIdFormulario());
                            item1.put("nombre_formulario", formulario.getNombre());
                            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
                            String fLimFormat = formato.format(formulario.getFechaLimite());
                            item1.put("fecha_limite", fLimFormat);

                            datos1.add(item1);
                        // 3.2 Lista de completados: validar estado borrador de registro
                        } else if ("C".equals(registro.getEstado())) {
                            // inicializa un item para agregar a datos
                            Map<String, Object> item2 = new LinkedHashMap<>();
                            // 3.2.1 Informacion de registro
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                            String fRegFormat = registro.getFechaRegistro().format(formatter);
                            item2.put("fecha_registro", fRegFormat);
                            item2.put("id_registro", registro.getIdRegistroRespuestas()); // se deberia mstrar?

                            // 3.2.2 Informacion de formulario
                            item2.put("id_formulario", formulario.getIdFormulario());
                            item2.put("nombre_formulario", formulario.getNombre());
                            datos2.add(item2);
                        }
                    }
                    // 4. Pasar a vista
                    request.setAttribute("drafts", datos1);
                    request.setAttribute("records", datos2);

                    view = request.getRequestDispatcher("/encuestador/responsesHistory.jsp");
                    view.forward(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                    session.setAttribute("error", "Error inesperado");
                    response.sendRedirect(getRedirectUrl(userRole));
                }
                break;

            case "editar":
                String idRegParam = request.getParameter("id"); //ID de registro a editar

                //Validar parametro
                int idReg;
                RegistroRespuestas registro;
                try {
                    if (idRegParam == null || idRegParam.isEmpty()) {
                        throw new IllegalArgumentException("ID de Registro requerido");
                    }

                    idReg = Integer.parseUnsignedInt(idRegParam);

                    //Extrae borrador si existe y pertenece al usuario
                    registro = registroDAO.findEncDraftById(idReg,idUser);

                    if (registro == null) {
                        throw new NotFoundException("No se encontró el registro");
                    } else {
                        System.out.println("Se extrajo reg "+ registro.getIdRegistroRespuestas()+", estado: "+registro.getEstado());
                        int idForm = registro.getEncHasFormulario().getFormulario().getIdFormulario();
                        System.out.println("Se mustra editor de registro en "+registro.getEstado()+ " con ID: "+idReg+" para form "+idForm);

                        request.setAttribute("registro", registro);

                        ArrayList<OpcionPregunta> opciones = opcionDAO.getByForm(idForm);
                        request.setAttribute("opciones", opciones);

                        ArrayList<Respuesta> respuestas = respuestaDAO.listaRespuestas(idReg);
                        request.setAttribute("respuestas", respuestas);
                        view = request.getRequestDispatcher("/encuestador/editResponse.jsp");
                        view.forward(request, response);
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Parámetros de form o registro inválidos");
                    session.setAttribute("error", "Parámetro de registro inválido");
                    response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet?action=historial");
                    return;
                } catch (IllegalArgumentException | NotFoundException e) {
                    session.setAttribute("error", e.getMessage());
                    response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet?action=historial");
                    return;
                }

                break;

            case "descartar":
                if (userRole!=3) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acción inválida");
                    return;
                }

                String idRegParam1 = request.getParameter("id");

                //Validar parametro
                int idReg1;
                RegistroRespuestas registro1;
                try {
                    if (idRegParam1 == null || idRegParam1.isEmpty()) {
                        throw new IllegalArgumentException("ID de Registro requerido");
                    }

                    idReg1 = Integer.parseUnsignedInt(idRegParam1);

                    //Extrae registro si existe, pertenece al usuario y es borrador
                    registro1 = registroDAO.findEncDraftById(idReg1,idUser);

                    if (registro1 == null) {
                        throw new NotFoundException("No se encontró el borrador");
                    } else {
                        System.out.println("Se eliminara el registro id: " + idReg1);
                        registroDAO.delete(idReg1);

                    }

                } catch (NumberFormatException e) {
                    System.out.println("Parámetro de registro inválido");
                    session.setAttribute("error", "Parámetros de form o registro inválidos");
                    response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet?action=historial");
                    return;
                } catch (IllegalArgumentException | NotFoundException e) {
                    session.setAttribute("error", e.getMessage());
                    response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet?action=historial");
                    return;
                } finally {
                    response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet?action=historial");
                }

            break;

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
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

        //ArrayList<EncHasFormulario> asignaciones = ehfDAO.getByUser(idUser);

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        System.out.println("accion de dopost es: " + action);
        RequestDispatcher view;

        switch (action) {

            case "editar":

                if (userRole!=3) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acción inválida");
                    return;
                }

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
                // 1. Validar parámetro
                RegistroRespuestas reg;

                try {
                    if (idRegParam == null || idRegParam.isEmpty()) {throw new IllegalArgumentException("ID de Registro requerido");}

                    int idReg = Integer.parseUnsignedInt(idRegParam);

                    reg = registroDAO.findEncDraftById(idReg,idUser);

                    if (reg == null) {
                        throw new NotFoundException("No se encontró el borrador");
                    } else {

                        // 2. Validar respuestas
                        // 2.1 Validar existencia y relacion
                        Map<Integer, String> errores = new HashMap<>();
                        Map<Integer, String> inputs = new HashMap<>();

                        ArrayList<Respuesta> respuestas = respuestaDAO.listaRespuestas(idReg);
                        int idPrimeraPregunta = respuestas.get(0).getPregunta().getIdPregunta();
                        Map<Integer, Pregunta> idR_preguntaMap = new HashMap<>();
                        for (Respuesta respuesta : respuestas) {
                            idR_preguntaMap.put(respuesta.getIdRespuesta(), respuesta.getPregunta());
                        }

                        Map<String, String[]> parametroMap = request.getParameterMap();
                        // Itera sobre los parámetros para identificar elementos que comiencen con "respuesta_"
                        for (String paramName : parametroMap.keySet()) {
                            if (paramName.startsWith("respuesta_")) {
                                // Extrae el id de la pregunta del nombre del input (por ejemplo, "respuesta_45")
                                String idStr = paramName.substring("respuesta_".length());
                                int idRespuesta = -1;
                                try {
                                    //int idR = Integer.parseInt(idStr);
                                    idRespuesta = Integer.parseUnsignedInt(idStr);

                                    if (!idR_preguntaMap.containsKey(idRespuesta)) {
                                        System.err.println("Mapa de respuestas-preguntas no contiene key ID de respuesta: " + idRespuesta);
//                                        throw new IllegalArgumentException("Solicitud malformada");
                                        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                                        return;
//                                    } else {
//                                        System.out.println("Mapa de respuestas-preguntas contiene key ID de respuesta: " + idRespuesta);
//                                        Pregunta pregunta = idR_preguntaMap.get(idRespuesta);
                                    }


                                    // 2.2 Validar contenido textual
                                    String[] valores = parametroMap.get(paramName);
                                    String valor = (valores != null && valores.length > 0) ? valores[0] : null;
                                    // Aplica .trim() sólo si el valor no es null
                                    String valorNormalizado = (valor != null) ? valor.trim() : "";
                                    System.out.println(paramName+" value es: "+valorNormalizado);

                                    inputs.put(idRespuesta, valorNormalizado);

                                    Pregunta pregunta = idR_preguntaMap.get(idRespuesta);

                                    if ("B".equals(nuevoEstado1)) {
                                        if (valor != null && !valor.trim().isEmpty()) {
                                            // Solo validamos si se ingresó algún valor
                                            String errorMsg = validarPregunta(pregunta, valor);
                                            if (errorMsg != null) {
                                                errores.put(pregunta.getIdPregunta()-idPrimeraPregunta+1, errorMsg);
                                            }
                                        }
                                    } else {
                                        String errorMsg = validarPregunta(pregunta, valor);
                                        if (errorMsg != null) {
                                            errores.put(pregunta.getIdPregunta()-idPrimeraPregunta+1, errorMsg);}
                                    }
                                } catch (NumberFormatException e) {
                                    System.err.println("ID de pregunta inválido en el parámetro: " + paramName);
                                    throw new IllegalArgumentException("Solicitud malformada");
                                }
                            }
                        }
                        // 2.3 Validar que no existan duplicados ni sobrantes ni faltantes
                        if (!inputs.keySet().equals(idR_preguntaMap.keySet())) {
                            System.err.println("IDs recibidos de respuestas no se igualan a las esperadas (DB)");
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                            return;
//                            throw new IllegalArgumentException("Solicitud malformada");
                        }

                        // 3. Si hay errores de validación, reenvía al formulario para que el usuario corrija los datos.
                        if (!errores.isEmpty()) {
                            //request.setAttribute("error", errores);
                            session.setAttribute("validationErrors", errores);
                            session.setAttribute("valoresFormulario", inputs);
                            response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet?action=editar&id="+idReg);
                            return;
                        }

                        // 4. Si la validación fue exitosa, modificar las respuestas y el registro.
                        inputs.forEach((k, v) -> {respuestaDAO.updateResponse(idReg,k,v);});

                        registroDAO.updateState(idReg,nuevoEstado1);


                    }

//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
                } catch (IllegalArgumentException | NotFoundException e) {
                    session.setAttribute("error", e.getMessage());
                    //response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
                    response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet?action=historial");
                    return;
                }  catch (Exception e) {
                    e.printStackTrace();
                    session.setAttribute("error", "Error inesperado al editar registro");
                    response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet?action=historial");
                    return;
                }

                session.setAttribute("success", "Registro actualizado con éxito y guardado como "+acto1);
                response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet?action=historial");

                break;

            case "guardar":
                
                String acto = request.getParameter("acto");

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
                System.out.println("se hace para: " + nuevoEstado);

                String idEncHasFormularioParam = request.getParameter("idasignacion");
                // 1. Validar parametro
                try {
                    if (idEncHasFormularioParam == null || idEncHasFormularioParam.isEmpty()) {throw new IllegalArgumentException("ID de Asignación requerido");}

                    int idAsignacion = Integer.parseUnsignedInt(idEncHasFormularioParam);

                    EncHasFormulario asig = ehfDAO.getById(idAsignacion);

                    if (asig == null || asig.getUsuario() == null || !Objects.equals(asig.getUsuario().getIdUsuario(), idUser)) {
                        throw new NotFoundException("No encontrado");
                    } else {

                        // 2. Validar respuestas
                        // 2.1 Validar existencia y relacion
                        int idForm1 = asig.getFormulario().getIdFormulario();

                        Map<Integer, String> errores = new HashMap<>();
                        Map<Integer, String> inputs = new HashMap<>();

                        // Se recuperan las preguntas y opciones según el formulario
                        ArrayList<Pregunta> preguntas = preguntaDAO.getPreguntasConOpcionesPorFormulario(idForm1);
                        //System.out.println("preguntas: " + Arrays.toString(preguntas.toArray()));

                        Map<Integer, Pregunta> idP_preguntaMap = new HashMap<>();
                        int idPrimeraPregunta = preguntas.get(0).getIdPregunta();
                        for (Pregunta pregunta : preguntas) {
                            idP_preguntaMap.put(pregunta.getIdPregunta(), pregunta);
                        }
                        //ArrayList<OpcionPregunta> opciones = opcionDAO.getByForm(idForm1);

                        Map<String, String[]> parametroMap = request.getParameterMap();
                        for (String paramName : parametroMap.keySet()) {
                            if (paramName.startsWith("pregunta_")) {
                                String idPregStr = paramName.substring("pregunta_".length());
                                try {
                                    int idPregunta = Integer.parseUnsignedInt(idPregStr);

                                    if (!idP_preguntaMap.containsKey(idPregunta)) {
                                        System.err.println("Mapa de preguntas-preguntas no contiene key ID de pregunta: " + idPregunta);
                                        throw new IllegalArgumentException("Solicitud malformada");
//                                    } else {
//                                        Pregunta pregunta = idP_preguntaMap.get(idPregunta);
                                    }

                                    // 2.2 Validar contenido textual
                                    String[] valores = parametroMap.get(paramName);
                                    String valor = (valores != null && valores.length > 0) ? valores[0] : null;
                                    // Aplica .trim() sólo si el valor no es null
                                    String valorNormalizado = (valor != null) ? valor.trim() : "";
                                    System.out.println(paramName+" value es: "+valorNormalizado);

                                    inputs.put(idPregunta, valorNormalizado);

                                    Pregunta pregunta = idP_preguntaMap.get(idPregunta);

                                    if ("B".equals(nuevoEstado)) {
                                        if (valor != null && !valor.trim().isEmpty()) {
                                            // Solo validamos si se ingresó algún valor
                                            String errorMsg = validarPregunta(pregunta, valor);
                                            if (errorMsg != null) {
                                                errores.put(pregunta.getIdPregunta()-idPrimeraPregunta+1, errorMsg);
                                            }
                                        }
                                    } else {
                                        String errorMsg = validarPregunta(pregunta, valor);
                                        if (errorMsg != null) {
                                            errores.put(pregunta.getIdPregunta()-idPrimeraPregunta+1, errorMsg);
                                        }
                                    }

                                } catch (NumberFormatException e) {
                                    System.err.println("ID de pregunta inválido en el parámetro: " + paramName);

                                    return;
                                }
                            }
                        }
                        // 2.3 Validar que no existan duplicados ni sobrantes ni faltantes
                        if (!inputs.keySet().equals(idP_preguntaMap.keySet())) {
                            System.err.println("IDs recibidos de preguntas no se igualan a las esperadas (DB)");
                            throw new IllegalArgumentException("Solicitud malformada");
                        }

                        // 3. Si hay errores de validación, reenvía al formulario para que el usuario corrija los datos.
                        if (!errores.isEmpty()) {
                            //request.setAttribute("error", errores);
                            session.setAttribute("validationErrors", errores);
                            session.setAttribute("valoresFormulario", inputs);
                            response.sendRedirect(request.getContextPath() + "/encuestador/VerFormulariosServlet?action=guardar&id_form="+idForm1+"&id_asig="+idAsignacion);
                            return;
                        }

                        // 4. Si la validación fue exitosa, crear el registro principal.
                        RegistroRespuestas nuevoRegistro = new RegistroRespuestas();
                        nuevoRegistro.setFechaRegistro(LocalDateTime.now(ZoneId.of("America/Lima")));
                        nuevoRegistro.setEstado(nuevoEstado);
                        nuevoRegistro.setEncHasFormulario(asig);
                        int idRegistro = registroDAO.crearRegistroRespuestas(nuevoRegistro);
                        System.out.println("Nuevo Registro id es: " + idRegistro);

                        respuestaDAO.guardarRespuestas(idRegistro, inputs);

                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    session.setAttribute("error", "ID de asignacion no es numero valido");
                    response.sendRedirect(getRedirectUrl(userRole));
                    return;

                } catch (IllegalArgumentException | NotFoundException e) {
                    e.printStackTrace();
                    response.sendRedirect(getRedirectUrl(userRole));
                    session.setAttribute("error", e.getMessage());
//                    response.sendError(400, e.getMessage());
//                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect(getRedirectUrl(userRole));
                    return;
                }
                session.setAttribute("success", "Registro creado con éxito y guardado como "+acto);

                response.sendRedirect(getRedirectUrl(userRole));

            break;

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            break;

        }
    }


    // MÉTODO PRIVADO PARA VALIDAR EL VALOR DE UNA PREGUNTA
    private String validarPregunta(Pregunta pregunta, String valor) {
        // y, opcionalmente, isRequerida() y getOpciones() (para combobox)
        String tipo = pregunta.getTipoDato();

        // Si el campo es requerido y el valor es nulo o vacío, se retorna un error.
        if (pregunta.getRequerido() && (valor == null || valor.trim().isEmpty())) {
            return "El campo es obligatorio.";
        }

        // Si el valor no es obligatorio y está vacío, se acepta (retornamos null, sin error)
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }

        valor = valor.trim();

        if (tipo.contains("int")) {
            if (!valor.matches("-?\\d+")) {
                return "Ingrese un número inválido";
            }
            try {
                int val = Integer.parseInt(valor);
                switch (tipo) {
                    case "un medium int":
                        if (val<0 || val > 500) {
                            return "Ingrese un número positivo menor o igual a 500.";
                        }
                    break;
                    case "un small int":
                        if (val<0 || val > 20) {
                            return "Ingrese un número positivo menor o igual a 20.";
                        }
                    break;
                    case "un large int":
                        if (val<0 || val > 100_000) {
                            return "Ingrese un número positivo menor.";
                        }
                    break;
                    case "signed int":
                        if (val<-100_000 || val > 100_000) {
                            return "Ingrese un número entre -100 000 y 100 000.";
                        }
                    break;
                }
            } catch (NumberFormatException e) {
                return "Debe ingresar un número válido.";
            }

        } else if ("date".equalsIgnoreCase(tipo)) {
            System.out.println("En fecha date: "+valor);
            if (!valor.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return "Introduzca una fecha válida (dd-mm-yyyy).";
            }
            try {
                //DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                DateTimeFormatter sqlFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        .withResolverStyle(ResolverStyle.SMART)
                        .withZone(ZoneId.of("America/Lima"));
                LocalDate.parse(valor.trim(), sqlFormatter);


            } catch (DateTimeParseException e) {
                return "Introduzca una fecha válida (dd-mm-yyyy).";
            }
        } else if ("select".equalsIgnoreCase(tipo) || "combobox".equalsIgnoreCase(tipo)) {
            // Se asume que getOpciones() devuelve una lista de opciones válidas (por ejemplo, List<String>)
            List<String> opcionesValidas = opcionDAO.getByPreguntaToString(pregunta.getIdPregunta());
            if (opcionesValidas != null && !opcionesValidas.contains(valor)) {
                return "La opción seleccionada no es válida.";
            }
        } else if ("tel".equalsIgnoreCase(tipo)) {
            try {
                Integer.parseUnsignedInt(valor);
                if (!valor.matches("^9\\d{8}$")) {
                    return "Ingrese un número de Perú";
                }
            } catch (NumberFormatException e) {
                return "Debe ingresar un teléfono válido.";
            }
        } else if ("email".equalsIgnoreCase(tipo)) {
            String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            if (!valor.matches(regex)) {
                return "Ingrese un mail válido";
            }
        } else if ("dni".equalsIgnoreCase(tipo)) {
            try {
                Integer.parseUnsignedInt(valor);
                if (!valor.matches("^\\d{8}$")) {
                    return "Ingrese un DNI de Perú";
                }
            } catch (NumberFormatException e) {
                return "Debe ingresar un número de identificación válido.";
            }
        } else if ("decimal2".equalsIgnoreCase(tipo)) {
            if (!valor.matches("-?\\d+(\\.\\d{1,2})?")) {
                return "Debe ingresar un número decimal con hasta 2 decimales.";
            }
            try {
                double num = Double.parseDouble(valor);
                if (num < -100_000 || num > 100_000) {
                    return "Ingrese un número entre -100000 y 100000.";
                }
            } catch (NumberFormatException e) {
                return "Debe ingresar un número decimal válido.";
            }
        }

        // Si no se encontró ningún error, se retorna null.
        return null;
    }

    private String getRedirectUrl(int userRole) {
        if (userRole == 3) {
            return (getServletContext().getContextPath() + "/encuestador/VerFormulariosServlet");
        } else if (userRole == 2) {
            return (getServletContext().getContextPath() + "/coordinador/GestionFormServlet");
        } else {
            // Valor por defecto en caso de otro rol
            return (getServletContext().getContextPath() + "/access-denied.jsp");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Ejemplo básico para otros métodos HTTP
        response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
    }
}


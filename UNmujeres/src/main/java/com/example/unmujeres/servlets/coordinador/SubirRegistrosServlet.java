package com.example.unmujeres.servlets.coordinador;

import com.example.unmujeres.beans.EncHasFormulario;
import com.example.unmujeres.beans.Pregunta;
import com.example.unmujeres.beans.RegistroRespuestas;
import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.daos.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import javassist.NotFoundException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.*;

@MultipartConfig
@WebServlet(name = "SubirRegistrosServlet", value = "/coordinador/SubirRegistrosServlet")
public class SubirRegistrosServlet extends HttpServlet {

    EncHasFormularioDAO ehfDAO = new EncHasFormularioDAO();
    FormularioDAO formularioDAO = new FormularioDAO();
    RegistroRespuestasDAO registroDAO = new RegistroRespuestasDAO();
    RespuestaDAO respuestaDAO = new RespuestaDAO();
    private static String REPORTES_BASE_PATH;
    OpcionPreguntaDAO opcionDAO = new OpcionPreguntaDAO();
    PreguntaDAO preguntaDAO = new PreguntaDAO();
    BaseDAO baseDAO = new BaseDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        // Obtener sesión sin crear una nueva
        HttpSession session = request.getSession(false);

        Usuario user = (Usuario) session.getAttribute("usuario");
        int idUser = user.getIdUsuario();
        int userRole = user.getIdroles();

        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,"Método no permitido");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);    // Obtener la sesión sin crear una nueva

        Usuario user = (Usuario) session.getAttribute("usuario");
        int idUser = user.getIdUsuario();


        System.out.println("Iniciando proceso de subir registros");
        //Validación del parámetro idEhf.
        String idEhfParam = request.getParameter("idEhf");
        System.out.println("Parametro del asig es: " + idEhfParam);
        if (idEhfParam == null || idEhfParam.trim().isEmpty()) {
            //request.setAttribute("error", "Imposible encontrar un formulario nulo, es requerido elegir uno.");
            session.setAttribute("error", "Imposible encontrar un formulario nulo, es requerido elegir uno.");
            request.getRequestDispatcher("/coordinador/GestionFormServlet").forward(request, response);
            return;
        }

        int idEhf=-1;
        EncHasFormulario ehf = null;
        try {
            idEhf = Integer.parseUnsignedInt(idEhfParam);

            ehf = ehfDAO.getById(idEhf);
            if (ehf == null) {
                throw new NotFoundException("No se encontró formulario");
            } else if (ehf.getUsuario().getIdUsuario() != idUser) {
                throw new NotFoundException("No se encontró formulario");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error en el parametro idEhf");
            session.setAttribute("error", "Imposible encontrar ese formulario.");
            response.sendRedirect(request.getContextPath() + "/coordinador/GestionFormServlet");
            return;
        } catch (NotFoundException e) {
            session.setAttribute("error", e.getMessage());
        }

        //Obtención del archivo CSV subido (parámetro"csvFile").
        Part filePart = request.getPart("csvFile");
        if (filePart == null || filePart.getSize() == 0) {  //Validar que exista y no esté vacío
            //request.setAttribute("error", "Debe seleccionar un archivo.");
            session.setAttribute("error", "Debe seleccionar un archivo para Subir Registros masivamente.");
            response.sendRedirect(request.getContextPath() + "/coordinador/GestionFormServlet");
            return;
        }
        String fileName = filePart.getSubmittedFileName();
        if (fileName == null || !fileName.toLowerCase().endsWith(".csv")) {    //Validar extensión .csv
            //request.setAttribute("error", "El archivo debe ser de tipo CSV (.csv)");
            session.setAttribute("error", "El archivo debe ser de tipo CSV (.csv).");
            response.sendRedirect(request.getContextPath() + "/coordinador/GestionFormServlet");
            return;
        }
        int idForm = ehf.getFormulario().getIdFormulario();
        String templateName = "PLANTILLA_UN_Formulario"+idForm+".csv";
        Path basePath = Paths.get(REPORTES_BASE_PATH);
        Path filePath = basePath.resolve(templateName).normalize();
        if (!filePath.startsWith(basePath.toAbsolutePath())) {
            System.out.println("Plantilla no encontrada por ruta prohibida");
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        File template = filePath.toFile();
        if (!template.exists() || !template.isFile()) {
            System.out.println("Plantilla no encontrada");
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Plantilla no encontrada");
        }

        // Se recuperan las preguntas y opciones según el formulario
        ArrayList<Pregunta> preguntas = preguntaDAO.getPreguntasConOpcionesPorFormulario(idForm);
        Map<Integer, Pregunta> idP_preguntaMap = new HashMap<>();
        for (Pregunta pregunta : preguntas) {
            idP_preguntaMap.put(pregunta.getIdPregunta(), pregunta);
        }

        int nRegInsertados = 0;
        Connection conn = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(filePart.getInputStream(), StandardCharsets.UTF_8));
             BufferedReader templateReader = new BufferedReader(new FileReader(template, StandardCharsets.UTF_8))) {

            // Lectura y comparación de cabeceras
            for (int i = 0; i <= 5; i++) {
                String uploadedLine = reader.readLine();
                String templateLine = templateReader.readLine();
//                System.out.println("Linea de    input: "+uploadedLine);
//                System.out.println("Linea de template: "+templateLine);
                if (uploadedLine == null || templateLine == null) {
//                    System.err.println("Archivo incompleto. Línea " + (i+1) + " faltante. " +
//                            "Se esperaban 6 líneas de cabecera");
                    throw new IOException("Cabecera incompleta: línea "+(i+1)+"faltante. Asegúrese de usar el formato correcto.");
                }
                if (!uploadedLine.trim().equals(templateLine.trim())) {
//                    System.err.println("Error en cabecera línea " + (i+1) + ".\n" +
//                            "Se esperaba: '" + templateLine + "'\n" +
//                            "Se recibió: '" + uploadedLine + "'");
                    throw new IOException("Archivo con cabecera incompatible en línea "+(i+1)+". Asegúrese de usar el formato correcto.");
                }
            }

            String line;
            String delimiter = ";";
            int i=6;
            Map<Integer, List<String>> errores = new HashMap<>();
            conn = baseDAO.getConnection();
            conn.setAutoCommit(false);
            while ((line = reader.readLine()) != null) {
                Map<Integer, String> respuestasTexto = new HashMap<>();
                // Para cada línea, se crea un nuevo registro en la tabla Registro que asocia el idEhf.
                RegistroRespuestas nuevoRegistro = new RegistroRespuestas();
                nuevoRegistro.setFechaRegistro(LocalDateTime.now(ZoneId.of("America/Lima")));
                nuevoRegistro.setEstado("C"); // Siempre en estado C

                nuevoRegistro.setEncHasFormulario(ehf);
//                regs.add(nuevoRegistro)d;
                int idRegistro = registroDAO.crearRegTransaccion(conn,nuevoRegistro);   // int idRegistro = registroDAO.crearRegistroRespuestas(conn,nuevoRegistro);
                System.out.println("Nuevo Registro id es: "+idRegistro);
                nRegInsertados++;

                // Se separa la línea en celdas según el delimitador.
                String[] cells = line.split(delimiter);
                if (cells.length > preguntas.size()) {
                    throw new IllegalArgumentException("Número de columnas inconsistente en la línea "+(i+1)+" del archivo CSV");
                }
                int preguntaIndex = preguntas.get(0).getIdPregunta();
                int idPrimeraPregunta = preguntas.get(0).getIdPregunta();
                for (String cell : cells) {
                    Pregunta pregunta = idP_preguntaMap.get(preguntaIndex);
                    String respuesta = cell.trim();
                    // Manejar valores vacíos y placeholder
                    if (respuesta.isEmpty() || respuesta.equals("-")) {
                        String errorMsg = validarPregunta(pregunta, null);
                        if (errorMsg != null) {
                            String eMsg = "(línea "+(i+1)+") "+errorMsg;
                            errores
                                    .computeIfAbsent(pregunta.getIdPregunta()-idPrimeraPregunta+1, k -> new ArrayList<>())
                                    .add(eMsg);
                        } else {
                            respuestasTexto.put(preguntaIndex, null);
                        }
                    } else {
                        String errorMsg = validarPregunta(pregunta, respuesta);
                        if (errorMsg != null) {
                            String eMsg = "(línea "+(i+1)+") "+errorMsg;
                            errores
                                    .computeIfAbsent(pregunta.getIdPregunta()-idPrimeraPregunta+1, k -> new ArrayList<>())
                                    .add(eMsg);
                        } else {
                            respuestasTexto.put(preguntaIndex, respuesta);
                        }
                    }
                    preguntaIndex++;
                    if(errores.size()==5) {break;}
                }
                if(errores.size()==5) {break;}
                respuestaDAO.guardarRespTran(conn,idRegistro,respuestasTexto);  // respuestaDAO.guardarRespuestas(conn,idRegistro, respuestasTexto);
                i++;
            }

            if (!errores.isEmpty()) {
                //request.setAttribute("error", errores);
                session.setAttribute("validationErrors", errores);
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    session.setAttribute("error", "Error inesperado al abortar la subida masiva");
                }
                response.sendRedirect(request.getContextPath() + "/coordinador/GestionFormServlet");
                return;
            }
            conn.commit();

            if (nRegInsertados > 0) {
                System.out.println("Exito al subir "+nRegInsertados+" registros por csv");
                session.setAttribute("success", nRegInsertados+" registros importados correctamente");
            } else {
                session.setAttribute("error", "No se encontraron registros para importar.");
            }
            response.sendRedirect(request.getContextPath() + "/coordinador/GestionFormServlet");
            return;
        } catch (SQLException | IOException | IllegalArgumentException e) {
            e.printStackTrace();
            session.setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/coordinador/GestionFormServlet");
            return;
        } finally {

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
//            System.out.println("En fecha date: "+valor);
            if (!valor.matches("\\d{2}-\\d{2}-\\d{4}") && !valor.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                return "Introduzca una fecha válida (dd-mm-yyyy o dd/mm/yyyy).";
            }
            try {
                // 2) Escoger formateador según el separador
                if (valor.contains("-")) {
                    DateTimeFormatter fmtIso = DateTimeFormatter
                            .ofPattern("dd-MM-uuuu")
                            .withResolverStyle(ResolverStyle.SMART);
                    LocalDate.parse(valor.trim(), fmtIso);
                } else {
                    DateTimeFormatter fmtAlt = DateTimeFormatter
                            .ofPattern("dd/MM/uuuu")
                            .withResolverStyle(ResolverStyle.SMART);
                    LocalDate.parse(valor.trim(), fmtAlt);
                }

            } catch (DateTimeParseException e) {
                return "Introduzca una fecha válida (dd-mm-yyyy o dd/mm/yyyy).";
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

    @Override
    public void init() throws ServletException {
        ServletContext ctx = getServletContext();
        REPORTES_BASE_PATH = ctx.getRealPath("/WEB-INF/reportes");
    }

}
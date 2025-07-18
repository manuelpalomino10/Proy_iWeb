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
import javassist.NotFoundException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@MultipartConfig
@WebServlet(name = "SubirRegistrosServlet", value = "/coordinador/SubirRegistrosServlet")
public class SubirRegistrosServlet extends HttpServlet {

    EncHasFormularioDAO ehfDAO = new EncHasFormularioDAO();
    FormularioDAO formularioDAO = new FormularioDAO();
    RegistroRespuestasDAO registroDAO = new RegistroRespuestasDAO();
    RespuestaDAO respuestaDAO = new RespuestaDAO();
    private static String REPORTES_BASE_PATH;

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

        String templateName = "PLANTILLA_UN_Formulario"+ehf.getFormulario().getIdFormulario()+".csv";
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

        int nRegInsertados = 0;
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

            while ((line = reader.readLine()) != null) {
                Map<Integer, String> respuestasTexto = new HashMap<>();
                // Para cada línea, se crea un nuevo registro en la tabla Registro que asocia el idEhf.
                RegistroRespuestas nuevoRegistro = new RegistroRespuestas();
                nuevoRegistro.setFechaRegistro(LocalDateTime.now(ZoneId.of("America/Lima")));
                nuevoRegistro.setEstado("C"); // Siempre en estado C

                nuevoRegistro.setEncHasFormulario(ehf);
                int idRegistro = registroDAO.crearRegistroRespuestas(nuevoRegistro);
                System.out.println("Nuevo Registro id es: "+idRegistro);
                nRegInsertados++;

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

            if (nRegInsertados > 0) {
                System.out.println("Exito al subir "+nRegInsertados+" registros por csv");
                session.setAttribute("success", nRegInsertados+" registros importados correctamente");
            } else {
                session.setAttribute("error", "No se encontraron registros para importar.");
            }
            response.sendRedirect(request.getContextPath() + "/coordinador/GestionFormServlet");
            return;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            session.setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/coordinador/GestionFormServlet");
            return;
        } finally {

        }

    }

    @Override
    public void init() throws ServletException {
        ServletContext ctx = getServletContext();
        REPORTES_BASE_PATH = ctx.getRealPath("/WEB-INF/reportes");
    }

}
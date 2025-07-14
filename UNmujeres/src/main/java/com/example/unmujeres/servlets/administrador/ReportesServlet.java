package com.example.unmujeres.servlets.administrador;

import com.example.unmujeres.beans.Roles;
import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.beans.Zona;
import com.example.unmujeres.daos.FormularioDAO;
import com.example.unmujeres.daos.RolesDAO;
import com.example.unmujeres.daos.ZonaDAO;
import com.example.unmujeres.dtos.ContenidoReporteDTO;
import com.example.unmujeres.dtos.ReporteDTO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import javassist.NotFoundException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet(name = "ReportesServlet", value = "/administrador/ReportesServlet")
public class ReportesServlet extends HttpServlet {

    ZonaDAO zonaDAO = new ZonaDAO();
    RolesDAO rolesDAO = new RolesDAO();
    FormularioDAO formularioDAO = new FormularioDAO();

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

        String action = request.getParameter("action") == null ? "listaReportes" : request.getParameter("action");
        RequestDispatcher view;

        String zonaParam = request.getParameter("zona");
        String rolParam = request.getParameter("rol");
        String dateRangeParam = request.getParameter("daterange");
        //System.out.println("rango de fechas: " + dateRangeParam);

        // Convertir zona y rol a enteros
        int idZona;
        int idRol;
        try {
            idZona = (zonaParam != null && !zonaParam.trim().isEmpty()) ? Integer.parseUnsignedInt(zonaParam) : 0;
            idRol = (rolParam != null && !rolParam.trim().isEmpty()) ? Integer.parseUnsignedInt(rolParam) : 0;
        } catch (NumberFormatException e) {
            //request.setAttribute("error", "Error en el parametro zona o rol");
            System.out.println("Error en parametros de zona o rol");
            session.setAttribute("error", "Error en el parámetro zona o rol. No se filtran registros");
            response.sendRedirect(request.getContextPath() + "/administrador/ReportesServlet");
            return;
        }

        // Variables para almacenar las fechas. Inicialmente nulas.
        String fi=null,ff = null;

        if ( dateRangeParam != null && !dateRangeParam.trim().isEmpty()) {
            System.out.println("dateRangeParam: " + dateRangeParam);
            try {
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                DateTimeFormatter sqlFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                String[] parts = dateRangeParam.trim().split(" - ");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Formato inválido. Use: DD-MM-YYYY - DD-MM-YYYY");
                }

                LocalDate startDate = LocalDate.parse(parts[0].trim(), inputFormatter);
                LocalDate endDate = LocalDate.parse(parts[1].trim(), inputFormatter);
                // Validaciones adicionales
                if (endDate.isBefore(startDate)) {
                    throw new IllegalArgumentException("La fecha final debe ser posterior a la fecha inicial");
                }
                if (startDate.isAfter(LocalDate.now())) {
                    throw new IllegalArgumentException("No se permiten fechas futuras");
                }
                if (startDate.isBefore(LocalDate.of(2000, 1, 1))) {
                    throw new IllegalArgumentException("No se permiten fechas anteriores al año 2000");
                }

                fi = startDate.format(sqlFormatter);
                ff = endDate.format(sqlFormatter);

            } catch (DateTimeParseException e) {
                System.out.println("error de parse de fecha");
                session.setAttribute("error", "Debe seleccionar un rango de fechas válido (DD-MM-YYYY - DD-MM-YYYY).");
                //request.getRequestDispatcher("/administrador/listaReportes.jsp").forward(request, response);
                response.sendRedirect(request.getContextPath() + "/administrador/ReportesServlet");
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("error de formato de fecha");
                session.setAttribute("error", e.getMessage());
                //request.getRequestDispatcher("/administrador/listaReportes.jsp").forward(request, response);
                response.sendRedirect(request.getContextPath() + "/administrador/ReportesServlet");
                return;
            }
        }

        switch (action) {
            case "listaReportes":
                try {
                    System.out.println("Se consulto listaReportes");

                    List<ReporteDTO> reportes = formularioDAO.getReportes(idZona, idRol, fi, ff);
                    List<Zona> listaZonas = zonaDAO.listarZonas();
                    List<Roles> listaRoles = rolesDAO.listarRoles();

                    // Guardar en el request los datos para la vista
                    request.setAttribute("listaZonas", listaZonas);
                    request.setAttribute("listaRoles", listaRoles);
                    request.setAttribute("reportes", reportes);
                    request.setAttribute("zona", idZona);
                    request.setAttribute("rol", idRol);
                    request.setAttribute("daterange", dateRangeParam);

                    request.getRequestDispatcher("/administrador/listaReportes.jsp").forward(request, response);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "descargar":
                String idFormParam = request.getParameter("id_form");
                int idForm;
                // Verificar que el parámetro exista y no esté vacío
                if (idFormParam == null || idFormParam.trim().isEmpty()) {
                    System.out.println("Error el parametro idForm es nulo o vacío");
                    //request.setAttribute("error", "Imposible obtener un formulario nulo, es requerido elegir uno.");
                    session.setAttribute("error", "Imposible obtener un formulario nulo.");
                    response.sendRedirect(request.getContextPath() + "/ReportesServlet");
                    return;
                } else {
                    try {
                        // Intentamos convertir el parámetro a entero
                        idForm = Integer.parseUnsignedInt(idFormParam);
                        System.out.println("Se hace reporte para el formulario ID: " + idForm);
                    } catch (NumberFormatException e) {
                        System.out.println("Error el parsing de parametro idForm, no es int");
                        //request.setAttribute("error", "Imposible obtener un formulario con ese valor");
                        session.setAttribute("error", "Imposible obtener ese formulario, no es válido.");
                        //request.getRequestDispatcher("/administrador/listaReportes.jsp").forward(request, response);
                        response.sendRedirect(request.getContextPath() + "/ReportesServlet");
                        return;
                    }
                }

                try {
                    System.out.println("Se inicio descarga de reporte");

                    List<ContenidoReporteDTO> contenido = formularioDAO.getContenidoReporte(idForm, idZona, idRol, fi, ff);
                    System.out.println("Numero de respuestas en contenido: " + contenido.size());

                    if (contenido.isEmpty()) {
                        throw new NotFoundException("No hay registros que exportar");
                    }

                    String csvFilePath = getServletContext().getRealPath("/WEB-INF/reportes/PLANTILLA_UN_Formulario"+idForm+".csv");
                    File originalFile = new File(csvFilePath);
                    if (!originalFile.exists()) {
                        //request.setAttribute("error", "El archivo CSV original no se encontró.");
                        session.setAttribute("error", "El archivo CSV de plantilla no se encontró.");
                        System.out.println("El archivo CSV de plantilla no se encontró para este formulario.");
                        response.sendRedirect(request.getContextPath() + "/administrador/ReportesServlet");
//                        request.getRequestDispatcher("/administrador/listaReportes.jsp").forward(request, response);
                        return;
                    }

                    int numeroFilas = 0;

                    // Crear un archivo temporal para la descarga
                    String fileName = genNombreReporte(idForm);
                    //String fileName = "Reporte Formulario"+idForm+".csv";
                    Path plantilla = originalFile.toPath();
                    Path temp      = Files.createTempFile("Reporte_Formulario"+idForm, ".csv");
                    try (BufferedReader br = Files.newBufferedReader(plantilla, StandardCharsets.UTF_8);
                         PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(temp.toFile()), StandardCharsets.UTF_8), true)) {

                        pw.write("\uFEFF");
                        for(int i = 0; i < 6; i++) {
                            String header = br.readLine();
                            if(header == null) break;
                            pw.println(header);
                        }

                        // Se recorrerá la lista y se agrupará por idRegistro:
                        int currentRegistro = -1;
                        StringBuilder rowContent = new StringBuilder();
                        // Definir el delimitador usado en el CSV (por ejemplo, coma)
                        String delimiter = ";";
                        String prevCode = contenido.get(0).getCodEnc();
                        for (ContenidoReporteDTO resp : contenido) {
                            String respuestaValue = (resp.getRespuesta() == null) ? "" : resp.getRespuesta();
                            if (resp.getIdRegistro() != currentRegistro) {
                                // Si no es el primer registro, escribir la fila anterior
                                if (currentRegistro != -1) {
                                    rowContent.append(delimiter).append(prevCode);
                                    pw.println(rowContent.toString());
                                }
                                // Comenzar una nueva línea
                                currentRegistro = resp.getIdRegistro();
                                rowContent = new StringBuilder();
                                // Escribir el primer valor de la nueva línea (sin separador al inicio)
                                rowContent.append(respuestaValue);
                                numeroFilas++;
                            } else {
                                // Mismo idRegistro: agregar el valor en la siguiente columna
                                rowContent.append(delimiter).append(respuestaValue);
                                prevCode = (resp.getCodEnc() == null) ? "" : resp.getCodEnc();
                            }
                        }
                        // Escribir la última línea si existe contenido
                        if (rowContent.length() > 0) {
                            String lastCode = contenido.get(contenido.size()-1).getCodEnc();
                            rowContent.append(delimiter).append(lastCode);
                            pw.println(rowContent.toString());
                        }

                    }

                    //session.setAttribute("success", "Reporte generado exitosamente con " + numeroFilas + " registros");
                    // Enviamos el archivo modificado como descarga
                    response.setContentType("text/csv; charset=UTF-8");
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

                    try (FileInputStream fis = new FileInputStream(temp.toFile());
                            OutputStream os = response.getOutputStream()) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        os.flush();
                    } catch (IOException ioe) {
                        //request.setAttribute("error", "Error al enviar el archivo: " + ioe.getMessage());
                        session.setAttribute("error", "Error al enviar el archivo: " + ioe.getMessage());
                        request.getRequestDispatcher("/administrador/ReportesServlet?action=listaReportes").forward(request, response);
                        return;
                    } finally {
                        temp.toFile().delete();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    session.setAttribute("error", "Error inesperado: " + e.getMessage());
                    request.getRequestDispatcher("/administrador/ReportesServlet?action=listaReportes").forward(request, response);
                    return;
                }
            break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private String genNombreReporte(int idF) {
        LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Lima"));
        String fecha = ahora.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String hora = ahora.format(DateTimeFormatter.ofPattern("HHmmss"));
        return "Reporte_Formulario"+idF+"_"+fecha+"_"+hora+".csv";
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);

    }

    @Override
    public void init() throws ServletException {
        // Código de inicialización (opcional)
    }
}
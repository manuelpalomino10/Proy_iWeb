package com.example.unmujeres.servlets.administrador;

import com.example.unmujeres.beans.Roles;
import com.example.unmujeres.beans.Zona;
import com.example.unmujeres.beans.Respuesta;
import com.example.unmujeres.daos.FormularioDAO;
import com.example.unmujeres.daos.RolesDAO;
import com.example.unmujeres.daos.ZonaDAO;
import com.example.unmujeres.dtos.ContenidoReporteDTO;
import com.example.unmujeres.dtos.ReporteDTO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet(name = "ReportesServlet", value = "/ReportesServlet")
public class ReportesServlet extends HttpServlet {

    ZonaDAO zonaDAO = new ZonaDAO();
    RolesDAO rolesDAO = new RolesDAO();
    FormularioDAO formularioDAO = new FormularioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action") == null ? "listaReportes" : request.getParameter("action");
        RequestDispatcher view;

        String zonaParam = request.getParameter("zona");
        String rolParam = request.getParameter("rol");
        String dateRangeParam = request.getParameter("daterange");

        // Variables para almacenar las fechas. Inicialmente nulas.
        String fi=null,ff = null;

        // Convertir zona y rol a enteros
        int idZona = 0;
        int idRol = 0;
        try {
            idZona = (zonaParam != null) ? Integer.parseInt(zonaParam) : 0;
            idRol = (rolParam != null) ? Integer.parseInt(rolParam) : 0;
        } catch (NumberFormatException e) {
            // Si ocurre algún error, se asigna por defecto 0 (que equivale a "Todos")
            System.out.println("Error en el parametro zona o rol");
            request.setAttribute("error", "Error en el parametro zona o rol");
            idZona = 0;
            idRol = 0;
        }

        if (dateRangeParam != null && !dateRangeParam.trim().isEmpty()) {
            // Se espera que el parámetro tenga el formato: "DD-MM-YYYY - DD-MM-YYYY"
            String[] parts = dateRangeParam.split(" - ");
            if (parts.length == 2) {
                // Formato que viene del input: dd-MM-yyyy
                SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                // Formato que se usará en SQL: yyyy-MM-dd
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    fi = outputFormat.format(inputFormat.parse(parts[0].trim()));
                    ff = outputFormat.format(inputFormat.parse(parts[1].trim()));

                } catch (ParseException e) {
                    // Manejar el error: por ejemplo, asignar un mensaje de error o valores por defecto
                    request.setAttribute("error", "El rango de fechas no tiene un formato válido.");
                }
            } else {
                request.setAttribute("error", "Debe seleccionar un rango de fechas válido (DD-MM-YYYY - DD-MM-YYYY)");
            }
//        } else {
//            request.setAttribute("error", "No ingreso un rango de fechas.");
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
                    request.setAttribute("zonaSel", idZona);
                    request.setAttribute("rolSel", idRol);
                    request.setAttribute("dateRange", dateRangeParam);

                    request.getRequestDispatcher("/administrador/listaReportes.jsp").forward(request, response);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "descargar":
                String idFormParam = request.getParameter("id_form");
                // Verificar que el parámetro exista y no esté vacío
                if (idFormParam == null || idFormParam.trim().isEmpty()) {
                    System.out.println("Error el parametro idForm es nulo");
                    request.setAttribute("error", "Imposible obtener un formulario nulo, es requerido elegir uno.");
                    request.getRequestDispatcher("/ReportesServlet").forward(request, response);
                    return;
                }

                int idForm;
                try {
                    // Intentamos convertir el parámetro a entero
                    idForm = Integer.parseInt(idFormParam);
                } catch (NumberFormatException e) {
                    // Si ocurre un error, se notifica al usuario (sin asignar un valor por defecto)
                    System.out.println("Error el parametro idForm no es int");
                    request.setAttribute("error", "Imposible obtener un formulario con ese valor");
                    request.getRequestDispatcher("/ReportesServlet.jsp").forward(request, response);
                    return;
                }

                try {
                    System.out.println("Se inicio descarga de reporte");

                    List<ContenidoReporteDTO> contenido = formularioDAO.getContenidoReporte(idForm, idZona, idRol, fi, ff);

                    String csvFilePath = getServletContext().getRealPath("/WEB-INF/reportes/PLANTILLA_UN_Formulario"+idForm+".csv");
                    File originalFile = new File(csvFilePath);
                    if (!originalFile.exists()) {
                        request.setAttribute("error", "El archivo CSV original no se encontró.");
                        System.out.println("El archivo CSV de plantilla no se encontró para este formulario.");
                        request.getRequestDispatcher("/ReportesServlet.jsp").forward(request, response);
                        return;
                    }

                    // Crear un archivo temporal para la descarga
                    String fileName = "Reporte Formulario"+idForm+".csv";
                    File tempFile = File.createTempFile("Reporte_Formulario"+idForm, ".csv");
                    try (BufferedReader br = new BufferedReader(new FileReader(originalFile));
                         PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {
                        String line;
                        int lineNumber = 0;

                        // Copiar las primeras 6 líneas sin cambios
                        while ((line = br.readLine()) != null && lineNumber < 6) {
                            pw.println(line);
                            lineNumber++;
                        }

                        // Se recorrerá la lista y se agrupará por idRegistro:
                        int currentRegistro = -1;
                        StringBuilder rowContent = new StringBuilder();
                        // Definir el delimitador usado en el CSV (por ejemplo, coma)
                        String delimiter = ";";

                        for (ContenidoReporteDTO resp : contenido) {
                            String respuestaValue = (resp.getRespuesta() == null) ? "" : resp.getRespuesta();
                            System.out.println(respuestaValue);
                            if (resp.getIdRegistro() != currentRegistro) {
                                // Si no es el primer registro, escribir la fila anterior
                                if (currentRegistro != -1) {
                                    pw.println(rowContent.toString());
                                }
                                // Comenzar una nueva línea
                                currentRegistro = resp.getIdRegistro();
                                rowContent = new StringBuilder();
                                // Escribir el primer valor de la nueva línea (sin separador al inicio)
                                rowContent.append(respuestaValue);
                            } else {
                                // Mismo idRegistro: agregar el valor en la siguiente columna
                                rowContent.append(delimiter).append(respuestaValue);
                            }
                        }
                        // Escribir la última línea si existe contenido
                        if (rowContent.length() > 0) {
                            pw.println(rowContent.toString());
                        }

                    }


                    // Enviamos el archivo modificado como descarga
                    response.setContentType("text/csv");
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

                    try (FileInputStream fis = new FileInputStream(tempFile);
                            OutputStream os = response.getOutputStream()) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        os.flush();
                    } catch (IOException ioe) {
                        request.setAttribute("error", "Error al enviar el archivo: " + ioe.getMessage());
                        request.getRequestDispatcher("/ReportesServlet?action=listaReportes").forward(request, response);
                        return;
                    } finally {
                        // Opcional: eliminar el archivo temporal
                        tempFile.delete();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lógica para manejar solicitudes POST
        doGet(request, response); // Ejemplo: reutilizar lógica GET
    }

    @Override
    public void init() throws ServletException {
        // Código de inicialización (opcional)
    }
}
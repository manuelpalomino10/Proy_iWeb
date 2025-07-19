package com.example.unmujeres.servlets.administrador;

import com.example.unmujeres.beans.*;
import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.daos.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@MultipartConfig
@WebServlet(name = "NuevoFormServlet", value = "/administrador/NuevoFormServlet")
public class NuevoFormServlet extends HttpServlet {

    CategoriaDAO categoriaDAO = new CategoriaDAO();

    FormularioDAO formularioDAO = new FormularioDAO();
    SeccionDAO seccionDAO = new SeccionDAO();
    PreguntaDAO preguntaDAO = new PreguntaDAO();
    OpcionPreguntaDAO opcionDAO = new OpcionPreguntaDAO();
    BaseDAO baseDAO = new BaseDAO();
    UsuarioDAO usuarioDAO = new UsuarioDAO();


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


        ArrayList<Categoria> categorias = categoriaDAO.getCategorias();
        request.setAttribute("categorias", categorias);
        String hoy = String.valueOf(LocalDate.now());
        request.setAttribute("hoy", hoy);

        request.getRequestDispatcher("crearForm.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        // Obtener sesión sin crear una nueva
        HttpSession session = request.getSession(false);

        Usuario user = (Usuario) session.getAttribute("usuario");
        int idUser = user.getIdUsuario();
        int userRole = user.getIdroles();

        Formulario nuevoFormulario = new Formulario();
        nuevoFormulario.setFechaCreacion(new Date(System.currentTimeMillis()));

        request.getParameter("nombre_form");
        String nombreFormParam = request.getParameter("nombreForm");
        String fechaLimiteParam = request.getParameter("fechaLimite");

        String esperadosParam = request.getParameter("esperados");
        String idCategoriaParam = request.getParameter("idCategoria");

        ArrayList<Categoria> categorias = categoriaDAO.getCategorias();

        int idCategoria;
        int esperados;
        if (idCategoriaParam != null && esperadosParam != null) {
            try {
                idCategoria = Integer.parseInt(idCategoriaParam);
                esperados = Integer.parseInt(esperadosParam);

                ArrayList<Integer> idsCategorias = new ArrayList<>();
                Categoria cat = new Categoria();
                for (Categoria categoria : categorias) {
                    idsCategorias.add(categoria.getIdCategoria());
                    if (categoria.getIdCategoria() == idCategoria) {
                        cat = categoria;
                    }
                }

                if (esperados < 50) {
                    throw new IllegalArgumentException("Los registros esperados deben ser mayor que 50");
                } else if (!idsCategorias.contains(idCategoria)) {
                    throw new IllegalArgumentException("No existe esa categoría");
                }

                nuevoFormulario.setCategoria(cat);
                nuevoFormulario.setRegistrosEsperados(esperados);
            } catch (NumberFormatException e) {
                session.setAttribute("error", "Categoría o cantidad de Registros esperados no válidos");
                response.sendRedirect(request.getContextPath() + "/administrador/NuevoFormServlet");
                //response.sendError(400, "Solicitud malformada");
                return;
            } catch (IllegalArgumentException e) {
                session.setAttribute("error", "Solicitud malformada");
                response.sendRedirect(request.getContextPath() + "/administrador/NuevoFormServlet");
                //response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Solicitud malformada");
                return;
            }
        } else {
            session.setAttribute("error", "Registros esperados y Categoría no puede ser nulos");
        }

        try {
            if (nombreFormParam==null || nombreFormParam.trim().isEmpty()) {
                throw new NullPointerException("El formulario debe tener un nombre");
            } else {

            }
            if (fechaLimiteParam ==null || fechaLimiteParam.trim().isEmpty()) {
                throw new NullPointerException("Tiene que asignar una fecha límite al formulario");
            } else {
                DateTimeFormatter sqlFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate fechaLimite = LocalDate.parse(fechaLimiteParam.trim(), sqlFormatter);

                if (fechaLimite.isBefore(LocalDate.now())) {
                    throw new IllegalArgumentException("La fecha límite debe ser futura");
                }
                nuevoFormulario.setFechaLimite(java.sql.Date.valueOf(fechaLimite));
            }

            nuevoFormulario.setNombre(nombreFormParam);

        } catch (IllegalArgumentException | DateTimeParseException e) {
            session.setAttribute("error", "Solicitud malformada");
            response.sendRedirect(request.getContextPath() + "/administrador/NuevoFormServlet");
            //response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return;
        }

        Part filePart = request.getPart("csvFile");
        if (filePart == null || filePart.getSize() == 0) {  //Validar que exista y no esté vacío
            //request.setAttribute("error", "Debe seleccionar un archivo.");
            session.setAttribute("error", "Debe seleccionar un archivo.");
            response.sendRedirect(request.getContextPath() + "/administrador/NuevoFormServlet");
            //response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Debe seleccionar un archivo.");
            return;
        }
        String fileName = filePart.getSubmittedFileName();
        if (fileName == null || !fileName.toLowerCase().endsWith(".csv")) {    //Validar extensión .csv
            //request.setAttribute("error", "El archivo debe ser de tipo CSV (.csv)");
            session.setAttribute("error", "El archivo debe ser de tipo CSV (.csv).");
            response.sendRedirect(request.getContextPath() + "/administrador/NuevoFormServlet");
            return;
        }

        int numPreguntas = 0;
        Connection conn = null;
        File savedFile = null;

        String tempFileName = "temp_" + System.currentTimeMillis() + ".csv";
        String uploadPath =getServletContext().getRealPath("/WEB-INF/reportes");
        System.out.println(uploadPath);
        File tempFile = getFile(uploadPath, tempFileName, filePart);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(filePart.getInputStream(), "UTF-8"))) {
            conn = baseDAO.getConnection();
            conn.setAutoCommit(false);
            int idForm = formularioDAO.crearFormulario(conn,nuevoFormulario);
            nuevoFormulario.setIdFormulario(idForm);

            String delimiter = ";";

            String[] headers = reader.readLine().split(delimiter);
            String[] obligatoriedad = reader.readLine().split(delimiter);
            String[] tipos = reader.readLine().split(delimiter);
            String[] secciones = reader.readLine().split(delimiter);
            String[] ejemplos = reader.readLine().split(delimiter);

// Validar consistencia
            numPreguntas = headers.length;
            if (obligatoriedad.length != numPreguntas
                    || tipos.length != numPreguntas
                    || secciones.length != numPreguntas
                    || ejemplos.length != numPreguntas) {

                throw new Exception("Número de columnas inconsistente en el archivo CSV");
            }

// 4. Procesar preguntas
            Map<String, Seccion> mapaSecciones = new HashMap<>(); // nombre_sec -> objeto Seccion

            for (int i = 0; i < numPreguntas; i++) {
                // Validar obligatoriedad
                String oblig = obligatoriedad[i].trim();
                if (!"obligatorio".equalsIgnoreCase(oblig) && !"opcional".equalsIgnoreCase(oblig)) {
                    throw new Exception("Valor inválido en obligatoriedad: '"+oblig+"' en columna " +obligatoriedad[i]);
                }
                boolean requerido = "obligatorio".equalsIgnoreCase(oblig);

                // Validar tipo de dato
                String tipo = tipos[i].trim().toLowerCase();
                if (!"texto".equals(tipo) && !"número positivo pequeño".equals(tipo) && !"número positivo mediano".equals(tipo)
                        && !"número positivo grande".equals(tipo) && !"número con signo".equals(tipo)
                        && !"fecha".equals(tipo) && !"combobox".equals(tipo)
                        && !"teléfono".equals(tipo) && !"email".equals(tipo) && !"dni".equals(tipo) && !"número decimal".equals(tipo)) {
                    throw new Exception("Tipo de dato inválido: '"+tipo+"' en columna " +(i+1));
                }

                // Mapear tipo a formato BD
                String tipoBD = switch (tipo) {
                    case "texto" -> "text";
                    case "fecha" -> "date";
                    case "teléfono" -> "tel";
                    case "email" -> "email";
                    case "dni" -> "dni";
                    case "combobox" -> "combobox";
                    case "número positivo pequeño" -> "un small int";
                    case "número positivo mediano" -> "un medium int";
                    case "número positivo grande" -> "un large int";
                    case "número con signo" -> "signed int";
                    case "número decimal" -> "decimal2";
                    default -> "Default"; // Para texto
                };

                // Procesar sección
                String nombreSec = secciones[i].trim();
                Seccion seccion = mapaSecciones.get(nombreSec);

                if (seccion == null) {
                    seccion = new Seccion();
                    seccion.setNombreSec(nombreSec);
                    seccion.setFormulario(nuevoFormulario);
                    int idSec=seccionDAO.crearSeccion(conn,seccion); // Insertar y obtener ID
                    seccion.setIdSeccion(idSec);
                    mapaSecciones.put(nombreSec, seccion);
                }

                // Crear pregunta
                Pregunta pregunta = new Pregunta();
                pregunta.setEnunciado(headers[i].trim());
                pregunta.setTipoDato(tipoBD);
                pregunta.setSeccion(seccion); // sección del mapa
                pregunta.setRequerido(requerido);
                int idPregunta = preguntaDAO.crearPregunta(conn,pregunta);
                pregunta.setIdPregunta(idPregunta);

                // Procesar opciones si es combobox
                if ("combobox".equals(tipo)) {
                    String opcionesStr = ejemplos[i].trim();
                    if (!opcionesStr.contains("/")) {
                        throw new Exception("Pregunta en columna "+(i+1)+" requiere al menos dos opciones en fila 5");
                    }

                    String[] opciones = opcionesStr.split("/");
                    for (String opcion : opciones) {
                        if (!opcion.trim().isEmpty()) {
                            OpcionPregunta op = new OpcionPregunta();
                            op.setOpcion(opcion.trim());
                            op.setPregunta(pregunta);
                            int idOpcion = opcionDAO.crearOpcion(conn,op);
                        }
                    }
                }
            }
            conn.commit();

            // Renombrar el archivo con el formato requerido
            String newFileName = "PLANTILLA_UN_Formulario" + idForm + ".csv";
            savedFile = new File(uploadPath + File.separator + newFileName);

            System.out.println("Archivo guardado: " + savedFile.getAbsolutePath());

            if (!tempFile.renameTo(savedFile)) {
                // Si falla el renombrado (puede pasar entre sistemas de archivos), copiar manualmente
                try (InputStream in = new FileInputStream(tempFile);
                     OutputStream out = new FileOutputStream(savedFile)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                }
                tempFile.delete(); // Eliminar el archivo temporal
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            session.setAttribute("error", e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    // Log adicional si falla el rollback
                }
            }

            response.sendRedirect(request.getContextPath() + "/administrador/NuevoFormServlet");
            return;
        }finally {
            // Cerrar conexión y restaurar estado
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);  // Restaurar autocommit
                    conn.close();  // Devolver al pool
                } catch (SQLException e) {
                    // Log de error en cierre
                }
            }
        }
        asignarNuevoForm(nuevoFormulario);
        session.setAttribute("success", "Nuevo Formulario creado con éxito con "+numPreguntas+" preguntas. Se guardo el archivo CSV subido como plantilla.");
        response.sendRedirect(request.getContextPath() + "/administrador/NuevoFormServlet");
        return;

    }

    private void asignarNuevoForm(Formulario nuevoFormulario) {
        System.out.println("asignando nuevo formulario");
        ArrayList<Usuario> cordis = usuarioDAO.getCordis();

        int idForm = nuevoFormulario.getIdFormulario();
        System.out.println("id Form: "+idForm);

        for (Usuario cordi : cordis) {

            try {
                CoordiGestionEncDAO asigDAO = new CoordiGestionEncDAO();
                asigDAO.asignarFormulario(cordi.getIdUsuario(), idForm);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private static File getFile(String uploadPath, String tempFileName, Part filePart) {
        File tempFile = new File(uploadPath + File.separator + tempFileName);
        try {
            // Guardar el archivo subido temporalmente
            try (InputStream fileContent = filePart.getInputStream();
                 OutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fileContent.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
        } catch (Exception e) {}
        return tempFile;
    }

    @Override
    public void init() throws ServletException {
        // Código de inicialización (opcional)
    }

    @Override
    public void destroy() {
        // Código de limpieza (opcional)
    }
}
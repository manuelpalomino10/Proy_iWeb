package com.example.unmujeres.servlets.administrador;

import com.example.unmujeres.beans.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "DescargarPlantillaServlet", value = {"/administrador/downloadTemp","/coordinador/downloadTemp"})
public class DescargarPlantillaServlet extends HttpServlet {

    private static String REPORTES_BASE_PATH;
    private static final Set<String> ALLOWED_KEYS = Set.of("mass", "nform");

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

        try {
            String key = request.getParameter("file");
            if (key == null || !ALLOWED_KEYS.contains(key)) {throw new IllegalArgumentException("Imposible encontrar el archivo");}

            String fileName;
            if ("nform".equals(key)) {
                fileName = "PLANTILLA_Nuevo_Formulario.csv";
            } else if ("mass".equals(key)) {
                String idFormParam = request.getParameter("form");
                if (idFormParam == null || idFormParam.isEmpty()) {
                    throw new IllegalArgumentException("Imposible encontrar el archivo");
                }
                if (!idFormParam.matches("\\d{1,9}")) {
                    throw new IllegalArgumentException("Imposible encontrar el archivo");
                }
                fileName = "PLANTILLA_UN_Formulario"+idFormParam+".csv";
            } else {throw new IllegalArgumentException("Inválido");}

            // 3. Prevención de Path Traversal
            Path basePath = Paths.get(REPORTES_BASE_PATH);
            Path filePath = basePath.resolve(fileName).normalize();

            if (!filePath.startsWith(basePath.toAbsolutePath())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            File file = filePath.toFile();

            // 4. Verificar existencia del archivo
            if (!file.exists() || !file.isFile()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Archivo no encontrado");
                return;
            }

            // 5. Enviar
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
            response.setContentLengthLong(file.length());

            try (FileInputStream fis = new FileInputStream(file);
                 OutputStream os = response.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.flush();
            } catch (IOException ioe) {
                session.setAttribute("error", "Error al enviar el archivo: " + ioe.getMessage());
                response.sendRedirect(getRedirectUrl(userRole));
                return;
            }

        } catch (Exception e) {
            System.out.println("Error en descarga: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    public void init() throws ServletException {
        ServletContext ctx = getServletContext();
        REPORTES_BASE_PATH = ctx.getRealPath("/WEB-INF/reportes");
    }

    private String getRedirectUrl(int userRole) {
        if (userRole == 1) {
            return (getServletContext().getContextPath() + "/administrador/NuevoFormServlet");
        } else if (userRole == 2) {
            return (getServletContext().getContextPath() + "/coordinador/GestionFormServlet");
        } else {
            // Valor por defecto en caso de otro rol
            return (getServletContext().getContextPath() + "/dashboard");
        }
    }

    @Override
    public void destroy() {
        // Código de limpieza (opcional)
    }
}
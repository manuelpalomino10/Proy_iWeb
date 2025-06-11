package com.example.unmujeres.servlets.coordinador;

import com.example.unmujeres.daos.Usuario;
import com.example.unmujeres.daos.UsuarioDAO;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Logger;

@WebServlet("/subirFotoCOORD")
@MultipartConfig(
        maxFileSize = 1024 * 1024 * 5, // 5MB
        fileSizeThreshold = 1024 * 1024 // 1MB en memoria
)
public class SubirFotoServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(SubirFotoServlet.class.getName());
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idUsuario") == null) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print("{\"status\": \"error\", \"message\": \"Usuario no autenticado.\"}");
            out.flush();
            return;
        }

        // Recuperar el ID
        int idUsuario = (Integer) session.getAttribute("idUsuario");

        try {
            Part filePart = request.getPart("foto");
            if (filePart == null || filePart.getSize() == 0) {
                throw new IllegalArgumentException("Debe seleccionar una imagen.");
            }

            // Validar
            String contentType = filePart.getContentType();
            if (!contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Solo se permiten imágenes (JPEG, PNG, etc.).");
            }

            //
            byte[] fotoBytes = filePart.getInputStream().readAllBytes();

            // Actualizar en BD
            boolean exito = usuarioDAO.actualizarFoto(idUsuario, fotoBytes);
            if (!exito) {
                throw new SQLException("No se pudo guardar la foto en la base de datos.");
            }
            // Actualziar la sesion con la nueva foto, esto para mostarlo en los topbars correspondientes
            String fotoBase64 = Base64.getEncoder().encodeToString(fotoBytes);
            session.setAttribute("fotoBase64", fotoBase64);

            //Actualziar el objeto usuario en sesión
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            if (usuario != null) {
                usuario.setFoto(fotoBytes);
                session.setAttribute("usuario", usuario);
            }
            LOGGER.info("Foto actualizada exitosamente para el usuario con ID: " + idUsuario);


            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"status\": \"success\", \"fotoBase64\":\"" + fotoBase64 + "\"}");
            out.flush();

        } catch (IllegalArgumentException e) {
            LOGGER.warning("Error de validación: " + e.getMessage());
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}");
            out.flush();
        } catch (SQLException e) {
            LOGGER.severe("Error de base de datos al subir foto: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error de base de datos al guardar la foto.");
        } catch (Exception e) {
            LOGGER.severe("Error inesperado: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error inesperado al procesar la foto.");
        }
    }
}
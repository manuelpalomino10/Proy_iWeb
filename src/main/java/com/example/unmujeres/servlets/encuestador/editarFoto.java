package com.example.unmujeres.servlets.encuestador;
import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.daos.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/editarFoto")
@MultipartConfig
public class editarFoto extends HttpServlet {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sesion = request.getSession();
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");

        if (usuario == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //
        Part filePart = request.getPart("foto");
        if (filePart != null && filePart.getSize() > 0) {
            byte[] fotoBytes = filePart.getInputStream().readAllBytes();


            try {
                boolean actualizo = usuarioDAO.actualizarFoto(usuario.getIdUsuario(), fotoBytes);
                System.out.println("[DEBUG] Filas afectadas: " + (actualizo ? "1" : "0"));

                if (actualizo) {
                    usuario.setFotoBytes(fotoBytes);
                    sesion.setAttribute("usuario", usuario);
                }
            } catch (SQLException e) {
                e.printStackTrace(); // o usar un logger
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al actualizar la foto");
                return;
            }

        } else {
            System.out.println("[DEBUG] No se recibió archivo o está vacío.");
        }

        response.setContentType("application/json");
        response.getWriter().write("{\"success\": true}");
    }
}





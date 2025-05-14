package com.example.onumujeres.servlets;

import com.example.onumujeres.daos.UsuarioDAO;
import com.example.onumujeres.beans.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Blob;
import java.io.InputStream;
import java.io.OutputStream;

@WebServlet("/perfilEnc")
public class PerfilServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer idUsuario = (Integer) session.getAttribute("idUsuario"); // Obtén el ID del usuario de la sesión

        if (idUsuario == null) {
            response.sendRedirect("perfilEnc.jsp"); // Redirige al login si no hay sesión
            return;
        }

        try {
            Usuario usuario = usuarioDAO.obtenerUsuarioPorId(idUsuario);
            request.setAttribute("usuario", usuario);

            // Manejar la visualización de la foto
            Blob fotoBlob = usuario.getFoto();
            if (fotoBlob != null) {
                InputStream in = fotoBlob.getBinaryStream();
                // Set content type and headers for image display (e.g., image/jpeg, image/png)
                response.setContentType("image/jpeg"); // Adjust as needed
                response.setHeader("Content-Disposition", "inline; filename=\"profile_image.jpg\""); // Adjust as needed

                OutputStream out = response.getOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                in.close();
                out.close();
            } else {
                // If no photo, forward to JSP to display default or placeholder
                request.getRequestDispatcher("perfilEnc.jsp").forward(request, response);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp"); // Página de error
        }
    }
}
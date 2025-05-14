package com.example.onumujeres.servlets;

import com.example.onumujeres.daos.UsuarioDAO;  // Asegúrate de que esta línea esté presente
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

@WebServlet("/editarFoto")
@MultipartConfig(maxFileSize = 16177215)
public class EditarFotoServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");

        if (idUsuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            Part filePart = request.getPart("foto");
            InputStream fileContent = filePart.getInputStream();

            Blob fotoBlob = null;
            try {
                fotoBlob = new javax.sql.rowset.serial.SerialBlob(fileContent.readAllBytes());
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("error.jsp");
                return;
            }

            boolean actualizado = usuarioDAO.actualizarFoto(idUsuario, fotoBlob);

            if (actualizado) {
                response.sendRedirect("perfil");
            } else {
                response.sendRedirect("error.jsp");
            }

        } catch (SQLException | IOException | ServletException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
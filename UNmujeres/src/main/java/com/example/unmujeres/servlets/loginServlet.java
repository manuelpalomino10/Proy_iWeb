package com.example.unmujeres.servlets;

import com.example.unmujeres.daos.HashUtil;
import com.example.unmujeres.daos.UsuarioDAO;
import com.example.unmujeres.beans.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;

@WebServlet("/login")
public class loginServlet extends HttpServlet {

    private final UsuarioDAO usuarioDao = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("correo");
        String contraseña = request.getParameter("contraseña");

        String contraseñaHasheada = HashUtil.hashSHA256(contraseña);

        Usuario usuario = null; // Inicializar usuario fuera del try para que sea accesible
        Usuario usuarioConFoto = null; // Inicializar usuarioConFoto

        try {
            // Llama a la función de validación con la contraseña ya hasheada
            usuario = usuarioDao.validarUsuario(correo, contraseñaHasheada);

            if (usuario != null && usuario.isEstado()) {
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                session.setAttribute("idUsuario", usuario.getIdUsuario());

                // Cargar el usuario completo con la fotoBytes si no está ya en el objeto 'usuario'
                usuarioConFoto = usuarioDao.obtenerUsuarioConDistrito(usuario.getIdUsuario());

                if (usuarioConFoto != null && usuarioConFoto.getFotoBytes() != null && usuarioConFoto.getFotoBytes().length > 0) {
                    String base64Image = Base64.getEncoder().encodeToString(usuarioConFoto.getFotoBytes());
                    session.setAttribute("fotoBase64", base64Image); // Guardar la foto en base64 en la sesión
                } else {
                    session.removeAttribute("fotoBase64"); // Asegurarse de que no haya una foto antigua si el usuario no tiene una
                }

                session.setAttribute("usuario", usuarioConFoto != null ? usuarioConFoto : usuario);

                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                request.setAttribute("errorMessage",
                        "Correo o contraseña incorrectos, o usuario inactivo");
                request.getRequestDispatcher("/Sistema/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Imprime el stack trace completo en la consola del servidor
            request.setAttribute("errorMessage", "Error al intentar iniciar sesión. Por favor, inténtelo de nuevo más tarde.");
            // O un mensaje más específico si puedes determinar la causa del SQL Exception
            request.getRequestDispatcher("/Sistema/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/Sistema/login.jsp").forward(request, response);
    }
}

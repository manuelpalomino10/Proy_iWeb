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


@WebServlet("/login")
public class loginServlet extends HttpServlet {

    private final UsuarioDAO usuarioDao = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("correo");
        String contraseña = request.getParameter("contraseña");

        String contraseñaHasheada = HashUtil.hashSHA256(contraseña);

// Llama a la función de validación con la contraseña ya hasheada
        Usuario usuario = usuarioDao.validarUsuario(correo, contraseñaHasheada);

        if (usuario != null && usuario.isEstado()) {
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario);
            // <-- Línea añadida: guardamos también el ID
            session.setAttribute("idUsuario", usuario.getIdUsuario());
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            request.setAttribute("errorMessage",
                    "Correo o contraseña incorrectos, o usuario inactivo");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}

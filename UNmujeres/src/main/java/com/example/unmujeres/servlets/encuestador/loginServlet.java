package com.example.unmujeres.servlets.encuestador;

import com.example.unmujeres.daos.HashUtil;
import com.example.unmujeres.daos.UsuarioDAO;


import com.example.unmujeres.beans.Usuario; import com.example.unmujeres.daos.UsuarioDAO; import jakarta.servlet.ServletException; import jakarta.servlet.annotation.WebServlet; import jakarta.servlet.http.HttpServlet; import jakarta.servlet.http.HttpServletRequest; import jakarta.servlet.http.HttpServletResponse; import jakarta.servlet.http.HttpSession; import java.io.IOException; import java.sql.SQLException; import java.util.Base64;

@WebServlet("/login") public class loginServlet extends HttpServlet {

    private final UsuarioDAO usuarioDao = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("correo");
        String contraseña = request.getParameter("contraseña");

        String contraseñaHasheada = HashUtil.hashSHA256(contraseña);

        Usuario usuario = null;
        Usuario usuarioConFoto = null;

        try {
            usuario = usuarioDao.validarUsuario(correo, contraseñaHasheada);

            if (usuario != null && usuario.isEstado()) {
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                session.setAttribute("idUsuario", usuario.getIdUsuario());

                usuarioConFoto = usuarioDao.obtenerUsuarioConDistrito(usuario.getIdUsuario());

                if (usuarioConFoto != null && usuarioConFoto.getFotoBytes() != null && usuarioConFoto.getFotoBytes().length > 0) {
                    String base64Image = Base64.getEncoder().encodeToString(usuarioConFoto.getFotoBytes());
                    session.setAttribute("fotoBase64", base64Image);
                } else {
                    session.removeAttribute("fotoBase64");
                }

                session.setAttribute("usuario", usuarioConFoto != null ? usuarioConFoto : usuario);

                int idroles = usuario.getIdroles();
                System.out.println("Usuario autenticado: ID=" + usuario.getIdUsuario() + ", idroles=" + idroles);

                switch (idroles) {
                    case 1: // Administrador
                    case 3: // Encuestador
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                        break;
                    case 2: // Coordinador
                        request.setAttribute("errorMessage", "Rol de coordinador no soportado.");
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                        break;
                    default:
                        request.setAttribute("errorMessage", "Rol de usuario no reconocido (idroles=" + idroles + ").");
                        request.getRequestDispatcher("/Sistema/error.jsp").forward(request, response);
                        break;
                }

                System.out.println("[DEBUG] Usuario base - ID: " + usuario.getIdUsuario()
                        + ", Rol: " + usuario.getIdroles());

                usuarioConFoto = usuarioDao.obtenerUsuarioConDistrito(usuario.getIdUsuario());

                if (usuarioConFoto != null) {
                    System.out.println("[DEBUG] Usuario con foto - ID: " + usuarioConFoto.getIdUsuario()
                            + ", Rol: " + usuarioConFoto.getIdroles());

                    // Verificar si los roles coinciden
                    if (usuario.getIdroles() != usuarioConFoto.getIdroles()) {
                        System.err.println("[ERROR] Inconsistencia de roles! Base: " + usuario.getIdroles()
                                + ", Con foto: " + usuarioConFoto.getIdroles());
                    }
                }


                if (usuarioConFoto != null && usuarioConFoto.getIdroles() > 0) {
                    session.setAttribute("usuario", usuarioConFoto);
                } else {

                    session.setAttribute("usuario", usuario);
                    System.err.println("[WARNING] Usando usuario base por problema en usuarioConFoto");
                }
            } else {
                request.setAttribute("errorMessage", "Correo o contraseña incorrectos, o usuario inactivo");
                request.getRequestDispatcher("/Sistema/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error al intentar iniciar sesión: " + e.getMessage());
            request.getRequestDispatcher("/Sistema/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/Sistema/login.jsp").forward(request, response);
    }

}
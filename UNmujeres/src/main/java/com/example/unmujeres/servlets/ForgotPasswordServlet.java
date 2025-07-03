package com.example.unmujeres.servlets;

import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.daos.UsuarioDAO;
import com.example.unmujeres.utils.EmailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/forgotPassword.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email").trim();

        try {
            // 1. Buscar usuario por correo
            Usuario usuario = usuarioDAO.buscarPorCorreo(email);

            if (usuario == null) {
                request.setAttribute("error", "No existe una cuenta asociada a este correo electrónico");
                doGet(request, response);
                return;
            }

            // 2. Generar token único
            String token = UUID.randomUUID().toString();
            System.out.println("[DEBUG] Token generado para " + email + ": " + token);

            // 3. Guardar token en la base de datos
            boolean tokenGuardado = usuarioDAO.crearTokenRecuperacion(usuario.getIdUsuario(), token);

            if (!tokenGuardado) {
                request.setAttribute("error", "Error al generar el token de recuperación");
                doGet(request, response);
                return;
            }

            // 4. Crear enlace de recuperación
            String appUrl = request.getRequestURL().toString()
                    .replace(request.getServletPath(), "");
            String resetLink = appUrl + "/reset-password?token=" + token;

            // 5. Enviar correo electrónico
            String asunto = "Recuperación de contraseña - ONU Mujeres";
            String contenido = "<h2 style='color:#2c3e50;'>Restablece tu contraseña</h2>"
                    + "<p>Hemos recibido una solicitud para restablecer tu contraseña.</p>"
                    + "<p><a href='" + resetLink + "' style='background-color:#4e73df;color:white;padding:10px 20px;text-decoration:none;border-radius:5px;display:inline-block;margin:10px 0;'>Restablecer contraseña</a></p>"
                    + "<p><small>Este enlace expirará en 10 minutos.</small></p>"
                    + "<p style='color:#7f8c8d;font-size:0.9em;'>Si no solicitaste este cambio, puedes ignorar este mensaje.</p>";

            EmailUtil.sendEmail(usuario.getCorreo(), asunto, contenido);

            // 6. Mostrar confirmación
            request.setAttribute("success", "Se ha enviado un enlace de recuperación a tu correo electrónico");
            doGet(request, response);

        } catch (SQLException e) {
            System.err.println("[ERROR] Error en ForgotPasswordServlet: " + e.getMessage());
            request.setAttribute("error", "Error al procesar la solicitud. Por favor, inténtalo nuevamente.");
            doGet(request, response);
        }
    }
}
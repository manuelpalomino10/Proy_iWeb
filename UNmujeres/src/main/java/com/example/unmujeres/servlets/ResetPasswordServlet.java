package com.example.unmujeres.servlets;

import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.daos.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
        System.out.println("[DEBUG] ResetPasswordServlet inicializado");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        System.out.println("[DEBUG] GET /reset-password?token=" + token);

        try {
            Usuario usuario = usuarioDAO.buscarPorToken(token);

            if (usuario == null) {
                System.out.println("[DEBUG] Token no válido o expirado: " + token);
                request.setAttribute("error", "El enlace de recuperación no es válido o ha expirado");
            } else {
                System.out.println("[DEBUG] Token válido para usuario ID: " + usuario.getIdUsuario());
                request.setAttribute("token", token);
            }

            request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);

        } catch (SQLException e) {
            System.err.println("[ERROR] Error al buscar token: " + e.getMessage());
            request.setAttribute("error", "Error al verificar el enlace. Intenta nuevamente.");
            request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        String nuevaPassword = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        System.out.println("[DEBUG] POST /reset-password");
        System.out.println("[DEBUG] Token recibido: " + token);
        System.out.println("[DEBUG] Nueva contraseña recibida: " + (nuevaPassword != null ? "[PROVIDED]" : "null"));

        try {
            // Validaciones de contraseña
            List<String> erroresPwd = new ArrayList<>();
            Map<String, Boolean> requisitosPwd = new LinkedHashMap<>();

            if (nuevaPassword == null || nuevaPassword.trim().isEmpty()) {
                System.out.println("[DEBUG] Contraseña vacía o nula");
                erroresPwd.add("La contraseña es obligatoria");
                requisitosPwd.put("len", false);
                requisitosPwd.put("may", false);
                requisitosPwd.put("min", false);
                requisitosPwd.put("num", false);
                requisitosPwd.put("spec", false);
            } else {
                boolean len = nuevaPassword.length() >= 8;
                boolean may = nuevaPassword.matches(".*[A-Z].*");
                boolean min = nuevaPassword.matches(".*[a-z].*");
                boolean num = nuevaPassword.matches(".*\\d.*");
                boolean spec = nuevaPassword.matches(".*[\\W_].*");

                System.out.println("[DEBUG] Validación contraseña - "
                        + "len: " + len + ", "
                        + "may: " + may + ", "
                        + "min: " + min + ", "
                        + "num: " + num + ", "
                        + "spec: " + spec);

                if (!len) erroresPwd.add("Mínimo 8 caracteres");
                if (!may) erroresPwd.add("Al menos una mayúscula");
                if (!min) erroresPwd.add("Al menos una minúscula");
                if (!num) erroresPwd.add("Al menos un número");
                if (!spec) erroresPwd.add("Al menos un carácter especial");
                if (!nuevaPassword.equals(confirmPassword)) erroresPwd.add("Las contraseñas no coinciden");

                requisitosPwd.put("len", len);
                requisitosPwd.put("may", may);
                requisitosPwd.put("min", min);
                requisitosPwd.put("num", num);
                requisitosPwd.put("spec", spec);
            }

            request.setAttribute("requisitosPwd", requisitosPwd);

            if (!erroresPwd.isEmpty()) {
                System.out.println("[DEBUG] Errores de validación: " + erroresPwd);
                request.setAttribute("error", String.join(", ", erroresPwd));
                request.setAttribute("token", token);
                doGet(request, response);
                return;
            }

            // Buscar usuario por token
            Usuario usuario = usuarioDAO.buscarPorToken(token);

            if (usuario == null) {
                System.out.println("[DEBUG] Token no válido al intentar resetear");
                request.setAttribute("error", "El enlace de recuperación no es válido o ha expirado");
                doGet(request, response);
                return;
            }

            System.out.println("[DEBUG] Actualizando contraseña para usuario ID: " + usuario.getIdUsuario());
            boolean actualizado = usuarioDAO.actualizarContraseña(usuario.getIdUsuario(), nuevaPassword);

            if (actualizado) {
                System.out.println("[DEBUG] Contraseña actualizada correctamente, limpiando token");
                usuarioDAO.limpiarToken(usuario.getIdUsuario());
                response.sendRedirect(request.getContextPath() + "/login?success=Contraseña+actualizada+correctamente");
            } else {
                System.out.println("[DEBUG] Fallo al actualizar contraseña en BD");
                request.setAttribute("error", "Error al actualizar la contraseña");
                request.setAttribute("token", token);
                doGet(request, response);
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Error en ResetPasswordServlet: " + e.getMessage());
            request.setAttribute("error", "Error al procesar la solicitud. Por favor, inténtalo nuevamente.");
            request.setAttribute("token", token);
            doGet(request, response);
        }
    }
}
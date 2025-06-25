package com.example.unmujeres.servlets.coordinador;

import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.daos.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/coordinador/editarPasswordCoordi")
public class EditarPasswordServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.getRequestDispatcher("/coordinador/editarPassword.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            String nuevaContraseña = request.getParameter("contraseña");
            String confirmacion = request.getParameter("confirmarContraseña");

            // Validaciones de contraseña
            List<String> erroresPwd = new ArrayList<>();
            Map<String, Boolean> requisitosPwd = new LinkedHashMap<>();

            if (nuevaContraseña == null || nuevaContraseña.trim().isEmpty()) {
                erroresPwd.add("La contraseña es obligatoria");
                requisitosPwd.put("len", false);
                requisitosPwd.put("may", false);
                requisitosPwd.put("min", false);
                requisitosPwd.put("num", false);
                requisitosPwd.put("spec", false);
            } else {
                boolean len = nuevaContraseña.length() >= 8;
                boolean may = nuevaContraseña.matches(".*[A-Z].*");
                boolean min = nuevaContraseña.matches(".*[a-z].*");
                boolean num = nuevaContraseña.matches(".*\\d.*");
                boolean spec = nuevaContraseña.matches(".*[\\W_].*");

                if (!len) erroresPwd.add("Mínimo 8 caracteres");
                if (!may) erroresPwd.add("Al menos una mayúscula");
                if (!min) erroresPwd.add("Al menos una minúscula");
                if (!num) erroresPwd.add("Al menos un número");
                if (!spec) erroresPwd.add("Al menos un carácter especial");
                if (!nuevaContraseña.equals(confirmacion)) erroresPwd.add("Las contraseñas no coinciden");

                requisitosPwd.put("len", len);
                requisitosPwd.put("may", may);
                requisitosPwd.put("min", min);
                requisitosPwd.put("num", num);
                requisitosPwd.put("spec", spec);
            }

            request.setAttribute("requisitosPwd", requisitosPwd);

            if (!erroresPwd.isEmpty()) {
                request.setAttribute("error", String.join(", ", erroresPwd));
                doGet(request, response);
                return;
            }

            // Actualizar contraseña
            boolean actualizado = usuarioDAO.actualizarContraseña(
                    usuario.getIdUsuario(),
                    nuevaContraseña
            );

            if (actualizado) {
                Usuario usuarioActualizado = usuarioDAO.obtenerUsuarioPorId(usuario.getIdUsuario());
                session.setAttribute("usuario", usuarioActualizado);
                response.sendRedirect(request.getContextPath() +
                        "/coordinador/perfilCOORD?success=Contrase%C3%B1a+actualizada+correctamente");
            } else {
                manejarError(request, response, "No se pudo actualizar la contraseña");
            }

        } catch (SQLException e) {
            manejarError(request, response, "Error de base de datos: " + e.getMessage());
        }
    }

    private void manejarError(HttpServletRequest request, HttpServletResponse response,
                              String mensajeError) throws ServletException, IOException {
        request.setAttribute("error", mensajeError);
        doGet(request, response);
    }
}
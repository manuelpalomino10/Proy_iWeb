package com.example.unmujeres.servlets.encuestador;

import com.example.unmujeres.beans.Distritos;
import com.example.unmujeres.daos.UsuarioDAO;
import com.example.unmujeres.daos.DistritoDAO;
import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.daos.HashUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/encuestador/editarDatos")
public class EditarPerfilServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;
    private DistritoDAO distritoDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
        distritoDAO = new DistritoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            Usuario usuarioActual = usuarioDAO.obtenerUsuarioPorId(usuario.getIdUsuario());
            List<Distritos> distritos = distritoDAO.obtenerTodosDistritos();

            request.setAttribute("usuario", usuarioActual);
            request.setAttribute("distritos", distritos);
            request.getRequestDispatcher("/encuestador/editarDatos.jsp").forward(request, response);

        } catch (SQLException e) {
            manejarError(request, response, "Error al cargar datos del usuario", null);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
        Map<String, String> errores = new HashMap<>();
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setIdUsuario(usuarioSesion.getIdUsuario());

        try {
            // 1. Obtener datos actuales como base
            Usuario usuarioActual = usuarioDAO.obtenerUsuarioPorId(usuarioSesion.getIdUsuario());

            // 2. Procesar solo campos con datos nuevos
            String direccion = request.getParameter("direccion");
            if (direccion != null && !direccion.trim().isEmpty()) {
                usuarioActualizado.setDireccion(direccion.trim());
            } else {
                usuarioActualizado.setDireccion(usuarioActual.getDireccion());
            }

            String distritoParam = request.getParameter("distritos_iddistritos");
            if (distritoParam != null && !distritoParam.isEmpty()) {
                try {
                    usuarioActualizado.setIddistritos(Integer.parseInt(distritoParam));
                } catch (NumberFormatException e) {
                    errores.put("distrito", "Distrito no válido");
                }
            } else {
                usuarioActualizado.setIddistritos(usuarioActual.getIddistritos());
            }

            // 3. Validación condicional de contraseña
            String contraseña = request.getParameter("contraseña");
            String confirmarContraseña = request.getParameter("confirmarContraseña");

            if (contraseña != null && !contraseña.trim().isEmpty()) {
                Map<String, Object> resultadoValidacion = validarContraseña(contraseña, confirmarContraseña);

                @SuppressWarnings("unchecked")
                List<String> erroresPwd = (List<String>) resultadoValidacion.get("errores");
                if (!erroresPwd.isEmpty()) {
                    errores.put("contraseña", String.join(", ", erroresPwd));
                } else {
                    usuarioActualizado.setContraseña(HashUtil.hashSHA256(contraseña));
                }

                request.setAttribute("requisitosPwd", resultadoValidacion.get("requisitos"));
            } else {
                // Mantener contraseña actual si no se proporciona nueva
                usuarioActualizado.setContraseña(usuarioActual.getContraseña());
            }

            // 4. Si hay errores, volver al formulario
            if (!errores.isEmpty()) {
                request.setAttribute("errores", errores);
                manejarError(request, response, "Corrija los errores del formulario", usuarioActualizado);
                return;
            }

            // 5. Actualizar usuario
            boolean actualizado = usuarioDAO.actualizarUsuario(usuarioActualizado);

            if (actualizado) {
                // Actualizar datos en sesión
                Usuario usuarioCompleto = usuarioDAO.obtenerUsuarioPorId(usuarioSesion.getIdUsuario());
                session.setAttribute("usuario", usuarioCompleto);
                session.setAttribute("exito", "Datos actualizados correctamente");
                response.sendRedirect(request.getContextPath() + "/encuestador/perfil");
            } else {
                manejarError(request, response, "No se pudo actualizar los datos", usuarioActualizado);
            }

        } catch (SQLException e) {
            manejarError(request, response, "Error de base de datos: " + e.getMessage(), usuarioActualizado);
        }
    }

    private Map<String, Object> validarContraseña(String contraseña, String confirmacion) {
        List<String> errores = new ArrayList<>();
        Map<String, Boolean> requisitos = new LinkedHashMap<>();

        boolean len = contraseña.length() >= 8;
        boolean may = contraseña.matches(".*[A-Z].*");
        boolean min = contraseña.matches(".*[a-z].*");
        boolean num = contraseña.matches(".*\\d.*");
        boolean spec = contraseña.matches(".*[\\W_].*");

        if (!len) errores.add("Mínimo 8 caracteres");
        if (!may) errores.add("Al menos una mayúscula");
        if (!min) errores.add("Al menos una minúscula");
        if (!num) errores.add("Al menos un número");
        if (!spec) errores.add("Al menos un carácter especial");
        if (!contraseña.equals(confirmacion)) errores.add("Las contraseñas no coinciden");

        requisitos.put("len", len);
        requisitos.put("may", may);
        requisitos.put("min", min);
        requisitos.put("num", num);
        requisitos.put("spec", spec);

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("errores", errores);
        resultado.put("requisitos", requisitos);

        return resultado;
    }

    private void manejarError(HttpServletRequest request, HttpServletResponse response,
                              String mensaje, Usuario usuario) throws ServletException, IOException {

        request.setAttribute("error", mensaje);

        if (usuario != null) {
            request.setAttribute("usuario", usuario);
        }

        try {
            List<Distritos> distritos = distritoDAO.obtenerTodosDistritos();
            request.setAttribute("distritos", distritos);
        } catch (SQLException e) {
            request.setAttribute("errorSecundario", "Error al cargar distritos");
        }

        request.getRequestDispatcher("/encuestador/editarDatos.jsp").forward(request, response);
    }
}
package com.example.unmujeres.servlets;

import com.example.unmujeres.beans.Distritos;
import com.example.unmujeres.daos.UsuarioDAO;
import com.example.unmujeres.daos.DistritoDAO;
import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.daos.HashUtil; // Corrige el paquete según corresponda
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;

@WebServlet("/editarDatos")
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

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        //
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Usuario usuarioActualizado = usuarioDAO.obtenerUsuarioPorId(usuario.getIdUsuario());
            List<Distritos> distritos = distritoDAO.obtenerTodosDistritos();

            request.setAttribute("usuario", usuario);
            request.setAttribute("distritos", distritos);
            request.getRequestDispatcher("/encuestador/editarDatos.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar datos: " + e.getMessage());
            request.getRequestDispatcher("/encuestador/editarDatos.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/Sistema/login");
            return;
        }

        try {
            String direccion = request.getParameter("direccion");
            String distritoParam = request.getParameter("distritos_iddistritos");
            String contraseña = request.getParameter("contraseña");
            String confirmarContraseña = request.getParameter("confirmarContraseña");

            // ==== VALIDACIÓN DE CONTRASEÑA COMO EN REGISTRO ====
            List<String> erroresPwd = new ArrayList<>();
            Map<String, Boolean> requisitosPwd = new LinkedHashMap<>();
            if (contraseña == null || contraseña.trim().isEmpty()) {
                erroresPwd.add("La contraseña es obligatoria");
                // Todos en falso si está vacío
                requisitosPwd.put("len", false);
                requisitosPwd.put("may", false);
                requisitosPwd.put("min", false);
                requisitosPwd.put("num", false);
                requisitosPwd.put("spec", false);
            } else {
                boolean len = contraseña.length() >= 8;
                boolean may = contraseña.matches(".*[A-Z].*");
                boolean min = contraseña.matches(".*[a-z].*");
                boolean num = contraseña.matches(".*\\d.*");
                boolean spec = contraseña.matches(".*[\\W_].*");
                if (!len) erroresPwd.add("Mínimo 8 caracteres");
                if (!may) erroresPwd.add("Al menos una mayúscula");
                if (!min) erroresPwd.add("Al menos una minúscula");
                if (!num) erroresPwd.add("Al menos un número");
                if (!spec) erroresPwd.add("Al menos un carácter especial");
                // Confirmar contraseña
                if (!contraseña.equals(confirmarContraseña)) erroresPwd.add("Las contraseñas no coinciden");
                requisitosPwd.put("len", len);
                requisitosPwd.put("may", may);
                requisitosPwd.put("min", min);
                requisitosPwd.put("num", num);
                requisitosPwd.put("spec", spec);
            }
            request.setAttribute("requisitosPwd", requisitosPwd); // SIEMPRE lo pasamos

            // Si hay errores, no seguimos
            if (!erroresPwd.isEmpty()) {
                manejarError(request, response, String.join(", ", erroresPwd));
                return;
            }

            // --- Actualizar usuario solo si la contraseña es válida ---
            Usuario usuarioActualizado = new Usuario();
            usuarioActualizado.setIdUsuario(usuarioSesion.getIdUsuario());
            usuarioActualizado.setDireccion(direccion);
            // ¡AQUÍ! Hashea la contraseña antes de guardar
            usuarioActualizado.setContraseña(HashUtil.hashSHA256(contraseña));


            if (distritoParam != null && !distritoParam.isEmpty()) {
                usuarioActualizado.setIddistritos(Integer.parseInt(distritoParam));
            }

            boolean actualizado = usuarioDAO.actualizarUsuario(usuarioActualizado);

            if (actualizado) {
                Usuario usuarioCompleto = usuarioDAO.obtenerUsuarioPorId(usuarioSesion.getIdUsuario());
                session.setAttribute("usuario", usuarioCompleto);
                response.sendRedirect("perfil");
            } else {
                manejarError(request, response, "No se pudo actualizar los datos");
            }

        } catch (SQLException | NumberFormatException e) {
            manejarError(request, response, "Error al guardar: " + e.getMessage());
        }
    }

    private void manejarError(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        request.setAttribute("error", mensaje);
        try {
            List<Distritos> distritos = distritoDAO.obtenerTodosDistritos();
            request.setAttribute("distritos", distritos);
        } catch (SQLException e) {
            request.setAttribute("error", mensaje + " | Error adicional al cargar distritos");
        }
        request.getRequestDispatcher("/encuestador/editarDatos.jsp").forward(request, response);
    }

}
package com.example.unmujeres.servlets;

import com.example.unmujeres.beans.Distritos;
import com.example.unmujeres.daos.UsuarioDAO;
import com.example.unmujeres.daos.DistritoDAO;
import com.example.unmujeres.beans.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;




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
            request.getRequestDispatcher("editarDatos.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar datos: " + e.getMessage());
            request.getRequestDispatcher("editarDatos.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

        // Validar sesión
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Recoger parámetros del formulario
            String direccion = request.getParameter("direccion");
            String distritoParam = request.getParameter("distritos_iddistritos");
            String contraseña = request.getParameter("contraseña");

            // Actualizar objeto usuario
            Usuario usuarioActualizado = new Usuario();
            usuarioActualizado.setIdUsuario(usuarioSesion.getIdUsuario());
            usuarioActualizado.setDireccion(direccion);
            usuarioActualizado.setContraseña(contraseña);

            // Procesar distrito
            if (distritoParam != null && !distritoParam.isEmpty()) {
                usuarioActualizado.setIddistritos(Integer.parseInt(distritoParam));
            }

            // Ejecutar actualización
            boolean actualizado = usuarioDAO.actualizarUsuario(usuarioActualizado);

            if (actualizado) {
                // Actualizar datos en sesión
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
        request.getRequestDispatcher("editarDatos.jsp").forward(request, response);
    }
}
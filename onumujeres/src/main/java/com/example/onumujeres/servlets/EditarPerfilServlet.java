package com.example.onumujeres.servlets;

import com.example.onumujeres.daos.UsuarioDAO;
import com.example.onumujeres.daos.DistritoDAO;
import com.example.onumujeres.beans.Usuario;
import com.example.onumujeres.beans.Distrito;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/editar-datos")
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
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");

        if (idUsuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            Usuario usuario = usuarioDAO.obtenerUsuarioPorId(idUsuario);
            List<Distrito> distritos = distritoDAO.obtenerTodosDistritos();

            request.setAttribute("usuario", usuario);
            request.setAttribute("distritos", distritos);
            request.getRequestDispatcher("editar-datos.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
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
            String direccion = request.getParameter("direccion");
            String distritoParam = request.getParameter("distritos_iddistritos");
            Integer distritos_iddistritos = null;
            if (distritoParam != null && !distritoParam.isEmpty()) {
                distritos_iddistritos = Integer.parseInt(distritoParam);
            }
            String contraseña = request.getParameter("contraseña");

            Usuario usuario = new Usuario();
            usuario.setIdusuario(idUsuario);
            usuario.setDireccion(direccion);
            usuario.setDistritos_iddistritos(distritos_iddistritos);
            usuario.setContraseña(contraseña);

            boolean actualizado = usuarioDAO.actualizarUsuario(usuario);

            if (actualizado) {
                response.sendRedirect("perfil"); // Redirige al perfil actualizado
            } else {
                response.sendRedirect("error.jsp"); // Manejar el error
            }

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
package com.example.unmujeres.filtros;

import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.daos.UsuarioDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebFilter(filterName = "SessionFilter")
public class SessionFilter implements Filter {

    UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        System.out.println("SESSION FILTER: Verificando sesión para acceder a: " + req.getRequestURI());
        // 1. Verificar sesión activa
        if (session == null || session.getAttribute("usuario") == null) {
            System.out.println("SESSION FILTER: sesión nula o inválida");
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // 2. Verificar objeto usuario válido
        Usuario user = (Usuario) session.getAttribute("usuario");
        if (user == null || user.getIdUsuario() <= 0 || user.getIdroles() <= 0) {
            session.invalidate();
            System.out.println("SESSION FILTER: sesión con usuario invalido o nulo o baneado");
            res.sendRedirect(req.getContextPath() + "/login?error=invalid_user");
            return;
        // 2.1 Verificar estado activo
        // puede afectar rendimiento en servidor
        } else {
            try {
                if (!usuarioDAO.usuarioActivo(user.getIdUsuario())) {
                    session.invalidate();
                    res.sendRedirect(req.getContextPath() + "/login?error=banned_user");
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("SESSION FILTER: ACEPTADO");
        chain.doFilter(request, response);
    }
}
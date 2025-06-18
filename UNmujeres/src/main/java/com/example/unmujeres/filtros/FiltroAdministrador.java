package com.example.unmujeres.filtros;

import com.example.unmujeres.beans.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter({"/DashboardCuidado", "/CrearCordiServlet","/editarPasswordAdmin","/perfilAD","/ReportesServlet","/UsuarioServlet"})
public class FiltroAdministrador implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuario == null) {

            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        if (usuario.getIdroles() != 1) {
            // No es administrador
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso restringido solo a administradores.");
            return;
        }

        // Usuario válido, continuar
        chain.doFilter(req, res);
    }
}

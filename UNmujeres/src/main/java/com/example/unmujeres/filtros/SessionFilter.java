package com.example.unmujeres.filtros;

import com.example.unmujeres.beans.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter(filterName = "SessionFilter")
public class SessionFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);


        System.out.println("FILTRO DE SESSION");
        System.out.println("SESSIONFILTER: Verificando sesión para acceder a: " + req.getRequestURI());
        // 1. Verificar sesión activa
        if (session == null || session.getAttribute("usuario") == null) {
            System.out.println("SESSIONFILTER: sesión nula o invalido");
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // 2. Verificar objeto usuario válido
        Usuario user = (Usuario) session.getAttribute("usuario");
        if (user == null || user.getIdUsuario() <= 0 || user.getIdroles() <= 0) {
            session.invalidate();
            System.out.println("SESSIONFILTER: sesión con usuario invalido o nulo");
            res.sendRedirect(req.getContextPath() + "/login?error=invalid_user");
            return;
        }

        chain.doFilter(request, response);
    }
}
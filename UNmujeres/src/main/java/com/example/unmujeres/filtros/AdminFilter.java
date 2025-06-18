package com.example.unmujeres.filtros;

import com.example.unmujeres.beans.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter(urlPatterns = {"/administrador/*"})
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        Usuario user = (Usuario) req.getSession().getAttribute("usuario");
        System.out.println("FILTRO DE ADMINISTRADOR");

        // Validación de rol
        if (user.getIdroles() != 1) {
            System.out.println("F_ADMIN: DENEGADO");
            res.sendRedirect(req.getContextPath() + "/access-denied.jsp");
            return;
        }

        System.out.println("F_ADMIN: ACEPTADO");
        chain.doFilter(request, response);
    }
}

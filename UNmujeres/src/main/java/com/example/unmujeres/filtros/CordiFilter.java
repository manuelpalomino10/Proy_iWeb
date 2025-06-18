package com.example.unmujeres.filtros;

import com.example.unmujeres.beans.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter(urlPatterns = {"/coordinador"})
public class CordiFilter extends SessionFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        Usuario user = (Usuario) req.getSession().getAttribute("usuario");
        System.out.println("FILTRO DE COORDINADOR");

        // Validación de rol
        if (user.getIdroles() != 2) {
            System.out.println("F_CORDI: DENEGADO");
            res.sendRedirect(req.getContextPath() + "/access-denied");
            return;
        }

        System.out.println("F_CORDI: ACEPTADO");
        chain.doFilter(request, response);
    }
}

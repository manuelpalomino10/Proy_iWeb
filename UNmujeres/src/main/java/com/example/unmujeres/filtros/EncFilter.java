package com.example.unmujeres.filtros;

import com.example.unmujeres.beans.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter(urlPatterns = {"/encuestador/*"})
public class EncFilter extends SessionFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        Usuario user = (Usuario) req.getSession().getAttribute("usuario");

        // Validación de rol
        if (user.getIdroles() != 3) {
            System.out.println("FILTRO ENC: DENEGADO");
            res.sendRedirect(req.getContextPath() + "/access-denied.jsp");
            return;
        }

        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);
                
        System.out.println("FILTRO ENC: ACEPTADO");
        chain.doFilter(request, response);
    }
}

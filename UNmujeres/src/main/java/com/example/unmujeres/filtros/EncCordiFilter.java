package com.example.unmujeres.filtros;

import com.example.unmujeres.beans.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class EncCordiFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        Usuario user = (Usuario) req.getSession().getAttribute("usuario");
        System.out.println("FILTRO DE ENCUESTADOR o CORDI");
        int rol = user.getIdroles();

        // Roles 2 (Coordinador) o 3 (Encuestador)
        if (rol != 2 && rol != 3) {
            System.out.println("F_ENC.o.CORD: DENEGADO");
            res.sendRedirect(req.getContextPath() + "/access-denied");
            return;
        }
        System.out.println("F_ENC.o.CORD: ACEPTADO");
        chain.doFilter(request, response);
    }
}

package com.example.unmujeres.servlets;

import com.example.unmujeres.daos.EncuestadorDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/verify")
public class VerifyEmailServlet extends HttpServlet {
    private final EncuestadorDAO dao = new EncuestadorDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        boolean valido = false;
        if (code != null) {
            try {
                valido = dao.findByCodigo(code) != null;
            } catch (SQLException e) {
                throw new ServletException("Error de base de datos", e);
            }
        }
        if (!valido) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        req.setAttribute("code", code);
        req.getRequestDispatcher("/Sistema/verify.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirm");

        Map<String, String> errores = new HashMap<>();
        Map<String, Boolean> requisitosPwd = new LinkedHashMap<>();
        List<String> erroresPwd = new ArrayList<>();

        if (password == null || password.trim().isEmpty()) {
            erroresPwd.add("La contraseña es obligatoria");
            requisitosPwd.put("len", false);
            requisitosPwd.put("may", false);
            requisitosPwd.put("min", false);
            requisitosPwd.put("num", false);
            requisitosPwd.put("spec", false);
        } else {
            boolean len = password.length() >= 8;
            boolean may = password.matches(".*[A-Z].*");
            boolean min = password.matches(".*[a-z].*");
            boolean num = password.matches(".*\\d.*");
            boolean spec = password.matches(".*[\\W_].*");
            if (!len) erroresPwd.add("Mínimo 8 caracteres");
            if (!may) erroresPwd.add("Al menos una mayúscula");
            if (!min) erroresPwd.add("Al menos una minúscula");
            if (!num) erroresPwd.add("Al menos un número");
            if (!spec) erroresPwd.add("Al menos un carácter especial");
            requisitosPwd.put("len", len);
            requisitosPwd.put("may", may);
            requisitosPwd.put("min", min);
            requisitosPwd.put("num", num);
            requisitosPwd.put("spec", spec);
        }

        if (confirm == null || !confirm.equals(password)) {
            erroresPwd.add("Las contraseñas no coinciden");
        }

        if (!erroresPwd.isEmpty()) {
            errores.put("password", String.join(", ", erroresPwd));
        }
        req.setAttribute("requisitosPwd", requisitosPwd);
        req.setAttribute("code", code);

        if (!errores.isEmpty()) {
            req.setAttribute("errores", errores);
            req.getRequestDispatcher("/Sistema/verify.jsp").forward(req, resp);
            return;
        }

        try {
            String hashed = dao.hashPassword(password);
            dao.activate(code, hashed);
        } catch (SQLException e) {
            throw new ServletException("Error al activar cuenta", e);
        }

        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
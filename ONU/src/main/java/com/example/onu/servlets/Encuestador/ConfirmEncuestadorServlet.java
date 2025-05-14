package com.example.onu.servlets.Encuestador;


import com.example.onu.daos.EncuestadorDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/encuestador/confirm")
public class ConfirmEncuestadorServlet extends HttpServlet {
    private final EncuestadorDAO dao = new EncuestadorDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Mostrar el formulario de contraseña si viene el code
        String code = req.getParameter("code");
        if (code == null || code.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Código de validación faltante");
            return;
        }
        req.getRequestDispatcher("/createPassword.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String code            = req.getParameter("code");
        String password        = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        // 1) Verificar que llegue el code
        if (code == null || code.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Código de validación faltante");
            return;
        }

        // 2) Verificar que las contraseñas coincidan
        if (!password.equals(confirmPassword)) {
            req.setAttribute("error", "Las contraseñas no coinciden");
            req.getRequestDispatcher("/createPassword.jsp").forward(req, resp);
            return;
        }

        // 3) Validar complejidad
        String pwdRegex = "^(?=.{8,}$)(?=.*[A-Z])(?=.*\\d)(?=.*\\W).*$";
        if (!password.matches(pwdRegex)) {
            req.setAttribute("error",
                    "La contraseña debe tener al menos 8 caracteres, " +
                            "1 mayúscula, 1 número y 1 carácter especial.");
            req.getRequestDispatcher("/createPassword.jsp").forward(req, resp);
            return;
        }

        // 4) Activar cuenta (hash + update)
        String hashed = dao.hashPassword(password);
        try {
            dao.activate(code, hashed);
        } catch (SQLException ex) {
            throw new ServletException("No se pudo activar la cuenta", ex);
        }

        // 5) Redirigir a éxito
        req.getRequestDispatcher("/success.jsp").forward(req, resp);
    }
}

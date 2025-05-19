package com.example.onu.servlets.Encuestador;

import com.example.onu.beans.Usuario;
import com.example.onu.daos.EncuestadorDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/encuestador/register")
public class RegistroEncuestadorServlet extends HttpServlet {
    private final EncuestadorDAO dao = new EncuestadorDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, String> errores = new HashMap<>();

        String nombre    = req.getParameter("nombre");
        String apellido  = req.getParameter("apellido");
        String dniParam  = req.getParameter("dni");
        String direccion = req.getParameter("direccion");
        String distrito  = req.getParameter("distrito");
        String correo    = req.getParameter("correo");
        String password  = req.getParameter("password");

        // Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            errores.put("nombre", "El nombre es obligatorio");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            errores.put("apellido", "El apellido es obligatorio");
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            errores.put("direccion", "La dirección es obligatoria");
        }
        if (distrito == null || distrito.trim().isEmpty()) {
            errores.put("distrito", "El distrito es obligatorio");
        }
        if (correo == null || correo.trim().isEmpty()) {
            errores.put("correo", "El correo es obligatorio");
        }
        // Contraseña: mínimo 8, al menos una letra mayúscula, una letra minúscula, un número y un caracter especial
        if (password == null || password.trim().isEmpty()) {
            errores.put("password", "La contraseña es obligatoria");
        } else if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$")) {
            errores.put("password", "Debe tener mínimo 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial");
        }

        // DNI debe ser numérico y de 8 dígitos
        int dni = 0;
        if (dniParam == null || dniParam.trim().isEmpty()) {
            errores.put("dni", "El DNI es obligatorio");
        } else {
            try {
                dni = Integer.parseInt(dniParam);
                if (dniParam.length() != 8) {
                    errores.put("dni", "El DNI debe tener 8 dígitos");
                }
            } catch (NumberFormatException e) {
                errores.put("dni", "El DNI debe ser numérico");
            }
        }

        // Validar unicidad solo si no hay errores básicos
        if (errores.isEmpty()) {
            try {
                if (dao.existeDni(dni)) {
                    errores.put("dni", "El DNI ya está registrado.");
                }
                if (dao.existeEmail(correo)) {
                    errores.put("correo", "El correo ya está registrado.");
                }
            } catch (SQLException ex) {
                throw new ServletException("Error validando unicidad", ex);
            }
        }

        // Si hay errores, vuelve al form con errores y valores previos
        if (!errores.isEmpty()) {
            req.setAttribute("errores", errores);
            req.setAttribute("nombre", nombre);
            req.setAttribute("apellido", apellido);
            req.setAttribute("dni", dniParam);
            req.setAttribute("direccion", direccion);
            req.setAttribute("distrito", distrito);
            req.setAttribute("correo", correo);
            req.getRequestDispatcher("/form.jsp").forward(req, resp);
            return;
        }

        // 4) Construir bean Usuario y registrar normalmente
        Usuario u = new Usuario();
        u.setNombres(nombre);
        u.setApellidos(apellido);
        u.setDNI(dni);
        u.setDireccion(direccion);
        u.setDistritos_iddistritos(Integer.parseInt(distrito));
        u.setCorreo(correo);
        u.setContraseña(password);
        u.setCod_enc(dao.generarCodigoUnico());
        u.setEstado((byte)0);
        u.setRoles_idroles(3);

        try {
            dao.insert(u);
        } catch (SQLException ex) {
            throw new ServletException("Error al insertar usuario", ex);
        }

        // Redirige a success.jsp (evita reenvío de formulario al refrescar)
        resp.sendRedirect(req.getContextPath() + "/success.jsp");
    }
}

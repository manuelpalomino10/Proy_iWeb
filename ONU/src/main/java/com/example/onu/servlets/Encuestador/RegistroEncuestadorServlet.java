package com.example.onu.servlets.Encuestador;

import com.example.onu.beans.Usuario;
import com.example.onu.daos.EncuestadorDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/encuestador/register")
public class RegistroEncuestadorServlet extends HttpServlet {
    private final EncuestadorDAO dao = new EncuestadorDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Mostrar el formulario de perfil (sin contraseña)
        req.getRequestDispatcher("/form.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 1) Leer parámetros del formulario (sin password)
        String nombre    = req.getParameter("nombre");
        String apellido  = req.getParameter("apellido");
        String dniParam  = req.getParameter("dni");
        String direccion = req.getParameter("direccion");
        String distrito  = req.getParameter("distrito");
        String correo    = req.getParameter("correo");

        // 2) Validar que DNI sea numérico
        int dni;
        try {
            dni = Integer.parseInt(dniParam);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "DNI inválido, sólo números.");
            req.getRequestDispatcher("/form.jsp").forward(req, resp);
            return;
        }

        // 3) Validar unicidad de DNI y correo
        try {
            if (dao.existeDni(dni)) {
                req.setAttribute("error", "El DNI ya está registrado.");
                req.getRequestDispatcher("/form.jsp").forward(req, resp);
                return;
            }
            if (dao.existeEmail(correo)) {
                req.setAttribute("error", "El correo ya está registrado.");
                req.getRequestDispatcher("/form.jsp").forward(req, resp);
                return;
            }
        } catch (SQLException ex) {
            throw new ServletException("Error validando unicidad", ex);
        }

        // 4) Construir bean Usuario sin contraseña
        Usuario u = new Usuario();
        u.setNombres(nombre);
        u.setApellidos(apellido);
        u.setDNI(dni);
        u.setDireccion(direccion);
        // Distrito ya es obligatorio, asumimos que viene válido
        u.setDistritos_iddistritos(Integer.parseInt(distrito));
        u.setCorreo(correo);
        u.setContraseña(null);                    // sin contraseña todavía
        u.setCod_enc(dao.generarCodigoUnico());   // código único para confirmar correo
        u.setEstado((byte)0);                     // 0 = pendiente validación
        u.setRoles_idroles(3);                    // 3 = Encuestador

        // 5) Insertar el usuario en BD
        try {
            dao.insert(u);
            // TODO: aquí podrías enviar el correo con el enlace de validación usando u.getCod_enc()
        } catch (SQLException ex) {
            throw new ServletException("Error al insertar usuario", ex);
        }

        // 6) Forward a una página tipo "Revisa tu correo"
        req.getRequestDispatcher("/emailSent.jsp")
                .forward(req, resp);
    }
}

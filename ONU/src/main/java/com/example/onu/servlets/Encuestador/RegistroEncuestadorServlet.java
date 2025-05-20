package com.example.onu.servlets.Encuestador;

import com.example.onu.beans.Usuario;
import com.example.onu.beans.Distritos;
import com.example.onu.beans.Zona;
import com.example.onu.daos.DistritoDAO;
import com.example.onu.daos.EncuestadorDAO;
import com.example.onu.daos.ZonaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/encuestador/register")
public class RegistroEncuestadorServlet extends HttpServlet {
    private final EncuestadorDAO dao        = new EncuestadorDAO();
    private final DistritoDAO  distritoDao = new DistritoDAO();
    private final ZonaDAO      zonaDao     = new ZonaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            // Carga listas para el form
            List<Zona>       listaZonas     = zonaDao.listarZonas();
            List<Distritos>  listaDistritos = distritoDao.listarDistritos();
            req.setAttribute("listaZonas",     listaZonas);
            req.setAttribute("listaDistritos", listaDistritos);
        } catch (SQLException ex) {
            throw new ServletException("Error cargando zonas/distritos", ex);
        }
        req.getRequestDispatcher("/form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String,String> errores = new HashMap<>();

        // 1) Recoge parámetros
        String nombre        = req.getParameter("nombre");
        String apellido      = req.getParameter("apellido");
        String dniParam      = req.getParameter("dni");
        String direccion     = req.getParameter("direccion");
        String distritoParam = req.getParameter("distrito"); // "iddistritos-idzona"
        String correo        = req.getParameter("correo");
        String password      = req.getParameter("password");
        System.out.println( password);

        // 2) Recargar listas para re-pintar el form si hay errores
        try {
            req.setAttribute("listaZonas",     zonaDao.listarZonas());
            req.setAttribute("listaDistritos", distritoDao.listarDistritos());
        } catch (SQLException ex) {
            throw new ServletException("Error recargando zonas/distritos", ex);
        }

        // 3) Validaciones
        if (nombre == null || nombre.trim().isEmpty())   errores.put("nombre",   "El nombre es obligatorio");
        if (apellido == null || apellido.trim().isEmpty()) errores.put("apellido", "El apellido es obligatorio");
        if (direccion == null || direccion.trim().isEmpty()) errores.put("direccion","La dirección es obligatoria");
        if (correo == null || correo.trim().isEmpty())     errores.put("correo",   "El correo es obligatorio");

        // Contraseña
        if (password == null || password.trim().isEmpty()) {
            errores.put("password", "La contraseña es obligatoria");
        } else {
            List<String> erroresPwd = new ArrayList<>();
            if (password.length() < 8) erroresPwd.add("Mínimo 8 caracteres");
            if (!password.matches(".*[A-Z].*")) erroresPwd.add("Al menos una mayúscula");
            if (!password.matches(".*[a-z].*")) erroresPwd.add("Al menos una minúscula");
            if (!password.matches(".*\\d.*")) erroresPwd.add("Al menos un número");
            if (!password.matches(".*[\\W_].*")) erroresPwd.add("Al menos un carácter especial");
            if (!erroresPwd.isEmpty()) errores.put("password", String.join(", ", erroresPwd));
        }

        // DNI
        int dni = 0;
        if (dniParam == null || dniParam.trim().isEmpty()) {
            errores.put("dni", "El DNI es obligatorio");
        } else {
            try {
                dni = Integer.parseInt(dniParam);
                if (dniParam.length() != 8) errores.put("dni", "El DNI debe tener 8 dígitos");
            } catch (NumberFormatException e) {
                errores.put("dni", "El DNI debe ser numérico");
            }
        }

        // Distrito–Zona
        Integer idDistrito = null, idZona = null;
        if (distritoParam != null && distritoParam.contains("-")) {
            String[] parts = distritoParam.split("-", 2);
            try {
                idDistrito = Integer.parseInt(parts[0]);
                idZona      = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                errores.put("distrito", "Selecciona un distrito válido");
            }
        } else {
            errores.put("distrito", "Selecciona un distrito válido");
        }

        // 4) Unicidad en BD
        if (errores.isEmpty()) {
            try {
                if (dao.existeDni(dni))      errores.put("dni",    "El DNI ya está registrado");
                if (dao.existeEmail(correo)) errores.put("correo", "El correo ya está registrado");
            } catch (SQLException ex) {
                throw new ServletException("Error validando unicidad", ex);
            }
        }

        // 5) Si hay errores, volver al form
        if (!errores.isEmpty()) {
            req.setAttribute("errores",    errores);
            req.setAttribute("nombre",     nombre);
            req.setAttribute("apellido",   apellido);
            req.setAttribute("dni",        dniParam);
            req.setAttribute("direccion",  direccion);
            req.setAttribute("distrito",   distritoParam);
            req.setAttribute("correo",     correo);
            req.getRequestDispatcher("/form.jsp").forward(req, resp);
            return;
        }

        // 6) Todo ok: crear bean y guardar
        Usuario u = new Usuario();
        u.setNombres(nombre);
        u.setApellidos(apellido);
        u.setDNI(dni);
        u.setDireccion(direccion);
        u.setZona_idzona(idZona);
        u.setDistritos_iddistritos(idDistrito);
        u.setCorreo(correo);

        // **Hasheo la contraseña y la asigno**
        String hashed = dao.hashPassword(password);
        u.setContrasena(hashed);
        u.setCod_enc(dao.generarCodigoUnico());
        u.setEstado((byte)1);
        u.setRoles_idroles(3);

        try {
            dao.insert(u);
        } catch (SQLException ex) {
            throw new ServletException("Error al insertar usuario", ex);
        }

        resp.sendRedirect(req.getContextPath() + "/success.jsp");
    }
}

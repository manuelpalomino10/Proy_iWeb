package com.example.unmujeres.servlets;

import com.example.unmujeres.daos.Usuario;
import com.example.unmujeres.beans.Distritos;
import com.example.unmujeres.beans.Zona;
import com.example.unmujeres.daos.DistritoDAO;
import com.example.unmujeres.daos.EncuestadorDAO;
import com.example.unmujeres.daos.ZonaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import com.example.unmujeres.utils.EmailUtil;

@WebServlet("/register")
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
        // La página de registro está ubicada en la carpeta "Sistema" y no debe
        // pasar por los filtros de rol que protegen la carpeta "/encuestador".
        // Por ello se actualiza la ruta del JSP.
        req.getRequestDispatcher("/Sistema/form.jsp").forward(req, resp);
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

        //
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

        //
        if (errores.isEmpty()) {
            try {
                if (dao.existeDni(dni))      errores.put("dni",    "El DNI ya está registrado");
                if (dao.existeEmail(correo)) errores.put("correo", "El correo ya está registrado");
            } catch (SQLException ex) {
                throw new ServletException("Error validando unicidad", ex);
            }
        }

        //
        if (!errores.isEmpty()) {
            req.setAttribute("errores",    errores);
            req.setAttribute("nombre",     nombre);
            req.setAttribute("apellido",   apellido);
            req.setAttribute("dni",        dniParam);
            req.setAttribute("direccion",  direccion);
            req.setAttribute("distrito",   distritoParam);
            req.setAttribute("correo",     correo);
            // Si hay errores se reenvía al formulario público ubicado en la
            // carpeta "Sistema".
            req.getRequestDispatcher("/Sistema/form.jsp").forward(req, resp);
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
        u.setToken(dao.generarCodigoUnico());
        u.setTokenExpires(new java.sql.Timestamp(System.currentTimeMillis() + 24L * 60 * 60 * 1000));
        // estado 0 = pendiente de activación
        u.setEstado((byte)0);
        u.setRoles_idroles(3);

        try {
            dao.insert(u);
            // Después de registrar, enviar correo de verificación
            String base = req.getRequestURL().toString()
                    .replace(req.getRequestURI(), req.getContextPath());
            String link = base + "/verify?code=" + u.getToken();
            String cuerpo = "Para activar su cuenta haga clic en el siguiente " +
                    "enlace: <a href='" + link + "'>Verificar correo</a>";
            EmailUtil.sendEmail(correo, "Verificación de cuenta", cuerpo);
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ServletException("Error al insertar usuario", ex);
        } catch (Exception ex) { // <-- Captura cualquier error de correo
            ex.printStackTrace();
            throw new ServletException("Error al enviar correo", ex);
        }

        resp.sendRedirect(req.getContextPath() + "/Sistema/success.jsp");
    }
}

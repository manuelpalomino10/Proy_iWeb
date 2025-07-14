package com.example.unmujeres.servlets.administrador;
import com.example.unmujeres.beans.Formulario;
import com.example.unmujeres.beans.Zona;
import com.example.unmujeres.daos.CoordiGestionEncDAO;
import com.example.unmujeres.daos.FormularioDAO;
import com.example.unmujeres.daos.RegistroCordiDao;
import com.example.unmujeres.daos.administrador.ZonaDao;
import com.example.unmujeres.utils.EmailUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


@WebServlet(name = "CrearCordiServlet", value = "/administrador/CrearCordiServlet")
public class CrearCordiServlet extends HttpServlet {
   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

       ZonaDao zonaDao = new ZonaDao();
       ArrayList<Zona> listaZonas = zonaDao.obtenerZonas(); // Debes implementar estoAdd commentMore actions
       request.setAttribute("listaZonas", listaZonas);

       String exito = request.getParameter("exito");
       request.setAttribute("registroExitoso", exito != null && exito.equals("true"));

      request.getRequestDispatcher("/administrador/registrarCordi.jsp").forward(request,response);
   }

   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("text/html");
      response.setCharacterEncoding("UTF-8");

      String nombres = request.getParameter("nombres");
      String apellidos = request.getParameter("apellidos");
      String dniStr = request.getParameter("DNI");
      String correo = request.getParameter("correo");
      String idZonaStr = request.getParameter("idZona");

       // Validación de campos vacíos
       if (nombres == null || nombres.isEmpty() ||
               apellidos == null || apellidos.isEmpty() ||
               dniStr == null || correo == null || idZonaStr == null ||
               dniStr.isEmpty() || correo.isEmpty() || idZonaStr.isEmpty()) {

           request.setAttribute("error", "Todos los campos son obligatorios.");
           cargarZonasYValores(request, nombres, apellidos, dniStr, correo, idZonaStr);
           request.getRequestDispatcher("/administrador/registrarCordi.jsp").forward(request, response);
           return;
       }

       // Validación de formato de DNI: exactamente 8 dígitos
       if (!dniStr.matches("\\d{8}")) {
           request.setAttribute("error", "El DNI debe tener exactamente 8 dígitos numéricos.");
           cargarZonasYValores(request, nombres, apellidos, dniStr, correo, idZonaStr);
           request.getRequestDispatcher("/administrador/registrarCordi.jsp").forward(request, response);
           return;
       }

       try {
         int dni = Integer.parseInt(dniStr);
         int idZona = Integer.parseInt(idZonaStr);
         RegistroCordiDao registroCordiDao = new RegistroCordiDao();

          boolean correoExiste = registroCordiDao.existeCorreo(correo);
          boolean dniExiste = registroCordiDao.existeDni(dni);

          if (correoExiste || dniExiste) {
              request.setAttribute("errorCorreo", correoExiste);
              request.setAttribute("errorDni", dniExiste);
              ZonaDao zonaDao = new ZonaDao();
              ArrayList<Zona> listaZonas = zonaDao.obtenerZonas();

              // Mantener datos ingresados
              request.setAttribute("nombres", nombres);
              request.setAttribute("apellidos", apellidos);
              request.setAttribute("DNI", dniStr);
              request.setAttribute("correo", correo);
              request.setAttribute("idZonaSeleccionada", idZonaStr);
              request.setAttribute("listaZonas", listaZonas);

              request.getRequestDispatcher("/administrador/registrarCordi.jsp").forward(request, response);
              return;
          }

          String codigo = registroCordiDao.generarCodigoUnico();
          int idNuevoCordi = registroCordiDao.insertarPendiente(nombres, apellidos, dni, correo, idZona, codigo);
          asignarNuevoCordi(idNuevoCordi);

          // Enviar correo de verificación
          String base = request.getRequestURL().toString()
                  .replace(request.getRequestURI(), request.getContextPath());
          String link = base + "/verify?code=" + codigo;
          String cuerpo = "Para activar su cuenta haga clic en el siguiente enlace: " +
                  "<a href='" + link + "'>Verificar correo</a>";
          EmailUtil.sendEmail(correo, "Verificación de cuenta", cuerpo);

         // Redirige al doGet() correctamente usando ruta completo
         response.sendRedirect(request.getContextPath() + "/administrador/CrearCordiServlet?exito=true");

      } catch (NumberFormatException e) {
         request.setAttribute("error", "DNI debe ser un número válido");

          // También recarga zonas para mostrar el form con errorAdd commentMore actions
          ZonaDao zonaDao = new ZonaDao();
          ArrayList<Zona> listaZonas = zonaDao.obtenerZonas();
          request.setAttribute("listaZonas", listaZonas);

          // Mantener datos ingresados
          request.setAttribute("nombres", nombres);
          request.setAttribute("apellidos", apellidos);
          request.setAttribute("DNI", dniStr);
          request.setAttribute("correo", correo);
          request.setAttribute("idZonaSeleccionada", idZonaStr);

         request.getRequestDispatcher("/administrador/registrarCordi.jsp").forward(request, response);
      } catch (SQLException e) {
          throw new RuntimeException(e);
      }
   }

    // Método de apoyo para evitar duplicación de código
    private void cargarZonasYValores(HttpServletRequest request, String nombres, String apellidos,
                                     String dniStr, String correo, String idZonaStr) {
        try {
            ZonaDao zonaDao = new ZonaDao();
            ArrayList<Zona> listaZonas = zonaDao.obtenerZonas();
            request.setAttribute("listaZonas", listaZonas);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("listaZonas", new ArrayList<Zona>()); // lista vacía como fallback
            request.setAttribute("error", "No se pudieron cargar las zonas.");
        }

        request.setAttribute("nombres", nombres);
        request.setAttribute("apellidos", apellidos);
        request.setAttribute("DNI", dniStr);
        request.setAttribute("correo", correo);
        request.setAttribute("idZonaSeleccionada", idZonaStr);
    }

   private void asignarNuevoCordi(int idCordi) {
       FormularioDAO formularioDAO = new FormularioDAO();
       ArrayList<Formulario> formularios = formularioDAO.getFormularios();

       for (Formulario form : formularios) {
           try {
               CoordiGestionEncDAO asigDAO = new CoordiGestionEncDAO();
               asigDAO.asignarFormulario(idCordi, form.getIdFormulario());
           } catch (SQLException e) {
               throw new RuntimeException(e);
           }
       }
   }

}

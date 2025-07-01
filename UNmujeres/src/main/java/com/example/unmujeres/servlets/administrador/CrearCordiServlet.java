package com.example.unmujeres.servlets.administrador;
import com.example.unmujeres.beans.Zona;
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

      if (dniStr == null || dniStr.isEmpty() || correo == null || correo.isEmpty()) {
          request.setAttribute("error", "Los campos DNI y correo son obligatorios");

         // Recargar zonas también en caso de error
          ZonaDao zonaDao = new ZonaDao();
          ArrayList<Zona> listaZonas = zonaDao.obtenerZonas();

          request.setAttribute("listaZonas", listaZonas);

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
          registroCordiDao.insertarPendiente(nombres, apellidos, dni, correo, idZona, codigo);

          // Enviar correo de verificación
          String base = request.getRequestURL().toString()
                  .replace(request.getRequestURI(), request.getContextPath());
          String link = base + "/verify?code=" + codigo;
          String cuerpo = "Para activar su cuenta haga clic en el siguiente enlace: " +
                  "<a href='" + link + "'>Verificar correo</a>";
          EmailUtil.sendEmail(correo, "Verificación de cuenta", cuerpo);

         // Redirige al doGet() correctamente usando ruta completa
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
}

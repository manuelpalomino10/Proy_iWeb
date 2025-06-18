package com.example.unmujeres.servlets.administrador;
import com.example.unmujeres.beans.Zona;
import com.example.unmujeres.daos.RegistroCordiDao;
import com.example.unmujeres.daos.ZonaDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "CrearCordiServlet", value = "/CrearCordiServlet")
public class CrearCordiServlet extends HttpServlet {
   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

      ZonaDAO zonaDao = new ZonaDAO();
      ArrayList<Zona> listaZonas = new ArrayList<>(zonaDao.listarZonas());
      request.setAttribute("listaZonas", listaZonas);
      request.getRequestDispatcher("administrador/registrarCordi.jsp").forward(request,response);
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
         ZonaDAO zonaDao = new ZonaDAO();
         ArrayList<Zona> listaZonas = new ArrayList<>(zonaDao.listarZonas());
         request.setAttribute("listaZonas", listaZonas);
         request.getRequestDispatcher("administrador/registrarCordi.jsp").forward(request, response);
         return;
      }

      try {
         int dni = Integer.parseInt(dniStr);
         int idZona = Integer.parseInt(idZonaStr);
         RegistroCordiDao registroCordiDao = new RegistroCordiDao();
         registroCordiDao.nuevoCordi(nombres, apellidos, dni, correo, idZona);

         // Redirige al doGet() correctamente usando ruta completa
         response.sendRedirect(request.getContextPath() + "/CrearCordiServlet");

      } catch (NumberFormatException e) {
         request.setAttribute("error", "DNI debe ser un número válido");

         // También recarga zonas para mostrar el form con error
         ZonaDAO zonaDao = new ZonaDAO();
         ArrayList<Zona> listaZonas = new ArrayList<>(zonaDao.listarZonas());
         request.setAttribute("listaZonas", listaZonas);

         request.getRequestDispatcher("administrador/registrarCordi.jsp").forward(request, response);
      }
   }
}

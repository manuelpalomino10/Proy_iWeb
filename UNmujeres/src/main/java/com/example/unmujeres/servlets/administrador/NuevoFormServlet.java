package com.example.unmujeres.servlets.administrador;

import com.example.unmujeres.beans.Categoria;
import com.example.unmujeres.beans.Formulario;
import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.daos.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;

@WebServlet(name = "NuevoFormServlet", value = "/administrador/NuevoFormServlet")
public class NuevoFormServlet extends HttpServlet {

    CategoriaDAO categoriaDAO = new CategoriaDAO();

    FormularioDAO formularioDAO = new FormularioDAO();
    SeccionDAO seccionDAO = new SeccionDAO();
    PreguntaDAO preguntaDAO = new PreguntaDAO();
    OpcionPreguntaDAO opcionDAO = new OpcionPreguntaDAO();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        // Obtener sesión sin crear una nueva
        HttpSession session = request.getSession(false);

        Usuario user = (Usuario) session.getAttribute("usuario");
        int idUser = user.getIdUsuario();
        int userRole = user.getIdroles();


        ArrayList<Categoria> categorias = categoriaDAO.getCategorias();
        request.setAttribute("categorias", categorias);
        String hoy = String.valueOf(LocalDate.now());
        request.setAttribute("hoy", hoy);

        request.getRequestDispatcher("crearForm.jsp").forward(request, response);





    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        // Obtener sesión sin crear una nueva
        HttpSession session = request.getSession(false);

        Usuario user = (Usuario) session.getAttribute("usuario");
        int idUser = user.getIdUsuario();
        int userRole = user.getIdroles();

        Formulario nuevoFormulario = new Formulario();
        nuevoFormulario.setFechaCreacion(new Date(System.currentTimeMillis()));

        request.getParameter("nombre_form");
        String nombreFormParam = request.getParameter("nombreForm");
        String fechaLimiteParam = request.getParameter("fechaLimite");
        String esperadosParam = request.getParameter("esperados");
        String idCategoriaParam = request.getParameter("idCategoria");

//        int idCategoria;
//        int esperados;

        try {
            if (nombreFormParam==null && nombreFormParam.trim().isEmpty()) {
                throw new NullPointerException("El formulario debe tener un nombre");
            } else {
                nuevoFormulario.setNombre(nombreFormParam);
            }
            if (fechaLimiteParam ==null && fechaLimiteParam.trim().isEmpty()) {
                throw new NullPointerException("Tiene que asignar una fecha límite al formulario");
            } else {
                DateTimeFormatter sqlFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate fechaLimite = LocalDate.parse(fechaLimiteParam.trim(), sqlFormatter);

                if (fechaLimite.isBefore(LocalDate.now())) {
                    throw new IllegalArgumentException("No se permiten fechas futuras");
                }

                //nuevoFormulario.setFechaCreacion(fechaLimite);
            }
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage());
            response.sendRedirect("error.jsp");
            return;
        } catch (DateTimeParseException e) {
            session.setAttribute("error", e.getMessage());
            response.sendRedirect("error.jsp");
        }


        Part filePart = request.getPart("csvFile");
        if (filePart == null || filePart.getSize() == 0) {  //Validar que exista y no esté vacío
            //request.setAttribute("error", "Debe seleccionar un archivo.");
            session.setAttribute("error", "Debe seleccionar un archivo.");
            response.sendRedirect(request.getContextPath() + "/coordinador/SubirRegistrosServlet");
            return;
        }
        String fileName = filePart.getSubmittedFileName();
        if (fileName == null || !fileName.toLowerCase().endsWith(".csv")) {    //Validar extensión .csv
            //request.setAttribute("error", "El archivo debe ser de tipo CSV (.csv)");
            session.setAttribute("error", "El archivo debe ser de tipo CSV (.csv).");
            response.sendRedirect(request.getContextPath() + "/coordinador/SubirRegistrosServlet");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(filePart.getInputStream(), "UTF-8"))) {
            String line;
            String delimiter = ";";

            while ((line = reader.readLine()) != null) {

            }
        }


    }

    @Override
    public void init() throws ServletException {
        // Código de inicialización (opcional)
    }

    @Override
    public void destroy() {
        // Código de limpieza (opcional)
    }
}
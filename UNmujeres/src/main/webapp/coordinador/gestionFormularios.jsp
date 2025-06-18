<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.unmujeres.dtos.FormularioDto" %>


<%
  ArrayList<FormularioDto> lista = (ArrayList<FormularioDto>) request.getAttribute("lista");
%>

<!DOCTYPE html>
<html lang="es">
<head>
  <jsp:include page="../header.jsp" />
  <title>Gestión de Formularios - ONU Mujeres</title>

  <!-- DataTables CSS -->
  <link rel="stylesheet" href="https://cdn.datatables.net/1.13.4/css/jquery.dataTables.min.css">
</head>
<body id="page-top">
<div id="wrapper">
  <jsp:include page="../sidebarCoordi.jsp" />

  <div id="content-wrapper" class="d-flex flex-column">
    <div id="content">
      <jsp:include page="../topbarEnc.jsp" />

      <div class="container-fluid">
        <h1 class="h3 mb-4 text-gray-800">Gestión de Formularios</h1>

        <div class="card shadow mb-4">
          <div class="card-body">
            <div class="table-responsive">
              <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                <thead>
                <tr>
                  <th>Nombre de Formulario</th>
                  <th>Categoría</th>
                  <th>Fecha creación</th>
                  <th>Estado</th>
                  <th>Eliminar</th>
                </tr>
                </thead>
                <tbody>
                <% for (FormularioDto formularioDto : lista) { %>
                <tr>
                  <td><%= formularioDto.getNombreForm() %></td>
                  <td><%= formularioDto.getNombreCat() %></td>
                  <td><%= formularioDto.getFechaCreacion() %></td>
                  <td>
                    <form action="${pageContext.request.contextPath}/coordinador/GestionFormServlet" method="post">
                      <input type="hidden" name="id" value="<%= formularioDto.getId() %>">
                      <input type="hidden" name="estado" value="<%= formularioDto.isEstado() ? "true" : "false" %>">
                      <button type="submit" class="btn <%= formularioDto.isEstado() ? "btn-danger" : "btn-success" %> btn-sm">
                        <%= formularioDto.isEstado() ? "Desactivado" : "Activado" %>
                      </button>
                    </form>
                  </td>
                  <td>
                    <form action="${pageContext.request.contextPath}/coordinador/EliminarFormServlet" method="post" onsubmit="return confirm('¿Estás seguro de eliminar este formulario?');">
                      <input type="hidden" name="id" value="<%= formularioDto.getId() %>">
                      <button type="submit" class="btn btn-danger btn-sm">
                        <i class="fas fa-trash-alt"></i> Eliminar
                      </button>
                    </form>
                  </td>
                </tr>
                <% } %>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

    </div>
    <jsp:include page="../footer.jsp" />
  </div>
</div>

<a class="scroll-to-top rounded" href="#page-top">
  <i class="fas fa-angle-up"></i>
</a>
</body>
</html>

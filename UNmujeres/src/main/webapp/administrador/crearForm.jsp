<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.unmujeres.beans.Categoria" %>
<%@ page import="java.time.LocalDate" %><%--
  Created by IntelliJ IDEA.
  User: Luis
  Date: 23/06/2025
  Time: 17:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  ArrayList<Categoria> categorias = (ArrayList<Categoria>) request.getAttribute("categorias");

  String nombreForm="";
  int catSel=0;
  if (request.getAttribute("nombreForm")!=null) {
      nombreForm = (String) request.getAttribute("nombreForm");
  }
  if (request.getAttribute("cat")!=null) {
    catSel = (int) request.getAttribute("cat");
  }
%>
<!DOCTYPE html>
<html lang="es">

<jsp:include page="/header.jsp" />

<body id="page-top">

<!-- Page Wrapper -->
<div id="wrapper">

  <!-- Sidebar -->
  <jsp:include page="/sidebarAdmin.jsp" />
  <!-- End of Sidebar -->

  <!-- Content Wrapper -->
  <div id="content-wrapper" class="d-flex flex-column">

    <!-- Main Content -->
    <div id="content">

      <!-- Topbar -->
      <jsp:include page="../topbarAdmin.jsp" />
      <!-- End of Topbar -->

      <!-- Begin Page Content -->
      <div class="container-fluid">

        <!-- Page Heading -->
        <h1 class="h3 mb-2 text-gray-800">Generar Reporte</h1>

          <!-- Contenedor del formulario centrado -->
          <div class="col-lg-10 mx-auto">
              <div class="card shadow mb-4">
                  <div class="card-body">

                        <!-- ------------- FORMULARIO DE PARAMETROS DE FORMULARIO ------------- -->
                      <form id="fparametros" method="POST" action="<%=request.getContextPath()%>/administrador/NuevoFormServlet" enctype="multipart/form-data">

                        <!-- Primera fila: Nombre -->
                          <div class="form-row">
                            <div class=" form-group col-md-12">
                              <label for="nombreForm" class="form-label">Nombre de formulario:</label>
                              <input type="text" name="nombreForm" id="nombreForm" class="form-control" placeholder="Nombre de formulario" required/>
                            </div>
                          </div>

                          <!-- Segunda fila: Categoría y Registros Esperados -->
                          <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="idCategoria" class="form-label">Categoría:</label>
                                <select name="idCategoria" id="idCategoria" class="form-control">
                                <option <%= (catSel==0) ? "selected" : "" %> >Seleccionar Categoría</option>
                                <% for (Categoria categoria : categorias) { %>
                                <option value="<%= categoria.getIdCategoria() %>" <%= (categoria.getIdCategoria() == catSel) ? "selected" : "" %>>
                                  <%= categoria.getNombre() %>
                                </option>
                                <% } %>
                              </select>
                            </div>

                            <div class="form-group col-md-6">
                                <label for="esperados" class="form-label">Registros Esperados:</label>
                                <input type="number" name="esperados" id="esperados" class="form-control" min="50" placeholder="100" />
                            </div>

                          </div>

                          <!-- Tercera fila: Fechas -->
                          <div class="form-row">
                            <div class="form-group col-md-6">
                              <label for="fechaCreacion" class="form-label">Fecha de Creación:</label>
                              <input type="date" name="fechaCreacion" id="fechaCreacion" class="form-control" placeholder="DD-MM-YYYY" value="<%= LocalDate.now() %>" readonly/>
                            </div>

                            <div class="form-group col-md-6">
                                <label for="fechaLimite" class="form-label">Fecha límite:</label>
                                <input type="date" name="fechaLimite" id="fechaLimite" class="form-control" placeholder="DD-MM-YYYY" min="<%= LocalDate.now().plusDays(1) %>"/>
                            </div>
                          </div>

    <%--                          <!-- Quinta fila: Registros esperados -->--%>
    <%--                          <div class="row mt-3">--%>
    <%--                            <div class="col-md-4">--%>
    <%--                              <label for="esperados" class="form-label">Registros Esperados:</label>--%>
    <%--                              <input type="number" name="esperados" id="esperados" class="form-control" min="50" placeholder="100" />--%>
    <%--                            </div>--%>
    <%--                          </div>--%>

                          <!-- Cuarta fila: Archivo -->
                          <div class="form-row">
                            <div class="form-group col-md-8">
                              <label for="csvFile" class="form-label">Archivo CSV:</label>
                              <input type="file" name="csvFile" id="csvFile" class="form-control" accept=".csv" required/>
                            </div>
                          </div>

                        <!-- Sección 2 (Botón centrado) -->
                          <div class="text-center d-flex align-items-center justify-content-center">
                          <!-- Botón de filtrado -->
                              <button class="btn btn-primary w-25" type="submit">
                                  <span class="icon text-white-50">
                                      <i class="fas fa-upload"></i>
                                  </span>
                                  <span class="text">Crear</span>
                              </button>
                          </div>
                      </form>
                  </div>
              </div>
          </div>

        <hr>
          <% if (session.getAttribute("error") != null) { %>
        <div>
          <div class="alert alert-danger" role="alert"><%=session.getAttribute("error")%>
          </div>
        </div>
          <% session.removeAttribute("error"); %>
          <% } %>

          <% if (session.getAttribute("success") != null) { %>
        <div>
          <div class="alert alert-success" role="alert"><%=session.getAttribute("success")%>
          </div>
        </div>
          <% session.removeAttribute("success"); %>
          <% } %>
        <hr>

      </div>
      <!-- /.container-fluid -->

    </div>
    <!-- End of Main Content -->

    <!-- Footer -->
    <footer class="sticky-footer bg-white">
      <div class="container my-auto">
        <div class="copyright text-center my-auto">
          <span>Copyright &copy; ONU Mujeres - PUCP 2025</span>
        </div>
      </div>
    </footer>
    <!-- End of Footer -->

  </div>
  <!-- End of Content Wrapper -->

</div>
<!-- End of Page Wrapper -->
<jsp:include page="../footer.jsp" />
</body>
</html>

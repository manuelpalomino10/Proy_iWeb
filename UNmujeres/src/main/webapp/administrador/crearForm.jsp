<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.unmujeres.beans.Categoria" %><%--
  Created by IntelliJ IDEA.
  User: Luis
  Date: 23/06/2025
  Time: 17:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  ArrayList<Categoria> categorias = (ArrayList<Categoria>) request.getAttribute("categorias");

  String hoy=null;
  int catSel=0;
  if (request.getAttribute("hoy")!=null) {
    hoy   = (String) request.getAttribute("hoy");
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

        <!-- ------------- FORMULARIO DE PARAMETROS DE FORMULARIO ------------- -->
        <div class="container">
          <form id="fparametros" method="GET" action="<%=request.getContextPath()%>/administrador/NuevoFormServlet" class="row g-3">

            <!-- Sección 8 (Zona, Rol y Fecha) -->
            <div class="col-md-8">
              <div class="row">
                <!-- Primera fila: Categoría -->
                <div class="col-md-6">
                  <label for="idCategoria" class="form-label">Categoría:</label>
                  <select name="idCategoria" id="idCategoria" class="form-select">
                    <% for (Categoria categoria : categorias) { %>
                    <option value="<%= categoria.getIdCategoria() %>" <%= (categoria.getIdCategoria() == catSel) ? "selected" : "" %>>
                      <%= categoria.getNombre() %>
                    </option>
                    <% } %>
                  </select>
                </div>
              </div>

              <!-- Segunda fila: Fecha límite -->
              <div class="row mt-3">
                <div class="col-md-12">
                  <label for="fechaLimite" class="form-label">Fecha límite:</label>
                  <input type="date" name="fechaLimite" id="fechaLimite" class="form-control" placeholder="DD-MM-YYYY" value="<%=hoy%>" disabled/>
                </div>
              </div>

              <!-- Tercera fila: Registros esperados -->
              <div class="row mt-3">
                <div class="col-md-12">
                  <label for="esperados" class="form-label">Registros Esperados:</label>
                  <input type="number" name="esperados" id="esperados" class="form-control" min="50" placeholder="100" value="<%=catSel%>"/>
                </div>
              </div>

              <!-- Tercera fila: Registros esperados -->
              <div class="row mt-3">
                <div class="col-md-12">
                  <label for="csvFile" class="form-label">Archivo CSV:</label>
                  <input type="file" name="csvFile" id="csvFile" class="form-control"/>
                </div>
              </div>

            </div>

            <!-- Sección 4 (Botón centrado) -->
            <div class="col-md-4 d-flex align-items-center justify-content-center">
              <!-- Botón de filtrado -->
              <button class="btn btn-primary w-100" type="submit">
                <span class="icon text-white-50">
                          <i class="fas fa-upload"></i>
                </span>
                <span class="text">Crear</span>
              </button>
            </div>
          </form>
        </div>

        <hr/>

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

        </hr>

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

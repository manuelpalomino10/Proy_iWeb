<%--
  Created by IntelliJ IDEA.
  User: Luis
  Date: 7/06/2025
  Time: 23:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.unmujeres.beans.EncHasFormulario" %>

<%
  ArrayList<EncHasFormulario> asignaciones = (ArrayList<EncHasFormulario>) request.getAttribute("asignaciones");
  ArrayList<Integer> totalesRegistros = (ArrayList<Integer>) request.getAttribute("totalesRegistros");
%>
<!DOCTYPE html>
<html lang="es">

<jsp:include page="/header.jsp" />

<body id="page-top">

<!-- Page Wrapper -->
<div id="wrapper">

  <!-- Sidebar -->
  <jsp:include page="/sidebarCoordi.jsp" />
  <!-- End of Sidebar -->

  <!-- Content Wrapper -->
  <div id="content-wrapper" class="d-flex flex-column">

    <!-- Main Content -->
    <div id="content">

      <!-- Topbar -->
      <jsp:include page="../topbarEnc.jsp" />
      <!-- End of Topbar -->

      <!-- Begin Page Content -->
      <div class="container-fluid">

        <!-- Page Heading -->
        <h1 class="h3 mb-2 text-gray-800">Lista de Formularios</h1>
<%--        <p class="mb-4">Lista de formularios de tu zona.</p>--%>

        <!-- DataTales Example -->
        <div class="card shadow mb-4">
          <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary"></h6>
          </div>

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

          <div class="card-body">
            <div class="table-responsive">
              <%
                if (asignaciones == null || asignaciones.isEmpty()) {
              %>
              <p>No hay asignaciones.</p>
              <%
              } else {
              %>
              <table class="table table-bordered" id="tablaAsig" width="100%" cellspacing="0">

                <thead>
                <tr>
                  <th>Nombre</th>
                  <th>Total Respuestas Registradas</th>
                  <th>Respuestas esperadas</th>
                  <th>Fecha de asignación</th>
                  <th>Fecha Límite</th>
                  <th>Acciones</th>
                </tr>
                </thead>
                <tbody>
                <% for (EncHasFormulario asignacion : asignaciones) { %>
                <tr>
                  <td><%=asignacion.getFormulario().getNombre()%></td>
                  <td><%=totalesRegistros.get(asignacion.getFormulario().getIdFormulario()-1)%></td>
                  <td><%=asignacion.getFormulario().getRegistrosEsperados()%></td>
                  <td style="white-space: nowrap;"><%=asignacion.getFechaAsignacion()%></td>
                  <td style="white-space: nowrap;"><%=asignacion.getFormulario().getFechaLimite()%></td>
                  <td>
                    <a class="btn btn-success" href="<%=request.getContextPath()%>/SubirRegistrosServlet?action=crear&id_asig=<%= asignacion.getIdEncHasFormulario() %>&id_form=<%=asignacion.getFormulario().getIdFormulario()%>">Crear Registro</a>

<%--                    <a class="btn btn-success" href="<%=request.getContextPath()%>/SubirRegistrosServlet?action=crear&id_asig=<%= asignacion.getIdEncHasFormulario() %>&id_form=<%=asignacion.getFormulario().getIdFormulario()%>">Crear Registro</a>--%>
                    <form id="csvForm_<%= asignacion.getIdEncHasFormulario() %>" action="<%=request.getContextPath()%>/SubirRegistrosServlet" method="POST" enctype="multipart/form-data">
                      <input type="hidden" name="idEhf" value="<%= asignacion.getIdEncHasFormulario() %>" />
                      <button type="button" class="btn btn-primary importBtn" data-id="<%= asignacion.getIdEncHasFormulario() %>">
                        <i class="fas fa-upload mr-2"></i>Importar CSV
                      </button>
                      <input type="file" name="csvFile" id="csvFile_<%= asignacion.getIdEncHasFormulario() %>" class="d-none" accept="text/csv">
                    </form>
                  </td>
                </tr>
                <% } %>
                </tbody>
              <% } %>
              </table>
            </div>
          </div>
        </div>

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


<%--<script>--%>
<%--  $(function(){ $('#tablaAsig').DataTable(); });--%>
<%--</script>--%>
<script>
  // Al hacer clic, activa el selector de archivo de la fila correspondiente
  $(".importBtn").on("click", function() {
    var idAsig = $(this).data("id");
    $("#csvFile_" + idAsig).click();
  });

  // Cuando se selecciona un archivo, se envía el formulario de esa fila
  $("input[type='file']").on("change", function() {
    if ($(this).val() !== "") {
      // Extrae el id único del input file, que corresponde al id de la asignación
      var elementId = $(this).attr("id"); // ejemplo "csvFile_123"
      var idAsig = elementId.split("_")[1];
      $("#csvForm_" + idAsig).submit();
    }
  });
</script>
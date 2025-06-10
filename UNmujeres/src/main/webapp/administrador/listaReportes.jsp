<%--
  Created by IntelliJ IDEA.
  User: Luis
  Date: 7/06/2025
  Time: 01:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.unmujeres.dtos.ReporteDTO" %>
<%@ page import="com.example.unmujeres.beans.Zona" %>
<%@ page import="com.example.unmujeres.beans.Roles" %>

<%
  List<Zona> zonas = (List<Zona>) request.getAttribute("listaZonas");
  List<Roles> roles = (List<Roles>) request.getAttribute("listaRoles");
  List<ReporteDTO> lista = (List<ReporteDTO>) request.getAttribute("reportes");

  int zonaSel = (int) request.getAttribute("zonaSel");
  int rolSel  = (int) request.getAttribute("rolSel");
  String dateRange   = (String) request.getAttribute("dateRange");
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
          <h1 class="h3 mb-2 text-gray-800">Generar Reporte</h1>
          <%--        <p class="mb-4">Filtra y selecciona un formulario</p>--%>

          <!-- ------------- FORMULARIO DE FILTROS ------------- -->
          <form id="filtros" method="GET" action="<%=request.getContextPath()%>/ReportesServlet?action=listaReportes" class="filtros">
            <input type="hidden" name="action" value="listaReportes" />

            Zona:
            <select name="zona">
              <option value="0" <%= (zonaSel == 0) ? "selected" : "" %>>Todos</option>
              <% for (Zona zona : zonas) { %>
              <option value="<%= zona.getIdzona() %>" <%= (zona.getIdzona() == zonaSel) ? "selected" : "" %>>
                <%= zona.getNombre() %>
              </option>
              <% } %>
            </select>

            Rol:
            <select name="rol">
              <option value="0" <%= (rolSel == 0) ? "selected" : "" %>>Todos</option>
              <% for (Roles rol : roles) { %>
              <option value="<%= rol.getIdRoles() %>" <%= (rol.getIdRoles() == rolSel) ? "selected" : "" %>>
                <%= rol.getNombre() %>
              </option>
              <% } %>
            </select>

            <!-- Input para el rango de fechas -->
            <label for="daterange">Rango de fechas:</label>
            <input type="text" name="daterange" id="daterange" class="form-control" placeholder="DD-MM-YYYY - DD-MM-YYYY" value="<%=dateRange%>"/>

            <button class="btn btn-primary" type="submit">Filtrar</button>
          </form>

          <hr/>

          <!-- DataTales Example -->
          <div class="card shadow mb-4">
            <div class="card-header py-3">
              <h6 class="m-0 font-weight-bold text-primary"></h6>
            </div>
            <div class="card-body">
              <div class="table-responsive">
                <%
                  if (lista == null || lista.isEmpty()) {
                %>
                <p>No hay resultados. Ajusta los filtros y presiona <strong>Filtrar</strong>.</p>
                <%
                } else {
                %>

                <table class="table table-bordered" id="tablaAsig" width="100%" cellspacing="0">
                  <thead>
                  <tr>
                    <th>Título Formulario</th>
                    <%--        <th>Zona</th>--%>
                    <%--        <th>Rol</th>--%>
                    <%--        <th>Rango de Fechas</th>--%>
                    <th>Filtro</th>
                    <th># Registros</th>
                    <th>Acciones</th>
                  </tr>
                  </thead>
                  <tbody>
                  <% for (ReporteDTO reporte : lista) { %>
                  <tr>
                    <td><%=reporte.getNombreFormulario()%></td>
                    <%--        <td><%=reporte.getZona()%></td>--%>
                    <%--        <td><%=reporte.getRol()%></td>--%>
                    <%--        <td><%=reporte.getFecha()%></td>--%>
                    <td>
                        <%=reporte.getIdZona()==0?"Todas las zonas" : reporte.getIdZona()%>
                        <%=reporte.getIdRol()==0?"Todos los roles" : reporte.getIdRol()%>
                        <%=reporte.getFechaInicio()==null?"" : reporte.getFechaInicio()%> - <%=reporte.getFechaFin()==null?"" : reporte.getFechaFin()%>
                    </td>
                    <td><%=reporte.getTotalRegistros()%></td>
                    <td><a class="btn btn-primary" href="<%=request.getContextPath()%>/ReportesServlet?action=descargar&id_form=<%=reporte.getIdFormulario()%>&zona=<%=reporte.getIdZona()%>&rol=<%=reporte.getIdRol()%>&daterange=<%=dateRange%>">Descargar Reporte</a></td>

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
<%--  $(function(){ $('#tablaRep').DataTable(); });--%>
<%--</script>--%>
<script>
  $(function() {
    var dateRangeInput = $('#daterange');
    var initialValue = dateRangeInput.val().trim();
    var startDate, endDate;
    var hasValidValue = false;

    // Verificar si hay un valor inicial válido
    if (initialValue) {
      var parts = initialValue.split(" - ");
      if (parts.length === 2) {
        var startMoment = moment(parts[0], 'DD-MM-YYYY', true);
        var endMoment = moment(parts[1], 'DD-MM-YYYY', true);

        if (startMoment.isValid() && endMoment.isValid()) {
          startDate = startMoment;
          endDate = endMoment;
          console.log("fechas es valido")
          hasValidValue = true;
        }
      }
    }

    // Configuración del DateRangePicker
    dateRangeInput.daterangepicker({
      autoUpdateInput: false, // IMPORTANTE: Desactivar actualización automática
      startDate: hasValidValue ? startDate : moment().startOf('month'),
      endDate: hasValidValue ? endDate : moment().endOf('month'),
      locale: {
        format: 'DD-MM-YYYY',
        cancelLabel: 'Limpiar',
        applyLabel: 'Aplicar',
        customRangeLabel: 'Personalizado',
        daysOfWeek: ['Dom', 'Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb'],
        monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre']
      },
      showDropdowns: true,
      opens: 'right'
    });

    // Manejar selección de rango
    dateRangeInput.on('apply.daterangepicker', function(ev, picker) {
      $(this).val(
              picker.startDate.format('DD-MM-YYYY') + ' - ' +
              picker.endDate.format('DD-MM-YYYY')
      );
    });

    // Manejar cancelación
    dateRangeInput.on('cancel.daterangepicker', function(ev, picker) {
      $(this).val('');
    });

    // Si no hay valor inicial válido, limpiar el input
    if (!hasValidValue) {
      dateRangeInput.val('');
    }
  });
</script>



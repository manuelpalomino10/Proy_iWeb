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
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDate" %>

<%
  List<Zona> zonas = (List<Zona>) request.getAttribute("listaZonas");
  List<Roles> roles = (List<Roles>) request.getAttribute("listaRoles");
  List<ReporteDTO> lista = (List<ReporteDTO>) request.getAttribute("reportes");

  int zonaSel = (int) request.getAttribute("zona");
  int rolSel  = (int) request.getAttribute("rol");
  String dateRange = "";
  if (request.getAttribute("daterange")!=null) {
    dateRange   = (String) request.getAttribute("daterange");
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

          <!-- Page Heading -->
          <h1 class="h3 mb-2 text-gray-800">Generar Reporte</h1>
          <%--        <p class="mb-4">Filtra y selecciona un formulario</p>--%>

          <!-- ------------- FORMULARIO DE FILTROS ------------- -->
          <div class="container-fluid">
            <form id="filtros" method="GET"
                  action="<%=request.getContextPath()%>/administrador/ReportesServlet?action=listaReportes"
                  class="row">

              <input type="hidden" name="action" value="listaReportes" />

              <div class="col-12 d-flex align-items-end justify-content-start flex-wrap" style="gap: 1.5rem;">

                <!-- Zona -->
                <div class="form-row">
                  <label for="zona" class="form-label">Zona:</label>
                  <select name="zona" id="zona" class="form-control form-select-sm">
                    <option value="0" <%= (zonaSel == 0) ? "selected" : "" %>>Todos</option>
                    <% String nZonaSel = "";
                    for (Zona zona : zonas) {
                        if (zona.getIdzona() == zonaSel) {
                            nZonaSel= "Zona "+zona.getNombre();
                        }
                    %>
                    <option value="<%= zona.getIdzona() %>" <%= (zona.getIdzona() == zonaSel) ? "selected" : "" %>>
                      <%= zona.getNombre() %>
                    </option>
                    <% } %>
                  </select>
                </div>

                <!-- Rol -->
                <div class="form-row">
                  <label for="rol" class="form-label">Rol:</label>
                  <select name="rol" id="rol" class="form-control form-select-sm">
                    <option value="0" <%= (rolSel == 0) ? "selected" : "" %>>Todos</option>
                    <% String nRolSel = "";
                    for (Roles rol : roles) {
                        if (rol.getIdRoles() == rolSel) {
                            nRolSel= "Rol "+rol.getNombre();
                        }
                    %>
                    <option value="<%= rol.getIdRoles() %>" <%= (rol.getIdRoles() == rolSel) ? "selected" : "" %>>
                      <%= rol.getNombre() %>
                    </option>
                    <% } %>
                  </select>
                </div>

                <!-- Rango de fechas -->
                <div class="form-row">
                  <label for="daterange" class="form-label">Rango de fechas:</label>
                  <input type="text" name="daterange" id="daterange"
                         class="form-control form-select-sm"
                         placeholder="DD-MM-YYYY - DD-MM-YYYY"
                         pattern="^\d{2}-\d{2}-\d{4} - \d{2}-\d{2}-\d{4}$"
                         <%
                           String dateRangeVal;
                           if (dateRange==null) {
                            dateRangeVal="";
                           } else {
                            dateRangeVal=dateRange;
                           }
                         %>
                         value="<%=dateRangeVal%>" />
                </div>

                <!-- Botón -->
                <div class="form-row align-items-center">
                <div class="px-2">
                  <button class="btn btn-primary btn-sm" type="submit">
                    <i class="fas fa-filter me-1"></i> Filtrar
                  </button>
                </div>

                <div class="px-2">
                  <button class="btn-outline-primary btn-circle" onclick="limpiarFormulario()">
                    <i class="fas fa-trash"></i>
                  </button>
                </div>
                </div>

              </div>
            </form>
          </div>
            <br>
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
                <p>No hay reportes disponibles. Ajusta los filtros y presiona <strong>Filtrar</strong>.</p>
                <%
                } else {
                %>

                <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                  <thead>
                  <tr>
                    <th>Título Formulario</th>
                    <th>Filtro</th>
                    <th># Registros</th>
                    <th>Acciones</th>
                  </tr>
                  </thead>
                  <tbody>
                  <% for (ReporteDTO reporte : lista) { %>
                  <tr>
                    <td><%=reporte.getNombreFormulario()%></td>
                    <td>
                        <%=reporte.getIdZona()==0?"Todas las zonas" : nZonaSel%>
                        <br>
                        <%=reporte.getIdRol()==0?"Todos los roles" : nRolSel%>
                        <br>
                        <%
                        DateTimeFormatter dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        String inicioFmt = "";
                        if (reporte.getFechaInicio() != null && !reporte.getFechaInicio().isEmpty()) {
                            LocalDate fecha = LocalDate.parse(reporte.getFechaInicio(), dbFormatter);
                            inicioFmt = fecha.format(displayFormatter);
                        }
                        String finFmt = "";
                        if (reporte.getFechaFin() != null && !reporte.getFechaFin().isEmpty()) {
                            LocalDate fecha = LocalDate.parse(reporte.getFechaFin(), dbFormatter);
                            finFmt = fecha.format(displayFormatter);
                        }
                        %>
                        <%=reporte.getFechaInicio()==null?"" : inicioFmt+" - "%><%=reporte.getFechaFin()==null?"" : finFmt%>
                    </td>
                    <td><%=reporte.getTotalRegistros()%></td>
                    <td><a class="btn btn-primary" href="<%=request.getContextPath()%>/administrador/ReportesServlet?action=descargar&id_form=<%=reporte.getIdFormulario()%>&zona=<%=reporte.getIdZona()%>&rol=<%=reporte.getIdRol()%>&daterange=<%=dateRange%>">Descargar Reporte</a></td>

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
    if ($.fn.DataTable.isDataTable('#dataTable')) {
        $('#dataTable').DataTable().destroy(); // Destruye cualquier instancia existente
    }
    $('#dataTable').DataTable({
        "order": [], // Deshabilita el orden inicial si lo deseas, o ajusta según necesites
        "pageLength": 10, // Número de filas por página por defecto
        "language": { // Configuración del idioma
            "url": "//cdn.datatables.net/plug-ins/1.13.4/i18n/es-ES.json" // URL para español
        }
    });

</script>
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

  function limpiarFormulario() {
    const form = document.getElementById("filtros");

    // Borra todos los campos manualmente
    // form.querySelectorAll("input, select").forEach(el => {
    // if (el.tagName === "SELECT") {
    //   el.selectedIndex = 0;
    // } else if (el.type !== "submit" && el.type !== "button") {
    //   el.value = "";
    // }
    // });

    // Limpiar inputs
    form.querySelectorAll('input[type="text"]').forEach(input => {
      input.value = '';
    });

    // Restablecer selects
    form.querySelectorAll('select').forEach(select => {
      select.selectedIndex = 0;
    });

    // Opcional: envia el formulario limpio directamente
    //form.submit();
  }
</script>



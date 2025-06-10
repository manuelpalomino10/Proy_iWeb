<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/header.jsp" />

<body id="page-top">

<!-- Page Wrapper -->
<div id="wrapper">

    <!-- Sidebar -->
    <jsp:include page="/sidebarCoordi.jsp" />
    <!-- End Sidebar -->

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">

            <!-- Topbar -->
            <jsp:include page="/topbarCoordi.jsp" />
            <!-- End Topbar -->

            <!-- Main Content -->
            <div class="container-fluid">
                <h1 class="h3 mb-2 text-gray-800">Gestión de Encuestadores</h1>
                <p class="mb-4">Administra tus encuestadores: desbanéalos o asígnales formularios.</p>

                <!-- Tabla con scroll -->
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Encuestadores</h6>
                    </div>
                    <div class="card-body p-0">
                        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                            <thead class="thead-light">
                            <tr>
                                <th>Nombre</th>
                                <th>Email</th>
                                <th>Estado</th>
                                <th>Acciones</th>
                            </tr>
                            </thead>
                            <tbody
                            <c:forEach var="enc" items="${listaEncuestadores}">
                                <tr>
                                    <td><c:out value="${enc.nombres} ${enc.apellidos}"/></td>
                                    <td><c:out value="${enc.correo}"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${enc.estado}">
                                                <span class="badge badge-success">Activo</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-danger">Baneado</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <button class="btn btn-sm btn-warning"
                                                data-toggle="modal"
                                                data-target="#banModal"
                                                data-nombre="${enc.nombres} ${enc.apellidos}"
                                                data-id="${enc.idUsuario}">
                                            Banear
                                        </button>
                                        <button class="btn btn-sm btn-primary"
                                                data-toggle="modal"
                                                data-target="#assignModal"
                                                data-nombre="${enc.nombres} ${enc.apellidos}"
                                                data-id="${enc.idUsuario}">
                                            Asignar
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>

            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- End Content -->

        <!-- Footer -->
        <jsp:include page="/footer.jsp" />
        <!-- End Footer -->

    </div>
    <!-- End Content Wrapper -->

</div>
<!-- End Page Wrapper -->

<!-- Modals específicos de esta página -->
<!-- Banear Encuestador -->
<div class="modal fade" id="banModal" tabindex="-1">
    <div class="modal-dialog"><div class="modal-content">
        <div class="modal-header">
            <h5 class="modal-title">Banear Encuestador</h5>
            <button type="button" class="close" data-dismiss="modal">×</button>
        </div>
        <div class="modal-body">
            ¿Seguro que quieres banear a <strong id="banName"></strong>?
        </div>
        <div class="modal-footer">
            <button class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
            <button id="banConfirmBtn" class="btn btn-danger">Sí, banear</button>
        </div>
    </div></div>
</div>


<!-- Asignar Formularios -->
<div class="modal fade" id="assignModal" tabindex="-1">
    <div class="modal-dialog modal-lg"><div class="modal-content">
        <div class="modal-header">
            <h5 class="modal-title">Asignar Formularios a <span id="assignName"></span></h5>
            <button type="button" class="close" data-dismiss="modal">×</button>
        </div>
        <div class="modal-body">
            <div class="form-group">
                <label>Selecciona formularios</label>
                <div id="formulariosList" class="list-group">
                    <!-- JS inyectará aquí los checkboxes -->
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
            <button id="assignConfirmBtn" class="btn btn-primary">Confirmar Asignación</button>
        </div>
    </div></div>
</div>


<!-- Script de inicialización -->
<script>
    $(document).ready(function(){
        // Tu DataTable
        $('#dataTable').DataTable({ destroy:true, /* …tu config…*/ });

        // Cuando se abre el modal de ban
        $('#banModal').on('show.bs.modal', e => {
            let btn = $(e.relatedTarget);
            let id   = btn.data('id');      // ASEGÚRATE de pasar data-id en el botón
            let name = btn.data('nombre');
            $('#banName').text(name);
            $('#banIdInput').val(id);
        });
        // Al confirmar ban
        $('#banConfirmBtn').click(() => $('#banForm').submit());

        // Cuando se abre el modal de asignar
        $('#assignModal').on('show.bs.modal', e => {
            let btn = $(e.relatedTarget);
            let id   = btn.data('id');
            let name = btn.data('nombre');
            $('#assignName').text(name);
            $('#assignIdInput').val(id);

            // Limpia y carga checkboxes: supongamos que tienes una lista JS de formularios
            let formularios = [
                {id:1, nombre:'Encuesta Satisfacción'},
                {id:2, nombre:'Datos Demográficos'},
                /* … */
            ];
            let $list = $('#formulariosList').empty();
            formularios.forEach(f => {
                $list.append(`
        <label class="list-group-item">
          <input type="checkbox" name="formularios" value="${f.id}"> ${f.nombre}
        </label>`);
            });
        });
        // Al confirmar asignación
        $('#assignConfirmBtn').click(() => $('#assignForm').submit());
    });

</script>

<!-- Form para banear -->
<form id="banForm" action="${pageContext.request.contextPath}/gestion_encuestadores" method="post" style="display:none">
    <input type="hidden" name="action" value="banear"/>
    <input type="hidden" id="banIdInput"  name="idusuario" value=""/>
</form>

<!-- Form para asignar -->
<form id="assignForm" action="${pageContext.request.contextPath}/gestion_encuestadores" method="post" style="display:none">
    <input type="hidden" name="action" value="asignar"/>
    <input type="hidden" id="assignIdInput" name="idusuario" value=""/>
    <!-- Los checkboxes de formularios los clonaremos vía JS -->
    <div id="assignFieldsContainer"></div>
</form>


</body>
</html>

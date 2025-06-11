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
                                        <button class="btn btn-sm btn-danger"
                                                data-toggle="modal"
                                                data-target="#banModal"
                                                data-nombre="${enc.nombres} ${enc.apellidos}"
                                                data-id="${enc.idUsuario}"
                                                data-action="${enc.estado ? 'banear' : 'desbanear'}">
                                            <c:choose>
                                                <c:when test="${enc.estado}">
                                                    Banear
                                                </c:when>
                                                <c:otherwise>
                                                    Desbanear
                                                </c:otherwise>
                                            </c:choose>
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
            <h5 class="modal-title" id="banTitle">Banear Encuestador</h5>
            <button type="button" class="close" data-dismiss="modal">×</button>
        </div>
        <div class="modal-body">
            ¿Seguro que quieres <span id="banVerb">banear</span> a <strong id="banName"></strong>?
        </div>
        <div class="modal-footer">
            <button class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
            <button id="banConfirmBtn" class="btn btn-danger"><span id="banButtonVerb">Sí, banear</span></button>
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
            <p class="mb-2">Selecciona un formulario de la lista y presiona
                <strong>Asignar</strong>. Los formularios asignados se mostrarán
                debajo.</p>
            <div class="row">
                <div class="col-md-6">
                    <h6>Formularios Disponibles</h6>
                    <ul id="availableList" class="list-group mb-3"></ul>
                </div>
                <div class="col-md-6">
                    <h6>Formularios del Encuestador</h6>
                    <ul id="assignedList" class="list-group mb-3"></ul>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button id="assignCloseBtn" class="btn btn-primary" data-dismiss="modal">Aceptar</button>
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
            let id     = btn.data('id');
            let name   = btn.data('nombre');
            let action = btn.data('action');
            $('#banName').text(name);
            $('#banIdInput').val(id);
            $('#banForm input[name=action]').val(action);

            if(action === 'banear') {
                $('#banTitle').text('Banear Encuestador');
                $('#banVerb').text('banear');
                $('#banButtonVerb').text('Sí, banear');
            } else {
                $('#banTitle').text('Desbanear Encuestador');
                $('#banVerb').text('desbanear');
                $('#banButtonVerb').text('Sí, desbanear');
            }
        });
        // Al confirmar ban
        $('#banConfirmBtn').click(() => $('#banForm').submit());

        // Cuando se abre el modal de asignar
        $('#assignModal').on('show.bs.modal', e => {
            let btn  = $(e.relatedTarget);
            let id   = btn.data('id');
            let name = btn.data('nombre');

            $('#assignName').text(name);
            $('#assignIdInput').val(id);

            $('#availableList').empty();
            $('#assignedList').empty();
            $('#assignFieldsContainer').empty();

            $.getJSON('${pageContext.request.contextPath}/gestion_encuestadores', {
                action: 'get_formularios',
                idusuario: id
            }, function(data){
                const disp = data.disponibles || [];
                const asign = data.asignados || [];

                if(disp.length === 0){
                    $('#availableList').append('<li class="list-group-item">No hay formularios disponibles para asignar.</li>');
                } else {
                    disp.forEach(f => {
                        $('#availableList').append(
                            `<li class="list-group-item d-flex justify-content-between align-items-center" data-id="${f.id}" data-nombre="${f.nombre}">
                                ${f.nombre}
                                <button type="button" class="btn btn-sm btn-success assign-btn">Asignar</button>
                            </li>`);
                    });
                }

                if(asign.length === 0){
                    $('#assignedList').append('<li class="list-group-item">No hay formularios asignados.</li>');
                } else {
                    asign.forEach(f => {
                        $('#assignedList').append(
                            `<li class="list-group-item d-flex justify-content-between align-items-center" data-id="${f.id}" data-nombre="${f.nombre}">
                                ${f.nombre}
                                <button type="button" class="btn btn-sm btn-danger unassign-btn">Desasignar</button>
                            </li>`);
                        if(f.id){
                            $('#assignFieldsContainer').append(
                                `<input type="hidden" name="formularios" value="${f.id}" data-id="${f.id}">`
                            );
                        }
                    });
                }
            }).fail(function(jqXHR, textStatus, errorThrown){
                console.error('Error al obtener formularios:', textStatus, errorThrown, jqXHR.responseText);
                $('#assignModal .modal-body').prepend(
                    '<div class="alert alert-danger">Error al cargar los formularios.</div>'
                );
            });
        });

        function submitAssignments(){
            $.post('${pageContext.request.contextPath}/gestion_encuestadores', $('#assignForm').serialize());
        }

        // Asignar uno por uno
        $('#availableList').on('click', '.assign-btn', function(){
            let li = $(this).closest('li');
            let id = li.data('id');
            let nombre = li.data('nombre');
            li.remove();
            if($('#availableList li').length === 0){
                $('#availableList').append('<li class="list-group-item">No hay formularios disponibles para asignar.</li>');
            }
            $('#assignedList').find('.list-group-item:contains("No hay formularios asignados")').remove();

            $('#assignedList').append(
                `<li class="list-group-item d-flex justify-content-between align-items-center" data-id="${id}" data-nombre="${nombre}">
                    ${nombre}
                    <button type="button" class="btn btn-sm btn-danger unassign-btn">Desasignar</button>
                </li>`);
            if(id){
                $('#assignFieldsContainer').append(
                    `<input type="hidden" name="formularios" value="${id}" data-id="${id}">`
                );
            }
            submitAssignments();
        });

        // Quitar asignación
        $('#assignedList').on('click', '.unassign-btn', function(){
            let li = $(this).closest('li');
            let id = li.data('id');
            let nombre = li.data('nombre');
            li.remove();
            $('#assignedList').children().length === 0 && $('#assignedList').append('<li class="list-group-item">No hay formularios asignados.</li>');
            $('#availableList').find('.list-group-item:contains("No hay formularios disponibles")').remove();
            $('#availableList').append(
                `<li class="list-group-item d-flex justify-content-between align-items-center" data-id="${id}" data-nombre="${nombre}">
                    ${nombre}
                    <button type="button" class="btn btn-sm btn-success assign-btn">Asignar</button>
                </li>`);
            $('#assignFieldsContainer').find(`input[data-id='${id}']`).remove();
            submitAssignments();
        });

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
    <!-- Campos ocultos con IDs de formularios asignados -->
    <div id="assignFieldsContainer"></div>
</form>


</body>
</html>

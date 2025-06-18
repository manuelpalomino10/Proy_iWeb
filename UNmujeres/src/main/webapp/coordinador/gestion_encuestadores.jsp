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
                <c:if test="${not empty param.success}">
                    <div class="alert alert-success alert-dismissible fade show">
                        <c:out value="${param.success}"/>
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                    </div>
                </c:if>
                <c:if test="${not empty param.warn}">
                    <div class="alert alert-warning alert-dismissible fade show">
                        <c:out value="${param.warn}"/>
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                    </div>
                </c:if>
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
                            <tbody>
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
                                        <a class="btn btn-sm btn-primary"
                                           href="${pageContext.request.contextPath}/gestion_encuestadores?idusuario=${enc.idUsuario}">
                                            Asignar
                                        </a>
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
            <h5 class="modal-title">Asignar Formularios a <c:out value="${assignName}"/></h5>
            <button type="button" class="close" data-dismiss="modal">×</button>
        </div>
        <div class="modal-body">

            <div class="row">
                <div class="col-md-6">
                    <h6>Formularios Disponibles</h6>
                    <c:forEach var="f" items="${dispFormularios}">
                        <form class="d-flex justify-content-between align-items-center mb-2" method="post" action="${pageContext.request.contextPath}/gestion_encuestadores">
                            <input type="hidden" name="action" value="asignar_form"/>
                            <input type="hidden" name="idusuario" value="${assignId}"/>
                            <input type="hidden" name="idformulario" value="${f.idFormulario}"/>
                            <span class="mr-2"><c:out value="${f.nombre}"/></span>
                            <button type="submit" class="btn btn-sm btn-success">Asignar</button>
                        </form>
                    </c:forEach>
                    <c:if test="${empty dispFormularios}">
                        <p>No hay formularios disponibles para asignar.</p>
                    </c:if>
                </div>
                <div class="col-md-6">
                    <h6>Formularios del Encuestador</h6>
                    <c:forEach var="f" items="${asigFormularios}">
                        <form class="d-flex justify-content-between align-items-center mb-2" method="post" action="${pageContext.request.contextPath}/gestion_encuestadores">
                            <input type="hidden" name="action" value="desasignar_form"/>
                            <input type="hidden" name="idusuario" value="${assignId}"/>
                            <input type="hidden" name="idformulario" value="${f.idFormulario}"/>
                            <span class="mr-2"><c:out value="${f.nombre}"/></span>
                            <c:choose>
                                <c:when test="${f.respuestasCount > 0}">
                                    <button type="submit" class="btn btn-sm btn-secondary" disabled
                                            data-toggle="tooltip"
                                            title="No se puede desasignar este formulario porque ya tiene respuestas">
                                        Desasignar
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <button type="submit" class="btn btn-sm btn-danger">Desasignar</button>
                                </c:otherwise>
                            </c:choose>
                        </form>
                    </c:forEach>
                    <c:if test="${empty asigFormularios}">
                        <p>No hay formularios asignados.</p>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>
</div>

<!-- Script de inicialización -->
<script>
    $(function(){
        $('#dataTable').DataTable({destroy:true});
        $('#banModal').on('show.bs.modal', e => {
            let btn = $(e.relatedTarget);
            let id   = btn.data('id');
            let name = btn.data('nombre');
            let action = btn.data('action');
            $('#banName').text(name);
            $('#banIdInput').val(id);
            $('#banForm input[name=action]').val(action);
            if(action === 'banear'){
                $('#banTitle').text('Banear Encuestador');
                $('#banVerb').text('banear');
                $('#banButtonVerb').text('Sí, banear');
            } else {
                $('#banTitle').text('Desbanear Encuestador');
                $('#banVerb').text('desbanear');
                $('#banButtonVerb').text('Sí, desbanear');
            }
        });
        $('#banConfirmBtn').click(()=>$('#banForm').submit());
        $('[data-toggle="tooltip"]').tooltip();
        <c:if test="${showAssignModal}">
        $('#assignModal').modal('show');
        </c:if>
    });
</script>

<!-- Form para banear -->
<form id="banForm" action="${pageContext.request.contextPath}/gestion_encuestadores" method="post" style="display:none">
    <input type="hidden" name="action" value="banear"/>
    <input type="hidden" id="banIdInput"  name="idusuario" value=""/>
</form>
</body>

</html>

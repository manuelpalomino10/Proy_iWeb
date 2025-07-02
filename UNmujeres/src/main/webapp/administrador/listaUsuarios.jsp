<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/header.jsp" />

<body id="page-top">
<div id="wrapper">

    <jsp:include page="/sidebarAdmin.jsp" />

    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
            <jsp:include page="/topbarAdmin.jsp" />

            <div class="container-fluid">
                <h1 class="h3 mb-2 text-gray-800">Gestión de Usuarios</h1>
                <p class="mb-4">Lista de usuarios registrados y su estado actual.</p>

                <c:if test="${not empty param.success}">
                    <div class="alert alert-success alert-dismissible fade show">
                        <c:out value="${param.success}" />
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                    </div>
                </c:if>

                <!-- Tabla de Usuarios -->
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Usuarios Registrados</h6>
                    </div>
                    <div class="card-body px-3">
                        <div class="table-responsive">
                            <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                <thead class="thead-light">
                                <tr>
                                    <th>Nombre</th>
                                    <th>DNI</th>
                                    <th>Correo</th>
                                    <th>Rol</th>
                                    <th>Zona</th>
                                    <th>Estado</th>
                                    <th>Acciones</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="usuario" items="${lista}">
                                    <tr>
                                        <td><c:out value="${usuario.nombreCompleto}" /></td>
                                        <td><c:out value="${usuario.dni}" /></td>
                                        <td><c:out value="${usuario.correo}" /></td>
                                        <td><c:out value="${usuario.nombreRol}" /></td>
                                        <td><c:out value="${usuario.nombreZona}" /></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${usuario.estado}">
                                                    <span class="badge badge-success">Activo</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-danger">Inactivo</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <form action="${pageContext.request.contextPath}/administrador/UsuarioServlet" method="post" class="d-inline">
                                                <input type="hidden" name="id" value="${usuario.id}" />
                                                <input type="hidden" name="estado" value="${usuario.estado}" />
                                                <input type="hidden" name="accion" value="cambiarEstado" />
                                                <button type="submit" class="btn btn-sm
                                                    <c:out value='${usuario.estado ? "btn-danger" : "btn-success"}'/>">
                                                    <c:out value="${usuario.estado ? 'Desactivar' : 'Activar'}" />
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

            </div>
        </div>

        <jsp:include page="/footer.jsp" />
    </div>
</div>

<!-- Scroll Top Button-->
<a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
</a>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<!DOCTYPE html>
<html lang="es">
<head>
    <jsp:include page="../header.jsp" />
    <title>Registrar Coordinador Interno - ONU Mujeres</title>
    <!-- SweetAlert2 -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body id="page-top">
<div id="wrapper">

    <jsp:include page="../sidebarAdmin.jsp" />

    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">

            <jsp:include page="../topbarAdmin.jsp" />

            <div class="container-fluid">
                <h1 class="h3 mb-4 text-gray-800">Registrar Coordinador Interno</h1>
                <% if (request.getAttribute("error") != null) { %>
                <div>
                    <div class="alert alert-danger" role="alert"><%=request.getAttribute("error")%>
                    </div>
                </div>
                <% request.removeAttribute("error"); %>
                <% } %>
                <!-- Contenedor del formulario centrado -->
                <div class="col-lg-10 mx-auto">
                    <div class="card shadow mb-4">
                        <div class="card-body">
                            <form method="post" action="${pageContext.request.contextPath}/administrador/CrearCordiServlet">

                                <div class="form-row">
                                    <div class="form-group col-md-6">
                                        <label for="nombres">Nombres</label>
                                        <input type="text" class="form-control ${errorNombre!=null ? 'is-invalid' : ''}"
                                               id="nombres" name="nombres"
                                               value="${nombres != null ? nombres : ''}" required>
                                        <c:if test="${errorNombre!= null}">
                                            <div class="invalid-feedback">${errorNombre}</div>
                                        </c:if>
                                    </div>

                                    <div class="form-group col-md-6">
                                        <label for="apellidos">Apellidos</label>
                                        <input type="text" class="form-control ${errorApellido!=null ? 'is-invalid' : ''}"
                                               id="apellidos" name="apellidos"
                                               value="${apellidos != null ? apellidos : ''}" required>
                                        <c:if test="${errorApellido!= null}">
                                            <div class="invalid-feedback">${errorApellido}</div>
                                        </c:if>
                                    </div>
                                </div>

                                <div class="form-row">
                                    <div class="form-group col-md-6">
                                        <label for="DNI">DNI</label>
                                        <input type="text"
                                               class="form-control ${errorDni!=null ? 'is-invalid' : ''}"
                                               id="DNI" name="DNI" pattern="\d{8}" maxlength="8" inputmode="numeric"
                                               value="${DNI != null ? DNI : ''}" required>
                                        <c:if test="${errorDni!= null}">
<%--                                            <div class="invalid-feedback">El DNI ya está registrado.</div>--%>
                                            <div class="invalid-feedback">${errorDni}</div>
                                        </c:if>
                                    </div>

                                    <div class="form-group col-md-6">
                                        <label for="correo">Correo</label>
                                        <input type="email"
                                               class="form-control ${errorCorreo!=null ? 'is-invalid' : ''}"
                                               id="correo" name="correo"
                                               value="${correo != null ? correo : ''}" required>
                                        <c:if test="${errorCorreo!= null}">
<%--                                            <div class="invalid-feedback">El correo ya está registrado.</div>--%>
                                            <div class="invalid-feedback">${errorCorreo}</div>
                                        </c:if>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="idzona">Zona</label>
                                    <select name="idZona" id="idzona" class="form-control w-50 ${errorZona!=null ? 'is-invalid' : ''}" required>
                                        <option value="">-- Seleccione una zona --</option>
                                        <c:forEach var="zona" items="${listaZonas}">
                                            <option value="${zona.idzona}"
                                                    <c:if test="${zona.idzona == idZonaSeleccionada}">
                                                        selected
                                                    </c:if>>
                                                    ${zona.nombre}
                                            </option>
                                        </c:forEach>
                                        <c:if test="${errorZona!= null}">
                                            <div class="invalid-feedback">${errorZona}</div>
                                        </c:if>
                                    </select>
                                </div>

                                <div class="text-center">
                                    <button type="submit" class="btn btn-primary">Registrar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <!-- Fin del contenedor alineado -->
            </div>
        </div>
        <jsp:include page="../footer.jsp" />
    </div>
</div>

<a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
</a>

<c:if test="${registroExitoso}">
    <script>
        Swal.fire({
            icon: 'success',
            title: '¡Registro exitoso!',
            text: 'El nuevo coordinador fue registrado correctamente.',
            confirmButtonColor: '#3085d6'
        });
    </script>
</c:if>

</body>
</html>

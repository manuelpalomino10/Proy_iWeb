<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- Timestamp inicial anti-caché --%>
<%
    long now = System.currentTimeMillis();
    pageContext.setAttribute("now", now);
%>

<!DOCTYPE html>
<html lang="es">

<jsp:include page="../header.jsp" />



<body id="page-top">
<%-- Mostrar mensajes de éxito/error --%>
<c:if test="${not empty param.success}">
    <div class="alert alert-success alert-dismissible fade show">
        ¡Datos actualizados correctamente!
        <button type="button" class="close" data-dismiss="alert">×</button>
    </div>
</c:if>
<c:if test="${not empty param.error}">
    <div class="alert alert-danger alert-dismissible fade show">
        Error: ${param.error}
        <button type="button" class="close" data-dismiss="alert">×</button>
    </div>
</c:if>
<div id="wrapper">
    <jsp:include page="../sidebarCoordi.jsp" />

    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
            <jsp:include page="../topbarCoordi.jsp" />

            <div class="container-fluid">
                <div class="card shadow m-4 d-flex m-auto" style="max-width: 900px; margin: 2rem auto">
                    <div class="card-header py-2 bg-gradient-primary p-4 d-flex flex-row align-items-center fa-inverse">
                        <i class="fas fa-2x mr-2 fa-user-circle"></i>
                        <h5 class="m-0 font-weight-bold white">Perfil de Usuario</h5>
                    </div>

                    <div class="card-body">
                        <div id="messageContainer"></div>

                        <!-- SECCIÓN DE FOTO DE PERFIL (solo visualización) -->
                        <div class="text-center">
                            <div id="imageContainer">
                                <c:choose>
                                    <c:when test="${not empty usuario.fotoBytes}">
                                        <img id="currentProfileImage" src="data:image/jpeg;base64,${fotoBase64}"
                                             class="profile-img" alt="Foto de perfil" style="width: 200px; height: 200px; border-radius: 50%; object-fit: cover;"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img id="currentProfileImage" src="${pageContext.request.contextPath}/img/perfil-del-usuario.png"
                                             class="profile-img" alt="Foto de perfil por defecto" style="width: 200px; height: 200px; border-radius: 50%; object-fit: cover;"/>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <!-- Datos del usuario -->
                        <br>
                        <div class="row">
                            <div class="col-md-6 form-group">
                                <div class="text">Nombre</div>
                                <input type="text" class="form-control" id="username" value="${usuario.nombres}" readonly>
                            </div>
                            <div class="col-md-6 form-group">
                                <div class="text">Apellido</div>
                                <input type="text" class="form-control" id="apellido" value="${usuario.apellidos}" readonly>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 form-group">
                                <div class="text">Correo Electrónico</div>
                                <input type="email" class="form-control" id="email" value="${usuario.correo}" readonly>
                            </div>
                            <div class="col-md-6 form-group">
                                <div class="text">DNI</div>
                                <input type="text" class="form-control" id="dni" value="${usuario.DNI}" readonly>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 form-group">
                                <div class="text">Dirección</div>
                                <input type="text" class="form-control" id="direccion" value="${usuario.direccion}" readonly>
                            </div>
                            <div class="col-md-6 form-group">
                                <div class="text">Distrito</div>
                                <input type="text" class="form-control" id="distrito"
                                       value="${usuario.distrito != null ? usuario.distrito.nombre : 'No especificado'}" readonly>
                            </div>
                        </div>

                        <br>
                        <div class="text-center">
                            <div class="card-body">
                                <a href="${pageContext.request.contextPath}/coordinador/editarPerfilCoordi" class="btn btn-primary">
                                    <i class="fas fa-edit mr-2"></i>Editar Datos
                                </a>
                            </div>
                            <br>
                        </div>

                    </div>
                </div>
            </div>
        </div>

        <footer class="sticky-footer bg-white">
            <div class="container my-auto">
                <div class="copyright text-center my-auto">
                    <span>Copyright © ONU Mujeres - PUCP 2025</span>
                </div>
            </div>
        </footer>
    </div>
</div>

<a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
</a>

<jsp:include page="../footer.jsp" />

<script>
    // Función para ocultar mensajes automáticamente
    function autoHideAlerts() {
        // Seleccionar todos los mensajes de alerta
        const alerts = document.querySelectorAll('.alert.alert-success, .alert.alert-danger');

        alerts.forEach(alert => {
            // Configurar temporizador para ocultar cada alerta
            setTimeout(() => {
                // Usar el método 'fade' de Bootstrap y luego remover
                $(alert).fadeTo(500, 0, function() {
                    $(this).slideUp(500, function() {
                        $(this).remove();
                    });
                });
            }, 2000); // 2 segundos
        });
    }

    // Ejecutar cuando el DOM esté listo
    document.addEventListener('DOMContentLoaded', autoHideAlerts);

    // También ejecutar después de recargar la página (para mensajes en parámetros URL)
    window.onload = autoHideAlerts;
</script>

</body>
</html>

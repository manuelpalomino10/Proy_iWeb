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
<div id="wrapper">
    <jsp:include page="../sidebarEnc.jsp" />

    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
            <jsp:include page="../topbarEnc.jsp" />

            <div class="container-fluid">
                <div class="card shadow m-4 d-flex m-auto" style="max-width: 900px; margin: 2rem auto">
                    <div class="card-header py-2 bg-gradient-primary p-4 d-flex flex-row align-items-center fa-inverse">
                        <i class="fas fa-2x mr-2 fa-user-circle"></i>
                        <h5 class="m-0 font-weight-bold white">Perfil de Usuario</h5>
                    </div>

                    <div class="card-body">
                        <div id="messageContainer"></div>

                        <!-- FORMULARIO DE ACTUALIZACIÓN DE FOTO -->
                        <form action="editarFoto" method="post" enctype="multipart/form-data" id="fotoForm" class="text-center">
                            <div id="imageContainer">
                                <c:choose>
                                    <c:when test="${not empty usuario.fotoBytes}">
                                        <img id="currentProfileImage" src="data:image/jpeg;base64,${fotoBase64}"
                                             class="profile-img" alt="Foto de perfil"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img id="currentProfileImage" src="${pageContext.request.contextPath}/img/perfil-del-usuario.png"
                                             class="profile-img" alt="Foto de perfil por defecto"/>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <br><br>
                            <button type="button" class="btn btn-primary" onclick="document.getElementById('fileInput').click();">
                                <i class="fas fa-camera mr-2"></i>Actualizar Foto
                            </button>
                            <input type="file" name="foto" id="fileInput" class="d-none" accept="image/*" onchange="uploadImage(this);">
                        </form>

                        <script>
                            function uploadImage(input) {
                                if (!input.files[0]) return;

                                // 1) Preview inmediato
                                const reader = new FileReader();
                                reader.onload = e => {
                                    document.getElementById('currentProfileImage').src = e.target.result;
                                };
                                reader.readAsDataURL(input.files[0]);

                                // 2) Envío a BD
                                const formData = new FormData(document.getElementById('fotoForm'));
                                showMessage('Subiendo foto…', 'info');

                                fetch('subirFoto', {
                                    method: 'POST',
                                    body: formData
                                })
                                    .then(res => {
                                        if (!res.ok) {
                                            return res.json().then(err => { throw new Error(err.error); });
                                        }
                                        return res.json();
                                    })
                                    .then(data => {
                                        // 3) Recarga la versión guardada
                                        const ts = new Date().getTime();
                                        document.getElementById('currentProfileImage').src =
                                            `obtenerFoto?dni=${userDni}&t=${ts}`;
                                        showMessage('¡Foto actualizada correctamente! Recargando...', 'success');

                                        // Recarga la página después de 1 segundo para ver el mensaje
                                        setTimeout(() => {
                                            location.reload();
                                        }, 100);
                                    });
                            }

                            function showMessage(text, type) {
                                const c = document.getElementById('messageContainer');
                                c.innerHTML = '';
                                let icon = 'info-circle';
                                if (type === 'success') icon = 'check-circle';
                                if (type === 'danger') icon = 'exclamation-circle';

                                const div = document.createElement('div');
                                div.className = `alert alert-${type} text-center`;
                                div.innerHTML = `<i class="fas fa-${icon} mr-1"></i>${text}`;
                                c.appendChild(div);
                                if (type !== 'danger') setTimeout(() => div.remove(), 4000);
                            }
                        </script>

                        <!-- Datos del usuario -->
                        <div class="row">
                            <br>
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
                                <a href="${pageContext.request.contextPath}/editarDatos" class="btn btn-primary">
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


<%--
<div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">¿Seguro que quieres salir?</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">Los cambios no guardados como borrador o completado no se guardarán. Haz click en "Cerrar sesión" para terminar.</div>
            <div class="modal-footer">
                <button class="btn btn-info" type="button" data-dismiss="modal">Seguir Aquí</button>
                <a class="btn btn-danger" href="${pageContext.request.contextPath}/logout">Cerrar sesión</a>
            </div>
        </div>
    </div>
</div>
--%>

<%-- Mostrar mensajes de éxito/error --%>
<c:if test="${not empty param.success}">
    <div class="alert alert-success alert-dismissible fade show">
        ¡Foto actualizada correctamente!
        <button type="button" class="close" data-dismiss="alert">×</button>
    </div>
</c:if>
<c:if test="${not empty param.error}">
    <div class="alert alert-danger alert-dismissible fade show">
        Error: ${param.error}
        <button type="button" class="close" data-dismiss="alert">×</button>
    </div>
</c:if>

<jsp:include page="../footer.jsp" />
</body>
</html>

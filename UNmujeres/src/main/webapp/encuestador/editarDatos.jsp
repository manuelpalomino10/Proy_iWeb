<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>



<!DOCTYPE html>
<html lang="es">

<jsp:include page="../header.jsp" />

<body id="page-top">

<!-- Page Wrapper -->
<div id="wrapper">

    <!-- Sidebar -->
    <jsp:include page="../sidebarEnc.jsp" />
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

                <div class="card shadow m-4 d-flex m-auto" style="max-width: 900px;margin: 2rem auto">

                    <div class="card-header py-2 bg-gradient-primary p-4 d-flex flex-row align-items-center fa-inverse">
                        <i class="fas fa-2x mr-2 fa-user-circle"></i>
                        <h5 class="m-0 font-weight-bold white">Editar Perfil de Usuario</h5>
                    </div>

                    <div class="card-body">

                        <form action="${pageContext.request.contextPath}/encuestador/editarDatos" method="post">
                            <div class="row">
                                <div class="col-md-6 form-group">
                                    <div class="text">Dirección</div>
                                    <input type="text" class="form-control" id="direccion" name="direccion"
                                           value="${usuario.direccion}" required>
                                </div>

                                <div class="col-md-6 form-group">
                                    <div class="text">Distrito</div>
                                    <select class="form-control" id="distritos_iddistritos"
                                            name="distritos_iddistritos">
                                        <option value="">Seleccionar Distrito</option>
                                        <c:forEach var="distrito" items="${distritos}">
                                            <option value="${distrito.iddistritos}"
                                                    <c:if test="${usuario.iddistritos == distrito.iddistritos}">selected</c:if>>
                                                    ${distrito.nombre}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6 form-group" style="margin-bottom: 0;">
                                    <label for="contraseña" style="margin-bottom: 2px; font-weight: 600;">Nueva Contraseña</label>
                                    <input type="password" class="form-control" id="contraseña" name="contraseña"
                                           style="margin-bottom: 8px; margin-top: 0;"/>
                                    <ul style="list-style: none; padding-left: 0; margin: 4px 0 0 0;">
                                        <li style="color: #6c757d; font-size: 13px; font-weight: 600; margin-bottom: 2px;">
                                            La contraseña debe tener:
                                        </li>
                                        <c:set var="reqs" value="${requisitosPwd}" />
                                        <li style="color: ${reqs['len'] == null ? '#b0b0b0' : (reqs['len'] ? '#388e3c' : '#d32f2f')}; font-size: 12.5px; margin-bottom: 0;">
                                            Mínimo 8 caracteres
                                        </li>
                                        <li style="color: ${reqs['may'] == null ? '#b0b0b0' : (reqs['may'] ? '#388e3c' : '#d32f2f')}; font-size: 12.5px; margin-bottom: 0;">
                                            Al menos una mayúscula
                                        </li>
                                        <li style="color: ${reqs['min'] == null ? '#b0b0b0' : (reqs['min'] ? '#388e3c' : '#d32f2f')}; font-size: 12.5px; margin-bottom: 0;">
                                            Al menos una minúscula
                                        </li>
                                        <li style="color: ${reqs['num'] == null ? '#b0b0b0' : (reqs['num'] ? '#388e3c' : '#d32f2f')}; font-size: 12.5px; margin-bottom: 0;">
                                            Al menos un número
                                        </li>
                                        <li style="color: ${reqs['spec'] == null ? '#b0b0b0' : (reqs['spec'] ? '#388e3c' : '#d32f2f')}; font-size: 12.5px;">
                                            Al menos un carácter especial
                                        </li>
                                    </ul>
                                </div>


                                <div class="col-md-6 form-group">
                                    <label for="confirmarContraseña">Confirmar Contraseña</label>
                                    <input type="password" class="form-control" id="confirmarContraseña"
                                           name="confirmarContraseña"  >
                                </div>
                            </div>
                            <!-- Barra de progreso, ver script lineas mas abajo -->
                            <div class="row mt-3">
                                <div class="col-md-12">
                                    <div class="progress" style="height: 5px;">
                                        <div id="passwordStrengthBar" class="progress-bar" role="progressbar"></div>
                                    </div>
                                    <small id="passwordStrengthText" class="form-text text-muted"></small>
                                </div>
                            </div>
                            <br>
                            <div class="text-center">
                                <button type="submit" class="btn btn-primary">Guardar</button>
                                <a href="${pageContext.request.contextPath}/encuestador/perfil" class="btn btn-secondary">Cancelar</a>
                            </div>
                        </form>
                        <br>
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

<!-- Scroll to Top Button-->
<a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
</a>

<%-- Logout Modal
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
</div>
<!-- End of Page Wrapper -->
<jsp:include page="../footer.jsp" />
<!-- Script para mostrar fortaleza de contraseña -->
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const passwordInput = document.getElementById('contraseña');
        const strengthBar = document.getElementById('passwordStrengthBar');
        const strengthText = document.getElementById('passwordStrengthText');

        passwordInput.addEventListener('input', function() {
            const password = this.value;
            let strength = 0;

            // Validar longitud
            if (password.length >= 8) strength += 1;
            if (password.length >= 12) strength += 1;

            // Validar caracteres especiales
            if (/[A-Z]/.test(password)) strength += 1;
            if (/[0-9]/.test(password)) strength += 1;
            if (/[^A-Za-z0-9]/.test(password)) strength += 1;

            // Actualizar barra y texto
            const width = strength * 20;
            strengthBar.style.width = width + '%';

            if (strength <= 1) {
                strengthBar.className = 'progress-bar bg-danger';
                strengthText.textContent = 'Débil';
            } else if (strength <= 3) {
                strengthBar.className = 'progress-bar bg-warning';
                strengthText.textContent = 'Moderada';
            } else {
                strengthBar.className = 'progress-bar bg-success';
                strengthText.textContent = 'Fuerte';
            }
        });
    });
</script>
</body>
</html>
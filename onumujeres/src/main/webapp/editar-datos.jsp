<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>ONU Mujeres - Editar Perfil de Usuario</title>

    <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link
            href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
            rel="stylesheet">

    <link href="css/sb-admin-2.min.css" rel="stylesheet">

    <style>
        .form-control:read-only {
            background-color: #e9ecef;
            /* Color de fondo para campos readonly */
        }
    </style>

</head>

<body id="page-top">

<div id="wrapper">

    <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

        <a class="sidebar-brand d-flex align-items-center justify-content-center" href="indexEnc.html">
            <div class="sidebar-brand-icon">
                <img src="img/ONU.png" alt="Logo ONU Mujeres" style="max-width: 100%;">
            </div>
        </a>

        <hr class="sidebar-divider my-0">

        <li class="nav-item">
            <a class="nav-link" href="indexEnc.html">
                <i class="fas fa-fw fa-tachometer-alt"></i>
                <span>Resumen</span></a>
        </li>

        <hr class="sidebar-divider">

        <div class="sidebar-heading">
            Funciones
        </div>

        <li class="nav-item">
            <a class="nav-link" href="crearRespuesta.html">
                <i class="fas fa-fw fa-clipboard-list"></i>
                <span>Crear Nueva Respuesta</span></a>
        </li>

        <li class="nav-item">
            <a class="nav-link" href="asignForm.html">
                <i class="fas fa-fw fa-table"></i>
                <span>Formularios Asignados</span></a>
        </li>

        <li class="nav-item">
            <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseUtilities"
               aria-expanded="true" aria-controls="collapseUtilities">
                <i class="fas fa-fw fa-clock"></i>
                <span>Historial de Formularios</span>
            </a>
            <div id="collapseUtilities" class="collapse" aria-labelledby="headingUtilities"
                 data-parent="#accordionSidebar">
                <div class="bg-white py-2 collapse-inner rounded">
                    <h6 class="collapse-header">Seleccione:</h6>
                    <a class="collapse-item" href="historicForms.html#completadossSection">Completados</a>
                    <a class="collapse-item" href="historicForms.html#borradoresSection">Borradores</a>
                </div>
            </div>
        </li>

        <hr class="sidebar-divider">

        <div class="sidebar-heading">
            Preferencias
        </div>

        <li class="nav-item">
            <a class="nav-link" href="configuraciones.html">
                <i class="fas fa-fw fa-wrench"></i>
                <span>Configuración</span></a>
        </li>

        <li class="nav-item active">
            <a class="nav-link" href="perfilEnc.html">
                <i class="fas fa-fw fa-user"></i>
                <span>Perfil</span></a>
        </li>

        <hr class="sidebar-divider d-none d-md-block">

        <div class="text-center d-none d-md-inline">
            <button class="rounded-circle border-0" id="sidebarToggle"></button>
        </div>

        <div class="sidebar-card d-none d-lg-flex">
            <p class="text-center mb-2"><strong>ONU</strong></p>
            <a class="btn btn-success btn-sm mb-2" href="https://www.unwomen.org/es">Contacto</a>
        </div>

    </ul>
    <div id="content-wrapper" class="d-flex flex-column">

        <div id="content">

            <nav class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">

                <button id="sidebarToggleTop" class="btn btn-link d-md-none rounded-circle mr-3">
                    <i class="fa fa-bars"></i>
                </button>

                <ul class="navbar-nav ml-auto">

                    <li class="nav-item dropdown no-arrow">
                        <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button"
                           data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <span class="mr-2 d-none d-lg-inline text-gray-600 small">Nombre Usuario</span>
                            <img class="img-profile rounded-circle" src="img/undraw_profile.svg">
                        </a>
                        <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in"
                             aria-labelledby="userDropdown">
                            <a class="dropdown-item" href="perfilEnc.html">
                                <i class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>
                                Perfil
                            </a>
                            <a class="dropdown-item" href="configuraciones.html">
                                <i class="fas fa-cogs fa-sm fa-fw mr-2 text-gray-400"></i>
                                Configuración
                            </a>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item" href="#" data-toggle="modal" data-target="#logoutModal">
                                <i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
                                Cerrar Sesión
                            </a>
                        </div>
                    </li>

                </ul>

            </nav>
            <div class="container-fluid">

                <div class="card shadow m-4 d-flex m-auto" style="max-width: 900px;margin: 2rem auto">

                    <div class="card-header py-2 bg-gradient-primary p-4 d-flex flex-row align-items-center fa-inverse">
                        <i class="fas fa-2x mr-2 fa-user-circle"></i>
                        <h5 class="m-0 font-weight-bold white">Editar Perfil de Usuario</h5>
                    </div>

                    <div class="card-body">

                        <form action="editarPerfil" method="post">
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
                                                ${usuario.distritos_iddistritos == distrito.iddistritos ? 'selected' : ''}>
                                                    ${distrito.nombre}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6 form-group">
                                    <label for="contraseña">Nueva Contraseña</label>
                                    <input type="password" class="form-control" id="contraseña" name="contraseña"
                                           value="${usuario.contraseña}" required>
                                </div>
                                <div class="col-md-6 form-group">
                                    <label for="confirmarContraseña">Confirmar Contraseña</label>
                                    <input type="password" class="form-control" id="confirmarContraseña"
                                           name="confirmarContraseña" value="${usuario.contraseña}" required>
                                </div>
                            </div>
                            <br>
                            <div class="text-center">
                                <button type="submit" class="btn btn-primary">Guardar</button>
                                <a href="perfilEnc.jsp" class="btn btn-secondary">Cancelar</a>
                            </div>
                        </form>
                        <br>
                    </div>
                </div>

            </div>
        </div>
        <footer class="sticky-footer bg-white">
            <div class="container my-auto">
                <div class="copyright text-center my-auto">
                    <span>Copyright &copy; ONU Mujeres - PUCP 2025</span>
                </div>
            </div>
        </footer>
    </div>
</div>
<a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
</a>

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
            <div class="modal-body">Los cambios no guardados como borrador o completado no se guardarán. Haz click en
                "Cerrar sesión" para terminar.</div>
            <div class="modal-footer">
                <button class="btn btn-info" type="button" data-dismiss="modal">Seguir Aquí</button>
                <a class="btn btn-danger" href="login.html">Cerrar sesión</a>
            </div>
        </div>
    </div>
</div>

<script src="vendor/jquery/jquery.min.js"></script>
<script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

<script src="vendor/jquery-easing/jquery.easing.min.js"></script>

<script src="js/sb-admin-2.min.js"></script>

<script src="vendor/chart.js/Chart.min.js"></script>

<script src="js/demo/chart-area-demo.js"></script>
<script src="js/demo/chart-pie-demo.js"></script>

</body>

</html>
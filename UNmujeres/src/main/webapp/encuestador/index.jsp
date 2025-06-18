<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>



<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>




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

                <!-- Page Heading -->
                <div class="d-sm-flex align-items-center justify-content-between mb-4">
                    <h1 class="h3 mb-0 text-gray-800">Resumen</h1>
                </div>

                <!-- Content Row 1: Formularios Asignados - Completados - Pendientes -->
                <div class="row">
                    <!-- Formularios Asignados -->
                    <div class="col-md-4 mb-4">
                        <div class="card border-left-primary shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                                            FORMULARIOS ASIGNADOS
                                        </div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">${formulariosAsignados}</div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-list-alt fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Formularios Completados -->
                    <div class="col-md-4 mb-4">
                        <div class="card border-left-success shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                                            FORMULARIOS COMPLETADOS 7 DIAS
                                        </div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">${completadosUltimos7Dias}</div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-check-double fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Formularios Pendientes -->
                    <div class="col-md-4 mb-4">
                        <div class="card border-left-warning shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">
                                            FORMULARIOS PENDIENTES
                                        </div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">${borradores}</div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-hourglass-half fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Content Row 2: Último Formulario Registrado - Formularios por Vencer -->
                <div class="row">
                    <!-- Último Formulario Registrado -->
                    <div class="col-md-6 mb-4">
                        <div class="card border-left-info shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-info text-uppercase mb-1">
                                            ÚLTIMO FORMULARIO REGISTRADO EN LA PLATAFORMA
                                        </div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">${ultimoFormularioRegistrado}</div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-calendar-check fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Formularios por Vencer -->
                    <div class="col-md-6 mb-4">
                        <div class="card border-left-danger shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-danger text-uppercase mb-1">
                                            FORMULARIOS POR VENCER PRONTO
                                        </div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">${formulariosPorVencer}</div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-exclamation-triangle fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Content Row 3: Total de Respuestas - Formularios asignados hoy -->
                <div class="row">
                    <!-- Total de Respuestas Registradas en la plataforma -->
                    <div class="col-md-6 mb-4">
                        <div class="card border-left-secondary shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-secondary text-uppercase mb-1">
                                            TOTAL DE RESPUESTAS REGISTRADAS EN LA PLATAFORMA
                                        </div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">${totalRespuestas}</div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-clipboard-list fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Formularios Asignados Hoy -->
                    <div class="col-md-6 mb-4">
                        <div class="card border-left-dark shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-dark text-uppercase mb-1">
                                            FORMULARIOS ASIGNADOS HOY
                                        </div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">${formulariosAsignadosHoy}</div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-calendar-day fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>


                <br>

                <!-- Aquí podrías mantener los gráficos existentes, o agregarlos debajo -->

                <!-- Gráficos existentes -->
                <div class="row">

                    <!-- Gráfico de Barras: Totales Generales -->
                    <div class="col-xl-7 col-lg-7">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Totales de Formularios</h6>
                            </div>
                            <div class="card-body">
                                <canvas id="barChartTotales" width="500" height="350"></canvas>
                            </div>
                        </div>
                    </div>

                    <!-- Gráfico de Pastel: Formularios por Tipo -->
                    <div class="col-xl-5 col-lg-5">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Distribución por Tipo</h6>
                            </div>
                            <div class="card-body">
                                <canvas id="pieChartTipos" width="300" height="300"></canvas>
                            </div>
                        </div>
                    </div>

                </div>

                <!-- Chart.js -->
                <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

                <script>
                    // Gráfico de Barras (Horizontal) para Totales Generales
                    const barChart = new Chart(document.getElementById('barChartTotales'), {
                        type: 'bar',
                        data: {
                            labels: ['Última semana', 'Último mes'],
                            datasets: [{
                                label: 'Cantidad',
                                data: [${avanceResultados.totalesGenerales[0]}, ${avanceResultados.totalesGenerales[1]}],
                                backgroundColor: ['#36A2EB', '#FFCE56']
                            }]
                        },
                        options: {
                            indexAxis: 'y',
                            responsive: false,
                            plugins: {
                                legend: {
                                    display: false
                                }
                            },
                            scales: {
                                x: {
                                    beginAtZero: true,
                                    type: 'linear',

                                    ticks: { stepSize: 1,} // Incrementa en 1, mostrando solo números enteros
                                }

                            }
                        }
                    });

                    // Gráfico de Pastel para Formularios por Tipo
                    const pieLabels = [
                        <c:forEach items="${listaFormularios}" var="formulario" varStatus="loop">
                        "${formulario.key}"<c:if test="${!loop.last}">,</c:if>
                        </c:forEach>
                    ];

                    const pieData = [
                        <c:forEach items="${listaFormularios}" var="formulario" varStatus="loop">
                        ${formulario.value}<c:if test="${!loop.last}">,</c:if>
                        </c:forEach>
                    ];

                    const pieChart = new Chart(document.getElementById('pieChartTipos'), {
                        type: 'pie',
                        data: {
                            labels: pieLabels,
                            datasets: [{
                                data: pieData,
                                backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF']
                            }]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false,
                            plugins: {
                                legend: {
                                    position: 'bottom'
                                }
                            }
                        }
                    });
                </script>

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

<!-- Logout Modal-->
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
                <a class="btn btn-danger" href="login.html">Cerrar sesión</a>
            </div>
        </div>
    </div>
</div>

</div>
<!-- End of Page Wrapper -->
<jsp:include page="../footer.jsp" />
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="es">

<jsp:include page="/header.jsp" />

<body id="page-top">

<!-- Page Wrapper -->
<div id="wrapper">

    <!-- Sidebar -->
    <jsp:include page="/sidebarAdmin.jsp" />
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
                    <h1 class="h3 mb-0 text-gray-800">Resumen Administrador</h1>
                </div>

                <!-- Content Row 1: Tarjetas de resumen -->
                <div class="row">
                    <!-- Total de Usuarios -->
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-primary shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                                            Total de Usuarios</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">${totalUsuarios}</div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-users fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Coordinadores Activos -->
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-success shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                                            Coordinadores Activos</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">${coordinadoresActivos}</div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-user-check fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Encuestadores Activos -->
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-info shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-info text-uppercase mb-1">
                                            Encuestadores Activos</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">${encuestadoresActivos}</div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-user-edit fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Respuestas Registradas -->
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-warning shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">
                                            Respuestas Registradas</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">${totalRespuestas}</div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-file-alt fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Formularios Activos -->
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-danger shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-danger text-uppercase mb-1">
                                            Formularios Activos</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">${formulariosActivos}</div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-clipboard-list fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Promedio Respuestas/Encuestador -->
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-secondary shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-secondary text-uppercase mb-1">
                                            Promedio Respuestas/Encuestador</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800"><fmt:formatNumber value="${promedioRespuestas}" pattern="#.##" /></div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-chart-bar fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Usuarios Desactivados -->
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-dark shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-dark text-uppercase mb-1">
                                            Usuarios Desactivados</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">${usuariosDesactivados}</div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-user-slash fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Zona con Más Respuestas -->
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-primary shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                                            Zona con Más Respuestas</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">${zonaMasActiva}</div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-map-marker-alt fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>


                <!-- Fila 1 de gráficos -->
                <div class="row">
                    <!-- Respuestas por Zona (Últimos 30 días) -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Respuestas por Zona (Últimos 30 días)</h6>
                            </div>
                            <div class="card-body">
                                <div class="chart-area">
                                    <canvas id="respuestasPorZonaChart" width="700" height="350"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Distribución de Usuarios -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Distribución de Usuarios</h6>
                            </div>
                            <div class="card-body">
                                <div class="chart-pie pt-4 pb-2">
                                    <canvas id="distribucionUsuariosChart" width="300" height="300"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Fila 2 de gráficos -->
                <div class="row">
                    <!-- Respuestas por Día (Última Semana) -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Respuestas por Día (Última Semana)</h6>
                            </div>
                            <div class="card-body">
                                <div class="chart-area">
                                    <canvas id="respuestasUltimaSemanaChart" width="700" height="350"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Progreso de Formularios por Zona (%) -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Progreso de Formularios por Zona (%)</h6>
                            </div>
                            <div class="card-body">
                                <div class="chart-bar">
                                    <canvas id="progresoFormulariosZonaChart" width="700" height="350"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Chart.js -->
                <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

                <script>
                    // Gráfico de Barras: Respuestas por Zona (Últimos 30 días)
                    const respuestasPorZonaChart = new Chart(document.getElementById('respuestasPorZonaChart'), {
                        type: 'bar',
                        data: {
                            labels: [
                                <c:forEach items="${respuestas30Dias}" var="entry" varStatus="loop">
                                "${entry.key}"<c:if test="${!loop.last}">,</c:if>
                                </c:forEach>
                            ],
                            datasets: [{
                                label: 'Respuestas',
                                data: [
                                    <c:forEach items="${respuestas30Dias}" var="entry" varStatus="loop">
                                    ${entry.value}<c:if test="${!loop.last}">,</c:if>
                                    </c:forEach>
                                ],
                                backgroundColor: ['#36A2EB', '#FFCE56', '#FF6384', '#4BC0C0'],
                                borderColor: ['#36A2EB', '#FFCE56', '#FF6384', '#4BC0C0'],
                                borderWidth: 1
                            }]
                        },
                        options: {
                            responsive: true,
                            scales: {
                                y: {
                                    beginAtZero: true,
                                    ticks: { stepSize: 1 }
                                }
                            }
                        }
                    });

                    // Gráfico de Pastel: Distribución de Usuarios
                    const distribucionUsuariosChart = new Chart(document.getElementById('distribucionUsuariosChart'), {
                        type: 'pie',
                        data: {
                            labels: ['Coordinadores', 'Encuestadores', 'Desactivados'],
                            datasets: [{
                                data: [${coordinadoresActivos}, ${encuestadoresActivos}, ${usuariosDesactivados}],
                                backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56']
                            }]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false,
                            plugins: {
                                legend: { position: 'bottom' }
                            }
                        }
                    });

                    // Gráfico de Líneas: Respuestas por Día (Última Semana)
                    const respuestasUltimaSemanaChart = new Chart(document.getElementById('respuestasUltimaSemanaChart'), {
                        type: 'line',
                        data: {
                            labels: [
                                <c:forEach items="${respuestasUltimaSemana}" var="entry" varStatus="loop">
                                "${entry.key}"<c:if test="${!loop.last}">,</c:if>
                                </c:forEach>
                            ],
                            datasets: [{
                                label: 'Respuestas',
                                data: [
                                    <c:forEach items="${respuestasUltimaSemana}" var="entry" varStatus="loop">
                                    ${entry.value}<c:if test="${!loop.last}">,</c:if>
                                    </c:forEach>
                                ],
                                borderColor: '#36A2EB',
                                fill: false
                            }]
                        },
                        options: {
                            responsive: true,
                            scales: {
                                y: {
                                    beginAtZero: true,
                                    ticks: { stepSize: 1 }
                                }
                            }
                        }
                    });

                    // Gráfico de Barras: Progreso de Formularios por Zona (%)
                    const progresoFormulariosZonaChart = new Chart(document.getElementById('progresoFormulariosZonaChart'), {
                        type: 'bar',
                        data: {
                            labels: [
                                <c:forEach items="${progresoFormularios}" var="entry" varStatus="loop">
                                "${entry.key}"<c:if test="${!loop.last}">,</c:if>
                                </c:forEach>
                            ],
                            datasets: [{
                                label: 'Progreso (%)',
                                data: [
                                    <c:forEach items="${progresoFormularios}" var="entry" varStatus="loop">
                                    ${entry.value}<c:if test="${!loop.last}">,</c:if>
                                    </c:forEach>
                                ],
                                backgroundColor: ['#4BC0C0', '#FF6384', '#36A2EB', '#FFCE56'],
                                borderColor: ['#4BC0C0', '#FF6384', '#36A2EB', '#FFCE56'],
                                borderWidth: 1
                            }]
                        },
                        options: {
                            responsive: true,
                            scales: {
                                y: {
                                    beginAtZero: true,
                                    ticks: { stepSize: 10 }
                                }
                            }
                        }
                    });
                </script>
                ```

            </div>
            <!-- /.container-fluid -->
        </div>
        <!-- End of Main Content -->

        <!-- Footer -->
        <footer class="sticky-footer bg-white">
            <div class="container my-auto">
                <div class="copyright text-center my-auto">
                    <span>Copyright © ONU Mujeres - PUCP 2025</span>
                </div>
            </div>
        </footer>
        <!-- End of Footer -->
    </div>
    <!-- End of Content Wrapper -->
</div>
<!-- End of Page Wrapper -->

<jsp:include page="/footer.jsp" />
</body>
</html>

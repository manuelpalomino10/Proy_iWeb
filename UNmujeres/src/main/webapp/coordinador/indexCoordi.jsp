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
    <jsp:include page="/sidebarCoordi.jsp" />
    <!-- End of Sidebar -->

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">

        <!-- Main Content -->
        <div id="content">

            <!-- Topbar -->
            <jsp:include page="../topbarCoordi.jsp" />
            <!-- End of Topbar -->

            <!-- Begin Page Content -->
            <div class="container-fluid">

                <!-- Page Heading -->
                <div class="d-sm-flex align-items-center justify-content-between mb-4">
                    <h1 class="h3 mb-0 text-gray-800">Resumen Coordinador</h1>
                    >
                </div>

                <!-- Content Row 1: Tarjetas de resumen -->
                <div class="row">

                    <!-- Total Encuestadores -->
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-primary shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                                            Total Encuestadores
                                        </div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                                            <c:out value="${totalEncuestadores != null ? totalEncuestadores : '0'}"/>
                                        </div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-users fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Encuestadores Activos -->
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-success shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                                            Activos
                                        </div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                                            <c:out value="${encuestadoresActivos != null ? encuestadoresActivos : '0'}"/>
                                        </div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-user-check fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Encuestadores Baneados -->
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-warning shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">
                                            Baneados
                                        </div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                                            <c:out value="${encuestadoresBaneados != null ? encuestadoresBaneados : '0'}"/>
                                        </div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-user-slash fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Formularios Asignados -->
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-info shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-info text-uppercase mb-1">
                                            Formularios Asignados
                                        </div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                                            <c:out value="${formulariosAsignados != null ? formulariosAsignados : '0'}"/>
                                        </div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-clipboard-list fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Content Row 2: Gráficos -->
                <div class="row">

                    <!-- Gráfico de Área: Formularios Completados por Zona -->
                    <div class="col-xl-8 col-lg-7">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Cantidad de Formularios Completados por Zona</h6>
                            </div>
                            <div class="card-body">
                                <div class="chart-area" style="height: 300px;">
                                    <canvas id="myAreaChart"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Gráfico de Torta: Porcentaje Activos vs Baneados -->
                    <div class="col-xl-4 col-lg-5">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Porcentaje de Encuestadores Activos vs Baneados</h6>
                            </div>
                            <div class="card-body">
                                <div class="chart-pie pt-4 pb-2" style="height: 250px;">
                                    <canvas id="myPieChart"></canvas>
                                </div>
                                <div class="mt-4 text-center small">
                                    <span class="mr-2">
                                        <i class="fas fa-circle text-primary"></i> Activos
                                    </span>
                                    <span class="mr-2">
                                        <i class="fas fa-circle text-warning"></i> Baneados
                                    </span>
                                </div>
                            </div>
                        </div>
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
                    <span>Copyright © ONU Mujeres - PUCP 2025</span>
                </div>
            </div>
        </footer>
        <!-- End of Footer -->
    </div>
    <!-- End of Content Wrapper -->
</div>
<!-- End of Page Wrapper -->

<!-- Scroll to Top Button -->
<a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
</a>

<jsp:include page="/footer.jsp" />


<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.6/dist/chart.umd.min.js"></script>

<!-- Custom Chart Scripts -->
<script>
    console.log("[DEBUG] JSP - Iniciando script de gráficos");

    // Función para limpiar y parsear datos de JSP a JSON
    function parseJspMap(jspMapString) {
        if (!jspMapString || jspMapString === '{}') {
            console.log("[DEBUG] JSP - Mapa vacío o nulo: ", jspMapString);
            return {};
        }
        try {
            // Reemplazar = por : y añadir comillas a las claves
            let cleaned = jspMapString
                .replace(/=/g, ':')
                .replace(/(\w+):/g, '"$1":')
                .replace(/:(\d+\.?\d*)/g, ':$1');
            let parsed = JSON.parse(cleaned);
            console.log("[DEBUG] JSP - Mapa parseado: ", parsed);
            return parsed;
        } catch (e) {
            console.error("[ERROR] JSP - Error al parsear mapa: ", jspMapString, e);
            return {};
        }
    }

    // Parsear respuestasPorZona
    console.log("[DEBUG] JSP - respuestasPorZona raw: ", "${respuestasPorZona}");
    var respuestasPorZona = parseJspMap('${respuestasPorZona}');
    var areaLabels = Object.keys(respuestasPorZona);
    var areaData = Object.values(respuestasPorZona);
    console.log("[DEBUG] JSP - areaLabels: ", areaLabels);
    console.log("[DEBUG] JSP - areaData: ", areaData);

    if (areaLabels.length === 0) {
        console.warn("[WARN] JSP - No hay datos para el gráfico de área");
        areaLabels = ['Sin datos'];
        areaData = [0];
    }

    // Gráfico de Área: Formularios Completados por Zona
    var ctxArea = document.getElementById('myAreaChart');
    if (!ctxArea) {
        console.error("[ERROR] JSP - Canvas myAreaChart no encontrado");
    } else {
        try {
            var myAreaChart = new Chart(ctxArea.getContext('2d'), {
                type: 'line',
                data: {
                    labels: areaLabels,
                    datasets: [{
                        label: 'Formularios Completados',
                        data: areaData,
                        backgroundColor: 'rgba(78, 115, 223, 0.05)',
                        borderColor: 'rgba(78, 115, 223, 1)',
                        pointRadius: 3,
                        pointBackgroundColor: 'rgba(78, 115, 223, 1)',
                        pointBorderColor: 'rgba(255, 255, 255, 0.8)',
                        pointHoverRadius: 5,
                        pointHoverBackgroundColor: 'rgba(78, 115, 223, 1)',
                        pointHitRadius: 10,
                        pointBorderWidth: 2,
                        fill: true
                    }]
                },
                options: {
                    maintainAspectRatio: false,
                    scales: {
                        x: { grid: { display: false } },
                        y: { beginAtZero: true, grid: { color: 'rgba(0, 0, 0, 0.1)' } }
                    },
                    plugins: { legend: { display: false } }
                }
            });
            console.log("[DEBUG] JSP - Gráfico de área creado");
        } catch (e) {
            console.error("[ERROR] JSP - Error al crear gráfico de área: ", e);
        }
    }


    console.log("[DEBUG] JSP - porcentajeActivosVsBaneados raw: ", "${porcentajeActivosVsBaneados}");
    var porcentajeData = parseJspMap('${porcentajeActivosVsBaneados}');
    var pieData = [porcentajeData.activos || 0, porcentajeData.baneados || 0];
    var pieLabels = pieData[0] === 0 && pieData[1] === 0 ? ['Sin datos'] : ['Activos', 'Baneados'];
    console.log("[DEBUG] JSP - porcentajeData parsed: ", porcentajeData);
    console.log("[DEBUG] JSP - pieData: ", pieData);
    console.log("[DEBUG] JSP - pieLabels: ", pieLabels);

    // Gráfico de Torta: Porcentaje Activos vs Baneados
    var ctxPie = document.getElementById('myPieChart');
    if (!ctxPie) {
        console.error("[ERROR] JSP - Canvas myPieChart no encontrado");
    } else {
        try {
            var myPieChart = new Chart(ctxPie.getContext('2d'), {
                type: 'doughnut',
                data: {
                    labels: pieLabels,
                    datasets: [{
                        data: pieData,
                        backgroundColor: ['#4e73df', '#f6c23e'],
                        hoverBackgroundColor: ['#2e59d9', '#dda20a'],
                        hoverBorderColor: 'rgba(255, 255, 255, 1)'
                    }]
                },
                options: {
                    maintainAspectRatio: false,
                    plugins: { legend: { display: false } },
                    cutout: '50%'
                }
            });
            console.log("[DEBUG] JSP - Gráfico de torta creado");
        } catch (e) {
            console.error("[ERROR] JSP - Error al crear gráfico de torta: ", e);
        }
    }
</script>

</body>
</html>
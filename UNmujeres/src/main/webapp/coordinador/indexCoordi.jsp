<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>




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
                                            Total de Encuestadores en mi Zona
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
                                            Encuestadores Activos
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
                                            Encuestadores Baneados
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
                                            Formularios Asignados en mi Zona
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
                <div class="row">

                    <!-- Formularios Completados -->
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-success shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                                            Respuestas Completadas en mi Zona
                                        </div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                                            <c:out value="${formulariosCompletados != null ? formulariosCompletados : '0'}" />
                                        </div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-check-circle fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Formularios en Borrador -->
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-secondary shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-secondary text-uppercase mb-1">
                                            Formularios en Borrador
                                        </div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                                            <c:out value="${formulariosBorrador != null ? formulariosBorrador : '0'}" />
                                        </div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-edit fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Tasa de Avance -->
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-info shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-info text-uppercase mb-1">
                                            Nivel de Cumplimiento de Formularios
                                        </div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                                            <fmt:formatNumber value="${tasaAvance > 100 ? 100 : (tasaAvance != null ? tasaAvance : 0.0)}" type="number" maxFractionDigits="1" />%


                                        </div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-tachometer-alt fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Distrito Más Activo -->
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-primary shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                                            Distrito Más Activo
                                        </div>
                                        <div class="h6 mb-0 font-weight-bold text-gray-800">
                                            <c:out value="${distritoMasActivo != null ? distritoMasActivo : 'Sin datos'}" />
                                        </div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-map-marker-alt fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- Content Row 2: Gráficos -->
                <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                <div class="row align-items-stretch">

                    <!-- Gráfico de barras  -->
                    <div class="col-xl-8 col-lg-7 mb-4">
                        <div class="card shadow h-100">
                            <div class="card-header py-3 text-center">
                                <h6 class="m-0 font-weight-bold text-primary">Respuestas por Formulario</h6>
                            </div>
                            <div class="card-body d-flex flex-column">
                                <div class="flex-grow-1" style="min-height: 300px; overflow-x: auto;">
                                    <canvas id="graficoFormularios"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>
                    <script>
                        const labelsFormularios = [
                            <c:forEach var="entry" items="${respuestasPorFormularioEstado}">
                            '<c:out value="${entry.key}" />',
                            </c:forEach>
                        ];

                        const datosCompletados = [
                            <c:forEach var="entry" items="${respuestasPorFormularioEstado}">
                            <c:out value="${entry.value['C'] != null ? entry.value['C'] : 0}" />,
                            </c:forEach>
                        ];

                        const datosBorradores = [
                            <c:forEach var="entry" items="${respuestasPorFormularioEstado}">
                            <c:out value="${entry.value['B'] != null ? entry.value['B'] : 0}" />,
                            </c:forEach>
                        ];

                        const etiquetasVisibles = labelsFormularios.map(nombre => {
                            return nombre.length > 20 ? nombre.substring(0, 20) + "..." : nombre;
                        });

                        new Chart(document.getElementById("graficoFormularios"), {
                            type: 'bar',
                            data: {
                                labels: etiquetasVisibles,
                                datasets: [
                                    {
                                        label: 'Completado',
                                        data: datosCompletados,
                                        backgroundColor: '#28a745'
                                    },
                                    {
                                        label: 'Borrador',
                                        data: datosBorradores,
                                        backgroundColor: '#ffc107'
                                    }
                                ]
                            },
                            options: {
                                indexAxis: 'y',
                                responsive: true,
                                maintainAspectRatio: false,
                                plugins: {
                                    legend: { position: 'top' },
                                    tooltip: {
                                        callbacks: {
                                            title: function(context) {
                                                const index = context[0].dataIndex;
                                                return labelsFormularios[index];
                                            }
                                        }
                                    }
                                },
                                scales: {
                                    x: {
                                        stacked: true,
                                        beginAtZero: true,
                                        title: {
                                            display: true,
                                            text: 'Cantidad de respuestas',
                                            font: {
                                                size: 14,
                                                weight: 'bold'
                                            },
                                            color: '#007bff'
                                        }
                                    },
                                    y: {
                                        stacked: true,
                                        ticks: {
                                            autoSkip: false
                                        },
                                        title: {
                                            display: true,
                                            text: 'Formularios',
                                            font: {
                                                size: 14,
                                                weight: 'bold'
                                            },
                                            color: '#007bff'
                                        }
                                    }
                                }
                            }
                        });
                    </script>


                    <!-- SEGUNDO GRAFICO - ACTIVOS VS BANEADOS -->
                    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                    <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2.2.0"></script>

                    <div class="col-xl-4 col-lg-5 mb-4">
                        <div class="card shadow h-100"> <!-- Altura completa -->
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Porcentaje de Encuestadores Activos vs Baneados</h6>
                            </div>
                            <div class="card-body d-flex flex-column">
                                <div class="chart-pie flex-grow-1 pt-4 pb-2">
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

                <!-- TERCER GRAFICO - POR ZONA -->
                <div class="row">
                    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;700&display=swap" rel="stylesheet">

                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow h-100">
                            <div class="card-header py-3 text-center">
                                <h6 class="m-0 font-weight-bold text-primary">Porcentaje de Formularios</h6>
                            </div>
                            <div class="card-body d-flex justify-content-center align-items-center flex-column">
                                <div class="flex-grow-1 d-flex justify-content-center align-items-center" style="width: 100%;">
                                    <canvas id="graficoEstados" style="max-height: 350px;"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- Asegúrate de incluir Chart.js y el plugin de datalabels -->
                    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                    <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2.2.0"></script>

                    <script>
                        const labelsEstados = ['Completado', 'Borrador'];
                        const dataEstados = [
                            ${formulariosPorEstado['Completado'] != null ? formulariosPorEstado['Completado'] : 0},
                            ${formulariosPorEstado['Borrador'] != null ? formulariosPorEstado['Borrador'] : 0},

                        ];

                        const totalFormularios = dataEstados.reduce((a, b) => a + b, 0);

                        new Chart(document.getElementById("graficoEstados").getContext('2d'), {
                            type: 'doughnut',
                            data: {
                                labels: labelsEstados,
                                datasets: [{
                                    data: dataEstados,
                                    backgroundColor: ['#28a745', '#ffc107'],
                                }]
                            },
                            options: {
                                responsive: true,
                                maintainAspectRatio: false,
                                cutout: '50%',
                                plugins: {
                                    legend: {
                                        display: true,
                                        position: 'bottom',
                                        labels: {
                                            font: {
                                                family: 'Rubik, sans-serif',
                                                weight: 'bold'
                                            }
                                        }
                                    },
                                    datalabels: {
                                        color: '#000',
                                        font: {
                                            family: 'Rubik, sans-serif',
                                            weight: 'bold',
                                            size: 14
                                        },
                                        formatter: function(value, context) {
                                            const total = context.chart.data.datasets[0].data.reduce((a,b) => a + b, 0);
                                            const porcentaje = total > 0 ? ((value / total) * 100).toFixed(0) : 0;
                                            return value;
                                        },
                                        anchor: 'center',
                                        align: 'center'
                                    }


                                }
                            },
                            plugins: [
                                ChartDataLabels,
                                {
                                    id: 'centerText',
                                    beforeDraw(chart) {
                                        const { ctx } = chart;
                                        const { top, bottom, left, right } = chart.chartArea;
                                        const x = (left + right) / 2;
                                        const y = (top + bottom) / 2;

                                        ctx.save();
                                        ctx.font = 'bold 30px Poppins, sans-serif';
                                        ctx.fillStyle = '#000';
                                        ctx.textAlign = 'center';
                                        ctx.textBaseline = 'middle';
                                        ctx.fillText(totalFormularios, x, y);
                                        ctx.restore();
                                    }
                                }


                            ]
                        });
                    </script>



                    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                    <!-- Gráfico de barras horizontales: Cantidad de respuestas por distrito -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow h-100">
                            <div class="card-header py-3 text-center">
                                <h6 class="m-0 font-weight-bold text-primary">Respuestas por Distrito</h6>
                            </div>
                            <div class="card-body d-flex justify-content-center align-items-center">
                                <div class="chart-bar" style="width: 100%;">
                                    <canvas id="graficoDistrito" style="max-height: 350px;"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>

                    <script>
                        const labelsDistrito = [
                            <c:forEach var="entry" items="${respuestasPorDistrito}">
                            '<c:out value="${entry.key}" />',
                            </c:forEach>
                        ];
                        const dataDistrito = [
                            <c:forEach var="entry" items="${respuestasPorDistrito}">
                            <c:out value="${entry.value}" />,
                            </c:forEach>
                        ];

                        new Chart(document.getElementById("graficoDistrito"), {
                            type: 'bar',
                            data: {
                                labels: labelsDistrito,
                                datasets: [{
                                    label: 'Cantidad de respuestas',
                                    data: dataDistrito,
                                    backgroundColor: 'rgba(54, 162, 235, 0.6)',
                                    borderColor: 'rgba(54, 162, 235, 1)',
                                    borderWidth: 1
                                }]
                            },
                            options: {
                                maintainAspectRatio: false,
                                scales: {
                                    x: {
                                        grid: { display: false },
                                        title: {
                                            display: true,
                                            text: 'Cantidad de Respuestas',
                                            font: {
                                                size: 14,
                                                weight: 'bold'
                                            },
                                            color: '#007bff'
                                        }
                                    },
                                    y: {
                                        beginAtZero: true,
                                        grid: { color: 'rgba(0, 0, 0, 0.1)' },
                                        title: {
                                            display: true,
                                            text: 'Distrito',
                                            font: {
                                                size: 14,
                                                weight: 'bold'
                                            },
                                            color: '#007bff'
                                        }
                                    }
                                },
                                plugins: {
                                    legend: { display: false }
                                }
                            }
                        });
                    </script>



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
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2.2.0"></script>

<!-- Custom Chart Scripts -->
<script>
    console.log("[DEBUG] JSP - Iniciando script de gráficos");


    function parseJspMap(jspMapString) {
        if (!jspMapString || jspMapString === '{}') {
            console.log("[DEBUG] JSP - Mapa vacío o nulo: ", jspMapString);
            return {};
        }
        try {

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
                        x: {
                            grid: { display: false },
                            title: {
                                display: true,
                                text: 'Zona',
                                font: {
                                    size: 14,
                                    weight: 'bold'
                                },
                                color: '#007bff'
                            }
                        },
                        y: {
                            beginAtZero: true,
                            grid: { color: 'rgba(0, 0, 0, 0.1)' },
                            title: {
                                display: true,
                                text: 'Cantidad de respuestas',
                                font: {
                                    size: 14,
                                    weight: 'bold'
                                },
                                color: '#007bff'
                            }
                        }
                    },
                    plugins: {
                        legend: { display: false }
                    }
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
            var total = pieData.reduce((a,b) => a + b, 0);

            // Suma total
            var total = pieData.reduce((a,b) => a + b, 0);

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
                    cutout: '50%',
                    plugins: {
                        legend: { display: false },
                        datalabels: {
                            color: '#000',
                            font: {
                                family: 'Rubik, sans-serif',
                                weight: 'bold',
                                size: 14
                            },
                            formatter: function(value, context) {

                                return value + '%';
                            },
                            anchor: 'center',
                            align: 'center'
                        }
                    }
                },
                plugins: [ChartDataLabels,
                    {
                        id: 'centerText',
                        beforeDraw(chart) {
                            const {ctx, width, height} = chart;
                            ctx.save();
                            ctx.font = 'bold 24px Rubik, sans-serif';
                            ctx.fillStyle = '#000';
                            ctx.textAlign = 'center';
                            ctx.textBaseline = 'middle';

                            ctx.restore();
                        }
                    }
                ]
            });

            console.log("[DEBUG] JSP - Gráfico de torta creado con total en centro");
        } catch (e) {
            console.error("[ERROR] JSP - Error al crear gráfico de torta: ", e);
        }
    }

</script>

</body>
</html>

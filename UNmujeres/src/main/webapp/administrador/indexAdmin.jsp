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
    <jsp:include page="/sidebarAdmin.jsp" />
    <!-- End of Sidebar -->

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">

        <!-- Main Content -->
        <div id="content">

            <!-- Topbar -->
            <jsp:include page="../topbarAdmin.jsp" />
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
                <div class="row align-items-stretch">
                    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                    <!-- Respuestas por Zona (Últimos 30 días) -->
                    <div class="col-xl-8 col-lg-8 col-md-10 mb-4">
                    <div class="card shadow h-120">

                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Respuestas por Zona (Completadas vs Borrador)</h6>
                            </div>
                            <div class="card-body">
                                <div style="height: 300px;">
                                    <canvas id="graficoAdminZona"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>

                    <script>
                        const respuestasPorZonaEstado = {
                            <c:forEach var="entry" items="${respuestasPorZonaEstado}">
                            "${entry.key}": {
                                C: ${entry.value["C"] != null ? entry.value["C"] : 0},
                                B: ${entry.value["B"] != null ? entry.value["B"] : 0}
                            },
                            </c:forEach>
                        };

                        const zonas = Object.keys(respuestasPorZonaEstado);
                        const completados = zonas.map(z => respuestasPorZonaEstado[z].C);
                        const borradores = zonas.map(z => respuestasPorZonaEstado[z].B);

                        new Chart(document.getElementById('graficoAdminZona'), {
                            type: 'bar',
                            data: {
                                labels: zonas,
                                datasets: [
                                    {
                                        label: 'Completado',
                                        data: completados,
                                        backgroundColor: '#28a745'
                                    },
                                    {
                                        label: 'Borrador',
                                        data: borradores,
                                        backgroundColor: '#ffc107'
                                    }
                                ]
                            },
                            options: {
                                responsive: true,
                                maintainAspectRatio: false,
                                plugins: {
                                    legend: {
                                        position: 'top',
                                        labels: {
                                            font: {
                                                weight: 'bold'
                                            },
                                            color: '#1D4ED8' // azul
                                        }
                                    },
                                    title: {
                                        display: false
                                    }
                                },
                                scales: {
                                    x: {
                                        stacked: true,
                                        beginAtZero: true,
                                        title: {
                                            display: true,
                                            text: 'Zona',
                                            color: '#1D4ED8', // azul
                                            font: {
                                                size: 14,
                                                weight: 'bold'
                                            }
                                        }
                                    },
                                    y: {
                                        stacked: true,
                                        beginAtZero: true,
                                        title: {
                                            display: true,
                                            text: 'Cantidad de Formularios',
                                            color: '#1D4ED8', // azul
                                            font: {
                                                size: 14,
                                                weight: 'bold'
                                            }
                                        }
                                    }
                                }
                            }

                        });
                    </script>

                    <link href="https://fonts.googleapis.com/css2?family=Rubik:wght@500&display=swap" rel="stylesheet">
                    <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2"></script>
                    <!-- Distribución de Usuarios -->
                    <div class="col-xl-4 col-lg-5 col-md-6 mb-4">
                    <div class="card shadow h-100">

                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Distribución de Usuarios</h6>
                            </div>
                            <div class="card-body">
                                <div class="chart-pie pt-4 pb-2" style="width: 100%; max-width: 600px; margin: 0 auto;">
                                    <canvas id="distribucionUsuariosChart"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Fila 2 de gráficos -->
                <div class="row align-items-stretch">
                    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                    <!-- Respuestas por Día (Última Semana) -->
                    <div class="col-xl-6 col-lg-6 mb-4">
                        <div class="card shadow h-100">

                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Top 5 Encuestadores por Formularios Completados</h6>
                            </div>
                            <div class="card-body">
                                <canvas id="topEncuestadoresChart" style="height: 300px;"></canvas>
                            </div>
                        </div>
                    </div>

                    <!-- Script del gráfico -->
                    <script>
                        const topEncuestadoresLabels = [
                            <c:forEach var="entry" items="${topEncuestadores}" varStatus="status">
                            '<c:out value="${entry.key}" />'<c:if test="${!status.last}">,</c:if>
                            </c:forEach>
                        ];

                        const topEncuestadoresData = [
                            <c:forEach var="entry" items="${topEncuestadores}" varStatus="status">
                            <c:out value="${entry.value}" /><c:if test="${!status.last}">,</c:if>
                            </c:forEach>
                        ];

                        const ctxTop = document.getElementById('topEncuestadoresChart').getContext('2d');
                        new Chart(ctxTop, {
                            type: 'bar',
                            data: {
                                labels: topEncuestadoresLabels,
                                datasets: [{
                                    label: 'Formularios completados',
                                    data: topEncuestadoresData,
                                    backgroundColor: 'rgba(78, 115, 223, 0.8)',
                                    borderColor: 'rgba(78, 115, 223, 1)',
                                    borderWidth: 1
                                }]
                            },
                            options: {
                                indexAxis: 'y', // barras horizontales
                                responsive: true,
                                maintainAspectRatio: false,
                                plugins: {
                                    legend: { display: false },
                                },
                                scales: {
                                    x: {
                                        beginAtZero: true,
                                        ticks: { precision: 0 },
                                        title: {
                                            display: true,
                                            text: 'Cantidad de Formularios',
                                            color: '#1D4ED8', // azul
                                            font: {
                                                size: 14,
                                                weight: 'bold'
                                            }
                                        }
                                    },
                                    y: {
                                        title: {
                                            display: true,
                                            text: 'Encuestadores',
                                            color: '#1D4ED8', // azul
                                            font: {
                                                size: 14,
                                                weight: 'bold'
                                            }
                                        }
                                    }
                                }
                            }

                        });
                    </script>

                    <!-- Progreso de Formularios por Zona (%) -->
                    <div class="col-xl-6 col-lg-6 mb-4">
                        <div class="card shadow h-100">

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
                <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2"></script>

                <script>


                    // Gráfico de Pastel: Distribución de Usuarios
                    const totalUsuarios = ${coordinadoresActivos} + ${encuestadoresActivos} + ${usuariosDesactivados};

                    const centerTextPlugin = {
                        id: 'centerText',
                        beforeDraw: function(chart) {
                            const { width, height, chartArea } = chart;
                            const ctx = chart.ctx;
                            ctx.restore();
                            const fontSize = (height / 114).toFixed(2);
                            ctx.font = (fontSize * 1.9) + "em 'Rubik', sans-serif";
                            ctx.fillStyle = "#444";
                            ctx.textBaseline = "middle";
                            const text = totalUsuarios.toString();
                            const textX = (chartArea.left + chartArea.right) / 2 - ctx.measureText(text).width / 2;
                            const textY = (chartArea.top + chartArea.bottom) / 2;
                            ctx.fillText(text, textX, textY);
                            ctx.save();
                        }
                    };

                    const distribucionUsuariosChart = new Chart(document.getElementById('distribucionUsuariosChart'), {
                        type: 'doughnut',
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
                            cutout: '60%',
                            plugins: {
                                legend: { position: 'bottom' },
                                datalabels: {
                                    color: '#fff',
                                    font: {
                                        weight: 'bold',
                                        size: 16, // Aumentado
                                        family: 'Rubik'
                                    },
                                    textAlign: 'center',
                                    shadowBlur: 4,
                                    shadowColor: 'rgba(0, 0, 0, 0.3)', // Sombra opcional
                                    formatter: function(value) {
                                        return value;
                                    }
                                }

                            }
                        },
                        plugins: [ChartDataLabels, centerTextPlugin]
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
                            maintainAspectRatio: false,
                            plugins: {
                                legend: { display: false }
                            },
                            scales: {
                                x: {
                                    title: {
                                        display: true,
                                        text: 'Zonas',
                                        color: '#1D4ED8', // azul
                                        font: {
                                            size: 14,
                                            weight: 'bold'
                                        }
                                    }
                                },
                                y: {
                                    beginAtZero: true,
                                    ticks: { stepSize: 10 },
                                    title: {
                                        display: true,
                                        text: 'Porcentaje',
                                        color: '#1D4ED8', // azul
                                        font: {
                                            size: 14,
                                            weight: 'bold'
                                        }
                                    }
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

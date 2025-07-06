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
                                            FORMULARIOS DISPONIBLES PARA TI
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
                                            FORMULARIOS COMPLETADOS ESTA SEMANA
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
                                            FORMULARIOS GUARDADOS COMO BORRADOR
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
                                            ÚLTIMO FORMULARIO ASIGNADO
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
                                            FORMULARIOS QUE VENCEN PRONTO
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
                                            TOTAL DE RESPUESTAS REGISTRADAS
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
                                            RESPUESTAS COMPLETADAS HOY
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

                <!-- NUEVOS GRÁFICOS -->
                <div class="row">
                    <!-- Gráfico de Barras: Formularios más respondidos -->
                    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                    <div class="col-lg-7">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Cantidad de Respuestas por formularios</h6>
                            </div>
                            <div class="card-body" style="height: 400px;">
                                <canvas id="barChartFormularios" style="width: 100%; height: 100%;"></canvas>
                            </div>
                        </div>
                    </div>

                    <!-- Gráfico de Pastel: Formularios más utilizados -->
                    <div class="col-lg-5">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Formularios completados por semana</h6>
                            </div>
                            <div class="card-body" style="height: 400px;">
                                <canvas id="lineChartRespuestasPorDia" style="width:100%; height:400px;"></canvas>

                            </div>
                        </div>
                    </div>
                </div>


                <div class="row">
                    <!-- Gráfico: Formularios completados por distrito -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Formularios por zona geográfica</h6>
                            </div>
                            <div class="card-body">
                                <canvas id="barChartZonas" style="width:100%; height:350px;"></canvas>
                            </div>
                        </div>
                    </div>

                    <link href="https://fonts.googleapis.com/css2?family=Rubik:wght@500&display=swap" rel="stylesheet">
                    <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2"></script>
                    <!-- Gráfico: Estado de respuestas -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Estado de tus respuestas</h6>
                            </div>
                            <div class="card-body">
                                <canvas id="estadoRespuestasChart" style="width:100%; height:350px;"></canvas>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Chart.js -->
                <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2"></script>
                <link href="https://fonts.googleapis.com/css2?family=Rubik:wght@500&display=swap" rel="stylesheet">



                <script>
                    // Datos para los gráficos
                    const formularioLabels = [
                        <c:forEach items="${formulariosPorCategoria}" var="form" varStatus="loop">
                        "${form.key}"<c:if test="${!loop.last}">,</c:if>
                        </c:forEach>
                    ];

                    const formularioData = [
                        <c:forEach items="${formulariosPorCategoria}" var="form" varStatus="loop">
                        ${form.value}<c:if test="${!loop.last}">,</c:if>
                        </c:forEach>
                    ];

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

                    const zonaLabels = [
                        <c:forEach items="${formulariosPorZona}" var="zona" varStatus="loop">
                        "${zona.key}"<c:if test="${!loop.last}">,</c:if>
                        </c:forEach>
                    ];

                    const zonaData = [
                        <c:forEach items="${formulariosPorZona}" var="zona" varStatus="loop">
                        ${zona.value}<c:if test="${!loop.last}">,</c:if>
                        </c:forEach>
                    ];


                    const barChartFormularios = new Chart(document.getElementById('barChartFormularios'), {
                        type: 'bar',
                        data: {
                            labels: formularioLabels,
                            datasets: [{
                                label: 'Cantidad de respuestas',
                                data: formularioData,
                                backgroundColor: '#36A2EB'
                            }]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false,
                            plugins: {
                                legend: { display: false },


                            },
                            scales: {
                                x: {
                                    ticks: {
                                        color: '#36A2EB',
                                        callback: function(value) {
                                            let label = this.getLabelForValue(value);
                                            return label.length > 20 ? label.substring(0, 20) + '…' : label;
                                        }
                                    },
                                    title: {
                                        display: true,
                                        text: 'Formularios',
                                        color: '#000'
                                    }
                                },
                                y: {
                                    beginAtZero: true,
                                    ticks: { stepSize: 1 },
                                    title: {
                                        display: true,
                                        text: 'Cantidad de respuestas',
                                        color: '#000'
                                    }
                                }
                            }
                        }
                    });

                    const pieChart = new Chart(document.getElementById('pieChartTipos'), {
                        type: 'pie',
                        data: {
                            labels: pieLabels,
                            datasets: [{
                                data: pieData,
                                backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF', '#F67019']
                            }]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false,
                            plugins: {
                                legend: {
                                    position: 'bottom',
                                    labels: { color: '#000' }
                                },
                                tooltip: {
                                    callbacks: {
                                        label: function(context) {
                                            const value = context.raw;
                                            const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                            const percentage = Math.round((value / total) * 100);
                                            return '${context.label}: ${value} respuestas (${percentage}%)';
                                        }
                                    }
                                }
                            }
                        }
                    });

                    const barChartZonas = new Chart(document.getElementById('barChartZonas'), {
                        type: 'bar',
                        data: {
                            labels: zonaLabels,
                            datasets: [{
                                label: 'Cantidad de formularios',
                                data: zonaData,
                                backgroundColor: '#4BC0C0'
                            }]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false,
                            plugins: {
                                legend: { display: false },
                                tooltip: {
                                    
                                }
                            },
                            scales: {
                                x: {
                                    title: {
                                        display: true,
                                        text: 'Zona / Distrito',
                                        color: '#000'
                                    },
                                    ticks: {
                                        color: '#4BC0C0',
                                        callback: function(value) {
                                            let label = this.getLabelForValue(value);
                                            return label.length > 15 ? label.substring(0, 15) + '…' : label;
                                        }
                                    }
                                },
                                y: {
                                    beginAtZero: true,
                                    title: {
                                        display: true,
                                        text: 'Cantidad',
                                        color: '#000'
                                    }
                                }
                            }
                        }
                    });




                    const estadoCompletadas = Number("${estadoCompletadas != null ? estadoCompletadas : 0}");
                    const estadoBorradores = Number("${estadoBorradores != null ? estadoBorradores : 0}");
                    const totalEstado = estadoCompletadas + estadoBorradores;

                    console.log("estadoCompletadas =", estadoCompletadas);
                    console.log("estadoBorradores =", estadoBorradores);
                    console.log("totalEstado =", totalEstado);

                    const estadoData = {
                        labels: ['Completadas', 'Borradores'],
                        datasets: [{
                            label: 'Cantidad de respuestas',
                            data: [estadoCompletadas, estadoBorradores],
                            backgroundColor: ['#4CAF50', '#FFC107'],
                            borderColor: ['#388E3C', '#FFA000'],
                            borderWidth: 1
                        }]
                    };

                    const centerTextPlugin = {
                        id: 'centerText',
                        beforeDraw: function(chart) {
                            const ctx = chart.ctx;
                            const { chartArea } = chart;

                            ctx.save();

                            const text = totalEstado.toString();

                            // Forzamos tamaño sin depender del CSS
                            let fontSize = Math.min(chart.width, chart.height) / 4;  // Ajusta divisor
                            ctx.font = `60px 'Rubik', sans-serif`;

                            ctx.fillStyle = "#444";
                            ctx.textBaseline = "middle";
                            ctx.textAlign = "center";

                            const textX = (chartArea.left + chartArea.right) / 2;
                            const textY = (chartArea.top + chartArea.bottom) / 2;

                            ctx.fillText(text, textX, textY);

                            ctx.restore();
                        }
                    };


                    const estadoChart = new Chart(document.getElementById('estadoRespuestasChart'), {
                        type: 'doughnut',
                        data: estadoData,
                        options: {
                            responsive: true,
                            maintainAspectRatio: false,
                            cutout: '60%',
                            plugins: {
                                legend: {
                                    position: 'bottom',
                                    labels: { color: '#000' }
                                },
                                datalabels: {
                                    color: '#fff',
                                    font: {
                                        weight: 'bold',
                                        size: 16
                                    },
                                    formatter: function(value) {
                                        return value;
                                    }
                                }
                            }
                        },
                        plugins: [ChartDataLabels, centerTextPlugin]
                    });




                    const diaLabels = [
                        <c:forEach items="${respuestasPorDia}" var="dato" varStatus="loop">
                        "${dato.key}"<c:if test="${!loop.last}">,</c:if>
                        </c:forEach>
                    ];

                    const diaData = [
                        <c:forEach items="${respuestasPorDia}" var="dato" varStatus="loop">
                        ${dato.value}<c:if test="${!loop.last}">,</c:if>
                        </c:forEach>
                    ];

                    const lineChart = new Chart(document.getElementById('lineChartRespuestasPorDia'), {
                        type: 'line',
                        data: {
                            labels: diaLabels,
                            datasets: [{
                                label: 'Respuestas completadas',
                                data: diaData,
                                borderColor: '#36A2EB',
                                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                                fill: true
                            }]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false,
                            plugins: {
                                legend: {
                                    display: false
                                }
                            },
                            scales: {
                                x: {
                                    title: {
                                        display: true,
                                        text: 'Fecha',
                                        color: '#000'
                                    },
                                    ticks: {
                                        color: '#4BC0C0',
                                        callback: function(value) {
                                            let label = this.getLabelForValue(value);
                                            return label.length > 15 ? label.substring(0, 15) + '…' : label;
                                        }
                                    }
                                },
                                y: {
                                    beginAtZero: true,
                                    title: {
                                        display: true,
                                        text: 'Cantidad de Respuestas',
                                        color: '#000'
                                    }
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
                <a class="btn btn-danger" href="${pageContext.request.contextPath}/logout">Cerrar sesión</a>
            </div>
        </div>
    </div>
</div>

</div>
<!-- End of Page Wrapper -->
<jsp:include page="../footer.jsp" />

</body>
</html>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<jsp:include page="/header.jsp" />



<head>
    <title>Dashboard de Formularios</title>
    <style>
        .chart-container {
            position: relative;
            height: 300px;
            margin-bottom: 30px;
        }
        .section-title {
            background-color: #4e73df;
            color: white;
            padding: 10px;
            border-radius: 5px;
            margin-top: 20px;
        }
        .question-card {
            margin-bottom: 30px;

        }
        .table-responsive {
            max-height: 300px;
            overflow-y: auto;
            padding-right: 5px;
        }
    </style>
</head>


<body id="page-top">
<div id="wrapper">
    <jsp:include page="../sidebarAdmin.jsp" />

    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
            <jsp:include page="../topbarAdmin.jsp" />

            <div class="container-fluid">
                <h1 class="h3 mb-4 text-gray-800">Dashboard de Formularios</h1>

                <!-- Selector de formulario -->
                <div class="card shadow mb-4">
                    <div class="card-body">
                        <form method="get" action="DashboardCuidado" class="form-inline">
                            <div class="form-group">
                                <label for="formularioId" class="mr-2">Seleccione Formulario:</label>
                                <select name="formularioId" id="formularioId" class="form-control" onchange="this.form.submit()">
                                    <c:forEach var="form" items="${formularios}">
                                        <option value="${form.idFormulario}" ${form.idFormulario == formularioSeleccionado ? 'selected' : ''}>
                                                ${form.nombre}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </form>
                    </div>
                </div>



                <!-- Gráficos para preguntas Sí/No -->
                <div class="row">
                    <c:forEach var="grafico" items="${estadisticas.datosGraficos}">
                        <div class="col-xl-6 col-lg-6 mb-4">
                            <div class="card shadow">
                                <div class="card-header py-3">
                                    <h6 class="m-0 font-weight-bold text-primary">${grafico.key}</h6>
                                </div>
                                <div class="card-body">
                                    <div class="chart-container">
                                        <canvas id="chart_${grafico.key.hashCode()}"></canvas>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>


                <!-- Tablas por sección -->
                <c:forEach var="seccion" items="${estadisticas.respuestasAgrupadas}">
                    <div class="card shadow mb-4">
                        <div class="card-header py-3 bg-primary text-white">
                            <h6 class="m-0 font-weight-bold">${seccion.key}</h6>
                        </div>
                        <div class="card-body">
                            <c:forEach var="pregunta" items="${seccion.value}">
                                <div class="mb-4 question-card">
                                    <h5 class="font-weight-bold">${pregunta.key}</h5>
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-hover">
                                            <thead class="thead-light">
                                            <tr>
                                                <th width="70%">Respuesta</th>
                                                <th width="30%">Encuestador</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach var="respuesta" items="${pregunta.value}">
                                                <tr>
                                                    <td>${respuesta.respuesta}</td>
                                                    <td>${respuesta.encuestador}</td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>


                                        </table>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:forEach>
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


    <jsp:include page="/footer.jsp" />

    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <!-- Page level plugins -->
    <script src="/vendor/datatables/jquery.dataTables.min.js"></script>
    <script src="/vendor/datatables/dataTables.bootstrap4.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2"></script>
        <link href="https://fonts.googleapis.com/css2?family=Rubik:wght@500&display=swap" rel="stylesheet">


        <script>
            document.addEventListener('DOMContentLoaded', function() {

                const centerTextPlugin = {
                    id: 'centerText',
                    beforeDraw(chart) {
                        const { width, height, chartArea } = chart;
                        const ctx = chart.ctx;
                        const dataset = chart.data.datasets[0];
                        const total = dataset.data.reduce((a, b) => a + b, 0);

                        ctx.save();
                        const fontSize = (height / 114).toFixed(2);
                        ctx.font = (fontSize * 1.8) + "em 'Rubik', sans-serif";
                        ctx.fillStyle = "#444";
                        ctx.textBaseline = "middle";
                        const text = total.toString();
                        const textX = (chartArea.left + chartArea.right) / 2 - ctx.measureText(text).width / 2;
                        const textY = (chartArea.top + chartArea.bottom) / 2;
                        ctx.fillText(text, textX, textY);
                        ctx.restore();
                    }
                };

                <c:forEach var="grafico" items="${estadisticas.datosGraficos}">
                new Chart(document.getElementById('chart_${grafico.key.hashCode()}'), {
                    type: 'doughnut',
                    data: {
                        labels: [
                            <c:forEach var="entry" items="${grafico.value}" varStatus="loop">
                            '${entry.key}'<c:if test="${!loop.last}">, </c:if>
                            </c:forEach>
                        ],
                        datasets: [{
                            data: [
                                <c:forEach var="entry" items="${grafico.value}" varStatus="loop">
                                ${entry.value}<c:if test="${!loop.last}">, </c:if>
                                </c:forEach>
                            ],
                            backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc'],
                            hoverBackgroundColor: ['#2e59d9', '#17a673', '#2c9faf'],
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        cutout: '60%',
                        plugins: {
                            datalabels: {
                                color: '#fff',
                                font: {
                                    weight: 'bold',
                                    size: 19,
                                    family: 'Rubik'
                                },
                                formatter: (value) => value
                            },
                            legend: {
                                position: 'right',
                                labels: {
                                    font: { size: 12 },
                                    padding: 20
                                }
                            }
                        }
                    },
                    plugins: [ChartDataLabels, centerTextPlugin]
                });
                </c:forEach>
            });

        </script>

    <script>
        $(document).ready(function() {


            $("tr").each(function() {
                if ($(this).find("select").length > 0) {
                    $(this).addClass("hidden-row");
                }
            });


            $("tr").each(function() {
                if ($(this).find("input[type='text']").length > 0) {
                    $(this).removeClass("hidden-row");
                }
            });
        });


    </script>


</body>
</html>






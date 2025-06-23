

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="es">
<jsp:include page="/header.jsp" />

<body id="page-top">

<div id="wrapper">
    <jsp:include page="sidebarAdmin.jsp" />

    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
            <jsp:include page="topbarAdmin.jsp" />

            <div class="container-fluid">
                <div class="d-sm-flex align-items-center justify-content-between mb-4">
                    <h1 class="h3 mb-0 text-gray-800">Resumen de Respuestas</h1>
                </div>

                <!-- Fila 1 -->
                <!-- Fila 1 -->
                <div class="row">
                    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                    <!-- Pregunta 9 -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Niños/as de 0 a 5 años en el hogar</h6>
                            </div>
                            <div class="card-body">
                                <canvas id="graficoPregunta9"></canvas>
                            </div>
                        </div>
                    </div>
                    <!-- Pregunta 11 -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">¿Asisten a guardería o preescolar?</h6>
                            </div>
                            <div class="card-body">
                                <canvas id="graficoPregunta11"></canvas>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Fila 2 -->
                <div class="row">
                    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                    <!-- Pregunta 12 -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">¿Por qué no usa guarderías?</h6>
                            </div>
                            <div class="card-body">
                                <canvas id="graficoPregunta12"></canvas>
                            </div>
                        </div>
                    </div>
                    <!-- Pregunta 13 -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">¿Hay adultos mayores en el hogar?</h6>
                            </div>
                            <div class="card-body">
                                <canvas id="graficoPregunta13"></canvas>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Fila 3 -->
                <div class="row">
                    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                    <!-- Pregunta 16 -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">¿Usan apoyos para movilizarse?</h6>
                            </div>
                            <div class="card-body">
                                <canvas id="graficoPregunta16"></canvas>
                            </div>
                        </div>
                    </div>
                    <!-- Pregunta 17 -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">¿Hay personas con discapacidad?</h6>
                            </div>
                            <div class="card-body">
                                <canvas id="graficoPregunta17"></canvas>
                            </div>
                        </div>
                    </div>
                </div>


                <!-- Fila 4 -->
                <div class="row">
                    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                    <!-- Pregunta 20 -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">¿Tienen carnet CONADIS?</h6>
                            </div>
                            <div class="card-body">
                                <canvas id="graficoPregunta20"></canvas>
                            </div>
                        </div>
                    </div>

                    <!-- Pregunta 21 -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">¿Alguien trabaja en el hogar como doméstico remunerado?</h6>
                            </div>
                            <div class="card-body">
                                <canvas id="graficoPregunta21"></canvas>
                            </div>
                        </div>
                    </div>
                </div>


                <!-- Fila 5 -->
                <div class="row">
                    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                    <!-- Pregunta 23 -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">¿Tiene contrato formal donde trabaja?</h6>
                            </div>
                            <div class="card-body">
                                <canvas id="graficoPregunta23"></canvas>
                            </div>
                        </div>
                    </div>

                    <!-- Pregunta 24 -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">¿Participa en sindicato u organización?</h6>
                            </div>
                            <div class="card-body">
                                <canvas id="graficoPregunta24"></canvas>
                            </div>
                        </div>
                    </div>
                </div>


                <!-- Fila 6 -->
                <div class="row">
                    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                    <!-- Pregunta 26 -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">¿Conoce servicios de cuidados como Cuna Más, OMAPED, CIAM?</h6>
                            </div>
                            <div class="card-body">
                                <canvas id="graficoPregunta26"></canvas>
                            </div>
                        </div>
                    </div>

                    <!-- Pregunta 27 -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">¿Realiza trabajo remunerado fuera de casa?</h6>
                            </div>
                            <div class="card-body">
                                <canvas id="graficoPregunta27"></canvas>
                            </div>
                        </div>
                    </div>
                </div>


                <!-- Fila 7 -->
                <div class="row">
                    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                    <!-- Pregunta 28 -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">¿Tiene algún emprendimiento o negocio?</h6>
                            </div>
                            <div class="card-body">
                                <canvas id="graficoPregunta28"></canvas>
                            </div>
                        </div>
                    </div>
                </div>





                <script>
                    function generarBarChart(idCanvas, dataObj, label) {
                        new Chart(document.getElementById(idCanvas), {
                            type: 'bar',
                            data: {
                                labels: Object.keys(dataObj),
                                datasets: [{
                                    label: label,
                                    data: Object.values(dataObj),
                                    backgroundColor: ['#4e73df', '#1cc88a'],
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
                                            text: 'Respuesta',
                                            color: 'blue',
                                            font: { weight: 'bold' }
                                        }
                                    },
                                    y: {
                                        beginAtZero: true,
                                        title: {
                                            display: true,
                                            text: 'Cantidad',
                                            color: 'blue',
                                            font: { weight: 'bold' }
                                        }
                                    }
                                }
                            }
                        });
                    }

                    const datosPregunta9 = {
                        <c:forEach var="entry" items="${respuesta9}" varStatus="status">
                        "${entry.key}": ${entry.value}<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    };
                    const datosPregunta11 = {
                        <c:forEach var="entry" items="${respuesta11}" varStatus="status">
                        "${entry.key}": ${entry.value}<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    };
                    const datosPregunta12 = {
                        <c:forEach var="entry" items="${respuesta12}" varStatus="status">
                        "${entry.key}": ${entry.value}<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    };
                    const datosPregunta13 = {
                        <c:forEach var="entry" items="${respuesta13}" varStatus="status">
                        "${entry.key}": ${entry.value}<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    };
                    const datosPregunta16 = {
                        <c:forEach var="entry" items="${respuesta16}" varStatus="status">
                        "${entry.key}": ${entry.value}<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    };
                    const datosPregunta17 = {
                        <c:forEach var="entry" items="${respuesta17}" varStatus="status">
                        "${entry.key}": ${entry.value}<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    };
                    const datosPregunta20 = {
                        <c:forEach var="entry" items="${respuesta20}" varStatus="status">
                        "${entry.key}": ${entry.value}<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    };
                    const datosPregunta21 = {
                        <c:forEach var="entry" items="${respuesta21}" varStatus="status">
                        "${entry.key}": ${entry.value}<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    };
                    const datosPregunta23 = {
                        <c:forEach var="entry" items="${respuesta23}" varStatus="status">
                        "${entry.key}": ${entry.value}<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    };
                    const datosPregunta24 = {
                        <c:forEach var="entry" items="${respuesta24}" varStatus="status">
                        "${entry.key}": ${entry.value}<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    };
                    const datosPregunta26 = {
                        <c:forEach var="entry" items="${respuesta26}" varStatus="status">
                        "${entry.key}": ${entry.value}<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    };
                    const datosPregunta27 = {
                        <c:forEach var="entry" items="${respuesta27}" varStatus="status">
                        "${entry.key}": ${entry.value}<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    };
                    const datosPregunta28 = {
                        <c:forEach var="entry" items="${respuesta28}" varStatus="status">
                        "${entry.key}": ${entry.value}<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    };

                    window.onload = () => {
                        generarBarChart("graficoPregunta9", datosPregunta9, "Respuestas");
                        generarBarChart("graficoPregunta11", datosPregunta11, "Respuestas");
                        generarBarChart("graficoPregunta12", datosPregunta12, "Respuestas");
                        generarBarChart("graficoPregunta13", datosPregunta13, "Respuestas");
                        generarBarChart("graficoPregunta16", datosPregunta16, "Respuestas");
                        generarBarChart("graficoPregunta17", datosPregunta17, "Respuestas");
                        generarBarChart("graficoPregunta20", datosPregunta20, "Respuestas");
                        generarBarChart("graficoPregunta21", datosPregunta21, "Respuestas");
                        generarBarChart("graficoPregunta23", datosPregunta23, "Respuestas");
                        generarBarChart("graficoPregunta24", datosPregunta24, "Respuestas");
                        generarBarChart("graficoPregunta26", datosPregunta26, "Respuestas");
                        generarBarChart("graficoPregunta27", datosPregunta27, "Respuestas");
                        generarBarChart("graficoPregunta28", datosPregunta28, "Respuestas");
                    };
                </script>



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

<jsp:include page="/footer.jsp" />

<!-- Chart.js -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>






</body>
</html>



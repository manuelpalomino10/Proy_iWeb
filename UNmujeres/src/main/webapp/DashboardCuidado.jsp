

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
            <jsp:include page="topbarEnc.jsp" />

            <div class="container-fluid">
                <div class="d-sm-flex align-items-center justify-content-between mb-4">
                    <h1 class="h3 mb-0 text-gray-800">Resumen de Respuestas</h1>
                </div>

                <!-- Fila 1 -->
                <div class="row">
                    <!-- Asistencia a Guarderías -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Asistencia a Guarderías</h6>
                            </div>
                            <div class="card-body d-flex justify-content-center">
                                <div class="chart-area" style="max-width: 350px;">
                                    <canvas id="guarderiaChart" width="300" height="300"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>


                    <!-- Motivos para no usar Guarderías -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Motivos para no usar Guarderías</h6>
                            </div>
                            <div class="card-body">
                                <div class="chart-area">
                                    <canvas id="motivosChart" width="700" height="350"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Fila 2 -->
                <div class="row">
                    <!-- Enfermedades en Adultos Mayores -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Adultos Mayores con Enfermedades</h6>
                            </div>
                            <div class="card-body d-flex justify-content-center">
                                <div class="chart-area" style="max-width: 350px;">
                                    <canvas id="enfermosChart" width="300" height="300"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>



                    <!-- Composición del Hogar -->
                    <div class="col-xl-6 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Composición del Hogar</h6>
                            </div>
                            <div class="card-body">
                                <div class="chart-area">
                                    <canvas id="hogaresChart" width="700" height="350"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>



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

<!-- Asistencia a Guarderías -->
<script>
    new Chart(document.getElementById('guarderiaChart'), {
        type: 'doughnut',
        data: {
            labels: ['Sí', 'No'],
            datasets: [{
                data: [
                    ${asistenciaGuarderia['Sí'] != null ? asistenciaGuarderia['Sí'] : 0},
                    ${asistenciaGuarderia['No'] != null ? asistenciaGuarderia['No'] : 0}
                ],
                backgroundColor: ['#4e73df', '#1cc88a']
            }]
        }
    });
</script>

<!-- Motivos para no usar Guarderías -->
<script>
    const motivosLabels = [<c:forEach var="entry" items="${motivosNoGuarderia}">'${entry.key}',</c:forEach>];
    const motivosValues = [<c:forEach var="entry" items="${motivosNoGuarderia}">${entry.value},</c:forEach>];

    new Chart(document.getElementById('motivosChart'), {
        type: 'bar',
        data: {
            labels: motivosLabels,
            datasets: [{
                label: 'Hogares',
                data: motivosValues,
                backgroundColor: '#36b9cc'
            }]
        },
        options: {
            indexAxis: 'y'
        }
    });
</script>

<!-- Enfermedades en Adultos Mayores -->
<script>
    const enfermosLabels = [<c:forEach var="entry" items="${adultosMayoresEnfermos}">'${entry.key}',</c:forEach>];
    const enfermosValues = [<c:forEach var="entry" items="${adultosMayoresEnfermos}">${entry.value},</c:forEach>];

    new Chart(document.getElementById('enfermosChart'), {
        type: 'pie',
        data: {
            labels: enfermosLabels,
            datasets: [{
                data: enfermosValues,
                backgroundColor: ['#f6c23e', '#e74a3b', '#858796', '#1cc88a', '#36b9cc']
            }]
        }
    });
</script>

<!-- Composición del Hogar -->
<script>
    const hogarData = {
        labels: ['Ambos', 'Solo niños', 'Solo ancianos'],
        datasets: [{
            label: 'Hogares',
            data: [
                ${hogaresNinosAncianos['Ambos'] != null ? hogaresNinosAncianos['Ambos'] : 0},
                ${hogaresNinosAncianos['Solo niños'] != null ? hogaresNinosAncianos['Solo niños'] : 0},
                ${hogaresNinosAncianos['Solo ancianos'] != null ? hogaresNinosAncianos['Solo ancianos'] : 0}
            ],
            backgroundColor: ['#4e73df', '#1cc88a', '#e74a3b']
        }]
    };

    new Chart(document.getElementById('hogaresChart'), {
        type: 'bar',
        data: hogarData,
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
</script>

</body>
</html>


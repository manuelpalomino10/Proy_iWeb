// Call the dataTables jQuery plugin
$(document).ready(function() {
    $('#dataTable').DataTable({
        "order": [], // Deshabilita el orden inicial si lo deseas, o ajusta según necesites
        "pageLength": 10, // Número de filas por página por defecto
        "language": { // Configuración del idioma
            "url": "//cdn.datatables.net/plug-ins/1.13.4/i18n/es-ES.json" // URL para español
        }
    });
});

$(document).ready(function() {
    $('#tablaAsig').DataTable({
        "order": [], // Deshabilita el orden inicial si lo deseas, o ajusta según necesites
        "pageLength": 10, // Número de filas por página por defecto
        "language": { // Configuración del idioma
            "url": "//cdn.datatables.net/plug-ins/1.13.4/i18n/es-ES.json" // URL para español
        }
    });
});

$(document).ready(function() {
    $('#tablaDrafts').DataTable({
        "order": [], // Deshabilita el orden inicial si lo deseas, o ajusta según necesites
        "pageLength": 10, // Número de filas por página por defecto
        "language": { // Configuración del idioma
            "url": "//cdn.datatables.net/plug-ins/1.13.4/i18n/es-ES.json" // URL para español
        }
    });
});

$(document).ready(function() {
    $('#tablaRecords').DataTable({
        "order": [], // Deshabilita el orden inicial si lo deseas, o ajusta según necesites
        "pageLength": 10, // Número de filas por página por defecto
        "language": { // Configuración del idioma
            "url": "//cdn.datatables.net/plug-ins/1.13.4/i18n/es-ES.json" // URL para español
        }
    });
});
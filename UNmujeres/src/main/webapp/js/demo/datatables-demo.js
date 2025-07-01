// Call the dataTables jQuery plugin
$(document).ready(function() {
    $('#dataTable').DataTable({
        language: {
            url: "//cdn.datatables.net/plug-ins/1.13.4/i18n/es-ES.json"
        }
    });
});

$(document).ready(function() {
    $('#tablaAsig').DataTable({
        //paging: true,       // Habilita la paginación
        //ordering: true,     // Permite el ordenamiento por columnas
        //searching: true,    // Agrega la funcionalidad de búsqueda global
        //info: true,         // Muestra información de la tabla (por ejemplo, "Mostrando X de Y entradas")
        language: {
            url: "//cdn.datatables.net/plug-ins/1.13.4/i18n/es-ES.json"
        }
    });
});

$(document).ready(function() {
    $('#tablaDrafts').DataTable({
        language: {
            url: "//cdn.datatables.net/plug-ins/1.13.4/i18n/es-ES.json"
        }
    });
});

$(document).ready(function() {
    $('#tablaRecords').DataTable({
        language: {
            url: "//cdn.datatables.net/plug-ins/1.13.4/i18n/es-ES.json"
        }
    });
});
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.unmujeres.dtos.FormularioDto" %>
<%@ page import="com.example.unmujeres.beans.Categoria" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>

<%
    ArrayList<FormularioDto> lista = (ArrayList<FormularioDto>) request.getAttribute("lista");
    ArrayList<Categoria> categorias = (ArrayList<Categoria>) request.getAttribute("categorias");
    int selectedCategoria = request.getAttribute("selectedCategoria") != null ? (Integer) request.getAttribute("selectedCategoria") : 0;
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <%-- Incluye el encabezado (head) de la página, que contiene los meta tags y los CSS --%>
    <jsp:include page="../header.jsp" />
    <title>Gestión de Formularios - ONU Mujeres</title>

    <%-- CSS personalizado para el dropdown de acciones si lo necesitas --%>
    <style>
        /* Estilos para que los botones dentro del dropdown se vean y funcionen correctamente */
        .dropdown-menu .dropdown-item {
            display: flex;
            align-items: center;
            gap: 8px; /* Espacio entre el icono y el texto en el dropdown */
        }
        .dropdown-menu .dropdown-item form {
            margin: 0; /* Remover margen por defecto del formulario */
            padding: 0; /* Remover padding por defecto del formulario */
            width: 100%; /* Asegurar que el formulario ocupe todo el ancho del item */
            display: flex; /* Para alinear el botón dentro del formulario */
        }
        .dropdown-menu .dropdown-item form button {
            border: none;
            background: none;
            color: inherit; /* Hereda el color del texto del dropdown-item */
            width: 100%; /* El botón ocupa todo el ancho del formulario dentro del item */
            text-align: left; /* Alinea el texto del botón a la izquierda */
            padding: .25rem 1.5rem; /* Ajustar padding para que coincida con dropdown-item */
        }
        .dropdown-menu .dropdown-item form button:hover {
            background-color: #e9ecef; /* Color de hover similar al dropdown-item */
        }
        /* Estilos para el botón del dropdown principal (tres puntos) */
        .btn-action-dropdown {
            padding: .25rem .5rem; /* Pequeño padding */
            font-size: .875rem; /* Tamaño de fuente pequeño */
        }
    </style>
</head>
<body id="page-top">
<div id="wrapper">
    <%-- Incluye la barra lateral --%>
    <jsp:include page="../sidebarCoordi.jsp" />

    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
            <%-- Incluye la barra superior (topbar), que contiene el dropdown de usuario --%>
            <jsp:include page="../topbarCoordi.jsp" />

            <div class="container-fluid">

                <hr/>

                <%
                    Map<Integer, List<String>> errores = (Map<Integer, List<String>>) session.getAttribute("validationErrors");
                    if (errores != null) {
                %>
                <div class="alert alert-danger">
                    <ul>
                        <%
                            for (Map.Entry<Integer, List<String>> entry : errores.entrySet()) {
                        %>
                        <li><strong>Pregunta <%= entry.getKey() %>:</strong>
                            <ul>
                                <%
                                    // Por cada mensaje de esa pregunta…
                                    for (String msg : entry.getValue()) {
                                %>
                                <li><%= msg %></li>
                                <%
                                    }
                                %>
                            </ul>
                        </li>
                        <%
                            }
                            session.removeAttribute("validationErrors");
                        %>
                    </ul>
                </div>


                <%} if (session.getAttribute("error") != null) { %>
                <div>
                    <div class="alert alert-danger" role="alert"><%=session.getAttribute("error")%>
                    </div>
                </div>
                <% session.removeAttribute("error"); %>
                <% } %>

                <% if (session.getAttribute("success") != null) { %>
                <div>
                    <div class="alert alert-success" role="alert"><%=session.getAttribute("success")%>
                    </div>
                </div>
                <% session.removeAttribute("success"); %>
                <% } %>

                </hr>

                <h1 class="h3 mb-4 text-gray-800">Gestión de Formularios</h1>

                <form id="filtroCategoriaForm" method="get" action="<%=request.getContextPath()%>/coordinador/GestionFormServlet" class="mb-3">
                    <div class="form-group">
                        <label for="idCategoria">Filtrar por categoría:</label>
                        <select name="idCategoria" id="idCategoria" class="form-control" onchange="document.getElementById('filtroCategoriaForm').submit()">
                            <option value="0" <%= selectedCategoria == 0 ? "selected" : "" %>>Todas las categorías</option>
                            <% if (categorias != null) { for (Categoria c : categorias) { %>
                            <option value="<%= c.getIdCategoria() %>" <%= selectedCategoria == c.getIdCategoria() ? "selected" : "" %>><%= c.getNombre() %></option>
                            <% } } %>
                        </select>
                    </div>
                </form>

            <%-- Mensajes de éxito/advertencia/error --%>
                <%
                    String msg = request.getParameter("msg");
                    String type = request.getParameter("type"); // 'success', 'warning', 'danger'
                    String alertClass = "alert-info";

                    if ("success".equals(type)) {
                        alertClass = "alert-success";
                    } else if ("warning".equals(type)) {
                        alertClass = "alert-warning";
                    } else if ("danger".equals(type) || "error".equals(type)) {
                        alertClass = "alert-danger";
                    }
                %>
                <% if (msg != null && !msg.trim().isEmpty()) { %>
                <div class="alert <%= alertClass %> alert-dismissible fade show" role="alert">
                    <%= msg %>
                    <button type="button" class="close" data-dismiss="alert" aria-label="Cerrar">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <% } %>

                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Formularios</h6>
                    </div>
                    <div class="card-body px-3">
                        <div class="table-responsive">
                            <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                <thead class="thead-light">
                                <tr>
                                    <th>Nombre de Formulario</th>
                                    <th>Registros Completados</th>
                                    <th>Fecha creación</th>
                                    <th>Fecha límite</th>
                                    <th>Estado</th>
                                    <th>Acciones</th>
                                </tr>
                                </thead>
                                <tbody>
                                <% for (FormularioDto formularioDto : lista) { %>
                                <tr>
                                    <td><%= formularioDto.getNombreForm() %></td>
                                    <td><%= formularioDto.getNRegCompletados() %></td>
                                    <td><%= formularioDto.getFechaCreacion() %></td>
                                    <td><%= formularioDto.getFechaLimite() %></td>
                                    <td>
                                        <%-- ESTADO: Usar badges como en la tabla de encuestadores --%>
                                        <% if (formularioDto.isEstado()) { %>
                                        <span class="badge badge-success">Activo</span>
                                        <% } else { %>
                                        <span class="badge badge-danger">Inactivo</span>
                                        <% } %>
                                    </td>
                                    <td>
                                        <%-- Dropdown para las acciones --%>
                                        <div class="dropdown">
                                            <button class="btn btn-secondary dropdown-toggle btn-action-dropdown" type="button" id="dropdownMenuButton<%= formularioDto.getId() %>" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                <i class="fas fa-ellipsis-v"></i> <%-- Icono de tres puntos verticales --%>
                                            </button>
                                            <div class="dropdown-menu" aria-labelledby="dropdownMenuButton<%= formularioDto.getId() %>">

                                                <%-- Opción Activar/Desactivar --%>
                                                <a class="dropdown-item" href="#"
                                                   data-toggle="modal"
                                                   data-target="#estadoFormularioModal"
                                                   data-nombre-formulario="<%= formularioDto.getNombreForm() %>"
                                                   data-id-formulario="<%= formularioDto.getId() %>"
                                                   data-action-estado="<%= formularioDto.isEstado() ? "desactivar" : "activar" %>">
                                                    <% if (formularioDto.isEstado()) { %>
                                                    <i class="fas fa-ban"></i> Desactivar
                                                    <% } else { %>
                                                    <i class="fas fa-check-circle"></i> Activar
                                                    <% } %>
                                                </a>

                                                <%-- Opción Crear Respuesta --%>
                                                <a class="dropdown-item" href="<%=request.getContextPath()%>/coordinador/ServletA?action=guardar&id_asig=<%= formularioDto.getIdEncHasFormulario() %>&id_form=<%= formularioDto.getId() %>">
                                                    <i class="fas fa-plus-circle"></i> Crear Respuesta
                                                </a>

                                                <%-- Opción Subir Respuestas (AHORA ACTIVA EL MODAL) --%>
                                                <a class="dropdown-item" href="#"
                                                   data-toggle="modal"
                                                   data-target="#subirRespuestasModal"
                                                   data-nombre-formulario="<%= formularioDto.getNombreForm() %>"
                                                   data-id-formulario="<%= formularioDto.getId() %>"
                                                   data-id-ehf="<%= formularioDto.getIdEncHasFormulario() %>">
                                                    <i class="fas fa-upload"></i> Subir Registros
                                                </a>

                                                <div class="dropdown-divider"></div>
                                                <%if (!formularioDto.isEstado()) { %>
                                                    <%-- Opción Eliminar (ACTIVA EL MODAL) --%>
                                                    <a class="dropdown-item" href="#"
                                                       data-toggle="modal"
                                                       data-target="#eliminarFormularioModal"
                                                       data-nombre-formulario="<%= formularioDto.getNombreForm() %>"
                                                       data-id-formulario="<%= formularioDto.getId() %>">
                                                        <i class="fas fa-trash-alt"></i> Eliminar
                                                    </a>
                                                <% } %>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <% } %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <%-- Incluye el pie de página, que ahora contiene todos los scripts JavaScript y los modales principales --%>
        <jsp:include page="../footer.jsp" />
    </div>
</div>

<%-- Ancla para ir arriba --%>
<a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
</a>

<%-- Modal para Activar/Desactivar Formulario (específico de esta página) --%>
<form id="formEstadoFormulario" action="<%=request.getContextPath()%>/coordinador/GestionFormServlet" method="post">
    <div class="modal fade" id="estadoFormularioModal" tabindex="-1" role="dialog" aria-labelledby="estadoFormularioModalTitle" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="estadoFormularioModalTitle">Cambiar Estado de Formulario</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    ¿Seguro que quieres <span id="estadoFormularioVerb"></span> el formulario <strong id="estadoFormularioName"></strong>?
                    <input type="hidden" name="id" id="estadoFormularioIdInput" />
                    <input type="hidden" name="action" id="estadoFormularioActionInput" />
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
                    <button id="confirmEstadoFormularioBtn" type="submit" class="btn btn-primary">Sí</button>
                </div>
            </div>
        </div>
    </div>
</form>


<%-- Modal para Confirmar Eliminación de Formulario --%>
<%-- Modal para Confirmar Eliminación de Formulario --%>
<form id="formEliminarFormulario" action="<%=request.getContextPath()%>/coordinador/GestionFormServlet" method="post">
    <div class="modal fade" id="eliminarFormularioModal" tabindex="-1" role="dialog" aria-labelledby="eliminarFormularioModalTitle" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="eliminarFormularioModalTitle">Confirmar Eliminación</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    ¿Estás seguro de que deseas eliminar el formulario "<strong id="eliminarFormularioName"></strong>"?
                    Esta acción no se puede deshacer.
                    <input type="hidden" name="id" id="eliminarFormularioIdInput" value=""/>
                    <input type="hidden" name="action" value="eliminar"/>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
                    <button type="submit" class="btn btn-danger">Eliminar</button>
                </div>
            </div>
        </div>
    </div>
</form>

<%-- NUEVO: Modal para Subir Archivos CSV/Excel --%>
<div class="modal fade" id="subirRespuestasModal" tabindex="-1" role="dialog" aria-labelledby="subirRespuestasModalTitle" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="subirRespuestasModalTitle">Subir Respuestas para <strong id="uploadFormularioName"></strong></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form action="<%=request.getContextPath()%>/coordinador/SubirRegistrosServlet" method="post" enctype="multipart/form-data">
                <div class="modal-body">
                    <input type="hidden" name="idFormulario" id="uploadFormularioIdInput"/>
                    <input type="hidden" name="idEhf" id="uploadEhfIdInput"/>
                    <div class="form-group">
                        <label for="csvFile">Selecciona un archivo CSV:</label>
                        <input type="file" class="form-control-file" id="csvFile" name="csvFile" accept=".csv" required>
                        <small class="form-text text-muted">
                            Asegúrate de que el archivo tenga el
                            <a id="templateLink" class="pe-auto" data-base-href="<%=request.getContextPath()%>/coordinador/downloadTemp?file=mass" href="#">formato correcto</a>
                            para las respuestas del formulario.
                        </small>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
                    <button type="submit" class="btn btn-primary">Subir Archivo</button>
                </div>
            </form>
        </div>
    </div>
</div>


<%-- Script personalizado de esta página. DEBE ir AQUÍ, DESPUÉS de la inclusión del footer,
     ya que depende de las librerías JavaScript cargadas en el footer. --%>
<script>
    $(document).ready(function() {
        // Inicializa DataTables. Si 'datatables-demo.js' ya lo hace, puedes ajustar o quitar esta parte.
        // if ($.fn.DataTable.isDataTable('#dataTable')) {
        //     $('#dataTable').DataTable().destroy(); // Destruye cualquier instancia existente
        // }
        $('#dataTable').DataTable({
            "order": [], // Deshabilita el orden inicial si lo deseas, o ajusta según necesites
            "pageLength": 10, // Número de filas por página por defecto
            "language": { // Configuración del idioma
                "url": "//cdn.datatables.net/plug-ins/1.13.4/i18n/es-ES.json" // URL para español
            }
        });

        // Lógica para el modal de Activar/Desactivar formulario
        $('#estadoFormularioModal').on('show.bs.modal', function (event) {
            let button = $(event.relatedTarget);
            let idFormulario = button.data('id-formulario');
            let nombreFormulario = button.data('nombre-formulario');
            let actionEstado = button.data('action-estado');

            let modal = $(this);
            modal.find('#estadoFormularioName').text(nombreFormulario);
            modal.find('#estadoFormularioIdInput').val(idFormulario);
            modal.find('#estadoFormularioActionInput').val(actionEstado);

            if (actionEstado === 'desactivar') {
                modal.find('#estadoFormularioModalTitle').text('Desactivar Formulario');
                modal.find('#estadoFormularioVerb').text('desactivar');
                modal.find('#confirmEstadoFormularioBtn').removeClass('btn-primary').addClass('btn-secondary').text('Sí, Desactivar');
            } else {
                modal.find('#estadoFormularioModalTitle').text('Activar Formulario');
                modal.find('#estadoFormularioVerb').text('activar');
                modal.find('#confirmEstadoFormularioBtn').removeClass('btn-secondary').addClass('btn-primary').text('Sí, Activar');
            }
        });

        // Lógica para el modal de Confirmar Eliminación
        $('#eliminarFormularioModal').on('show.bs.modal', function (event) {
            let button = $(event.relatedTarget);
            let idFormulario = button.data('id-formulario');
            let nombreFormulario = button.data('nombre-formulario');

            let modal = $(this);
            modal.find('#eliminarFormularioName').text(nombreFormulario);
            modal.find('#eliminarFormularioIdInput').val(idFormulario);
        });

        // Envía el formulario oculto de eliminación cuando se confirma en el modal
        $('#confirmEliminarFormularioBtn').click(function() {
            $('#formEliminarFormulario').submit();
        });

        // NUEVO: Lógica para el modal de Subir Respuestas
        $('#subirRespuestasModal').on('show.bs.modal', function (event) {
            let button = $(event.relatedTarget); // Botón que activó el modal
            let idFormulario = button.data('id-formulario');
            let nombreFormulario = button.data('nombre-formulario');
            let idEhf = button.data('id-ehf');
            // console.log("el id de asignacion ehf es: ", idEhf);

            let modal = $(this);
            // Establece el nombre del formulario en el título del modal
            modal.find('#uploadFormularioName').text(nombreFormulario);
            // Pasa el ID del formulario al campo oculto del formulario dentro del modal
            modal.find('#uploadFormularioIdInput').val(idFormulario);
            // Pasa el ID de la asignación al campo oculto del formulario dentro del modal
            modal.find('#uploadEhfIdInput').val(idEhf);
            // Opcional: Limpiar el campo de archivo si se ha seleccionado uno previamente
            modal.find('#csvFile').val('');
            // Obtener la forma del link para plantilla según ID del formulario
            const link = modal.find("#templateLink");
            const base = link.attr("data-base-href");
            if (!base) {
                console.warn("No se encontró data-base-href en #templateLink");
                return;
            }
            //link.href = base+"&form="+encodeURIComponent(idFormulario);
            link.attr("href", base + "&form=" + encodeURIComponent(idFormulario));
        });
    });
</script>

</body>
</html>

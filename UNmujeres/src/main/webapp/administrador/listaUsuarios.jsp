<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.unmujeres.dtos.UsuarioDto" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<%
    ArrayList<UsuarioDto> lista = (ArrayList<UsuarioDto>)
            request.getAttribute("lista");
%>

<!DOCTYPE html>
<head>
    <jsp:include page="../header.jsp" />
    <title>Lista de Usuarios - ONU Mujeres</title>
    <style>
        <%-- Aquí puedes incluir tu CSS interno (el mismo que ya tenías para filtros) --%>
    </style>
</head>
<body id="page-top">
<div id="wrapper">

    <jsp:include page="../sidebarAdmin.jsp" />

    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">

            <jsp:include page="../topbarAdmin.jsp" />

            <div class="container-fluid">
                <h1 class="h3 mb-4 text-gray-800">Lista de Usuarios</h1>

                <div class="card shadow mb-4">
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                <thead>
                                <tr>
                                    <th>Nombre</th>
                                    <th>Apellido</th>
                                    <th>DNI</th>
                                    <th>Correo</th>
                                    <th>Rol</th>
                                    <th>Zona</th>
                                    <th>Estado</th>
                                </tr>
                                </thead>
                                <tbody>
                                <% for (UsuarioDto usuarioDto : lista) { %>
                                <tr>
                                    <td><%= usuarioDto.getNombres() %></td>
                                    <td><%= usuarioDto.getApellidos() %></td>
                                    <td><%= usuarioDto.getDni() %></td>
                                    <td><%= usuarioDto.getCorreo() %></td>
                                    <td><%= usuarioDto.getNombreRol() %></td>
                                    <td><%= usuarioDto.getNombreZona() %></td>
                                    <td>
                                        <form action="<%=request.getContextPath()%>/administrador/UsuarioServlet" method="post">
                                        <input type="hidden" name="id" value="<%= usuarioDto.getId() %>">
                                            <input type="hidden" name="estado" value="<%= usuarioDto.isEstado() ? "true" : "false" %>">
                                            <button type="submit" class="btn <%= usuarioDto.isEstado() ? "btn-danger" : "btn-success" %>">
                                                <%= usuarioDto.isEstado() ? "Desactivar" : "Activar" %>
                                            </button>
                                        </form>
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
        <jsp:include page="../footer.jsp" />
    </div>
</div>
<a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
</a>
<script>
    // Inicializa DataTables. Si 'datatables-demo.js' ya lo hace, puedes ajustar o quitar esta parte.
    if ($.fn.DataTable.isDataTable('#dataTable')) {
        $('#dataTable').DataTable().destroy(); // Destruye cualquier instancia existente
    }
    $('#dataTable').DataTable({
        "order": [], // Deshabilita el orden inicial si lo deseas, o ajusta según necesites
        "pageLength": 10, // Número de filas por página por defecto
        "language": { // Configuración del idioma
            "url": "//cdn.datatables.net/plug-ins/1.13.4/i18n/es-ES.json" // URL para español
        }
    });
</script>
</body>

</html>

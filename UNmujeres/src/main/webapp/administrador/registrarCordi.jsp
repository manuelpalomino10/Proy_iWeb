<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>


<!DOCTYPE html>
<html lang="es">
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

            <jsp:include page="../topbarEnc.jsp" />

            <div class="container-fluid">
                <h1 class="h3 mb-4 text-gray-800">Registrar Coordinador interno</h1>

                <div class="card shadow mb-4">
                    <div class="card-body">

                        <div class="search-container">
                            <div class="filter-group">
                                <form method="post" action="<%=request.getContextPath()%>/CrearCordiServlet">
                                    <div class="mb-3">
                                        <labe>Nombres</labe>
                                        <input type="text" class="form-control" name="nombres">
                                    </div>
                                    <div class="mb-3">
                                        <label>Apellidos</label>
                                        <input type="text" class="form-control" name="apellidos">
                                    </div>
                                    <div class="mb-3">
                                        <label>DNI</label>
                                        <input type="text" class="form-control" name="DNI">
                                    </div>
                                    <div class="mb-3">
                                        <label>Correo</label>
                                        <input type="text" class="form-control" name="correo">
                                    </div>
                                    <div class="mb-3">
                                        <label for="idzona">Zona</label>
                                        <select name="idZona" id="idzona" class="form-control" required>
                                            <c:forEach var="zona" items="${listaZonas}">
                                                <option value="${zona.idzona}">${zona.nombre}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <button type="submit" class="btn btn-primary">Registrar</button>
                                </form>
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
</body>
</html>

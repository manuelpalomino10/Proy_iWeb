<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Definir Contraseña</title>
    <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/sb-admin-2.min.css" rel="stylesheet">
</head>
<body class="bg-gradient-primary">
<div class="container">
    <div class="card o-hidden border-0 shadow-lg my-5">
        <div class="card-body p-5">
            <div class="text-center">
                <h1 class="h4 text-gray-900 mb-4">Define tu contraseña</h1>
            </div>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            <form action="${pageContext.request.contextPath}/encuestador/confirm" method="post" class="user">
                <!-- Código de validación oculto -->
                <input type="hidden" name="code" value="${param.code}" />

                <div class="form-group">
                    <input name="password" type="password" class="form-control form-control-user"
                           placeholder="Nueva contraseña" required>
                </div>
                <div class="form-group">
                    <input name="confirmPassword" type="password" class="form-control form-control-user"
                           placeholder="Reingresa contraseña" required>
                </div>
                <button type="submit" class="btn btn-primary btn-user btn-block">
                    Guardar Contraseña
                </button>
            </form>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/jquery-easing/jquery.easing.min.js"></script>
<script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>
</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: tteff
  Date: 7/2/2025
  Time: 8:02 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="utf-8">
    <title>Token Expirado</title>
    <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/css/sb-admin-2.min.css" rel="stylesheet">
</head>
<body id="page-top">
<div class="container-fluid mt-5">
    <div class="text-center">
        <h1 class="h3 mb-4 text-gray-800">Token expirado</h1>
        <p>El enlace de activación ha caducado. Solicite uno nuevo.</p>
        <a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-user btn-block" style="max-width:320px; margin:0 auto;">
            Volver al registro
        </a>
    </div>
</div>
<script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/jquery-easing/jquery.easing.min.js"></script>
<script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>
</body>
</html>

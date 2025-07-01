<%--
  Created by IntelliJ IDEA.
  User: tteff
  Date: 7/1/2025
  Time: 9:26 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Verificar correo</title>
    <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/css/sb-admin-2.min.css" rel="stylesheet"/>
</head>
<body class="bg-gradient-primary">
<div class="container">
    <div class="card o-hidden border-0 shadow-lg my-5">
        <div class="card-body p-5">
            <div class="text-center mb-4">
                <h1 class="h4 text-gray-900 mb-4">Activar Cuenta</h1>
            </div>
            <form method="post" action="${pageContext.request.contextPath}/verify" class="user">
                <input type="hidden" name="code" value="${code}"/>
                <div class="form-group">
                    <label for="password">Contraseña</label>
                    <input id="password" name="password" type="password" class="form-control form-control-user" required>
                </div>
                <div class="form-group">
                    <label for="confirm">Confirmar Contraseña</label>
                    <input id="confirm" name="confirm" type="password" class="form-control form-control-user" required>
                </div>
                <ul style="list-style:none; padding-left:0;">
                    <c:set var="reqs" value="${requisitosPwd}"/>
                    <li style="color:${reqs['len'] ? '#388e3c' : '#d32f2f'}; font-size:12px;">Mínimo 8 caracteres</li>
                    <li style="color:${reqs['may'] ? '#388e3c' : '#d32f2f'}; font-size:12px;">Al menos una mayúscula</li>
                    <li style="color:${reqs['min'] ? '#388e3c' : '#d32f2f'}; font-size:12px;">Al menos una minúscula</li>
                    <li style="color:${reqs['num'] ? '#388e3c' : '#d32f2f'}; font-size:12px;">Al menos un número</li>
                    <li style="color:${reqs['spec'] ? '#388e3c' : '#d32f2f'}; font-size:12px;">Al menos un carácter especial</li>
                </ul>
                <c:if test="${errores['password'] != null}">
                    <div class="text-danger mb-3">${errores['password']}</div>
                </c:if>
                <button type="submit" class="btn btn-primary btn-user btn-block">Activar</button>
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
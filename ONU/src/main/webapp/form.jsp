<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
  <title>Registro de Encuestador</title>
  <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css"/>
  <link href="${pageContext.request.contextPath}/css/sb-admin-2.min.css" rel="stylesheet"/>
  <style>
    .required-label {font-weight: bold;}
    .required-asterisk {color: red; margin-left: 3px;}
    .campo-error {color: red; font-size: 13px;}
    .nota-password {font-size: 13px; color: #333; margin-top: 2px;}
    .titulo-form {font-size: 2.2rem; font-weight: bold; margin-bottom: 25px;}
  </style>
</head>
<body class="bg-gradient-primary">
<div class="container">
  <div class="card o-hidden border-0 shadow-lg my-5">
    <div class="card-body p-5">
      <div class="text-center mb-4">
        <div class="titulo-form text-gray-900">Registro de Encuestador</div>
      </div>
      <form method="post" action="${pageContext.request.contextPath}/encuestador/register" class="user">

        <div class="form-group row">
          <div class="col-sm-6 mb-3 mb-sm-0">
            <label for="nombre" class="required-label">
              Nombre
              <c:if test="${errores != null && errores['nombre'] != null}">
                <span class="required-asterisk">*</span>
              </c:if>
            </label>
            <input id="nombre" name="nombre" type="text" class="form-control form-control-user"
                   value="${nombre != null ? nombre : ''}" required/>
            <c:if test="${errores != null && errores['nombre'] != null}">
              <div class="campo-error">${errores['nombre']}</div>
            </c:if>
          </div>
          <div class="col-sm-6">
            <label for="apellido" class="required-label">
              Apellido
              <c:if test="${errores != null && errores['apellido'] != null}">
                <span class="required-asterisk">*</span>
              </c:if>
            </label>
            <input id="apellido" name="apellido" type="text" class="form-control form-control-user"
                   value="${apellido != null ? apellido : ''}" required/>
            <c:if test="${errores != null && errores['apellido'] != null}">
              <div class="campo-error">${errores['apellido']}</div>
            </c:if>
          </div>
        </div>

        <div class="form-group">
          <label for="dni" class="required-label">
            DNI <span style="font-weight: normal; color: #888;">(8 dígitos)</span>
            <c:if test="${errores != null && errores['dni'] != null}">
              <span class="required-asterisk">*</span>
            </c:if>
          </label>
          <input id="dni" name="dni" type="text" class="form-control form-control-user"
                 maxlength="8" value="${dni != null ? dni : ''}" required/>
          <c:if test="${errores != null && errores['dni'] != null}">
            <div class="campo-error">${errores['dni']}</div>
          </c:if>
        </div>

        <div class="form-group">
          <label for="direccion" class="required-label">
            Dirección
            <c:if test="${errores != null && errores['direccion'] != null}">
              <span class="required-asterisk">*</span>
            </c:if>
          </label>
          <input id="direccion" name="direccion" type="text" class="form-control form-control-user"
                 value="${direccion != null ? direccion : ''}" required/>
          <c:if test="${errores != null && errores['direccion'] != null}">
            <div class="campo-error">${errores['direccion']}</div>
          </c:if>
        </div>

        <div class="form-group">
          <label for="distrito" class="required-label">
            Distrito
            <c:if test="${errores != null && errores['distrito'] != null}">
              <span class="required-asterisk">*</span>
            </c:if>
          </label>
          <input id="distrito" name="distrito" type="text" class="form-control form-control-user"
                 value="${distrito != null ? distrito : ''}" required/>
          <c:if test="${errores != null && errores['distrito'] != null}">
            <div class="campo-error">${errores['distrito']}</div>
          </c:if>
        </div>

        <div class="form-group">
          <label for="correo" class="required-label">
            Correo electrónico <span style="font-weight: normal; color: #888;">(ej: usuario@email.com)</span>
            <c:if test="${errores != null && errores['correo'] != null}">
              <span class="required-asterisk">*</span>
            </c:if>
          </label>
          <input id="correo" name="correo" type="email" class="form-control form-control-user"
                 value="${correo != null ? correo : ''}" required/>
          <c:if test="${errores != null && errores['correo'] != null}">
            <div class="campo-error">${errores['correo']}</div>
          </c:if>
        </div>

        <div class="form-group">
          <label for="password" class="required-label">
            Contraseña
            <c:if test="${errores != null && errores['password'] != null}">
              <span class="required-asterisk">*</span>
            </c:if>
          </label>
          <input id="password" name="password" type="password" class="form-control form-control-user" required/>
          <div class="nota-password">
            Debe ingresar una contraseña que contenga al menos 8 caracteres, incluyendo al menos una letra mayúscula, una letra minúscula, un número y un carácter especial.
          </div>
          <c:if test="${errores != null && errores['password'] != null}">
            <div class="campo-error">${errores['password']}</div>
          </c:if>
        </div>

        <button type="submit" class="btn btn-primary btn-user btn-block" style="font-size: 1.1rem;">
          Registrarme
        </button>
      </form>
    </div>
  </div>
</div>
<!-- Scripts comunes -->
<script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/jquery-easing/jquery.easing.min.js"></script>
<script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>
</body>
</html>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Registro de Encuestador</title>
  <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
  <link href="${pageContext.request.contextPath}/css/sb-admin-2.min.css" rel="stylesheet">
</head>
<body class="bg-gradient-primary">
  <div class="container">
    <div class="card o-hidden border-0 shadow-lg my-5">
      <div class="card-body p-0">
        <div class="row">
          <div class="col-lg-5 d-none d-lg-block bg-register-image"></div>
          <div class="col-lg-7">
            <div class="p-5">
              <div class="text-center">
                <h1 class="h4 text-gray-900 mb-4">Registro de Encuestador</h1>
              </div>
              <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
              </c:if>
              <form class="user"
                    action="${pageContext.request.contextPath}/encuestador/register"
                    method="post">
                <div class="form-group row">
                  <div class="col-sm-6 mb-3 mb-sm-0">
                    <input name="nombre" type="text"
                           class="form-control form-control-user"
                           placeholder="Nombre" required>
                  </div>
                  <div class="col-sm-6">
                    <input name="apellido" type="text"
                           class="form-control form-control-user"
                           placeholder="Apellido" required>
                  </div>
                </div>
                <div class="form-group">
                  <input name="dni" type="text"
                         class="form-control form-control-user"
                         placeholder="DNI (8 dígitos)" maxlength="8" required>
                </div>
                <div class="form-group">
                  <input name="direccion" type="text"
                         class="form-control form-control-user"
                         placeholder="Dirección" required>
                </div>
                <div class="form-group">
                  <input name="distrito" type="text"
                         class="form-control form-control-user"
                         placeholder="Distrito" required>
                </div>
                <div class="form-group">
                  <input name="correo" type="email"
                         class="form-control form-control-user"
                         placeholder="Correo electrónico" required>
                </div>
                <div class="form-group">
                  <input name="password" type="password"
                         class="form-control form-control-user"
                         placeholder="Contraseña (mín. 8 chars, 1 mayúscula, 1 número, 1 especial)"
                         required>
                </div>
                <button type="submit"
                        class="btn btn-primary btn-user btn-block">
                  Registrar Encuestador
                </button>
              </form>
              <hr>
              <div class="text-center">
                <a class="small"
                   href="${pageContext.request.contextPath}/login">
                  ¿Ya tienes cuenta? Inicia sesión
                </a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
  <script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script src="${pageContext.request.contextPath}/vendor/jquery-easing/jquery.easing.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>
</body>
</html>

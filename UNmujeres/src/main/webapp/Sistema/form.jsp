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
    .required-label { font-weight: bold; }
    .required-asterisk { color: red; margin-left: 3px; }
    .campo-error { color: red; font-size: 13px; }
    .nota-password { font-size: 13px; color: #333; margin-top: 2px; }
    .titulo-form { font-size: 2.2rem; font-weight: bold; margin-bottom: 25px; }
  </style>
</head>
<body class="bg-gradient-primary">
<div class="container">
  <div class="card o-hidden border-0 shadow-lg my-5">
    <div class="card-body p-5">
      <div class="text-center mb-4">
        <div class="titulo-form text-gray-900">Registro de Encuestador</div>
      </div>

      <form method="post" action="${pageContext.request.contextPath}/register" class="user">

        <!-- Nombre / Apellido -->
        <div class="form-group row">
          <div class="col-sm-6 mb-3 mb-sm-0">
            <label for="nombre" class="required-label">
              Nombre
              <c:if test="${errores['nombre'] != null}">
                <span class="required-asterisk">*</span>
              </c:if>
            </label>
            <input id="nombre" name="nombre" type="text" class="form-control form-control-user ${errores['nombre'] != null ? "is-invalid" : ''}"
                   value="${param.nombre}" required/>
            <c:if test="${errores['nombre'] != null}">
              <div class="invalid-feedback">${errores['nombre']}</div>
            </c:if>
          </div>
          <div class="col-sm-6">
            <label for="apellido" class="required-label">
              Apellido
              <c:if test="${errores['apellido'] != null}">
                <span class="required-asterisk">*</span>
              </c:if>
            </label>
            <input id="apellido" name="apellido" type="text" class="form-control form-control-user ${errores['apellido'] != null ? "is-invalid" : ''}"
                   value="${param.apellido}" required/>
            <c:if test="${errores['apellido'] != null}">
              <div class="invalid-feedback">${errores['apellido']}</div>
            </c:if>
          </div>
        </div>

        <!-- DNI -->
        <div class="form-group">
          <label for="dni" class="required-label">
            DNI <span style="font-weight: normal; color: #888;">(8 dígitos)</span>
            <c:if test="${errores['dni'] != null}">
              <span class="required-asterisk">*</span>
            </c:if>
          </label>
          <input id="dni" name="dni" type="text" class="form-control form-control-user ${errores['dni'] != null ? "is-invalid" : ''}"
                 maxlength="8" value="${param.dni}" required/>
          <c:if test="${errores['dni'] != null}">
            <div class="invalid-feedback">${errores['dni']}</div>
          </c:if>
        </div>

        <!-- Dirección -->
        <div class="form-group">
          <label for="direccion" class="required-label">
            Dirección
            <c:if test="${errores['direccion'] != null}">
              <span class="required-asterisk">*</span>
            </c:if>
          </label>
          <input id="direccion" name="direccion" type="text" class="form-control form-control-user ${errores['direccion'] != null ? "is-invalid" : ''}"
                 value="${param.direccion}" required/>
          <c:if test="${errores['direccion'] != null}">
            <div class="invalid-feedback">${errores['direccion']}</div>
          </c:if>
        </div>

        <!-- Zona y Distrito -->
        <div class="form-group row">
          <!-- Zona -->
          <div class="col-sm-6 mb-3 mb-sm-0">
            <label for="zona" class="required-label">
              Zona
              <c:if test="${errores['zona'] != null}">
                <span class="required-asterisk">*</span>
              </c:if>
            </label>
            <select id="zona" name="zona" class="form-control ${errores['zona'] != null ? "is-invalid" : ''}" required>
              <option value="" disabled selected>— seleccione zona —</option>
              <c:forEach var="z" items="${listaZonas}">
                <option value="${z.idzona}"
                        <c:if test="${param.zona eq z.idzona}">selected</c:if>>
                    ${z.nombre}
                </option>
              </c:forEach>
            </select>
            <c:if test="${errores['zona'] != null}">
              <div class="invalid-feedback">${errores['zona']}</div>
            </c:if>
          </div>

          <!-- Distrito -->
          <div class="col-sm-6">
            <label for="distrito" class="required-label">
              Distrito
              <c:if test="${errores['distrito'] != null}">
                <span class="required-asterisk">*</span>
              </c:if>
            </label>
            <select id="distrito" name="distrito" class="form-control ${errores['distrito'] != null ? "is-invalid" : ''}" required>
              <option value="" disabled selected>— seleccione distrito —</option>
              <c:forEach var="d" items="${listaDistritos}">
                <c:set var="distritoVal" value="${d.iddistritos}-${d.zona_idzona}" />
                <option value="${distritoVal}"
                        data-zona="${d.zona_idzona}"
                        <c:if test="${param.distrito eq distritoVal}">selected</c:if>>
                    ${d.nombre}
                </option>
              </c:forEach>
            </select>
            <c:if test="${errores['distrito'] != null}">
              <div class="invalid-feedback">${errores['distrito']}</div>
            </c:if>
          </div>
        </div>

        <!-- Correo -->
        <div class="form-group">
          <label for="correo" class="required-label">
            Correo electrónico
            <c:if test="${errores['correo'] != null}">
              <span class="required-asterisk">*</span>
            </c:if>
          </label>
          <input id="correo" name="correo" type="email" class="form-control form-control-user ${errores['correo'] != null ? "is-invalid" : ''}"
                 value="${param.correo}" required/>
          <c:if test="${errores['correo'] != null}">
            <div class="invalid-feedback">${errores['correo']}</div>
          </c:if>
        </div>

        <button type="submit" class="btn btn-primary btn-user btn-block" style="font-size:1.1rem;">
          Registrarme
        </button>
      </form>
    </div>
  </div>
</div>

<!-- JS para filtrar distritos según zona -->
<script>
  document.addEventListener('DOMContentLoaded', () => {
    const zonaSelect = document.getElementById('zona');
    const distSelect = document.getElementById('distrito');
    // guardamos todas las opciones de distrito
    const todasOpciones = Array.from(distSelect.querySelectorAll('option[data-zona]'));

    function filtrar() {
      const zonaId = zonaSelect.value;
      distSelect.innerHTML = '<option value="">— seleccione distrito —</option>';
      if (!zonaId) return;
      todasOpciones.forEach(opt => {
        if (opt.getAttribute('data-zona') === zonaId) {
          distSelect.appendChild(opt.cloneNode(true));
        }
      });
    }

    zonaSelect.addEventListener('change', filtrar);
    // si venimos de repost con una zona ya seleccionada
    if (zonaSelect.value) filtrar();
  });
</script>
</body>
</html>

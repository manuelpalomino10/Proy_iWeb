<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  long now = System.currentTimeMillis();
  pageContext.setAttribute("now", now);
%>
<!-- Top bar -->
<nav class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">

  <!-- Sidebar Toggle (Topbar) -->
  <button id="sidebarToggleTop" class="btn btn-link d-md-none rounded-circle mr-3">
    <i class="fa fa-bars"></i>
  </button>

  <!-- Top bar Navbar -->
  <ul class="navbar-nav ml-auto">


    <div class="topbar-divider d-none d-sm-block"></div>

    <!-- Nav Item - User Information -->

    <li class="nav-item dropdown no-arrow">
      <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button"
         data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                <span class="mr-2 d-none d-md-inline text-gray-600 small">
            Hola, ${usuario.nombres}
            <c:choose>
                <c:when test="${usuario.idroles == 1}"> - Administrador</c:when>
                <c:when test="${usuario.idroles == 2}"> - Coordinador Interno</c:when>
                <c:when test="${usuario.idroles == 3}"> - Encuestador</c:when>
            </c:choose>
        </span>
        <c:choose>
          <c:when test="${not empty sessionScope.fotoBase64}">
            <img class="img-profile rounded-circle"
                 src="data:image/jpeg;base64,${sessionScope.fotoBase64}"
                 style="width: 2.5rem; height: 2.5rem; object-fit: cover; border-radius: 50%;">
          </c:when>
          <c:otherwise>
            <img class="img-profile rounded-circle"
                 src="${pageContext.request.contextPath}/img/perfil-del-usuario.png"
                 style="width: 2.5rem; height: 2.5rem; object-fit: cover; border-radius: 50%;">
          </c:otherwise>
        </c:choose>
      </a>
      <!-- Dropdown - User Information -->
      <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in"
           aria-labelledby="userDropdown">
        <a class="dropdown-item" href="${pageContext.request.contextPath}/encuestador/perfil">
          <i class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>
          Perfil
        </a>


        <div class="dropdown-divider"></div>
        <a class="dropdown-item" href="${pageContext.request.contextPath}/logout">
          <i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
          Cerrar Sesión
        </a>
      </div>
    </li>

  </ul>

</nav>
<!-- End of Top bar -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
        <span class="mr-2 d-none d-lg-inline text-gray-600 small">Hola ${usuario.nombres}</span>
        <c:choose>
          <c:when test="${not empty usuario.fotoBytes}">
            <img class="img-profile rounded-circle"
                 src="data:image/jpeg;base64,${fotoBase64}" alt="Foto de perfil">
          </c:when>
          <c:otherwise>
            <img class="img-profile rounded-circle"
                 src="img/perfil-del-usuario.png" alt="Foto de perfil">
          </c:otherwise>
        </c:choose>
      </a>
      <!-- Dropdown - User Information -->
      <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in"
           aria-labelledby="userDropdown">
        <a class="dropdown-item" href="perfil">
          <i class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>
          Perfil
        </a>


        <div class="dropdown-divider"></div>
        <a class="dropdown-item" href="login">
          <i class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>
          Cerrar Sesión
        </a>
      </div>
    </li>

  </ul>

</nav>
<!-- End of Top bar -->

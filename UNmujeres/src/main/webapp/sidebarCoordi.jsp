<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

  <!-- Sidebar - Brand -->
  <a class="sidebar-brand d-flex align-items-center justify-content-center" href="${pageContext.request.contextPath}/dashboard">
    <div class="sidebar-brand-icon">
      <img src="${pageContext.request.contextPath}/img/ONU.png" alt="Logo ONU Mujeres" style="max-width: 100%;">
    </div>
  </a>

  <!-- Divider -->
  <hr class="sidebar-divider my-0">

  <!-- Nav Item - Dashboard -->
  <li class="nav-item ${pageAct eq 'dashboard' ? 'active' : ''}">
    <a class="nav-link" href="${pageContext.request.contextPath}/dashboard">
      <i class="fas fa-fw fa-tachometer-alt"></i>
      <span>Resumen</span></a>
  </li>




  <!-- Divider -->
  <hr class="sidebar-divider">



  <!-- Heading -->
  <div class="sidebar-heading">FUNCIONES</div>

  <!-- Gestión de Encuestadores -->Add commentMore actions
  <li class="nav-item ${pageAct == 'encuestadores' ? 'active' : ''}">
    <a class="nav-link" href="${pageContext.request.contextPath}/gestion_encuestadores">
      <i class="fas fa-fw fa-users"></i>
      <span>Gestión de Encuestadores</span>
    </a>
    <div id="collapseTwo" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
      <div class="bg-white py-2 collapse-inner rounded">
        <h6 class="collapse-header">Custom Components:</h6>
        <a class="collapse-item" href="buttons.html">Buttons</a>
        <a class="collapse-item" href="cards.html">Cards</a>
      </div>
    </div>
  </li>
  <li class="nav-item">
    <a class="nav-link" href="${pageContext.request.contextPath}/GestionFormServlet">
      <i class="fas fa-fw fa-edit"></i>
      <span>Gestion de Formularios</span>
    </a>
  </li>
  <li class="nav-item">
    <a class="nav-link" href="SubirRegistrosServlet">
      <i class="fas fa-fw fa-edit"></i>
      <span>Formularios</span>
    </a>
  </li>
  <hr class="sidebar-divider d-none d-md-block">
  <div class="text-center d-none d-md-inline">
    <button class="rounded-circle border-0" id="sidebarToggle"></button>
  </div>
</ul>

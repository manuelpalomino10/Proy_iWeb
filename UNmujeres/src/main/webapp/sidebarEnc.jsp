<%--
  Created by IntelliJ IDEA.
  User: Luis
  Date: 13/05/2025
  Time: 00:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- Sidebar -->
<ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

  <!-- Sidebar - Brand -->
  <a class="sidebar-brand d-flex align-items-center justify-content-center" href="dashboard">
    <div class="sidebar-brand-icon">
      <img src="img/ONU.png" alt="Logo ONU Mujeres" style="max-width: 100%;">
    </div>

  </a>

  <!-- Divider -->
  <hr class="sidebar-divider my-0">

  <!-- Nav Item - Dashboard -->
  <li class="nav-item ${pageAct eq 'dashboard' ? 'active' : ''}">
    <a class="nav-link" href="dashboard">
      <i class="fas fa-fw fa-tachometer-alt"></i>
      <span>Resumen</span></a>
  </li>

  <!-- Divider -->
  <hr class="sidebar-divider">

  <!-- Heading -->
  <div class="sidebar-heading">
    Funciones
  </div>

  <!-- Nav Item - Nueva Respuesta -->

  <!-- Nav Item - Ver Forms Asignados -->
  <li class="nav-item ${pageAct eq 'asignados' ? 'active' : ''}">
    <a class="nav-link" href="ServletA?action=lista">
      <i class="fas fa-fw fa-table"></i>
      <span>Formularios Asignados</span></a>
  </li>

  <!-- Historial Collapse Menu -->
  <li class="nav-item ${pageAct eq 'historial' ? 'active' : ''}">
    <a class="nav-link" href="ServletA?action=historial#borradoresSection">
      <i class="fas fa-fw fa-table"></i>
      <span>Historial de Formularios</span></a>
  </li>

  <!-- Divider -->
  <hr class="sidebar-divider">

  <!-- Heading -->

  <!-- Sidebar Toggler (Sidebar) -->
  <div class="text-center d-none d-md-inline">
    <button class="rounded-circle border-0" id="sidebarToggle"></button>
  </div>

  <!-- Sidebar Message -->
  <div class="sidebar-card d-none d-lg-flex">
    <p class="text-center mb-2"><strong>ONU</strong></p>
    <a class="btn btn-success btn-sm mb-2" href="https://www.unwomen.org/es">Contacto</a>
  </div>

</ul>
<!-- End of Sidebar -->
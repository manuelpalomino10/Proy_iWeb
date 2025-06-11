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




    <!-- Nav Item - Dashboard -->
    <li class="nav-item ${pageAct eq 'dashboard' ? 'active' : ''}">
        <a class="nav-link" href="${pageContext.request.contextPath}/DashboardCuidado">
            <i class="fas fa-fw fa-tachometer-alt"></i>
            <span>Estadisticas Repuestas</span></a>
    </li>


    <!-- Divider -->
    <hr class="sidebar-divider">



    <!-- Heading -->
    <div class="sidebar-heading">
        GESTIÓN
    </div>
    <li class="nav-item">
        <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseUsuarios"
           aria-expanded="true" aria-controls="collapseUsuarios">
            <i class="fas fa-fw fa-user-cog"></i>
            <span>Gestión de Usuarios</span>
        </a>
        <div id="collapseUsuarios" class="collapse" aria-labelledby="headingUsuarios" data-parent="#accordionSidebar">
            <div class="bg-white py-2 collapse-inner rounded text-dark">
                <a class="collapse-item" href="${pageContext.request.contextPath}/CrearCordiServlet">
                    Crear Coordinador
                </a>
                <a class="collapse-item" href="${pageContext.request.contextPath}/UsuarioServlet">
                    Lista de Usuarios
                </a>
            </div>
        </div>
    </li>
    <li class="nav-item ${pageAct eq 'historial' ? 'active' : ''}">
        <a class="nav-link" href="${pageContext.request.contextPath}/ReportesServlet">
            <i class="fas fa-fw fa-table"></i>
            <span>Generar Reportes</span>
        </a>
    </li>
    <hr class="sidebar-divider d-none d-md-block">
    <div class="text-center d-none d-md-inline">
        <button class="rounded-circle border-0" id="sidebarToggle"></button>
    </div>
</ul>


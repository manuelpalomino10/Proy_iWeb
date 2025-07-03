<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Restablecer Contraseña | ONU Mujeres</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .bg-gradient-primary {
            background: linear-gradient(135deg, #4e73df 0%, #224abe 100%);
        }
        .card {
            border: none;
            border-radius: 1rem;
            box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
            max-width: 900px;
        }
        .card-header {
            border-radius: 1rem 1rem 0 0 !important;
            background: linear-gradient(135deg, #4e73df 0%, #224abe 100%);
            color: white;
        }
        .btn-primary {
            background-color: #4e73df;
            border-color: #4e73df;
        }
        .btn-primary:hover {
            background-color: #224abe;
            border-color: #224abe;
        }
        body {
            background-color: #f8f9fc;
        }
        .requirement-list {
            list-style: none;
            padding-left: 0;
            margin: 8px 0 0 0;
        }
        .requirement-list li {
            font-size: 12.5px;
            margin-bottom: 0;
        }
        .progress {
            height: 5px;
        }
        .alert {
            border-radius: 0.5rem;
        }
        .btn-outline-danger {
            border-radius: 0.5rem;
        }
    </style>
</head>
<body class="bg-gray-100">
<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-xl-8 col-lg-10">
            <div class="card shadow-lg my-5">
                <div class="card-header py-4 text-center">
                    <h3 class="m-0">
                        <i class="fas fa-key me-2"></i> Restablecer Contraseña
                    </h3>
                </div>

                <div class="card-body p-5">
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show">
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                ${error}
                            <c:if test="${error.contains('enlace')}">
                                <div class="mt-2">
                                    <a href="${pageContext.request.contextPath}/forgot-password" class="btn btn-sm btn-outline-danger">
                                        <i class="fas fa-envelope me-1"></i> Solicitar nuevo enlace
                                    </a>
                                </div>
                            </c:if>
                        </div>
                    </c:if>

<%--                    <c:if test="${empty error}">--%>
                        <form id="resetForm" action="reset-password" method="post" class="needs-validation" novalidate>
                            <input type="hidden" name="token" value="${token}">

                            <div class="row">
                                <div class="col-md-6 mb-4">
                                    <label for="password" class="form-label">Nueva Contraseña</label>
                                    <input type="password" class="form-control" id="password" name="password" required>

                                    <!-- Lista de requisitos -->
                                    <ul class="requirement-list mt-2">
                                        <li style="color: #6c757d; font-size: 13px; font-weight: 600; margin-bottom: 2px;">
                                            La contraseña debe tener:
                                        </li>
                                        <c:set var="reqs" value="${requisitosPwd}" />
                                        <li style="color: ${reqs['len'] == null ? '#b0b0b0' : (reqs['len'] ? '#388e3c' : '#d32f2f')}">
                                            Mínimo 8 caracteres
                                        </li>
                                        <li style="color: ${reqs['may'] == null ? '#b0b0b0' : (reqs['may'] ? '#388e3c' : '#d32f2f')}">
                                            Al menos una mayúscula
                                        </li>
                                        <li style="color: ${reqs['min'] == null ? '#b0b0b0' : (reqs['min'] ? '#388e3c' : '#d32f2f')}">
                                            Al menos una minúscula
                                        </li>
                                        <li style="color: ${reqs['num'] == null ? '#b0b0b0' : (reqs['num'] ? '#388e3c' : '#d32f2f')}">
                                            Al menos un número
                                        </li>
                                        <li style="color: ${reqs['spec'] == null ? '#b0b0b0' : (reqs['spec'] ? '#388e3c' : '#d32f2f')}">
                                            Al menos un carácter especial
                                        </li>
                                    </ul>
                                </div>

                                <div class="col-md-6 mb-4">
                                    <label for="confirmPassword" class="form-label">Confirmar Contraseña</label>
                                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                                </div>
                            </div>

                            <!-- Barra de progreso -->
                            <div class="row mt-3">
                                <div class="col-md-12">
                                    <div class="progress mb-2">
                                        <div id="passwordStrengthBar" class="progress-bar" role="progressbar"></div>
                                    </div>
                                    <small id="passwordStrengthText" class="form-text text-muted"></small>
                                </div>
                            </div>

                            <div class="d-grid gap-2 mt-4">
                                <button type="submit" class="btn btn-primary btn-lg">
                                    <i class="fas fa-save me-2"></i> Restablecer Contraseña
                                </button>
                            </div>

                            <hr class="my-4">

                            <div class="text-center">
                                <a class="text-decoration-none" href="${pageContext.request.contextPath}/login">
                                    <i class="fas fa-arrow-left me-1"></i> Volver al Inicio de Sesión
                                </a>
                            </div>
                        </form>
<%--                    </c:if>--%>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const passwordInput = document.getElementById('password');
        const confirmInput = document.getElementById('confirmPassword');
        const strengthBar = document.getElementById('passwordStrengthBar');
        const strengthText = document.getElementById('passwordStrengthText');

        passwordInput.addEventListener('input', function() {
            const password = this.value;
            let strength = 0;

            // Validar longitud
            if (password.length >= 8) strength += 1;
            if (password.length >= 12) strength += 1;

            // Validar caracteres
            if (/[A-Z]/.test(password)) strength += 1;
            if (/[0-9]/.test(password)) strength += 1;
            if (/[^A-Za-z0-9]/.test(password)) strength += 1;

            // Actualizar barra
            const width = strength * 20;
            strengthBar.style.width = width + '%';

            // Actualizar texto y color
            if (strength <= 1) {
                strengthBar.className = 'progress-bar bg-danger';
                strengthText.textContent = 'Débil';
            } else if (strength <= 3) {
                strengthBar.className = 'progress-bar bg-warning';
                strengthText.textContent = 'Moderada';
            } else {
                strengthBar.className = 'progress-bar bg-success';
                strengthText.textContent = 'Fuerte';
            }
        });

        // Validación de confirmación
        document.getElementById('resetForm').addEventListener('submit', function(e) {
            if (passwordInput.value !== confirmInput.value) {
                e.preventDefault();
                alert('Las contraseñas no coinciden');
            }
        });
    });
</script>
</body>
</html>
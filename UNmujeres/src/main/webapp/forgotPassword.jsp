<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Reestablecer Contraseña | ONU Mujeres</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .bg-gradient-primary {
            background: linear-gradient(155deg, #f8f9fc 0%, #e6ebf5 100%);
        }
        .card {
            border: none;
            border-radius: 1rem;
            box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
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
    </style>
</head>
<body class="bg-gradient-primary">
<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-xl-6 col-lg-8 col-md-10">
            <div class="card shadow-lg my-5">
                <div class="card-header py-4 text-center">
                    <h3 class="m-0">
                        <i class="fas fa-address-book me-2"></i> Encuentra tu cuenta
                    </h3>
                </div>
                <div class="card-body p-5">
                    <div class="text-center mb-4">
                        <p class="text-muted">Introduce el correo electrónico asociado a tu cuenta para reestablecer tu contraseña.</p>
                    </div>

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show">
                                ${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <c:if test="${not empty success}">
                        <div class="alert alert-success alert-dismissible fade show">
                                ${success}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form class="needs-validation" novalidate action="${pageContext.request.contextPath}/forgot-password" method="post">
                        <div class="mb-4">
                            <label for="email" class="form-label">Correo Electrónico</label>
                            <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="fas fa-envelope"></i>
                                    </span>
                                <input type="email" class="form-control" id="email" name="email"
                                       placeholder="tucorreo@ejemplo.com" required>
                                <div class="invalid-feedback">
                                    Por favor ingresa un correo electrónico válido.
                                </div>
                            </div>
                        </div>

                        <div class="d-grid gap-2 mb-3">
                            <button type="submit" class="btn btn-primary btn-lg">
                                <i class="fas fa-paper-plane me-2"></i> Enviar Enlace
                            </button>
                        </div>

                        <hr class="my-4">

                        <div class="text-center">
                            <a class="text-decoration-none" href="${pageContext.request.contextPath}/login">
                                <i class="fas fa-arrow-left me-1"></i> Volver al Inicio de Sesión
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<!-- Form validation -->
<script>
    (function() {
        'use strict';
        var forms = document.querySelectorAll('.needs-validation');
        Array.prototype.slice.call(forms)
            .forEach(function(form) {
                form.addEventListener('submit', function(event) {
                    if (!form.checkValidity()) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                }, false);
            });
    })();
</script>
</body>
</html>
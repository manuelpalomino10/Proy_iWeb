<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Acceso Denegado</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background: linear-gradient(155deg, #f6f9fc 0%, #eef2f6 100%);
            height: 100vh;
            display: flex;
            align-items: center;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .denied-card {
            max-width: 500px;
            border-radius: 12px;
            box-shadow: 0 6px 18px rgba(0, 0, 0, 0.05);
            border: none;
            background: #ffffff;
            overflow: hidden;
            border: 1px solid rgba(78, 115, 223, 0.1);
        }
        .denied-icon {
            font-size: 3.5rem;
            color: #4e73df;
            margin-bottom: 1.5rem;
            text-shadow: 0 2px 4px rgba(78, 115, 223, 0.1);
        }
        .denied-title {
            color: #224abe;
            font-weight: 600;
            letter-spacing: -0.5px;
        }
        .denied-text {
            color: #6c757d;
            line-height: 1.6;
        }
        .btn-denied {
            background-color: #4e73df;
            border: none;
            padding: 10px 24px;
            font-weight: 500;
            border-radius: 8px;
            transition: all 0.3s ease;
            box-shadow: 0 2px 6px rgba(78, 115, 223, 0.3);
        }
        .btn-denied:hover {
            background-color: #224abe;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(34, 74, 190, 0.3);
        }
        .card-body {
            padding: 2.5rem;
        }
        h1 {
            font-size: 2.2rem;
            font-weight: 600;
            margin-bottom: 1rem;
            color: #2c3e50;
        }
        .text-muted {
            font-size: 1.1rem;
            color: #6c757d !important;
            line-height: 1.6;
            margin-bottom: 2rem !important;
            font-weight: 400; /* Peso regular para el subtítulo */
        }
    </style>
</head>
<body>
<div class="container">
    <div class="card denied-card mx-auto text-center">
        <div class="card-body py-4">
            <i class="bi bi-exclamation-triangle-fill denied-icon"></i>
            <h1 class="h1 mb-3">Lo sentimos, hubo un error...</h1>
            <p class="text-muted error-text mb-4">
                No se puede llevar a cabo esta acción
            </p>
            <div class="d-grid gap-2 col-md-8 mx-auto">
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-denied text-white">
                    <i class="fas fa-home me-2"></i>Volver al Login
                </a>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
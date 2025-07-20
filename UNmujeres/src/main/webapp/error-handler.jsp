<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>${tituloError}</title>
  <!-- Bootstrap 5 -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <!-- Google Fonts - Montserrat para los textos específicos -->
  <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600&display=swap" rel="stylesheet">
  <style>
    :root {
      --error-403: #e10217;
      --error-404: #224ABE;
      --error-500: #d19c06;
      --primary-btn: #4e73df;
      --primary-btn-hover: #274fc2;
    }

    body {
      font-family: 'Poppins', sans-serif;
      background-color: #f8f9fa;
      color: #333;
    }

    /* Fuente específica para los textos indicados */
    .error-card h1,
    .error-card .text-muted,
    .error-card .btn-primary {
      font-family: 'Montserrat', sans-serif;
    }

    .error-page {
      height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, #f5f7fa 0%, #e4e8f0 100%);
    }

    .error-card {
      max-width: 500px;
      padding: 2.5rem;
      border-radius: 12px;
      box-shadow: 0 10px 30px rgba(0,0,0,0.08);
      text-align: center;
      background: white;
      border: none;
      transform: translateY(0);
      transition: all 0.3s ease;
    }

    .error-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 15px 35px rgba(0,0,0,0.12);
    }

    .error-icon {
      font-size: 5.5rem;
      margin-bottom: 1.5rem;
      transition: all 0.3s ease;
    }

    .error-icon-403 {
      color: var(--error-403);
      text-shadow: 0 4px 8px rgba(220, 53, 69, 0.2);
    }

    .error-icon-404 {
      color: var(--error-404);
      text-shadow: 0 4px 8px rgba(34, 74, 190, 0.2);
    }

    .error-icon-500 {
      color: var(--error-500);
      text-shadow: 0 4px 8px rgba(255, 193, 7, 0.2);
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

    .btn-primary {
      background-color: var(--primary-btn);
      border-color: var(--primary-btn);
      padding: 0.6rem 1.8rem;
      font-size: 1rem;
      font-weight: 500;
      border-radius: 8px;
      letter-spacing: 0.5px;
      transition: all 0.3s ease;
    }

    .btn-primary:hover {
      background-color: var(--primary-btn-hover);
      border-color: var(--primary-btn-hover);
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(67, 97, 238, 0.3);
    }

    .alert-light {
      background-color: #f8f9fa;
      border: 1px solid #e9ecef;
      border-radius: 8px;
      margin-bottom: 2rem !important;
      font-family: 'Poppins', sans-serif; /* Mantiene Poppins para las alertas */
    }

    code {
      font-family: 'Courier New', monospace;
      color: #495057;
      background-color: #e9ecef;
      padding: 0.2rem 0.4rem;
      border-radius: 4px;
    }

    .fa-link {
      color: var(--error-404);
    }
  </style>
</head>
<body>
<div class="error-page">
  <div class="error-card bg-white">
    <div class="error-icon error-icon-${errorCode}">
      <i class="fas ${iconoError}"></i>
    </div>
    <h1 class="mb-3">${tituloError}</h1> <!-- "Página no encontrada" -->
    <p class="text-muted mb-4">${mensajeError}</p> <!-- "El recurso solicitado no existe" -->

    <c:if test="${not empty requestScope['jakarta.servlet.error.request_uri']}">
      <div class="alert alert-light mb-4">
        <i class="fas fa-link me-2"></i>
        <code>${requestScope['jakarta.servlet.error.request_uri']}</code>
      </div>
    </c:if>

    <a href="${pageContext.request.contextPath}/dashboard"
       class="btn btn-primary px-4">
      <i class="fas fa-home me-2"></i> Volver a Resumen
    </a>
  </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
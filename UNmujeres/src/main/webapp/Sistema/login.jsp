<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Panel de Administración - ONU Mujeres">
    <meta name="author" content="MP">

    <title>ONU Mujeres - Inicio de Sesión</title>

    <!-- Bootstrap core CSS -->
    <link href="${pageContext.request.contextPath}/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom fonts -->
    <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <!-- Fuente Montserrat para coincidir con la página de error -->
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600&display=swap" rel="stylesheet">

    <!-- Custom styles -->
    <link href="${pageContext.request.contextPath}/css/sb-admin-2.min.css" rel="stylesheet">

    <style>
        body {
            background: #4e73df;
            background-image: linear-gradient(180deg,#4e73df 10%,#224abe 100%);
            background-size: cover;
            height: 100vh;
        }

        .login-container {
            height: 100vh;
            display: flex;
            align-items: center;
        }

        .brand-wrapper {
            padding-right: 2rem;
            display: flex;
            justify-content: flex-end;
        }

        .brand-logo {
            max-height: 50vh;
            transition: all 0.3s ease;
        }

        .brand-logo:hover {
            transform: scale(1.03);
            filter: drop-shadow(0 0 10px rgba(255, 255, 255, 0.3));
        }

        .login-card {
            border: 0;
            border-radius: 0.375rem;
            box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
            width: 100%;
            max-width: 28rem;
        }

        .login-card-body {
            padding: 2rem;
        }

        .login-title {
            font-size: 2.25rem;
            font-weight: 600;
            margin-bottom: 0.5rem;
            color: #224abe;
            font-family: 'Montserrat', sans-serif;
            letter-spacing: 0.5px;
        }

        .login-subtitle {
            font-size: 1.1rem;
            color: #6c757d;
            margin-bottom: 2rem;
            font-family: 'Montserrat', sans-serif;
            font-weight: 400;
        }

    </style>
</head>

<body>
<div class="container-fluid login-container">
    <div class="row justify-content-center align-items-center w-100 mx-0">
        <!-- Brand Logo Column -->
        <div class="col-lg-6 brand-wrapper">
            <img src="${pageContext.request.contextPath}/img/logo-ONU-MUJERES_blanco.png"
                 alt="ONU Mujeres"
                 class="brand-logo img-fluid">
        </div>

        <!-- Login Form Column -->
        <div class="col-lg-5">
            <div class="card login-card">
                <div class="card-body login-card-body">
                    <div class="text-center mb-4">
                        <h2 class="login-title">¡Bienvenido!</h2>
                        <p class="login-subtitle">Por favor ingrese sus datos para iniciar sesión</p>
                    </div>

                    <% if (request.getAttribute("errorMessage") != null) { %>
                    <div class="alert alert-danger" role="alert">
                        <%= request.getAttribute("errorMessage") %>
                    </div>
                    <% } %>

                    <form action="${pageContext.request.contextPath}/login" method="post">
                        <!-- Email Field -->
                        <div class="form-group mb-4">
                            <label for="correo" class="sr-only">Correo Electrónico</label>
                            <div class="input-group">
                                <div class="input-group-prepend">
                                        <span class="input-group-text">
                                            <i class="fas fa-envelope"></i>
                                        </span>
                                </div>
                                <input type="email"
                                       class="form-control"
                                       id="correo"
                                       name="correo"
                                       placeholder="Correo Electrónico"
                                       required>
                            </div>
                        </div>

                        <!-- Password Field -->
                        <div class="form-group mb-4">
                            <label for="contraseña" class="sr-only">Contraseña</label>
                            <div class="input-group">
                                <div class="input-group-prepend">
                                        <span class="input-group-text">
                                            <i class="fas fa-lock"></i>
                                        </span>
                                </div>
                                <input type="password"
                                       class="form-control"
                                       id="contraseña"
                                       name="contraseña"
                                       placeholder="Contraseña"
                                       required>
                                <div class="input-group-append">
                                        <span class="input-group-text password-toggle" id="togglePassword">
                                            <i class="far fa-eye"></i>
                                        </span>
                                </div>
                            </div>
                        </div>

                        <!-- Login Button -->
                        <button type="submit" class="btn btn-primary btn-block btn-login mb-3">
                            Iniciar Sesión
                        </button>

                        <!-- Forgot Password Link -->
                        <div class="text-center">
                            <a href="${pageContext.request.contextPath}/forgot-password" class="forgot-password-link">
                                ¿Olvidaste tu contraseña?
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap core JavaScript -->
<script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/jquery-easing/jquery.easing.min.js"></script>
<script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>

<script>
    // Toggle password visibility
    $(document).ready(function() {
        $('#togglePassword').click(function() {
            const passwordInput = $('#contraseña');
            const icon = $(this).find('i');

            if (passwordInput.attr('type') === 'password') {
                passwordInput.attr('type', 'text');
                icon.removeClass('fa-eye').addClass('fa-eye-slash');
            } else {
                passwordInput.attr('type', 'password');
                icon.removeClass('fa-eye-slash').addClass('fa-eye');
            }
        });
    });
</script>
</body>
</html>
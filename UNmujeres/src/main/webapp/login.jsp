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

    <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

    <link href="${pageContext.request.contextPath}/css/sb-admin-2.min.css" rel="stylesheet">

    <style>
        html, body {
            height: 100%;
        }
        .container {
            height: 100%;
            display: flex;
            justify-content: center;
            align-items: center; /* Vertically center the content */
        }
        .image-container {
            width: auto; /* Adjust as needed */
            max-height: 400px; /* Adjust to match login container height */
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 20px; /* Add some spacing between image and login form */
            /* CSS for fading edges (more complex, might not be perfect) */
            mask: linear-gradient(90deg, transparent 0%, white 20%, white 80%, transparent 100%);
            -webkit-mask: linear-gradient(90deg, transparent 0%, white 20%, white 80%, transparent 100%);
        }
        .image-container img {
            max-width: 100%;
            max-height: 100%;
            object-fit: contain;
        }
        .login-container {
            width: 100%;
            max-width: 400px; /* Adjust as needed */
        }
        .card {
            width: 100%;
        }
        .hidden {
            display: none;
        }
        .error-message {
            color: #e74a3b;
            font-size: 0.9rem;
            margin-top: 0.25rem;
        }
    </style>
</head>

<body class="bg-gradient-primary">

<div class="container">
    <div class="image-container">
        <img src="img/back.jpg" alt="Imagen ONU Mujeres">
    </div>
    <div class="login-container">
        <div class="card o-hidden border-0 shadow-lg">
            <div class="card-body p-0">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="p-5">
                            <div class="text-center">
                                <h1 class="h4 text-gray-900 mb-4">Iniciar Sesión - ONU Mujeres</h1>
                            </div>


                            <% if (request.getAttribute("errorMessage") != null) { %>
                            <div class="alert alert-danger" role="alert">
                                <%= request.getAttribute("errorMessage") %>
                            </div>
                            <% } %>

                            <form class="user" action="login" method="post">
                                <!-- Campo para Email -->
                                <div class="form-group">
                                    <input type="email" class="form-control form-control-user"
                                           id="correo" name="correo" aria-describedby="emailHelp"
                                           placeholder="Correo Electrónico" required>
                                </div>
                                <!-- Campo para Contraseña -->
                                <div class="form-group">
                                    <input type="password" class="form-control form-control-user"
                                           id="contraseña" name="contraseña" placeholder="Contraseña" required>
                                </div>

                                <button type="submit" class="btn btn-primary btn-user btn-block">
                                    Iniciar Sesión
                                </button>
                            </form>
                            <hr>
                            <div class="text-center">
                                <a class="small" href="encuestador/register">Registrarte</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/jquery-easing/jquery.easing.min.js"></script>
<script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>

</body>
</html>

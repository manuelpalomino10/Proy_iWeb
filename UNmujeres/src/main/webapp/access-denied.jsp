<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Acceso Denegado</title>
    <style>
        html, body {
            height: 100%;
            margin: 0;
            font-family: Arial, sans-serif;
            background-color: #e3f2fd; /* Azul claro */
            color: #0d47a1;
        }

        .container {
            height: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .card {
            background-color: #bbdefb; /* Fondo del cuadro */
            border: 1px solid #90caf9;
            border-radius: 10px;
            padding: 40px;
            text-align: center;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
        }

        h1 {
            font-size: 32px;
            margin-bottom: 20px;
        }

        p {
            font-size: 18px;
            margin-bottom: 30px;
        }

        .btn {
            background-color: #0d47a1;
            color: white;
            border: none;
            padding: 12px 25px;
            font-size: 16px;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
        }

        .btn:hover {
            background-color: #08306b;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="card">
        <h1>🔒 Acceso Denegado</h1>
        <p>No tienes permisos para acceder a esta sección del sistema.</p>
        <a href="login" class="btn">Volver al Login</a>
    </div>
</div>
</body>
</html>
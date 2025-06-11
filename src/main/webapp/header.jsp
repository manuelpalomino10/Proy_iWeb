<%--
  Created by IntelliJ IDEA.
  User: Luis
  Date: 13/05/2025
  Time: 00:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<head>

  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">

  <fmt:setLocale value="es_PE"/>
  <fmt:setTimeZone value="America/Lima"/>


  <title>${pageTitle != null ? pageTitle : 'ONU Mujeres'}</title>

  <!-- Custom fonts for this template-->
  <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
  <link
          href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
          rel="stylesheet">

  <!-- Custom styles for this template-->
  <link href="${pageContext.request.contextPath}/css/sb-admin-2.min.css" rel="stylesheet">

  <!-- DataTables CSS (elige uno, por ejemplo, el local) -->
  <link href="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.min.css" rel="stylesheet">
  <!-- (Opcional, si prefieres el CDN)
  <link rel="stylesheet" href="https://cdn.datatables.net/1.13.8/css/jquery.dataTables.min.css"/> -->

  <!-- Date Range Picker CSS -->
  <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />



  <style>
    .falta-respuesta {
      border: 2px solid red;
      background-color: #ffecec;
    }
  </style>

</head>


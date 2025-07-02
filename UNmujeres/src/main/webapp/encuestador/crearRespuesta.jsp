<%--
  Created by IntelliJ IDEA.
  User: Luis
  Date: 18/05/2025
  Time: 11:08
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.unmujeres.beans.Pregunta" %>
<%@ page import="com.example.unmujeres.beans.Seccion" %>
<%@ page import="com.example.unmujeres.beans.OpcionPregunta" %>
<%@ page import="com.example.unmujeres.beans.Usuario" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="jakarta.mail.Session" %>
<%
    // Se obtiene la lista de preguntas y las opciones desde el request.
    ArrayList<Pregunta> preguntas = (ArrayList<Pregunta>) request.getAttribute("preguntas");
    ArrayList<OpcionPregunta> opciones = (ArrayList<OpcionPregunta>) request.getAttribute("opciones");
    String idasignacion = request.getParameter("id_asig");
    // System.out.println("asignacion id es:" + idasignacion);


    Usuario user = (Usuario) session.getAttribute("usuario");
    String codEnc = user.getCodEnc();
%>

<html lang="es">
<jsp:include page="../header.jsp" />
<body id="page-top">

<div id="wrapper">

    <% Usuario usuarioSesion = (Usuario) session.getAttribute("usuario"); // Obtiene el usuario de la sesión
        if (usuarioSesion != null && usuarioSesion.getIdroles() == 3) { %>
    <jsp:include page="../sidebarEnc.jsp" />
    <% } else if (usuarioSesion != null && usuarioSesion.getIdroles() == 2) { %>
    <jsp:include page="../sidebarCoordi.jsp" />
    <% } %>


    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
            <jsp:include page="../topbarEnc.jsp" />

            <div class="container-fluid">
                <div class="d-sm-flex align-items-center justify-content-between mb-4">
                    <h1 class="h3 mb-0 text-gray-800">Crear nueva respuesta</h1>
                </div>


                <% if (codEnc != null) { %>
                <div class="d-flex align-items-center mb-3">
                    <label for="cod" class="me-2 mb-0 fw-semibold">Tu código es: </label>
                    <input type="text"
                           class="form-control w-auto"
                           id="cod"
                           name="cod"
                           value="<%= codEnc %>"
                           readonly />
                </div>
                <% }
                if (session.getAttribute("error") != null) { %>
                <div>
                    <div class="alert alert-danger" role="alert"><%=session.getAttribute("error")%>
                    </div>
                </div>
                <% session.removeAttribute("error"); %>
                <% } %>


                <%
                    Map<Integer, String> errores = (Map<Integer, String>) session.getAttribute("validationErrors");
                    if (errores != null) {
                %>
                <div class="alert alert-danger">
                    <ul>
                        <%
                        for (Map.Entry<Integer, String> entry : errores.entrySet()) {
                        %>
                        <li><strong>Pregunta <%= entry.getKey() %>:</strong> <%= entry.getValue() %></li>
                        <%
                        }
                        session.removeAttribute("validationErrors");
                        %>
                    </ul>
                </div>
                <%
                    }
                    Map<Integer, String> valoresForm = (Map<Integer, String>) session.getAttribute("valoresFormulario");
                    if (valoresForm != null) {
                        // Puedes copiar esos datos a un atributo de la request y luego removerlos
                        //request.setAttribute("valoresFormulario", valoresForm);
                        session.removeAttribute("valoresFormulario");
                    }
                %>


                <% if (preguntas != null && !preguntas.isEmpty()) {
                    int currentSeccionId = -1;
                %>
                <form id="respuestaForm" method="POST" action="<%=request.getContextPath()%>/shared/VerFormulariosServlet?action=guardar">
                    <input type="hidden" name="idasignacion" value="<%= request.getParameter("id_asig") %>" />


                <%
                        // Recorremos la lista de preguntas
                        for (Pregunta pregunta : preguntas) {
                            Seccion sec = pregunta.getSeccion();
                            // Si cambiamos de sección (o es la primera iteración), cerramos la sección anterior y abrimos una nueva.
                            if (sec.getIdSeccion() != currentSeccionId) {
                                if (currentSeccionId != -1) {
                    %>
            </div>      <!-- Cierra: div.row de la sección anterior -->
        </div>          <!-- Cierra: card-body de la sección anterior -->
    </div>              <!-- Cierra: card de la sección anterior -->
    <br/>
    <%      }
        currentSeccionId = sec.getIdSeccion();
    %>
    <!-- Inicia nueva sección -->
    <div class="card">                                          <!-- Contenedor principal de la sección -->
        <div class="card-header">                               <!-- Header de la sección -->
            <h5 class="card-title m-0 font-weight-bold text-primary">
                <%= sec.getNombreSec() %>
            </h5>
        </div>
        <div class="card-body">                                 <!-- Cuerpo de la sección -->
            <div class="row">                                   <!-- Fila para las columnas de preguntas -->
                <% } // Fin de if cambio de sección %>

                <!-- Contenedor de cada pregunta (columna) -->
                <div class="col-md-4">                         <!-- 3 columnas por fila -->
                    <div class="pregunta">
                        <p><strong>Pregunta:</strong> <%= pregunta.getEnunciado() %></p>
                        <div class="form-group">
                            <label for="pregunta_<%= pregunta.getIdPregunta() %>">Respuesta:</label>
                            <%
                                String inputError1="";
                                if (errores!=null) {
                                    if (errores.containsKey(pregunta.getIdPregunta())) {
                                        inputError1 = "falta-respuesta";
                                    }
                                }
                                // Si el tipo de dato de la pregunta es "combobox", mostramos un select con las opciones correspondientes
                                if ("combobox".equalsIgnoreCase(pregunta.getTipoDato())) {
                            %>
                            <select name="pregunta_<%= pregunta.getIdPregunta() %>"
                                    id="pregunta_<%= pregunta.getIdPregunta() %>"
                                    class="form-control <%= inputError1 %>"
                                    <%= pregunta.getRequerido() ? "required" : "" %>>
                                <option value="">-- Seleccione --</option>
                                <%
                                    if (opciones != null) {

                                        for (OpcionPregunta opcion : opciones) {
                                            String opcSelected = null;
                                            // Se filtran las opciones correspondientes a la pregunta actual
                                            if (opcion.getPregunta().getIdPregunta() == pregunta.getIdPregunta()) {
                                                if (valoresForm!=null) {
                                                    if (opcion.getOpcion().equals(valoresForm.get(pregunta.getIdPregunta()))) {
                                                        opcSelected = "selected";
                                                    }
                                                }
                                %>
                                <option value="<%= opcion.getOpcion() %>"<%=opcSelected%> >
                                    <%= opcion.getOpcion() %>
                                </option>
                                <%
                                            }
                                        }
                                    }
                                %>
                            </select>
                                <%
                                if (pregunta.getRequerido()) {
                                %>
                                <small class="form-text text-muted">* Respuesta obligatoria.</small>
                                <% } %>
                            <%
                            } else {
                                // Para otros tipos definimos el input adecuado; "date" e "int" se manejan, por defecto "text"
                                String inputType = "text";
                                if ("int".equalsIgnoreCase(pregunta.getTipoDato())) {
                                    inputType = "number";
                                } else if ("date".equalsIgnoreCase(pregunta.getTipoDato())) {
                                    inputType = "date";
                                }
                                String inputValue = "";
                                if (valoresForm!=null){
                                    inputValue = valoresForm.get(pregunta.getIdPregunta()) != null ? valoresForm.get(pregunta.getIdPregunta()) : "";
                                }
                                String inputError= "";
                                if (errores!=null) {
                                    if (errores.containsKey(pregunta.getIdPregunta())) {
                                        inputError = "falta-respuesta";
                                    }
                                }
                            %>
                            <input type="<%= inputType %>"
                                   class="form-control <%= inputError %>"
                                   id="pregunta_<%= pregunta.getIdPregunta() %>"
                                   name="pregunta_<%= pregunta.getIdPregunta() %>"
                                   <%= pregunta.getRequerido() ? "required" : "" %>
                                   value="<%= inputValue %>" />
<%--                                   value=""/>--%>
                            <%
                                if (pregunta.getRequerido()) {
                            %>
                            <small class="form-text text-muted">* Respuesta obligatoria.</small>
                            <% }
                            }
                            %>
                        </div>  <!-- /.form-group -->
                    </div>  <!-- /.pregunta -->
                </div>  <!-- /.col-md-4 -->
                <%
                    } // Fin del for de preguntas

                    // Cerrar la última sección si se procesó al menos una
                    if (currentSeccionId != -1) {
                %>
            </div>      <!-- Cierra: div.row de la última sección -->
        </div>          <!-- Cierra: card-body de la última sección -->
    </div>              <!-- Cierra: card de la última sección -->
    <br/>
    <% } %>
    </form>
    <% } else { %>
    <p>No se encontraron preguntas para mostrar.</p>
    <% } %>

    <!-- Botones de acción -->
    <div class="text-center mt-4 fixed-bottom">
        <button id="completadoBtn" type="button" class="btn btn-success btn-icon-split mr-2" >
        <span class="icon text-white-50">
            <i class="fas fa-check"></i>
        </span>
            <span class="text">Registrar Respuesta</span>
        </button>
        <% 
            if (usuarioSesion != null && usuarioSesion.getIdroles() == 3) { %>
        <button type="submit" formnovalidate form="respuestaForm" name="acto" value="borrador" class="btn btn-secondary btn-icon-split mr-2">
            <span class="icon text-white-50"><i class="fas fa-save"></i></span>
            <span class="text">Guardar como Borrador</span>
        </button>
        <% } %>
    </div>
</div>
</div>

<footer class="sticky-footer bg-white">
    <div class="container my-auto">
        <div class="copyright text-center my-auto">
            <span>Copyright &copy; ONU Mujeres - PUCP 2025</span>
        </div>
    </div>
</footer>




<jsp:include page="../footer.jsp" />

    <script>
        document.getElementById("completadoBtn").addEventListener("click", function(event) {
            // Recoger todos los inputs cuya id empieza por "pregunta_"
            var inputs = document.querySelectorAll("input[id^='pregunta_'][required], select[id^='pregunta_'][required]");
            var faltantes = [];

            inputs.forEach(function(input) {
                if (!input.value.trim()) {
                    faltantes.push(input); // Guarda los campos incompletos
                    input.classList.add("falta-respuesta"); // Aplica estilo
                } else {
                    input.classList.remove("falta-respuesta"); // Remueve el estilo si está completo
                }
            });

            if (faltantes.length > 0) {
                event.preventDefault();
                alert("Complete todas las respuestas requeridas antes de continuar.");
            } else {
                // Si la validación pasa, se muestra el modal manualmente.
                $('#SaveRegModal').modal('show');
            }
        });
    </script>
</body>
</html>


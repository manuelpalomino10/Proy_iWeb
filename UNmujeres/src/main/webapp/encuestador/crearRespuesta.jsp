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
    String nombre = user.getNombres()+" "+user.getApellidos();
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
                <div class="m-2 form-group row align-items-center">
                    <label for="cod" class="mr-2 mb-0 fw-semibold">Su código es: </label>
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
                        int count = 0;
                        for (Pregunta pregunta : preguntas) {
                            count++;
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
                        <p><strong>Pregunta <%= count %>:</strong> <%= pregunta.getEnunciado() %></p>
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
                                    title="pregunta <%= count %>"
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
                                String patron = "";
                                String aviso = (pregunta.getRequerido() ? "* Respuesta obligatoria" : null);
                                if ("int".equalsIgnoreCase(pregunta.getTipoDato()) ) {
                                    inputType = "number";
                                    patron = "pattern=\"\\d+\" min=\"0\" max=\"40\" inputmode=\"numeric\"";
                                } else if ("date".equalsIgnoreCase(pregunta.getTipoDato())) {
                                    inputType = "date";
                                } else if ("email".equalsIgnoreCase(pregunta.getTipoDato())) {
                                    inputType = "email";
                                    patron = "pattern=\"^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$\"";
                                } else if ("tel".equalsIgnoreCase(pregunta.getTipoDato())) {
                                    inputType="tel";
                                    patron = "pattern=\"9\\d{8}\" maxlength=\"9\" inputmode=\"numeric\"";
                                    aviso = "* Número de celular de Perú";
                                } else if ("dni".equalsIgnoreCase(pregunta.getTipoDato())) {
                                    patron = "pattern=\"\\d{8}\" maxlength=\"8\" inputmode=\"numeric\"";
                                    aviso = "* DNI";
                                }
                                String inputValue = "";
                                if (valoresForm!=null){
                                    inputValue = valoresForm.get(pregunta.getIdPregunta()) != null ? valoresForm.get(pregunta.getIdPregunta()) : "";
                                } else if (pregunta.getEnunciado().contains("persona que encuesta")) {
                                    inputValue = nombre;
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
                                   title="pregunta <%= count %>"
                                    <%= pregunta.getRequerido() ? "required" : "" %>
                                    <%= patron %>
                                   value="<%= inputValue %>" />
                            <%
                                if (aviso!=null) {
                            %>
                            <small class="form-text text-muted"><%= aviso %></small>
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
        <button id="borradorBtn" type="submit" formnovalidate form="respuestaForm" name="acto" value="borrador" class="btn btn-secondary btn-icon-split mr-2">
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

<!-- Modal de validación -->
<div class="modal fade" id="errorModal" tabindex="-1" role="dialog" aria-labelledby="errorModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content border-danger">
            <div class="modal-header bg-danger text-white">
                <h5 class="modal-title" id="errorModalLabel">Ingrese Respuestas Válidas</h5>
                <button type="button" class="close text-white" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <ul id="errorList" class="mb-0"></ul>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Entendido</button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../footer.jsp" />

    <script>
        const form       = document.getElementById("respuestaForm");
        const inputsAll  = form.querySelectorAll("input, textarea, select");

        // Validación “completa”
        document.getElementById("completadoBtn").addEventListener("click", e => {
            let valido = true;
            const errores = [];

            inputsAll.forEach(input => {
                input.classList.remove("falta-respuesta");
                let nom=input.title;
                const v = input.value.trim();

                if (input.required && v === "") {
                    let msj = "La respuesta a la "+nom+" es obligatoria";
                    input.classList.add("falta-respuesta");
                    errores.push(msj);
                    valido = false;
                    return;
                }
                if (v !== "" && !input.checkValidity()) {
                    let msj = "La respuesta a la "+nom+" tiene formato inválido";
                    input.classList.add("falta-respuesta");
                    errores.push(msj);
                    valido = false;
                }
            });

            if (!valido) {
                e.preventDefault();
                showErrorModal(
                    "Complete todos los campos requeridos",
                    errores
                );
            } else {
                form.submit();
            }
        });

        // Validación “borrador”
        document.getElementById("borradorBtn").addEventListener("click", e => {
            let valido = true;
            const errores = [];

            inputsAll.forEach(input => {
                input.classList.remove("falta-respuesta");
                let nom=input.title;
                const v = input.value.trim();

                if (v !== "" && !input.checkValidity()) {
                    let msj ="La respuesta a la "+nom+" tiene formato inválido";
                    input.classList.add("falta-respuesta");
                    errores.push(msj);
                    valido = false;
                }
            });

            if (!valido) {
                e.preventDefault();
                showErrorModal(
                    "Corrija el formato de los campos ingresados",
                    errores
                );
            }
        });

        function showErrorModal(title, errores) {
            // Título
            document.getElementById("errorModalLabel").textContent = title;

            // Lista de errores
            const ul = document.getElementById("errorList");
            ul.innerHTML = "";
            errores.forEach(msg => {
                const li = document.createElement("li");
                li.textContent = msg;
                ul.appendChild(li);
            });

            $('#errorModal').modal('show');
        }
    </script>
</body>
</html>


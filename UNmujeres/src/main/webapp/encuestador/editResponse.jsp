<%--
  Created by IntelliJ IDEA.
  User: Luis
  Date: 18/05/2025
  Time: 11:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.unmujeres.beans.OpcionPregunta" %>
<%@ page import="com.example.unmujeres.beans.*" %>
<%@ page import="java.util.Map" %>
<%
    ArrayList<Respuesta> respuestas = (ArrayList<Respuesta>) request.getAttribute("respuestas");
    RegistroRespuestas registro = (RegistroRespuestas) request.getAttribute("registro");
    ArrayList<OpcionPregunta> opciones = (ArrayList<OpcionPregunta>) request.getAttribute("opciones");

    Usuario user = (Usuario) session.getAttribute("usuario");
    String codEnc = user.getCodEnc();
%>

<html lang="es">
<jsp:include page="../header.jsp" />
<body id="page-top">

    <!-- Page Wrapper -->
    <div id="wrapper">

        <!-- Sidebar -->
        <jsp:include page="../sidebarEnc.jsp" />
        <!-- End of Sidebar -->

        <!-- Content Wrapper -->
        <div id="content-wrapper" class="d-flex flex-column">

            <!-- Main Content -->
            <div id="content">

                <!-- Topbar -->
                <jsp:include page="../topbarEnc.jsp" />
                <!-- End of Topbar -->

                <!-- Begin Page Content -->
                <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="d-sm-flex align-items-center justify-content-between mb-4">
                        <h1 class="h3 mb-0 text-gray-800">Editar respuesta</h1>
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
                    <% session.removeAttribute("error");
                    } %>


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
//                            System.out.println("Retribuidos valores form de inputs");
//                            System.out.println(valoresForm.keySet().toString());
//                            System.out.println(valoresForm.values().toString());
                            session.removeAttribute("valoresFormulario");
                        }
                    %>


                    <%
                        if (respuestas != null && !respuestas.isEmpty()) {
                            // Variable para controlar el cambio de sección.
                            int currentSeccionId = -1;
                    %>
                        <form id="respuestaForm" method="POST" action="<%=request.getContextPath()%>/encuestador/VerFormulariosServlet?action=editar">
                        <!-- Campo hidden para el id del registro de respuestas -->
                        <input type="hidden" name="id" value="<%= registro.getIdRegistroRespuestas() %>" />
                    <%
                        // Recorrer cada respuesta (la cual contiene la pregunta y sección asociada)
                        int count = 0;
                        for (Respuesta respuesta : respuestas) {
                            count++;
                            Pregunta pregunta = respuesta.getPregunta();
                            Seccion sec = pregunta.getSeccion();
                            // Si cambia la sección (o es la primera iteración), se inicia una nueva card.
                            if (sec.getIdSeccion() != currentSeccionId) {
                                // Si no es la primera sección, se cierra la card anterior y su contenedor row.
                                if (currentSeccionId != -1) {
                    %>
                                    </div>  <!-- Cierra div.row -->
                                </div>  <!-- Cierra card-body -->
                            </div>  <!-- Cierra card -->
                            <br/>
                    <%
                                }
                                currentSeccionId = sec.getIdSeccion();
                    %>
                            <!-- Nueva Card para la sección -->
                            <div class="card">
                                <div class="card-header">
                                    <h5 class="card-title m-0 font-weight-bold text-primary"><%= sec.getNombreSec() %></h5>
                                </div>
                                <div class="card-body">
                                    <div class="row">
                    <%
                            }
                    %>
                                <!-- Cada pregunta se coloca en una columna de 4 (12/4 = 3 columnas) -->
                                        <div class="col-md-4">
                                            <div class="pregunta">
                                                <p><strong>Pregunta <%= count %>:</strong> <%= pregunta.getEnunciado() %></p>
                                                <div class="form-group">
                                                    <label for="respuesta_<%= respuesta.getIdRespuesta() %>">Respuesta:</label>
                                                    <%
                                                        if ("combobox".equalsIgnoreCase(pregunta.getTipoDato())) {

                                                    %>
                                                    <select name="respuesta_<%= respuesta.getIdRespuesta() %>"
                                                            id="respuesta_<%= respuesta.getIdRespuesta() %>"
                                                            title="pregunta <%= count %>"
                                                            <%= pregunta.getRequerido() ? "required" : "" %>
                                                            class="form-control">
                                                        <option value="">-- Seleccione --</option>
                                                        <%
                                                            if(opciones != null) {
                                                                for(OpcionPregunta opcion : opciones) {
                                                                    if(opcion.getPregunta().getIdPregunta() == pregunta.getIdPregunta()) {
                                                                        String selected = "";
                                                                        if (valoresForm!=null) {
                                                                            if (opcion.getOpcion().equals(valoresForm.get(respuesta.getIdRespuesta()))) {
                                                                                selected = "selected";
                                                                            }
                                                                        } else {
                                                                            if (opcion.getOpcion().equals(respuesta.getRespuesta())) {
                                                                                selected = "selected";
                                                                            }
                                                                        }
                                                        %>
                                                        <option value="<%= opcion.getOpcion() %>" <%= selected %>>
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
                                                        String inputType = "text";
                                                        String patron = "";
                                                        String aviso = (pregunta.getRequerido() ? "* Respuesta obligatoria. " : "");
                                                        String place = "";
                                                        if ("un medium int".equalsIgnoreCase(pregunta.getTipoDato())) {
                                                            inputType = "number";
                                                            patron = "pattern=\"\\d+\" min=\"0\" max=\"500\" inputmode=\"numeric\"";
                                                            place = "500";
                                                        } else if ("un small int".equalsIgnoreCase(pregunta.getTipoDato())) {
                                                            inputType = "number";
                                                            patron = "pattern=\"\\d+\" min=\"0\" max=\"20\" inputmode=\"numeric\"";
                                                            place = "20";
                                                        } else if ("un large int".equalsIgnoreCase(pregunta.getTipoDato())) {
                                                            inputType = "number";
                                                            patron = "pattern=\"\\d+\" min=\"0\" max=\"100000\" inputmode=\"numeric\"";
                                                            place = "1000";
                                                        } else if ("date".equalsIgnoreCase(pregunta.getTipoDato())) {
                                                            inputType = "date";
                                                        } else if ("email".equalsIgnoreCase(pregunta.getTipoDato())) {
                                                            inputType = "email";
                                                            patron = "pattern=\"^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$\" inputmode=\"email\"";
                                                            place = "a@gmail.com";
                                                        } else if ("tel".equalsIgnoreCase(pregunta.getTipoDato())) {
                                                            inputType="tel";
                                                            patron = "pattern=\"9\\d{8}\" maxlength=\"9\" inputmode=\"numeric\"";
                                                            aviso += "* Número de celular de Perú.";
                                                            place = "9xxxxxxxx";
                                                        } else if ("dni".equalsIgnoreCase(pregunta.getTipoDato())) {
                                                            patron = "pattern=\"\\d{8}\" maxlength=\"8\" inputmode=\"numeric\"";
                                                            aviso += "* DNI peruano.";
                                                            place = "12345678";
                                                        } else if ("signed int".equalsIgnoreCase(pregunta.getTipoDato())) {
                                                            inputType = "number";
                                                            patron = "pattern=\"-?\\d+\" min=\"-100000\" max=\"100000\" inputmode=\"numeric\"";
                                                            place = "-1000";
                                                        } else if ("decimal2".equalsIgnoreCase(pregunta.getTipoDato())) {
                                                            inputType = "number";
                                                            patron = "pattern=\"-?\\d+(\\.\\d{1,2})?\" min=\"-100000\" max=\"100000\" step=\"0.5\" inputmode=\"numeric\"";
                                                            place = "100.99";
                                                        }
                                                        String inputValue = "";
                                                        if (valoresForm!=null){
                                                            inputValue = valoresForm.get(respuesta.getIdRespuesta()) != null ? valoresForm.get(respuesta.getIdRespuesta()) : "";
                                                        } else {
                                                            inputValue = respuesta.getRespuesta()!= null ? respuesta.getRespuesta() : "";
                                                        }
                                                        System.out.println("Value de input de respuesta "+respuesta.getIdRespuesta()+" es: "+inputValue);
                                                        String inputError= "";
                                                        if (errores!=null) {
                                                            if (errores.containsKey(pregunta.getIdPregunta())) {
                                                                inputError = "falta-respuesta";
                                                            }
                                                        }
                                                    %>
                                                    <input type="<%= inputType %>"
                                                           class="form-control <%= inputError %>"
                                                           id="respuesta_<%= respuesta.getIdRespuesta() %>"
                                                           name="respuesta_<%= respuesta.getIdRespuesta() %>"
                                                           title="pregunta <%= count %>"
                                                           placeholder="<%= place %>"
                                                            <%= pregunta.getRequerido() ? "required" : "" %>
                                                            <%= patron %>
                                                           value="<%= inputValue %>" />
                                                    <%
                                                        if (aviso!=null) {
                                                    %>
                                                    <small class="form-text text-muted"><%= aviso %></small>
                                                    <%  }
                                                    }
                                                    %>
                                                </div>
                                            </div>
                                        </div>
                    <%
                        } // Fin del for.

                        // Cerrar la última card y el contenedor row si se abrió alguna
                        if (currentSeccionId != -1) {
                    %>
                                    </div>  <!-- Cierra div.row de la última sección -->
                                </div>  <!-- Cierra card-body de la última sección -->
                            </div>  <!-- Cierra última card -->
                            <br/>
                    <%
                         }
                    %>
                        </form>
                    <%
                        } else {
                    %>
                        <p>No se encontraron preguntas para mostrar.</p>
                    <%
                        }
                    %>

<%--                </div>--%>
<%--            </div>--%>




                <div class="text-center mt-4 fixed-bottom">
                    <button id="completadoBtn" type="button" class="btn btn-success btn-icon-split mr-2">
                            <span class="icon text-white-50">
                                <i class="fas fa-check"></i>
                            </span>
                        <span class="text">Registrar Respuesta</span>
                    </button>
                    <button id="borradorBtn" type="submit" name="acto" value="borrador" formnovalidate form="respuestaForm" class="btn btn-secondary btn-icon-split mr-2">
                            <span class="icon text-white-50">
                                <i class="fas fa-save"></i>
                            </span>
                        <span class="text">Guardar como Borrador</span>
                    </button>
                </div>
            </div>

            <!-- Footer -->
            <footer class="sticky-footer bg-white">
                <div class="container my-auto">
                    <div class="copyright text-center my-auto">
                        <span>Copyright &copy; ONU Mujeres - PUCP 2025</span>
                    </div>
                </div>
            </footer>
            <!-- End of Footer -->

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

        </div>
        <!-- End of Content Wrapper -->

    </div>
    <!-- End of Page Wrapper -->
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
                //form.submit();
                $('#SaveRegModal').modal('show');
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
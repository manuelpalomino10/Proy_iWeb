<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- Scroll to Top Button-->
<a class="scroll-to-top rounded" href="#page-top">
  <i class="fas fa-angle-up"></i>
</a>

<!-- Logout Modal-->
<div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="lgoutModal" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="lgoutModal">¿Seguro que quieres salir?</h5>
        <button class="close" type="button" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">×</span>
        </button>
      </div>
      <div class="modal-body">Los cambios no guardados como borrador o completado no se guardarán. Haz click en "Cerrar sesión" para terminar.</div>
      <div class="modal-footer">
        <button class="btn btn-info" type="button" data-dismiss="modal">Seguir Aquí</button>
        <a class="btn btn-danger" href="${pageContext.request.contextPath}/login">Cerrar sesión</a>
      </div>
    </div>
  </div>
</div>

<!-- Save Form Modal-->
<div class="modal fade" id="SaveRegModal" tabindex="-1" role="dialog" aria-labelledby="saveModal" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="saveModal">Guardar y enviar</h5>
        <button class="close" type="button" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">×</span>
        </button>
      </div>
      <div class="modal-body">Los cambios se guardarán y enviarán. No podrás editar este registro luego.</div>
      <div class="modal-footer">
        <button type="button" id="confirmSubmit" class="btn btn-success btn-icon-split">
          <span class="icon text-white-50">
            <i class="fas fa-check"></i>
          </span>
          <span class="text">Confirmar</span>
        </button>
        <button class="btn btn-info" type="button" data-dismiss="modal">Seguir aquí</button>
      </div>
    </div>
  </div>
</div>

<!-- Bootstrap core JavaScript-->
<script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

<!-- Core plugin JavaScript-->
<script src="${pageContext.request.contextPath}/vendor/jquery-easing/jquery.easing.min.js"></script>

<!-- Custom scripts for all pages-->
<script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>

<!-- DataTables -->
<script src="${pageContext.request.contextPath}/vendor/datatables/jquery.dataTables.min.js"></script>
<link href="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.min.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.min.js"></script>

<script src="${pageContext.request.contextPath}/js/demo/datatables-demo.js"></script>

<script>
  // Evento para el botón de confirmación en el modal
  document.getElementById("confirmSubmit").addEventListener("click", function() {
    console.log("Confirmación en el modal. Se enviará el formulario con acción 'completado'.");
    var inputAccion = document.createElement("input");
    inputAccion.type = "hidden";
    inputAccion.name = "acto";
    inputAccion.value = "completado";
    document.getElementById("respuestaForm").appendChild(inputAccion);
    document.getElementById("respuestaForm").submit();
  });
</script>
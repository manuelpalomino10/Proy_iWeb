package com.example.unmujeres.filtros;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter("/*")
public class GlobalErrorHandlerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Wrapper personalizado
        ErrorCaptureResponseWrapper responseWrapper = new ErrorCaptureResponseWrapper(response);

        try {
            chain.doFilter(request, responseWrapper);

            // Manejar errores solo si la respuesta no está comprometida
            if (!responseWrapper.isCommitted()) {
                int status = responseWrapper.getStatus();

                if (status == HttpServletResponse.SC_NOT_FOUND ||
                        status == HttpServletResponse.SC_FORBIDDEN || status == HttpServletResponse.SC_BAD_REQUEST) {

                    prepareErrorPage(request, status);
                    request.getRequestDispatcher("/WEB-INF/views/errors/error-handler.jsp")
                            .forward(request, response);
                }
            }

        } catch (Exception e) {
            if (!responseWrapper.isCommitted()) {
                prepareErrorPage(request, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                request.getRequestDispatcher("/WEB-INF/views/errors/error-handler.jsp")
                        .forward(request, response);
            }
        }
    }

    private void prepareErrorPage(HttpServletRequest request, int errorCode) {
        request.setAttribute("errorCode", errorCode);

        switch(errorCode) {
            case 403:
                request.setAttribute("tituloError", "Acceso prohibido");
                request.setAttribute("mensajeError", "No tienes permisos para este recurso");
                request.setAttribute("iconoError", "fa-ban");
                break;

            case 404:
                request.setAttribute("tituloError", "Página no encontrada");
                request.setAttribute("mensajeError", "El recurso solicitado no existe");
                request.setAttribute("iconoError", "fas fa-file-circle-xmark");
                break;

            case 400:
                request.setAttribute("tituloError", "Solicitud inválida");
                request.setAttribute("mensajeError", "No se puede procesar su solicitud");
                request.setAttribute("iconoError", "fas fa-file-circle-xmark");
                break;

            default:
                request.setAttribute("tituloError", "Error del servidor");
                request.setAttribute("mensajeError", "Ocurrió un error inesperado");
                request.setAttribute("iconoError", "fa-exclamation-triangle");
        }
    }

    /**
     * Wrapper para capturar el estado de la respuesta sin comprometerla
     */
    private static class ErrorCaptureResponseWrapper extends HttpServletResponseWrapper {
        private int status = SC_OK;

        public ErrorCaptureResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public void sendError(int sc) throws IOException {
            this.status = sc;
            // No llamamos a super para evitar que Tomcat maneje el error
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {
            this.status = sc;
            // No llamamos a super para evitar que Tomcat maneje el error
        }

        @Override
        public void setStatus(int sc) {
            this.status = sc;
            super.setStatus(sc);
        }

        public int getStatus() {
            return this.status;
        }
    }
}
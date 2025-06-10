package com.example.unmujeres.servlets.coordinador;

@WebServlet("/encuestador")
public class EncuestadorServlet extends HttpServlet {

    private EncuestadorDAO dao;

    @Override
    public void init() {
        DataSource ds = /* obtén tu DataSource */;
        dao = new EncuestadorDAO(ds);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int coordiId = /* extrae del sessionScope: session.getAttribute("usuarioId") */;
        List<Encuestador> lista = dao.listarPorZona(coordiId);
        req.setAttribute("listaEncuestadores", lista);
        // Forward a tu JSP
        req.getRequestDispatcher("/gestion_encuestadores.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        int id = Integer.parseInt(req.getParameter("idusuario"));

        try {
            switch(action) {
                case "banear":
                    // estado 0 = baneado
                    dao.actualizarEstado(id, 0);
                    break;
                case "asignar":
                    String[] fids = req.getParameterValues("formularios");
                    List<Integer> lista = fids == null ? List.of() :
                            Arrays.stream(fids).map(Integer::parseInt).toList();
                    dao.asignarFormularios(id, lista, /* id del coordinador */);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Acción inválida");
                    return;
            }
            resp.sendRedirect(req.getContextPath()+"/encuestador");
        } catch(SQLException e) {
            throw new ServletException(e);
        }
    }
}

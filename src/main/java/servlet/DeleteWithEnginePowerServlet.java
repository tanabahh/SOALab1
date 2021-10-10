package servlet;

import exception.BadRequestException;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import service.VehicleService;
import utils.Validation;

@WebServlet(name = "deleteWithEnginePowerServlet", value = "/extra/delete")
public class DeleteWithEnginePowerServlet extends HttpServlet {
    private VehicleService service = new VehicleService();
    private Validation validation = new Validation();

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        try {
            long enginePower = validation.checkLong(req.getParameter("engine-power"), "engine-power");
            service.deleteWithEnginePower(enginePower);
        } catch (BadRequestException e) {
            req.setAttribute("error", e.getMessage());
            resp.setStatus(400);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/show-vehicle.jsp");
            dispatcher.forward(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/vehicle");
    }
}

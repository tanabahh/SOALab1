package servlet;

import exception.BadRequestException;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import service.VehicleService;
import utils.Validation;

@WebServlet(name = "deleteWithEnginePowerServlet", value = "/extra/deleteWithEnginePower")
public class DeleteWithEnginePowerServlet extends HttpServlet {
    private VehicleService service = new VehicleService();
    private Validation validation = new Validation();

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            long enginePower = validation.checkLong(req.getParameter("enginePower"), "enginePower");
            service.deleteWithEnginePower(enginePower);
        } catch (BadRequestException e) {
            req.setAttribute("error", e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/vehicle");
    }
}

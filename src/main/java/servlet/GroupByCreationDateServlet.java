package servlet;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.GroupByCreationDateDto;
import model.Vehicle;
import model.VehicleDto;
import service.VehicleService;
import utils.Validation;

@WebServlet(name = "groupByCreationDateServlet", value = "/extra/groupByCreationDate")
public class GroupByCreationDateServlet extends HttpServlet {

    private VehicleService service = new VehicleService();
    private Validation validation = new Validation();
    private Gson gson = new Gson();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        addHeaders(resp);
        resp.setHeader("Content-Type", "application/json; charset=UTF-16");

        PrintWriter writer = resp.getWriter();
        List<GroupByCreationDateDto> dto = null;
        try {
            dto = service.groupByCreationDate();
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            resp.setStatus(404);
        }
        writer.print(this.gson.toJson(dto));
        req.setAttribute("groups", dto);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/showVehicle.jsp");
        dispatcher.forward(req, resp);
    }

    private void addHeaders(HttpServletResponse resp) {
        resp.setHeader("Content-Type", "application/json");
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, HEAD, OPTIONS");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
    }
}

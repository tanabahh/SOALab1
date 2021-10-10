package servlet;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Vehicle;
import model.VehicleDto;
import service.VehicleService;
import utils.Validation;

@WebServlet(name = "getMaxCreatingDateServlet", value = "/extra/max-creation-date")
public class GetMaxCreatingDateServlet extends HttpServlet {

    private VehicleService service = new VehicleService();
    private Validation validation = new Validation();
    private Gson gson = new Gson();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        addHeaders(resp);
        resp.setHeader("Content-Type", "application/json; charset=UTF-16");

        PrintWriter writer = resp.getWriter();
        ArrayList<VehicleDto> dto = new ArrayList<VehicleDto>();
        try {
            Vehicle vehicle = service.getMaxCreatingDate();
            if (vehicle != null) {
                dto.add(new VehicleDto(vehicle));
            }
        } catch (Exception e) { //посмотреть какие ошибки
            req.setAttribute("error", e.getMessage());
            RequestDispatcher dispatcher = req.getRequestDispatcher("/show-vehicle.jsp");
            dispatcher.forward(req, resp);
            return;
        }
        writer.print(this.gson.toJson(dto));
        req.setAttribute("vehicle", dto);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/show-vehicle.jsp");
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

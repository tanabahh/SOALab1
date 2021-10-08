package servlet;

import com.google.gson.Gson;
import exception.BadRequestException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Coordinates;
import model.FuelType;
import model.Vehicle;
import model.VehicleDto;
import model.VehicleType;
import org.springframework.util.StringUtils;
import service.VehicleService;
import utils.Validation;

@WebServlet(name = "vehicleServlet", value = "/vehicle/*")
public class VehicleServlet extends HttpServlet {
    private final VehicleService service = new VehicleService();
    private final String[] names = {"id", "name", "creationDate", "enginePower", "type",
        "fuelType", "x", "y"};
    private final Validation validation = new Validation();
    private Gson gson = new Gson();

    public void init(ServletConfig servletConfig) {
        try {
            super.init(servletConfig);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        addHeaders(resp);

        resp.setHeader("Content-Type", "application/json; charset=UTF-16");

        String pageNumber = req.getParameter("page");
        String perPage = req.getParameter("perPage");

        String[] sortStateArray = null;
        if (!StringUtils.isEmpty(req.getParameter("sortState"))) {
            sortStateArray = req.getParameter("sortState").split(",");
        }
        Map<String, String[]> filterMap = req.getParameterMap().entrySet().stream()
            .filter(x -> Arrays.asList(names).contains(x.getKey()))
            .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        PrintWriter writer = resp.getWriter();
        List<VehicleDto> dto = null;
        try {
            List<Vehicle> vehicle = service.getVehicle(
                validation.checkPositiveIntOrNull(pageNumber, "pageNumber"),
                validation.checkPositiveIntOrNull(perPage, "perPage"),
                sortStateArray,
                filterMap
            );
            if (vehicle != null) {
                dto = vehicle.stream().map(
                    v -> new VehicleDto(v)
                ).collect(Collectors.toList());
            }
        } catch (BadRequestException e) {
            req.setAttribute("error", e.getMessage());
            resp.setStatus(404);
            writer.println(this.gson.toJson(e.getMessage()));
        }
        writer.print(this.gson.toJson(dto));
        //resp.sendRedirect("/showVehicle.jsp");
        //writer.flush();
        req.setAttribute("vehicle", dto);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/showVehicle.jsp");
        dispatcher.forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        System.out.println(req.getParameter("name"));
        System.out.println(req.getParameter("x"));
        req.setCharacterEncoding("utf8");
        addHeaders(resp);
        resp.setHeader("Content-Type", "application/json; charset=UTF-16LE");

        try {
            Integer x = validation.checkInteger(req.getParameter("x"), "x");
            Integer y = validation.checkIntegerNotNull(req.getParameter("y"), "y");
            VehicleType type = validation.checkType(req.getParameter("type"), "type");
            FuelType fuelType = validation.checkFuelType(req.getParameter("fuelType"), "fuelType");
            Vehicle vehicle = new Vehicle(
                validation.checkNotEmptyString(req.getParameter("name"), "name"),
                new Coordinates(x, y),
                validation.checkLong(req.getParameter("enginePower"), "enginePower"),
                type, fuelType);
            service.save(vehicle);
        } catch (BadRequestException e) {
            req.setAttribute("error", e.getMessage());
            resp.setStatus(404);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/showVehicle.jsp");
            dispatcher.forward(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/vehicle");

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        req.setCharacterEncoding("utf8");
        addHeaders(resp);
        resp.setHeader("Content-Type", "application/json; charset=UTF-16LE");

        try {
            Integer x = validation.checkInteger(req.getParameter("x"), "x");
            Integer y = validation.checkIntegerNotNull(req.getParameter("y"), "y");
            Integer id = validation.checkIntegerNotNull(req.getParameter("id"), "id");
            VehicleType type = validation.checkType(req.getParameter("type"), "type");
            FuelType fuelType = validation.checkFuelType(req.getParameter("fuelType"), "fuelType");
            Long enginePower = validation.checkLong(req.getParameter("enginePower"), "enginePower");
            service.update(id, req.getParameter("name"), x, y, type, fuelType, enginePower);
        } catch (BadRequestException e) {
            req.setAttribute("error", e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/vehicle");

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int id = validation.checkIntegerNotNull(req.getParameter("id"), "id");
            service.delete(id);
        } catch (BadRequestException e) {
            req.setAttribute("error", e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/vehicle");
    }

    private void addHeaders(HttpServletResponse resp) {
        resp.setHeader("Content-Type", "application/json");
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, HEAD, OPTIONS");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
    }
}

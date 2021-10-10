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
    private final String[] names = {"id", "name", "creation-date", "engine-power", "type",
        "fuel-type", "x", "y"};
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
        String perPage = req.getParameter("per-page");

        String[] sortStateArray = null;
        if (!StringUtils.isEmpty(req.getParameter("sort-state"))) {
            sortStateArray = req.getParameter("sort-state").split(",");
        }
        Map<String, String[]> filterMap = req.getParameterMap().entrySet().stream()
            .filter(x -> Arrays.asList(names).contains(x.getKey()))
            .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        PrintWriter writer = resp.getWriter();
        List<VehicleDto> dto = null;
        try {
            List<Vehicle> vehicle = service.getVehicle(
                validation.checkPositiveIntOrNull(pageNumber, "page-number"),
                validation.checkPositiveIntOrNull(perPage, "per-page"),
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
            resp.setStatus(400);
            writer.println(this.gson.toJson(e.getMessage()));
        }
        writer.print(this.gson.toJson(dto));
        //resp.sendRedirect("/show-vehicle.jsp");
        //writer.flush();
        req.setAttribute("vehicle", dto);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/show-vehicle.jsp");
        dispatcher.forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        req.setCharacterEncoding("utf8");
        addHeaders(resp);
        resp.setHeader("Content-Type", "application/json; charset=UTF-16LE");

        try {
            Integer x = validation.checkInteger(req.getParameter("x"), "x");
            Integer y = validation.checkIntegerNotNull(req.getParameter("y"), "y");
            VehicleType type = validation.checkType(req.getParameter("type"), "type");
            FuelType fuelType = validation.checkFuelType(req.getParameter("fuel-type"), "fuel-type");
            Vehicle vehicle = new Vehicle(
                validation.checkNotEmptyString(req.getParameter("name"), "name"),
                new Coordinates(x, y),
                validation.checkLong(req.getParameter("engine-power"), "engine-power"),
                type, fuelType);
            service.save(vehicle);
        } catch (BadRequestException e) {
            req.setAttribute("error", e.getMessage());
            resp.setStatus(400);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/show-vehicle.jsp");
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
            FuelType fuelType = validation.checkFuelType(req.getParameter("fuel-type"), "fuel-type");
            Long enginePower = validation.checkLong(req.getParameter("engine-power"), "engine-power");
            service.update(id, req.getParameter("name"), x, y, type, fuelType, enginePower);
        } catch (BadRequestException e) {
            resp.setCharacterEncoding("UTF-8");
            resp.sendError(400, e.getMessage());
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/vehicle");

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        try {
            int id = validation.checkIntegerNotNull(req.getParameter("id"), "id");
            service.delete(id);
        } catch (BadRequestException e) {
            resp.setCharacterEncoding("utf8");
            resp.sendError(400, e.getMessage());
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/vehicle");
    }

    private void addHeaders(HttpServletResponse resp) {
        resp.setHeader("Content-Type", "application/json; charset=UTF-8");
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET, POST, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
    }
}

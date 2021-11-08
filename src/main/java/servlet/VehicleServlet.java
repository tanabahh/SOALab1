package servlet;

import com.google.gson.Gson;
import exception.BadRequestException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import model.Coordinates;
import model.FuelType;
import model.Vehicle;
import model.VehicleDto;
import model.VehicleType;
import org.springframework.util.StringUtils;
import service.VehicleService;
import utils.Validation;

@Path( "/vehicle")
public class VehicleServlet {
    private final VehicleService service = new VehicleService();
    private final String[] names = {"id", "name", "creation-date", "engine-power", "type",
        "fuel-type", "x", "y"};
    private final Validation validation = new Validation();
    private Gson gson = new Gson();


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVehicle(
        @QueryParam("page") String pageNumber,
        @QueryParam("per-page") String perPage,
        @QueryParam("sort-state") String sortState,
        @Context UriInfo uriInfo) {

        String[] sortStateArray = null;
        if (!StringUtils.isEmpty(sortState)) {
            sortStateArray = sortState.split(",");
        }
        Map<String, String> filterMap = new HashMap<String, String>();
        if (uriInfo.getRequestUri().getQuery() != null) {
            String[] params = uriInfo.getRequestUri().getQuery().split("&");
            Arrays.stream(params).filter(x -> Arrays.asList(names).contains(x.split("=")[0]))
                .filter(x -> x.split("=").length > 1)
                .forEach(
                    x -> filterMap.put(x.split("=")[0], x.split("=")[1])
                );
        }
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
                    VehicleDto::new
                ).collect(Collectors.toList());
            }
        } catch (BadRequestException e) {
            String answer = this.gson.toJson(e.getMessage());
            return Response.status(400).entity(answer).build();
        }
        String answer = this.gson.toJson(dto);
        return Response.ok(answer).header("Access-Control-Allow-Origin", "*").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addVehicle(
        @QueryParam("name") String paramName,
        @QueryParam("x") String paramX,
        @QueryParam("y") String paramY,
        @QueryParam("engine-power") String paramEnginePower,
        @QueryParam("type") String paramType,
        @QueryParam("fuel-type") String paramFuelType,
        @Context UriInfo uriInfo
    ) {
        try {
            Integer x = validation.checkInteger(paramX, "x");
            Integer y = validation.checkIntegerNotNull(paramY, "y");
            VehicleType type = validation.checkType(paramType, "type");
            FuelType fuelType = validation.checkFuelType(paramFuelType, "fuel-type");
            Vehicle vehicle = new Vehicle(
                validation.checkNotEmptyString(paramName, "name"),
                new Coordinates(x, y),
                validation.checkLong(paramEnginePower, "engine-power"),
                type, fuelType);
            service.save(vehicle);
        } catch (BadRequestException e) {
            String answer = this.gson.toJson(e.getMessage());
            return Response.status(400).entity(answer).build();
        }
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateVehicle(
        @PathParam("id") Integer id,
        @QueryParam("name") String paramName,
        @QueryParam("x") String paramX,
        @QueryParam("y") String paramY,
        @QueryParam("engine-power") String paramEnginePower,
        @QueryParam("type") String paramType,
        @QueryParam("fuel-type") String paramFuelType,
        @Context UriInfo uriInfo
    ) {

        try {
            Vehicle vehicle = service.getById(id);
            Integer x = paramX == null ? vehicle.getCoordinates().getX() : validation.checkInteger(paramX, "x");
            Integer y = paramY == null ? vehicle.getCoordinates().getY() : validation.checkIntegerNotNull(paramY, "y");
            VehicleType type = paramType == null ? vehicle.getType() : validation.checkType(paramType, "type");
            FuelType fuelType = paramFuelType == null ? vehicle.getFuelType() : validation.checkFuelType(paramFuelType, "fuel-type");
            Long enginePower = paramEnginePower == null ? vehicle.getEnginePower() : validation.checkLong(paramEnginePower, "engine-power");
            String name = paramName == null ? vehicle.getName() : paramName;
            service.update(id, name, x, y, type, fuelType, enginePower);
        } catch (BadRequestException e) {
            String answer = this.gson.toJson(e.getMessage());
            return Response.status(400).entity(answer).build();
        }
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response deleteVehicle(
        @PathParam("id") Integer id,
        @Context UriInfo uriInfo
    ) {
        try {
            service.delete(id);
        } catch (BadRequestException e) {
            String answer = this.gson.toJson(e.getMessage());
            return Response.status(400).entity(answer).build();
        }
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }

}

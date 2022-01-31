package servlet;

import com.google.gson.Gson;
import exception.BadRequestException;

import java.util.*;
import java.util.stream.Collectors;
import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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

import model.*;
import org.springframework.util.StringUtils;
import service.VehicleServiceI;
import utils.Validation;

@Path( "/vehicle")
public class VehicleServlet {
    private final VehicleServiceI statelessRemoteBean = lookupRemoteStatelessBean();
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
            List<Vehicle> vehicle = statelessRemoteBean.getVehicle(
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
            Vehicle vehicle = statelessRemoteBean.getById(id);
            Integer x = paramX == null ? vehicle.getCoordinates().getX() : validation.checkInteger(paramX, "x");
            Integer y = paramY == null ? vehicle.getCoordinates().getY() : validation.checkIntegerNotNull(paramY, "y");
            VehicleType type = paramType == null ? vehicle.getType() : validation.checkType(paramType, "type");
            FuelType fuelType = paramFuelType == null ? vehicle.getFuelType() : validation.checkFuelType(paramFuelType, "fuel-type");
            Long enginePower = paramEnginePower == null ? vehicle.getEnginePower() : validation.checkLong(paramEnginePower, "engine-power");
            String name = paramName == null ? vehicle.getName() : paramName;
            statelessRemoteBean.update(id, name, x, y, type, fuelType, enginePower);
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
            statelessRemoteBean.delete(id);
            return Response.ok().header("Access-Control-Allow-Origin", "*").build();
        } catch (BadRequestException e) {
            String answer = this.gson.toJson(e.getMessage());
            return Response.status(400).entity(answer).build();
        }
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
            statelessRemoteBean.save(vehicle);
        } catch (BadRequestException e) {
            String answer = this.gson.toJson(e.getMessage());
            return Response.status(400).entity(answer).build();
        }
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }

    private static VehicleServiceI lookupRemoteStatelessBean() {
        final Hashtable<String, String> jndiProperties = new Hashtable<>();
        jndiProperties.put(javax.naming.Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        try {
            final javax.naming.Context context = new InitialContext(jndiProperties);
            // The app name is the application name of the deployed EJBs. This is typically the ear name
            // without the .ear suffix. However, the application name could be overridden in the application.xml of the
            // EJB deployment on the server.
            // Since we haven't deployed the application as a .ear, the app name for us will be an empty string
            final String appName = "global";
            // This is the module name of the deployed EJBs on the server. This is typically the jar name of the
            // EJB deployment, without the .jar suffix, but can be overridden via the ejb-jar.xml
            final String moduleName = "service_component-1.0-SNAPSHOT";
            // AS7 allows each deployment to have an (optional) distinct name. We haven't specified a distinct name for
            // our EJB deployment, so this is an empty string
            final String distinctName = "";
            // The EJB name which by default is the simple class name of the bean implementation class
            final String beanName = "VehicleService";
            // the remote view fully qualified class name
            final String viewClassName = VehicleServiceI.class.getName();
            // let's do the lookup
            String lookupName = "java:" + appName + "/" + moduleName + "/" + distinctName + "/" + beanName + "!" + viewClassName;
            return (VehicleServiceI) context.lookup(lookupName);
        } catch (NamingException e) {
            System.out.println("не получилось (");
            return new VehicleServiceI() {
                public List<Vehicle> getVehicle(Integer page, Integer perPage,
                                                String[] sortList, Map<String, String> filterMap){
                    throw new EJBException("ejb not available");
                }
                public void save(Vehicle vehicle){
                    throw new EJBException("ejb not available");
                }
                public Vehicle getById(Integer id){
                    throw new EJBException("ejb not available");
                }
                public void update(Integer id, String name, Integer x, Integer y,
                                   VehicleType type, FuelType fuelType, Long enginePower){
                    throw new EJBException("ejb not available");
                }
                public void delete(Integer id){
                    throw new EJBException("ejb not available");
                }
                public void deleteWithEnginePower(Long enginePower){
                    throw new EJBException("ejb not available");
                }
                public Vehicle getMaxCreatingDate(){
                    throw new EJBException("ejb not available");
                }
                public List<GroupByCreationDateDto> groupByCreationDate(){
                    throw new EJBException("ejb not available");
                }

            };
        }
    }

}

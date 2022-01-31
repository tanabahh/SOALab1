package servlet;

import com.google.gson.Gson;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import model.FuelType;
import model.GroupByCreationDateDto;
import model.Vehicle;
import model.VehicleType;
import service.VehicleServiceI;
import utils.Validation;

@Path( "/extra/group-by-creation-date")
public class GroupByCreationDateServlet {

    private final VehicleServiceI statelessRemoteBean = lookupRemoteStatelessBean();
    private final Validation validation = new Validation();
    private final Gson gson = new Gson();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response doGet(){
        List<GroupByCreationDateDto> dto = null;
        try {
            dto = statelessRemoteBean.groupByCreationDate();
        } catch (Exception e) {
            String answer = this.gson.toJson(e.getMessage());
            return Response.status(400).entity(answer).build();
        }
        String answer = this.gson.toJson(dto);
        return Response.ok(answer).build();
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
            // The EJB name which by default is the simple class name of the bean implementation class
            final String beanName = "VehicleService";
            // the remote view fully qualified class name
            final String viewClassName = VehicleServiceI.class.getName();
            // let's do the lookup
            String lookupName = "java:" + appName + "/" + moduleName + "/" + beanName + "!" + viewClassName;
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

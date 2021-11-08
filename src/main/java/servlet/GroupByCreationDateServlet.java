package servlet;

import com.google.gson.Gson;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import model.GroupByCreationDateDto;
import service.VehicleService;
import utils.Validation;

@Path( "/extra/group-by-creation-date")
public class GroupByCreationDateServlet {

    private final VehicleService service = new VehicleService();
    private final Validation validation = new Validation();
    private final Gson gson = new Gson();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response doGet(){
        List<GroupByCreationDateDto> dto = null;
        try {
            dto = service.groupByCreationDate();
        } catch (Exception e) {
            String answer = this.gson.toJson(e.getMessage());
            return Response.status(400).entity(answer).build();
        }
        String answer = this.gson.toJson(dto);
        return Response.ok(answer).build();
    }
}

package servlet;

import com.google.gson.Gson;
import java.util.ArrayList;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.Vehicle;
import model.VehicleDto;
import service.VehicleService;
import utils.Validation;

@Path("/extra/max-creation-date")
public class GetMaxCreatingDateServlet {

    private final VehicleService service = new VehicleService();
    private final Validation validation = new Validation();
    private final Gson gson = new Gson();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response doGet() {
        ArrayList<VehicleDto> dto = new ArrayList<VehicleDto>();
        try {
            Vehicle vehicle = service.getMaxCreatingDate();
            if (vehicle != null) {
                dto.add(new VehicleDto(vehicle));
            }
        } catch (Exception e) { //посмотреть какие ошибки
            String answer = this.gson.toJson(e.getMessage());
            return Response.status(400).entity(answer).build();
        }
        String answer = this.gson.toJson(dto);
        return Response.ok(answer).build();
    }
}

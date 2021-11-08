package servlet;

import com.google.gson.Gson;
import exception.BadRequestException;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import service.VehicleService;
import utils.Validation;

@Path("/extra/delete")
public class DeleteWithEnginePowerServlet{
    private final VehicleService service = new VehicleService();
    private final Validation validation = new Validation();
    private final Gson gson = new Gson();

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response doDelete(
        @QueryParam("engine-power")  String paramEnginePower
    ) {
        try {
            long enginePower = validation.checkLong(paramEnginePower, "engine-power");
            service.deleteWithEnginePower(enginePower);
        } catch (BadRequestException e) {
            String answer = this.gson.toJson(e.getMessage());
            return Response.status(400).entity(answer).build();
        }
        return Response.ok().build();
    }
}

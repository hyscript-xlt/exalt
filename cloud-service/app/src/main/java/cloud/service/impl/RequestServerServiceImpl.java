package cloud.service.impl;

import cloud.models.User;
import cloud.service.RequestsService;
import cloud.service.RequestServerService;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.BasicConfigurator;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Log4j
@Path("/request")
public class RequestServerServiceImpl implements RequestServerService {
    private static final RequestsService REQUEST = new RequestsServiceImpl();

    @POST
    @Override
    public Response postServerSize(User body) {
        BasicConfigurator.configure();

        Response response = REQUEST.validate(body);
        if (response.getStatus() == 200) {
            REQUEST.addToQueue(body);
        } else if (response.getStatus() == 201) {
            body.setUserId(response.getHeaderString("id"));
            REQUEST.addToQueue(body);
        }

        return response;
    }

    @Override
    public Response getServerIp(String userId) {
        return null;
    }

}

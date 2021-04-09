package cloud.service;

import cloud.models.User;

import javax.ws.rs.core.Response;

/**
 * The service is responsible for exposing the end users an endpoint <br/>
 * that will allow them to request a space from the server.
 */
public interface RequestServerService {
    /**
     * The request that will allocate a server by given size. <br/>
     * If the user exhausted his limit then the server will respond by appropriate message, <br/>
     * otherwise  it will send the IP address of allocated server.
     *
     * @param body contains info about desired size of the server
     */
    Response postServerSize(User body);

    /**
     * With each reserved server will be given server IP.
     *
     * @param userId that requested the new server
     * @return response that include info about Server IP.
     */
    Response getServerIp(String userId);

}

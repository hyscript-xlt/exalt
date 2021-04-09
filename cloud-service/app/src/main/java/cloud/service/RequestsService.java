package cloud.service;

import cloud.models.User;

import javax.ws.rs.core.Response;

/**
 * General service for validating user from DB and add them into Kafka queue. <br/>
 * <br/>
 * NOTE: If the queries count is going to increase in the future, <br/>
 * please consider having a dedicated service for each request.
 */
public interface RequestsService {

    /**
     * Validate user requests concerned with the user allocation of server size.
     *
     * @param user current user
     * @return Response whether the user request should be fulfilled or not
     */
    Response validate(User user);

    /**
     * If user request fulfilled it will be added to the queue and should be updated later.
     */
    void addToQueue(User user);
}

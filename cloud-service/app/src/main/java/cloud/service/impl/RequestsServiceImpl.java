package cloud.service.impl;

import cloud.clients.Client;
import cloud.config.ApplicationConfig;
import cloud.messaging.Producer;
import cloud.models.User;
import cloud.service.RequestsService;
import cloud.utils.RecordUtil;
import com.aerospike.client.Key;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Optional;
import java.util.UUID;

import static cloud.config.ApplicationConfig.safeGetDbName;

@Log4j
public class RequestsServiceImpl implements RequestsService {
    private static final Client CLIENT = Client.instanceOf();

    @Override
    public Response validate(User user) {
        User foundUser = Optional.ofNullable(user)
                .filter(u -> u.getUserId() != null && !u.getUserId().isEmpty())
                .map(u -> CLIENT.query(new Key(safeGetDbName(), getSetName(), u.getUserId())))
                .map(Optional::get)
                .map(e -> RecordUtil.recordTo(e, User.class))
                .orElse(null);

        if (foundUser != null && foundUser.getServerSize() + user.getServerSize() <= 100) {
            return Response.ok().build();
        } else if (user.getUserId().isEmpty() && user.getServerSize() <= 100) {
            String id = UUID.randomUUID().toString().substring(0, 10);
            return Response.status(Status.CREATED)
                    .entity("You have registered on the service, please get your server ip by following id: \n" + id)
                    .header("id", id)
                    .build();
        }

        return Response.status(Status.FORBIDDEN)
                .entity("Make sure you've not exceeded the allowed limit!").build();
    }

    @Override
    public void addToQueue(User user) {
        String topic = Try.of(ApplicationConfig::getRequestAllocationTopic).get();
        Producer.sendMessage(topic, user.getUserId(), user.getServerSize());
    }

    private String getSetName() {
        return Try.of(ApplicationConfig::getUserSetName)
                .onFailure(e -> log.error(e.getLocalizedMessage()))
                .get();
    }
}

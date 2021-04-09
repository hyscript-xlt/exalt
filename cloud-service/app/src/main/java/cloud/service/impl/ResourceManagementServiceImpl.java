package cloud.service.impl;

import cloud.clients.Client;
import cloud.models.User;
import cloud.service.ResourceManagementService;
import cloud.utils.CommonUtils;
import cloud.utils.RecordUtil;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import io.vavr.control.Try;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import javax.jws.WebService;
import java.util.Optional;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;

import static cloud.config.ApplicationConfig.safeGetDbName;
import static cloud.config.ApplicationConfig.safeGetUserSetName;


@WebService(serviceName = "management", endpointInterface = "cloud.service.ResourceManagementService")
public class ResourceManagementServiceImpl implements ResourceManagementService {
    /**
     * Imaginary Server pool from which should be allocated the servers for each user.
     */
    private static final AtomicInteger SERVER_POOL = new AtomicInteger(100);
    private Client client;
    private Spliterator<ConsumerRecord<String, String>> input;

    @Override
    public void init(Spliterator<ConsumerRecord<String, String>> in) {
        this.input = in;
        this.client = Client.newInstance();
    }

    @Override
    public void run() {
        if (input != null) {

            input.forEachRemaining(e -> {
                int size = Integer.parseInt(String.valueOf(e.value()));
                int poolSize = SERVER_POOL.intValue();

                if (poolSize - size < 0) {
                    int rem = poolSize - size;
                    requestNewServer();
                    SERVER_POOL.set(SERVER_POOL.intValue() + rem);
                } else {
                    SERVER_POOL.getAndSet(poolSize);
                }

                Optional<Record> userRec = client.query(new Key(safeGetDbName(), safeGetUserSetName(), e.key()));
                userRec.map(rec -> createUserFrom(rec, size))
                        .ifPresent(this::insertUser);
            });

            client.close();
            Thread.currentThread().interrupt();
        }
    }

    private void insertUser(User usr) {
        Key key = client.makeKeyDefaultDb(safeGetUserSetName(), usr.getUserId());
        client.insert(key, RecordUtil.classToBin(usr));
    }

    /**
     * Creates instance of {@link User} class from {@link Record}
     *
     * @param rec        to be converted
     * @param serverSize requested server size
     * @return converted user
     */
    private User createUserFrom(Record rec, int serverSize) {
        User user = RecordUtil.recordTo(rec, User.class);
        user.setServerSize(user.getServerSize() + serverSize);
        user.setServerIp(CommonUtils.generateRandomIp());
        return user;
    }

    /**
     * Imitation of the spinning new server pool from the cloud.
     */
    private synchronized void requestNewServer() {
        if (SERVER_POOL.intValue() != 100) {
            SERVER_POOL.set(100);
            Try.run(() -> Thread.sleep(20_000));
        }
    }
}

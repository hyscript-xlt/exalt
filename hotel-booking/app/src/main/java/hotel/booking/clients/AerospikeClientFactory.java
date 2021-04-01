package hotel.booking.clients;

import com.aerospike.client.AerospikeClient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

import static hotel.booking.config.ApplicationConfig.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AerospikeClientFactory {
    private final static AerospikeClient client = new AerospikeClient(getDbHost(), getDbPort());

    public static Optional<AerospikeClient> ofSingleInstance() {
        if (client.isConnected()) {
            return Optional.of(client);
        }
        return Optional.empty();
    }

    public static Optional<AerospikeClient> ofInstance() {
        AerospikeClient cl = new AerospikeClient(getDbHost(), getDbPort());
        if(cl.isConnected()) {
            return Optional.of(cl);
        }
        return Optional.empty();
    }

    public static AerospikeClient ofInstance(String host, int port) {
        return new AerospikeClient(host, port);
    }

    public static void close() {
        if(client.isConnected()) {
            client.close();
        }
    }

}

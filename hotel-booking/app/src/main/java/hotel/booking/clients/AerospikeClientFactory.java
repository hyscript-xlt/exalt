package hotel.booking.clients;

import com.aerospike.client.AerospikeClient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

import static hotel.booking.config.ApplicationConfig.getDbHost;
import static hotel.booking.config.ApplicationConfig.getDbPort;

/**
 * Dedicated class for instantiation {@link AerospikeClient}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AerospikeClientFactory {
    private final static AerospikeClient client = new AerospikeClient(getDbHost(), getDbPort());

    /**
     * Factory method that always return the same {@link #client} <br/>
     * default hostname and port indicated on {@link hotel.booking.config.ApplicationConfig}.
     *
     * @return {@link #client} wrapped into {@link Optional}
     */
    public static Optional<AerospikeClient> ofSingleInstance() {
        if (client.isConnected()) {
            return Optional.of(client);
        }
        return Optional.empty();
    }

    /**
     * Every new call is result of a new instance of the {@link AerospikeClient} </br>
     * which uses the default hostname and port indicated on {@link hotel.booking.config.ApplicationConfig}.
     *
     * @return {@link #client} wrapped into {@link Optional}
     */
    public static Optional<AerospikeClient> ofNewInstance() {
        AerospikeClient cl = new AerospikeClient(getDbHost(), getDbPort());
        if (cl.isConnected()) {
            return Optional.of(cl);
        }
        return Optional.empty();
    }

    /**
     * Creates new {@link AerospikeClient} by predefined hostname and port.
     *
     * @param host of running Aerospike DB
     * @param port of running Aerospike DB
     * @return new instance of {@link AerospikeClient}
     */
    public static AerospikeClient ofInstance(String host, int port) {
        return new AerospikeClient(host, port);
    }

    /**
     * Closes the connection of {@link #client}
     */
    public static void close() {
        if (client.isConnected()) {
            client.close();
        }
    }

}

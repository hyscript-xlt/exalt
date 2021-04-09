package cloud.clients;

import cloud.exceptions.AppConfigRuntimeExceptions;
import com.aerospike.client.AerospikeClient;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

import static cloud.config.ApplicationConfig.getDbHost;
import static cloud.config.ApplicationConfig.getDbPort;

/**
 * Dedicated class for instantiation {@link AerospikeClient}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AerospikeClientFactory {
    private final static AerospikeClient client = Try.of(() -> new AerospikeClient(getDbHost(), getDbPort()))
            .getOrElseThrow(e -> new AppConfigRuntimeExceptions("Please make sure that your DB is up & running." +
                    e.getLocalizedMessage()));

    /**
     * Factory method that always return the same {@link #client} <br/>
     * default hostname and port indicated on {@link cloud.config.ApplicationConfig}.
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
     * which uses the default hostname and port indicated on {@link cloud.config.ApplicationConfig}.
     *
     * @return {@link #client} wrapped into {@link Optional}
     */
    public static Optional<AerospikeClient> ofNewInstance() {
        AerospikeClient cl = Try.of(() -> new AerospikeClient(getDbHost(), getDbPort()))
                .getOrElseThrow(e -> new AppConfigRuntimeExceptions("Please make sure that your DB is up & running." +
                        e.getLocalizedMessage()));
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

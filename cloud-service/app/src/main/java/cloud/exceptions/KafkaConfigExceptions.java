package cloud.exceptions;

/**
 * This class is served as wrapper of the {@link Exception} class. <br/>
 * The purpose is to provide clear messages to the developer to understand the real root cause of the failure. <br/>
 * The exception will be thrown if the asked resource is not available by given URI.
 */
public class KafkaConfigExceptions extends Exception {

    public KafkaConfigExceptions(String message) {
        super(message);
    }
}

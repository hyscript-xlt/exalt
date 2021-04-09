package cloud.exceptions;

/**
 * This class is served as wrapper of the {@link RuntimeException} class. <br/>
 * The purpose is to be thrown if the resource is unavailable at the runtime <br/>
 * The exception will be thrown if the resource that throws {@link KafkaConfigExceptions} failed at the run time.
 */
public class KafkaRuntimeExceptions extends RuntimeException {

    public KafkaRuntimeExceptions(String message) {
        super(message);
    }
}

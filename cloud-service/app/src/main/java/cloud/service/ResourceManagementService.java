package cloud.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.Spliterator;

/**
 * The web service is responsible for the request consumptions from Kafka.  <br/>
 * The Spliterator is not thread-safe;  <br/>
 * besides, multiple threads should have a different range of Spliterator <br/>
 * so it is a must to have separate instances for each thread.
 */
@SOAPBinding(style = SOAPBinding.Style.RPC)
@WebService
public interface ResourceManagementService {

    /**
     * Initialize the given range of {@link Spliterator} that will be consumed later.
     *
     * @param in the Spliterator with range from 'x' to 'y' that should be consumed <br/>
     *           by the given {@link ResourceManagementService} instance
     */
    @WebMethod
    void init(Spliterator<ConsumerRecord<String, String>> in);

    /**
     * Consumes the messages from the {@link Spliterator}.
     */
    @WebMethod
    void run();
}

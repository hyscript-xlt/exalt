package cloud.service;

import cloud.config.ApplicationConfig;
import cloud.messaging.Consumer;
import cloud.service.impl.ResourceManagementServiceImpl;
import lombok.extern.log4j.Log4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;

import static cloud.config.ApplicationConfig.safeGetRequestAllocationTopic;

/**
 * The service is async and runs independently from the main application. <br/>
 * The service is being called every 20 seconds automatically to check whether there are messages to consume. <br/>
 * If the broker is not empty, the method splits the messages into several parts and consumes  <br/>
 * all the messages in a multi-threaded manner.
 */
@Log4j
@Startup
@Singleton
class DaemonResourceService {
    KafkaConsumer<String, String> consumer = Consumer.instanceOf(safeGetRequestAllocationTopic());

    /**
     * The service is being called every 20 seconds automatically by this method. <br/>
     * It reads from Kafka topic the messages and split them into at most the size of <br/>
     * {@link ApplicationConfig#getMaxThreadPoolSize()}
     *
     * @param timer that counts the time when the service is getting called
     */
    @Schedule(hour = "*", minute = "*", second = "*/20", info = "Every 20 seconds timer")
    void automaticallyScheduled(Timer timer) {
        Thread thread = new Thread(() -> {
            Thread.currentThread().setName("Background");
            Iterable<ConsumerRecord<String, String>> rec = consumer.poll(Duration.ofSeconds(5))
                    .records(safeGetRequestAllocationTopic());

            List<Spliterator<ConsumerRecord<String, String>>> splitContent = splitConsumer(rec);
            splitContent.stream()
                    .filter(Objects::nonNull)
                    .map(this::makeService)
                    .map(this::createThread)
                    .forEach(Thread::start);

            Thread.currentThread().interrupt();
        });
        thread.start();
        log.info("The background thread have been started.");
    }

    private Thread createThread(ResourceManagementService service) {
        return new Thread(service::run);
    }

    /**
     * Creates separate instances of {@link ResourceManagementService}. <br/>
     * Each one will be used in a different thread.
     *
     * @param e the spliterator which shows the range of consuming messages.
     * @return read {@link ResourceManagementService} for more info.
     */
    private ResourceManagementService makeService(Spliterator<ConsumerRecord<String, String>> e) {
        ResourceManagementService service = new ResourceManagementServiceImpl();
        service.init(e);
        return service;
    }

    /**
     * Splits {@link ConsumerRecord} iterable into at most {@link ApplicationConfig#getMaxThreadPoolSize()} parts.
     *
     * @param in the first requested result
     * @return list of split spliterators
     */
    private List<Spliterator<ConsumerRecord<String, String>>> splitConsumer(Iterable<ConsumerRecord<String, String>> in) {
        int i = 0;
        Spliterator<ConsumerRecord<String, String>> rec = in.spliterator().trySplit();
        List<Spliterator<ConsumerRecord<String, String>>> container = new ArrayList<>(); //
        container.add(rec);
        while (rec != null && i < ApplicationConfig.safeGetMaxThreadPoolSize()) {
            Spliterator<ConsumerRecord<String, String>> current = rec.trySplit();
            container.add(current);
            rec = current;
            i++;
        }
        return container;
    }
}

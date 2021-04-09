package cloud.messaging;

import cloud.config.KafkaConfig;
import cloud.exceptions.KafkaRuntimeExceptions;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A wrapper for {@link KafkaConsumer} client that consumes records from a Kafka cluster
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Consumer {

    /**
     * Creates KafkaConsumer and subscribes to the given topic.
     *
     * @param topic the topic name
     * @return {@link KafkaConsumer}
     */
    public static KafkaConsumer<String, String> instanceOf(String topic) {
        Properties props = Try.of(KafkaConfig::consumer)
                .getOrElseThrow(() -> new KafkaRuntimeExceptions("Something wrong with KafkaConfig::consumer"));
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Stream.of(topic).collect(Collectors.toList()));

        return consumer;
    }
}

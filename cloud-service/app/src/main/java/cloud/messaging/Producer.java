package cloud.messaging;

import cloud.config.KafkaConfig;
import cloud.exceptions.KafkaRuntimeExceptions;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * A wrapper for {@link KafkaProducer} client that publishes records to the Kafka cluster.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Producer {
    /**
     * As it is stated on {@link KafkaProducer} is thread safe and sharing a single producer instance across threads
     * will generally be faster than having multiple instances.
     */
    private static final KafkaProducer<String, Integer> producer = new KafkaProducer<>(
            Try.of(KafkaConfig::producer)
                    .getOrElseThrow(() -> new KafkaRuntimeExceptions("Something wrong with KafkaConfig::producer"))
    );

    /**
     * Wrapper for {@link KafkaProducer#send(ProducerRecord)} which asynchronously send a record to a topic.
     *
     * @param topic name where the records are stored and published.
     * @param key   the message key
     * @param value the message value
     */
    public static void sendMessage(String topic, String key, int value) {
        producer.send(new ProducerRecord<>(topic, key, value));
    }
}

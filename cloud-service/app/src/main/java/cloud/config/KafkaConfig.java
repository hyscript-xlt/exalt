package cloud.config;

import cloud.exceptions.KafkaConfigExceptions;

import java.util.Properties;

/**
 * Dedicated class for the Kafka consumer and producer. <br/>
 * It contains all vital configuration properties <br/>
 * that is a must for the instantiation Consumer or Producer.<br/>
 * Before changing anything please be sure that you don't brake the application.
 */
public class KafkaConfig extends BaseConfig {
    private static Properties consumer;
    private static Properties producer;

    /**
     * Used for both consumer and producer to read the properties files.
     * For more info please refer to {@link BaseConfig#readProperties(String, Class, Class)} method.
     *
     * @param p         NOTE: Please consider changing this comment if there will be more than one consumer and one producer<br/>
     *                  currently the value of p might be either Consumer or Producer.
     * @param configUri the name of property file without extension
     * @return the {@link Properties} instance that have been load the .properties' file
     * @throws KafkaConfigExceptions if the file cannot be found by given URI
     */
    private static Properties props(Properties p, String configUri) throws KafkaConfigExceptions {
        if (p == null) {
            p = readProperties(configUri, KafkaConfigExceptions.class, KafkaConfig.class);
        }
        return p;
    }

    /**
     * Standard getter for consumer properties. Please refer {@link #props(Properties, String)} for more info.
     */
    public static Properties consumer() throws KafkaConfigExceptions {
        consumer = props(consumer, "consumerkafka");
        return consumer;
    }

    /**
     * Standard getter for producer properties. Please refer {@link #props(Properties, String)} for more info.
     */
    public static Properties producer() throws KafkaConfigExceptions {
        producer = props(producer, "producerkafka");
        return producer;
    }

}

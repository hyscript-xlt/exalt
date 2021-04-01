package hotel.booking.config;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationConfigTest {

    @Test
    void shouldReadPropertiesCorrectly() {
        //When
        Properties props = ApplicationConfig.getProperties();

        //Then
        assertThat(props).isNotNull();
    }

    @Test
    void shouldReturnPortProperly() {
        //When
        int port = ApplicationConfig.getDbPort();

        //Then
        assertThat(port).isGreaterThan(1000);
    }
}
package hotel.booking.config;

import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationConfig {
    private static Properties properties;

    /**
     * Binds application.properties to the current class.
     *
     * @return {@link Properties}
     */
    //Visible for testing
    static Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            InputStream stream = ApplicationConfig.class
                    .getClassLoader()
                    .getResourceAsStream("application.properties");
            Try.run(() -> properties.load(stream)).onFailure(e -> System.out.println(e.getLocalizedMessage()));
        }
        return properties;
    }

    /**
     * Standard getter for host of the database.
     *
     * @return string value of the host
     */
    public static String getDbHost() {
        return getProperties().getProperty("db.host");
    }

    /**
     * Standard getter for the host port.
     *
     * @return port parsed as integer
     */
    public static int getDbPort() {
        return Try.of(() -> getProperties().getProperty("db.port"))
                .mapTry(Integer::parseInt)
                .onFailure(e -> System.out.println(e.getLocalizedMessage()))
                .get();
    }

    /**
     * Standard getter for database name.
     *
     * @return string value of the DB name
     */
    public static String getDbName() {
        return getProperties().getProperty("app.db.name");
    }

    /**
     * Standard getter for {@link hotel.booking.repository.Customer} table name.
     *
     * @return string value of the table name
     */
    public static String getCustomerSetName() {
        return getProperties().getProperty("customers.set.name");
    }

    /**
     * Standard getter for {@link hotel.booking.repository.Hotel} table name.
     *
     * @return string value of the table name
     */
    public static String getHotelSetName() {
        return getProperties().getProperty("hotel.set.name");
    }

    /**
     * Standard getter for {@link hotel.booking.repository.Reservation} table name.
     *
     * @return string value of the table name
     */
    public static String getReservationSetName() {
        return getProperties().getProperty("reservation.set.name");
    }

}

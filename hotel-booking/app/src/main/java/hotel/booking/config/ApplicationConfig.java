package hotel.booking.config;

import io.vavr.control.Try;

import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfig {
    private static Properties properties;
    private ApplicationConfig() {

    }

    //Visible for testing
    static Properties getProperties() {
        if(properties == null) {
            properties = new Properties();
            InputStream stream = ApplicationConfig.class
                    .getClassLoader()
                    .getResourceAsStream("application.properties");
            Try.run(() -> properties.load(stream)).onFailure(e -> System.out.println(e.getLocalizedMessage()));
        }
        return properties;
    }

    public static String getDbHost() {
        return getProperties().getProperty("db.host");
    }

    public static int getDbPort() {
        Integer integer = Try.of(() -> getProperties().getProperty("db.port"))
                .mapTry(Integer::parseInt)
                .onFailure(e -> System.out.println(e.getLocalizedMessage()))
                .get();
        return integer;
    }

    public static String getDbName() {
        return getProperties().getProperty("app.db.name");
    }

    public static String getCustomerSetName() {
        return getProperties().getProperty("customers.set.name");
    }

    public static String getHotelSetName() {
        return getProperties().getProperty("hotel.set.name");
    }

    public static String getRoomSetName() {
        return getProperties().getProperty("room.set.name");
    }

    public static String getReservationSetName() {
        return getProperties().getProperty("reservation.set.name");
    }

}

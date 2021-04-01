package hotel.booking.service.impl;

import com.aerospike.client.Key;
import com.aerospike.client.Record;
import hotel.booking.clients.Client;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The following services shares common operations, <br/>
 * to avoid code duplication the shared part represented here. <br/>
 * {@link hotel.booking.service.CustomerService}, <br/>
 * {@link hotel.booking.service.HotelService}, <br/>
 * {@link hotel.booking.service.ReservationService}
 *
 * @param <T> can be any POJO that used to unmarshal the requests
 */
abstract class CoreService<T> {

    /**
     * Performs query by key and converts the response {@link com.aerospike.client.Record} <br/>
     * to the desired class by {@link RecordUtil#recordTo(Record, Class)} method.
     *
     * @param id    is the value of the primary key for the desired record. <br/>
     *              The param is getting converted to the Key {@link com.aerospike.client.Key} <br/>
     *              by the {@link Client#makeKeyDefaultDb(String, String)} method.
     * @param clazz the desired class which is used by {@link RecordUtil#recordTo(Record, Class)}</br>
     *              to convert the results from {@link Client#query(Key)}
     * @return the converted results wrapped into {@link Optional}
     */
    Optional<T> getById(String id, Class<T> clazz) {
        return getClient().query(getClient().makeKeyDefaultDb(getSetName(), id))
                .map(record -> RecordUtil.recordTo(record, clazz));
    }

    /**
     * Converts the query result into given class type.
     *
     * @param name  of the key
     * @param value corresponding to the key
     * @param clazz the class the response should be converted
     * @return List of clazz
     */
    List<T> getBy(String name, String value, Class<T> clazz) {
        return getClient().findByStringValue(getSetName(), name, value)
                .stream()
                .map(e -> e.record)
                .map(e -> RecordUtil.recordTo(e, clazz))
                .collect(Collectors.toList());
    }

    /**
     * Converts the query result into given class type.
     *
     * @param name  of the key
     * @param value corresponding to the key
     * @param clazz the class the response should be converted
     * @return List of clazz
     */
    List<T> getBy(String name, long value, Class<T> clazz) {
        return getClient().findByLongValue(getSetName(), name, value)
                .stream()
                .map(e -> e.record)
                .map(e -> RecordUtil.recordTo(e, clazz))
                .collect(Collectors.toList());
    }

    /**
     * @return the set name(table name) of the implementing class.
     */
    abstract String getSetName();

    /**
     * @return the {@link Client} of the implementing class.
     */
    abstract Client getClient();
}

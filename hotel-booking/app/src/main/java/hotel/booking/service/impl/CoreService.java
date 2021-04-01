package hotel.booking.service.impl;

import com.aerospike.client.Key;
import hotel.booking.clients.Client;
import hotel.booking.repository.Customer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


abstract class CoreService<T> {

    Optional<T> getById(String id, Class<T> clazz) {
        return  getClient().query(getClient().makeKeyDefaultHost(getSetName(), id))
                .map(record -> RecordUtil.recordTo(record, clazz));
    }

    List<T> getBy(String name, String value, Class<T> clazz) {
        return getClient().findByStringValue(getSetName(), name, value)
                .stream()
                .map(e -> e.record)
                .map(e -> RecordUtil.recordTo(e, clazz))
                .collect(Collectors.toList());
    }

    List<T> getBy(String name, long value, Class<T> clazz) {
        return getClient().findByLongValue(getSetName(), name, value)
                .stream()
                .map(e -> e.record)
                .map(e -> RecordUtil.recordTo(e, clazz))
                .collect(Collectors.toList());
    }

    abstract String getSetName();
    abstract Client getClient();
}

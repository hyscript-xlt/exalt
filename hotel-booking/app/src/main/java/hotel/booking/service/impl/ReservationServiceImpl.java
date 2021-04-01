package hotel.booking.service.impl;

import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import hotel.booking.clients.Client;
import hotel.booking.repository.Reservation;
import hotel.booking.repository.Status;
import hotel.booking.service.ReservationService;

import javax.jws.WebService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static hotel.booking.config.ApplicationConfig.getReservationSetName;

@WebService(serviceName = "reservation", endpointInterface = "hotel.booking.service.ReservationService")
public class ReservationServiceImpl extends CoreService<Reservation> implements ReservationService {

    @Override
    public Reservation create(Reservation reservation) {
        String uuid = UUID.randomUUID().toString();
        Key key = getClient().makeKeyDefaultDb(getSetName(), uuid);
        Bin[] arr = Stream.of(
                new Bin("id", uuid),
                new Bin("customerId", reservation.getCustomerId()),
                new Bin("roomId", reservation.getRoomId()),
                new Bin("hotelId", reservation.getHotelId()),
                new Bin("startDate", reservation.getStartDate()),
                new Bin("endDate", reservation.getEndDate()),
                new Bin("status", reservation.getStatus())
        ).toArray(Bin[]::new);
        getClient().insert(key, arr);
        return reservation;
    }

    @Override
    public Reservation getByEntityRelationId(String id) {
        return getById(id, Reservation.class).orElse(null);
    }

    @Override
    public List<Reservation> getByStatus(Status value) {
        return getBy("status", value.name(), Reservation.class);
    }

    @Override
    public List<Reservation> getByNumericValue(String name, long value) {
        return getBy(name, value, Reservation.class);
    }

    @Override
    public List<Reservation> getByStringValue(String name, String value) {
        return getBy(name, value, Reservation.class);
    }

    @Override
    public boolean update(Reservation instance) {
        Reservation foundCustomer = getByEntityRelationId(instance.getId());
        if (foundCustomer != null) {
            //be careful here always update the ID with existing one -> CODE SMELL
            instance.setId(foundCustomer.getId());
            RecordUtil.updateExisting(foundCustomer, instance);
            Key key = getClient().makeKeyDefaultDb(getSetName(), foundCustomer.getId());
            getClient().update(key, RecordUtil.classToBin(foundCustomer));
            return true;
        }
        return false;
    }

    @Override
    public boolean removeBy(String id) {
        Key key = getClient().makeKeyDefaultDb(getSetName(), id);
        getClient().delete(key);
        return true;
    }

    @Override
    String getSetName() {
        return getReservationSetName();
    }

    @Override
    Client getClient() {
        return Client.instanceOf();
    }
}

package hotel.booking.service.impl;

import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import hotel.booking.clients.Client;
import hotel.booking.repository.Hotel;
import hotel.booking.service.HotelService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static hotel.booking.config.ApplicationConfig.getHotelSetName;


public class HotelServiceImpl extends CoreService<Hotel> implements HotelService {

    @Override
    public Hotel create(Hotel instance) {
        Bin[] bins = Stream.of(
                new Bin("id", instance.getId()),
                new Bin("name", instance.getName()),
                new Bin("country", instance.getCountry()),
                new Bin("address", instance.getAddress()),
                new Bin("rooms", instance.getRooms())
        ).toArray(Bin[]::new);
        Key key = getClient().makeKeyDefaultHost(getSetName(), String.valueOf(instance.getId()));
        getClient().insert(key, bins);
        return instance;
    }

    @Override
    public Optional<Hotel> getById(String id) {
        return getById(id, Hotel.class);
    }

    @Override
    public List<Hotel> getBy(String setName, String name, String value) {
        return getBy(name, value, Hotel.class);
    }

    @Override
    public boolean update(Hotel hotel) {
        Optional<Hotel> foundHotel = getById(hotel.getId());
        if (foundHotel.isPresent()) {
            Hotel tobeUpdated = foundHotel.get();
            //be careful here always update the ID with existing one -> CODE SMELL
            hotel.setId(tobeUpdated.getId());
            RecordUtil.updateExisting(tobeUpdated, hotel);
            Key key = getClient().makeKeyDefaultHost(getSetName(), tobeUpdated.getId());
            getClient().update(key, RecordUtil.classToBin(tobeUpdated));
            return true;
        }

        return false;
    }

    @Override
    String getSetName() {
        return getHotelSetName();
    }

    @Override
    Client getClient() {
        return Client.instanceOf();
    }
}

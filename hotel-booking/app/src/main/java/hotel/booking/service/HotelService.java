package hotel.booking.service;

import hotel.booking.repository.Hotel;

import java.util.List;
import java.util.Optional;

public interface HotelService {
    Hotel create(Hotel hotel);
    Optional<Hotel> getById(String id);
    List<Hotel> getBy(String setName, String name, String value);
    boolean update(Hotel hotel);
}

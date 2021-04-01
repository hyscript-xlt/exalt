package hotel.booking.repository;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class Hotel {
    private String id = UUID.randomUUID().toString();
    private String name;
    private String country;
    private String address;
    private List<Room> rooms;
}

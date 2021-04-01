package hotel.booking.repository;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class Room implements Serializable {
    private String id = UUID.randomUUID().toString();
    private int number;
    private int floor;
    private Status status;
}

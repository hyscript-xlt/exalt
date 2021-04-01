package hotel.booking.service.impl;

import hotel.booking.repository.Hotel;
import hotel.booking.repository.Room;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecordUtilTest {

    @Test
    void shouldUpdateProperly() {
        //Given
        Hotel existing = new Hotel();
        existing.setCountry("oldCountry");
        existing.setName("oldName");
        existing.setAddress("oldAddress");

        Hotel update = new Hotel();
        update.setId(existing.getId());
        update.setCountry("newCountry");
        update.setName("newName");
        update.setAddress("newAddress");
        List<Room> rooms = new ArrayList<>();
        update.setRooms(rooms);

        Hotel expected = new Hotel();
        expected.setId(existing.getId());
        expected.setCountry(update.getCountry());
        expected.setName(update.getName());
        expected.setAddress(update.getAddress());
        expected.setRooms(rooms);

        //When
       RecordUtil.updateExisting(existing, update);

        //Then
        Assertions.assertThat(existing).isEqualTo(expected);
    }
}
package hotel.booking.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Transient;
import javax.xml.bind.annotation.*;
import java.util.UUID;

@Data
//@XmlRootElement(name = "arg0")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Reservation {
    private String id;
    @XmlElement
    private Status status;
    @XmlElement
    private long startDate;
    @XmlElement
    private long endDate;
    @XmlElement
    private String roomId;
    @XmlElement
    private String hotelId;
    @XmlElement
    private String customerId;
}

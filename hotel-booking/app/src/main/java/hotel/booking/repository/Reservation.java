package hotel.booking.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Serves as relationship between Customer and Hotel.
 * Keeps all necessary information about the booking,
 * The endpoint {@link hotel.booking.service.impl.ReservationServiceImpl} exposes SOAP web service.
 * {@link XmlElement} used for unmarshalling the XML requests.
 */
@Data
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

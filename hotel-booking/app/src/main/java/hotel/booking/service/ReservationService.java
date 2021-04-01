package hotel.booking.service;

import hotel.booking.repository.Reservation;
import hotel.booking.repository.Status;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static javax.jws.soap.SOAPBinding.Style;


@SOAPBinding(style = Style.RPC)
@WebService
public interface ReservationService {
    @WebMethod
    Reservation create(@WebParam(name = "reservation")  Reservation instance);

    @WebMethod
    Reservation getByEntityRelationId(@WebParam(name = "id") String id);

    @WebMethod
    List<Reservation> getByStringValue(@WebParam(name = "key")  String name, @WebParam(name = "value")  String value);

    @WebMethod
    List<Reservation> getByNumericValue(@WebParam(name = "key")  String name, @WebParam(name = "value")  long value);

    @WebMethod
    List<Reservation> getByStatus(@WebParam(name = "status")  Status value);

    @WebMethod
    boolean update(@WebParam(name = "reservation")  Reservation instance);

    @WebMethod
    boolean removeBy(String id);
}

package hotel.booking.service;

import hotel.booking.repository.Reservation;
import hotel.booking.repository.Status;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

import static javax.jws.soap.SOAPBinding.Style;


@SOAPBinding(style = Style.RPC)
@WebService
public interface ReservationService {

    /**
     * Create the hotel from the request.
     */
    @WebMethod
    Reservation create(@WebParam(name = "reservation") Reservation instance);

    /**
     * Make query by relation Id.
     *
     * @return found Reservation object
     */
    @WebMethod
    Reservation getByEntityRelationId(@WebParam(name = "id") String id);

    /**
     * The method is intended to query by the key/value pairs where the value is string.
     *
     * @param name  corresponds to the key/attribute
     * @param value the value of given key
     * @return List of matched results.
     */
    @WebMethod
    List<Reservation> getByStringValue(@WebParam(name = "key") String name, @WebParam(name = "value") String value);

    /**
     * The method is intended to query by the key/value pairs where the value is numeric.
     *
     * @param name  corresponds to the key/attribute
     * @param value the value of given key
     * @return List of matched results.
     */
    @WebMethod
    List<Reservation> getByNumericValue(@WebParam(name = "key") String name, @WebParam(name = "value") long value);

    /**
     * The method is intended to query by 'status' field {@link Status}.
     *
     * @param value the value of the status
     * @return List of matched results.
     */
    @WebMethod
    List<Reservation> getByStatus(@WebParam(name = "status") Status value);

    /**
     * Updates existing relation.
     *
     * @param instance the info that should be replace the existing one
     * @return true if update is being successful, false otherwise
     */
    @WebMethod
    boolean update(@WebParam(name = "reservation") Reservation instance);

    /**
     * Delete by id.
     *
     * @return true if update is being successful, false otherwise
     */
    @WebMethod
    boolean removeBy(String id);
}

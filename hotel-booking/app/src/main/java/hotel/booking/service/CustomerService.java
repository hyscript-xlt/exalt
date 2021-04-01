package hotel.booking.service;

import hotel.booking.repository.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    /**
     * Create the customer from the request.
     */
    Customer create(Customer customer);

    /**
     * The method query the customers by id; the result might be null. <br/>
     * To avoid {@link NullPointerException} the value wrapped into {@link Optional} <br/>
     *
     * @param id of the customer
     * @return the customer from db, if no such customer exists return {@link Optional#empty()}
     */
    Optional<Customer> getById(String id);

    /**
     * Make query by given key/value pair. <br/>
     * Where name corresponds to the key(or attribute in case of XML) and value to the value of given key.
     *
     * @param setName equivalent to the table name in RDBMS
     * @param name    key or attribute
     * @param value   value of given key or attribute
     * @return List of customers that corresponds the given query
     */
    List<Customer> getBy(String setName, String name, String value);

    /**
     * Updates the existing customer. <br/>
     *
     * @param customer the info that should be replace the existing one
     * @return true if update is being successful, false otherwise
     */
    boolean update(Customer customer);
}

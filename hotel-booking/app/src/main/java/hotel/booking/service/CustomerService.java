package hotel.booking.service;

import hotel.booking.repository.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Customer create(Customer customer);
    Optional<Customer> getById(String id);
    List<Customer> getBy(String setName, String name, String value);
    boolean update(Customer customer);
}

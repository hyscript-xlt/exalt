package hotel.booking.service.impl;

import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import hotel.booking.clients.Client;
import hotel.booking.repository.Customer;
import hotel.booking.service.CustomerService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static hotel.booking.config.ApplicationConfig.getCustomerSetName;

public class CustomerServiceImpl extends CoreService<Customer> implements CustomerService {

    @Override
    public Customer create(Customer customer) {
        Key key = getClient().makeKeyDefaultHost(getCustomerSetName(), customer.getId());
        Bin[] arr = Stream.of(
                new Bin("id", customer.getId()),
                new Bin("name", customer.getName()),
                new Bin("surName", customer.getSurName()),
                new Bin("email", customer.getEmail()),
                new Bin("phone", customer.getPhone()))
                .toArray(Bin[]::new);
        getClient().insertWithoutUpdate(key, arr);
        return customer;
    }

    @Override
    public Optional<Customer> getById(String id) {
        return getById(id, Customer.class);
    }

    @Override
    public List<Customer> getBy(String setName, String name, String value) {
        return getBy(name, value, Customer.class);
    }

    @Override
    public boolean update(Customer customer) {
        Optional<Customer> foundCustomer = getById(customer.getId(), Customer.class);
        if (foundCustomer.isPresent()) {
            Customer tobeUpdated = foundCustomer.get();
            //be careful here always update the ID with existing one -> CODE SMELL
            customer.setId(tobeUpdated.getId());
            RecordUtil.updateExisting(tobeUpdated, customer);
            Key key = getClient().makeKeyDefaultHost(getSetName(), tobeUpdated.getId());
            getClient().update(key, RecordUtil.classToBin(tobeUpdated));
            return true;
        }

        return false;
    }

    @Override
    String getSetName() {
        return getCustomerSetName();
    }

    @Override
    Client getClient() {
        return Client.instanceOf();
    }
}

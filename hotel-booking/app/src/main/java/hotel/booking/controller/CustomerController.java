package hotel.booking.controller;

import hotel.booking.repository.Customer;
import hotel.booking.service.CustomerService;
import hotel.booking.service.impl.CustomerServiceImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static hotel.booking.config.ApplicationConfig.getCustomerSetName;

@Path("/customer")
public class CustomerController {
    private final CustomerService service = new CustomerServiceImpl();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newHotel(Customer customer) {
        service.create(customer);
        return Response.accepted().build();
    }

    @GET
    @Path("/id")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getById(@QueryParam("id") String id) {
        return service.getById(id)
                .map(customer -> Response.accepted(customer).build())
                .orElseGet(() -> Response.noContent().build());
    }

    @GET
    @Path("/name")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getByName(@QueryParam("name") String value) {
        return getBy("name", value);
    }

    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response fullUpdate(@QueryParam("id") String id, Customer body) {
        body.setId(id);
        service.update(body);
        return Response.accepted().build();
    }

    @GET
    @Path("/email")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getByEmail(@QueryParam("mail") String value) {
       return getBy("mail", value);
    }

    @GET
    @Path("/phone")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getByPhone(@QueryParam("phone") String value) {
        return getBy("phone", value);
    }

    @GET
    @Path("/surname")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getBySurname(@QueryParam("surName") String value) {
        return getBy("surName", value);
    }

    private Response getBy(String key, String value) {
        List<Customer> customers = service.getBy(getCustomerSetName(), key, value);
        if(customers.isEmpty()) {
            return Response.noContent().build();
        }
        return Response.accepted(customers).build();
    }
}
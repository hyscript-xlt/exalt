package hotel.booking.controller;

import hotel.booking.repository.Hotel;
import hotel.booking.service.HotelService;
import hotel.booking.service.impl.HotelServiceImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static hotel.booking.config.ApplicationConfig.*;

/**
 * REST endpoints for the Hotels.
 */
@Path("/hotel")
public class HotelController {
    //bad idea: need to find a way to inject the service
    private final HotelService hotelService = new HotelServiceImpl();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newHotel(Hotel hotel) {
        hotelService.create(hotel);
        return Response.accepted().build();
    }

    @GET
    @Path("/id")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getById(@QueryParam("id") String id) {
        return hotelService.getById(id)
                .map(hotel -> Response.accepted(hotel).build())
                .orElseGet(() -> Response.noContent().build());
    }

    @GET
    @Path("/name")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getByName(@QueryParam("name") String value) {
        List<Hotel> hotels = hotelService.getBy(getHotelSetName(), "name", value);
        if(hotels.isEmpty()) {
            return Response.noContent().build();
        }
        return Response.accepted(hotels).build();
    }

    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response fullUpdate(@QueryParam("id") String id, Hotel body) {
        body.setId(id);
        hotelService.update(body);
        return Response.accepted().build();
    }

    @GET
    @Path("/country")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getByCountry(@QueryParam("country") String value) {
        List<Hotel> hotels = hotelService.getBy(getHotelSetName(), "country", value);
        if(hotels.isEmpty()) {
            return Response.noContent().build();
        }
        return Response.accepted(hotels).build();
    }
}

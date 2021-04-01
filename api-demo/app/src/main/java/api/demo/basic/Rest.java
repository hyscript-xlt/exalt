package api.demo.basic;

import api.demo.util.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Path("/rest")
public class Rest {
    private static final List<String> someList = new ArrayList<>();
    private final ObjectMapper mapper = new ObjectMapper();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response defaultGet() {
        if (someList.size() == 0) {
            return Response.ok()
                    .entity(StringUtil.stringToJson("welcome", "This is the Starting page of REST api"))
                    .build();
        }
        return Response.ok()
                .entity(someList)
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(String in) {
        return Try.of(() -> mapper.readTree(in))
                .filter(e -> e.has("msg"))
                .map(e -> e.at("/msg"))
                .map(JsonNode::asText)
                .map(txt -> {
                    someList.add(txt);
                    return Response.ok("The value added successfully!").build();
                }).getOrElse(() -> Response.status(Response.Status.BAD_REQUEST)
                        .entity("Please check the input it should contains 'msg' key")
                        .build());
    }

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response get(@QueryParam("id") String id) {
        return Stream.of(id)
                .filter(Objects::nonNull)
                .map(val -> Try.of(() -> Integer.parseInt(val)))
                .filter(Try::isSuccess)
                .map(Try::get)
                .filter(ind -> ind < someList.size())
                .map(someList::get)
                .map(val -> Response.ok(val).build())
                .findAny()
                .orElse(Response.status(Response.Status.BAD_REQUEST)
                        .entity("Please make sure the param in a form /get?id=")
                        .build());
    }

    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@QueryParam("id") int index, String body) {
        if (index >= someList.size()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Index hasn't found!")
                    .build();
        }

        return Try.of(() -> mapper.readTree(body).at("/msg"))
                .map(JsonNode::asText)
                .map(msg -> {
                    String oldValue = someList.get(index);
                    someList.set(index, msg);
                    return oldValue;
                })
                .map(oldVal -> Response.ok("The value '" + oldVal + "' have been updated!").build())
                .getOrElse(() -> Response.notModified().build());
    }

    @GET
    @Path("/value")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findByValue(@QueryParam("msg") String value) {
        return someList.stream()
                .filter(e -> e.equalsIgnoreCase(value))
                .findFirst()
                .map(e -> Response.ok(e).build())
                .orElseGet(() -> Response.noContent().build());
    }

    @DELETE
    @Path("/value")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response removeByValue(@QueryParam("msg") String value) {
        boolean val = someList.removeIf(e -> e.equalsIgnoreCase(value));
        return val ? Response.ok(String.format("The value '%s' have been removed successfully!", value)).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/index")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response removeByValue(@QueryParam("id") int index) {
        if (index >= someList.size()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        String val = someList.remove(index);
        return Response.ok(String.format("The value '%s' have been removed successfully!", val)).build();
    }
}

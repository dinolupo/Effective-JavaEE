package io.github.dinolupo.doit.business.reminders.boundary;

import io.github.dinolupo.doit.business.reminders.entity.ToDo;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

@Path("todos")
public class TodosResource {

    @GET
    @Path("{id}")
    public ToDo find(@PathParam("id") long id){
        return new ToDo("Implement Rest Service Endpoint id="+id, "modify the test accordingly", 100);
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") long id) {
        System.out.printf("Deleted Object with id=%d\n", id);
    }

    @GET
    public List<ToDo> all() {
        List<ToDo> all = new ArrayList<>();
        all.add(find(42));
        return all;
    }

    // we use POST because we will use a technical key
    // if you use a business key, a key with a meaning in the business, then use PUT so you can pass
    // a key along with the client. PUT is idempotent.
    @POST
    public void save(ToDo todo) {
        System.out.printf("Saved ToDo: %s\n", todo);
    }

}

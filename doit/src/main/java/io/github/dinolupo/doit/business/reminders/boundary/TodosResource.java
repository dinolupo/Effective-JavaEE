package io.github.dinolupo.doit.business.reminders.boundary;

import com.sun.org.apache.xpath.internal.operations.Bool;
import io.github.dinolupo.doit.business.reminders.entity.ToDo;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Stateless
@Path("todos")
public class TodosResource {

    @Inject
    TodosManager todosManager;

    @Path("{id}")
    public TodoResource find(@PathParam("id") long id){
        return new TodoResource(id, todosManager);
    }

    @GET
    public List<ToDo> all() {
        return todosManager.findAll();
    }

    @POST
    public Response save(@Valid ToDo todo, @Context UriInfo uriInfo) {
        ToDo savedObject = todosManager.save(todo);
        long id = savedObject.getId();
        URI uri = uriInfo.getAbsolutePathBuilder().path("/" + id).build();
        Response response = Response.created(uri).build();
        return response;
    }
}

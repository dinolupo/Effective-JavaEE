package io.github.dinolupo.doit.business.reminders.boundary;

import com.sun.org.apache.xpath.internal.operations.Bool;
import io.github.dinolupo.doit.business.reminders.entity.ToDo;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
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

    @GET
    @Path("{id}")
    public ToDo find(@PathParam("id") long id){
        return todosManager.findById(id);
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") long id) {
        todosManager.delete(id);
    }

    @PUT
    @Path("{id}")
    public void update(@PathParam("id") long id, ToDo todo) {
        todo.setId(id);
        todosManager.save(todo);
    }

    @PUT
    @Path("{id}/status")
    public Response statusUpdate(@PathParam("id") long id, JsonObject status) {
        if (!status.containsKey("done")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .header("reason","JSON does not contain required key 'done'")
                    .build();
        }
        boolean isDone = status.getBoolean("done");
        ToDo todo = todosManager.updateStatus(id, isDone);
        if (todo == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .header("reason","ToDo with id " + id + " does not exist.")
                    .build();
        } else {
            return Response.ok(todo).build();
        }
    }

    @GET
    public List<ToDo> all() {
        return todosManager.findAll();
    }

    @POST
    public Response save(ToDo todo, @Context UriInfo uriInfo) {
        ToDo savedObject = todosManager.save(todo);
        long id = savedObject.getId();
        URI uri = uriInfo.getAbsolutePathBuilder().path("/" + id).build();
        Response response = Response.created(uri).build();
        return response;
    }
}

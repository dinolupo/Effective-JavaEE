package io.github.dinolupo.doit.business.reminders.boundary;

import io.github.dinolupo.doit.business.reminders.entity.ToDo;

import javax.ejb.Stateless;
import javax.inject.Inject;
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

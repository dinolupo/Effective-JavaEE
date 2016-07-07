package io.github.dinolupo.doit.business.reminders.boundary;

import io.github.dinolupo.doit.business.reminders.entity.ToDo;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.ArrayList;
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
    public void save(ToDo todo) {
        todosManager.save(todo);
    }
}

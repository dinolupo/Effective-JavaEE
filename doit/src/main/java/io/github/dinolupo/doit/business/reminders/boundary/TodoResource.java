package io.github.dinolupo.doit.business.reminders.boundary;

import io.github.dinolupo.doit.business.reminders.entity.ToDo;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by dinolupo.github.io on 10/07/16.
 */
public class TodoResource {

    long id;
    TodosManager todosManager;

    public TodoResource(long id, TodosManager todosManager) {
        this.id = id;
        this.todosManager = todosManager;
    }

    @GET
    public ToDo find(){
        return todosManager.findById(id);
    }


    @DELETE
    public void delete() {
        todosManager.delete(id);
    }

    @PUT
    public void update(ToDo todo) {
        todo.setId(id);
        todosManager.save(todo);
    }

    @PUT
    @Path("/status")
    public Response statusUpdate(JsonObject status) {
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
}

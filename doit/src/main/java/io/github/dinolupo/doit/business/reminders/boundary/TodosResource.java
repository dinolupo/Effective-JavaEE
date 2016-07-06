package io.github.dinolupo.doit.business.reminders.boundary;

import io.github.dinolupo.doit.business.reminders.entity.ToDo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("todos")
public class TodosResource {

    @GET
    public ToDo hello(){
        return new ToDo("Implement Rest Service", "modify the test accordingly", 100);
    }

}

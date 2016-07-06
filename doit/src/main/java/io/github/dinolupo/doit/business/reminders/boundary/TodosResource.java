package io.github.dinolupo.doit.business.reminders.boundary;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("todos")
public class TodosResource {

    @GET
    public String hello(){
        return "Hello, time is " + System.currentTimeMillis();
    }

}

package io.github.dinolupo.doit.business.reminders.boundary;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.json.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by dinolupo.github.io on 05/07/16.
 */
public class TodosResourceTest {

    private Client client;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        client = ClientBuilder.newClient();
        target = client.target("http://localhost:8080/doit/api/todos");
    }

    @Test
    public void crud() throws Exception {

        // create an object with POST
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        JsonObject todoToCreate = jsonObjectBuilder
                .add("caption", "Implement Rest Service with JPA")
                .add("description", "Connect a JPA Entity Manager")
                .add("priority", 100).build();

        Response postResponse = target.request().post(Entity.json(todoToCreate));
        assertThat(postResponse.getStatusInfo(),is(Response.Status.CREATED));

        String location = postResponse.getHeaderString("Location");
        System.out.printf("location = %s\n", location);

        // GET {id}, using the location returned before
        JsonObject jsonObject = client.target(location)
                .request(MediaType.APPLICATION_JSON)
                .get(JsonObject.class);

        assertTrue(jsonObject.getString("caption").contains("Implement Rest Service with JPA"));

        // update with PUT
        JsonObjectBuilder updateObjectBuilder = Json.createObjectBuilder();
        JsonObject updated = updateObjectBuilder
                .add("caption", "Implemented!")
                .build();
        client.target(location).request(MediaType.APPLICATION_JSON).put(Entity.json(updated));

        // find again with GET {id}
        JsonObject updatedTodo = client.target(location)
                .request(MediaType.APPLICATION_JSON)
                .get(JsonObject.class);

        assertTrue(updatedTodo.getString("caption").contains("Implemented!"));


        // GET all
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatusInfo(),is(Response.Status.OK));
        JsonArray allTodos = response.readEntity(JsonArray.class);
        assertFalse(allTodos.isEmpty());

        JsonObject todo = allTodos.getJsonObject(0);
        assertThat(todo.getString("caption"), startsWith("Implement"));
        System.out.println(todo);

        // DELETE non existing object
        Response deleteResponse = target.
                path("42").
                request(MediaType.APPLICATION_JSON)
                .delete();

        assertThat(deleteResponse.getStatusInfo(),is(Response.Status.NO_CONTENT));
    }
}

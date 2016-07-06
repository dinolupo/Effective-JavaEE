package io.github.dinolupo.doit.business.reminders.boundary;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
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
    public void fetchTodos() throws Exception {
        Response response = target.request(MediaType.TEXT_PLAIN).get();
        assertThat(response.getStatus(),is(200));
        String payload = response.readEntity(String.class);
        assertThat(payload, startsWith("Hello, time is"));
    }
}

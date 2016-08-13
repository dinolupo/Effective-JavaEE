package io.github.dinolupo.doit.business.reminders.boundary;

import org.junit.Before;
import org.junit.Test;

import javax.json.JsonObject;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;

/**
 * Created by dinolupo.github.io on 13/08/16.
 */
public class ToDoChangeTrackerTest {

    private WebSocketContainer webSocketContainer;
    private ChangesListener listener;

    @Before
    public void initContainer() throws URISyntaxException, IOException, DeploymentException {
        webSocketContainer = ContainerProvider.getWebSocketContainer();
        URI uri = new URI("ws://localhost:8080/doit/changes");
        this.listener = new ChangesListener();
        ClientEndpointConfig cec = ClientEndpointConfig.Builder.create()
                .decoders(Arrays.asList(JsonDecoder.class))
                .build();
        webSocketContainer.connectToServer(listener, cec, uri);
    }

    @Test
    public void receiveNotifications() throws InterruptedException {
        JsonObject message = listener.getMessage();
        assertNotNull(message);
        System.out.println("receiveNotifications message: " + message);
    }

}

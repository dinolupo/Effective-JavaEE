package io.github.dinolupo.doit.business.reminders.boundary;

import javax.json.JsonObject;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by dinolupo.github.io on 13/08/16.
 */
public class ChangesListener extends Endpoint {

    JsonObject message;
    CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        session.addMessageHandler(JsonObject.class, s -> {
            message = s;
            latch.countDown();
            System.out.println("onOpen message: " + message);
        });
    }

    public JsonObject getMessage() throws InterruptedException {
        latch.await(1, TimeUnit.MINUTES);
        return message;
    }

}

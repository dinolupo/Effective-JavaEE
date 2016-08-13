package io.github.dinolupo.doit.business.reminders.boundary;

import io.github.dinolupo.doit.business.encoders.JsonEncoder;
import io.github.dinolupo.doit.business.reminders.entity.ToDo;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Created by dinolupo.github.io on 26/07/16.
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@ServerEndpoint(value = "/changes", encoders = {JsonEncoder.class})
public class ToDoChangeTracker {

    private Session session;

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
    }

    @OnClose
    public void onClose(){
        session = null;
    }

    // only observe on success creation
    public void onToDoCreation(@Observes(during = TransactionPhase.AFTER_SUCCESS) @ChangeEvent(ChangeEvent.Type.CREATION) ToDo todo) throws EncodeException {
        if (session != null && session.isOpen()) {
            try {
                JsonObject event = Json.createObjectBuilder()
                        .add("id", todo.getId())
                        .add("mode", ChangeEvent.Type.CREATION.toString())
                        .build();
                session.getBasicRemote().sendObject(event);
            } catch (IOException e) {
                // ignore because the connection could be closed anytime
            }
        }
    }
}

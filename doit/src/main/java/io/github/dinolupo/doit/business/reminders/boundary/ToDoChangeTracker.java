package io.github.dinolupo.doit.business.reminders.boundary;

import io.github.dinolupo.doit.business.reminders.entity.ToDo;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
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
@ServerEndpoint("/changes")
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

    // only observe on success update
    public void onToDoChange(@Observes(during = TransactionPhase.AFTER_SUCCESS) ToDo todo) {
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(todo.toString());
            } catch (IOException e) {
                // ignore because the connection could be closed anytime
            }
        }
    }
}

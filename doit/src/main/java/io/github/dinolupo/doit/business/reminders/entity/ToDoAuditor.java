package io.github.dinolupo.doit.business.reminders.entity;

import io.github.dinolupo.doit.business.reminders.boundary.ChangeEvent;

import javax.ejb.EJB;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

/**
 * Created by dinolupo.github.io on 26/07/16.
 */
public class ToDoAuditor {

    // CDI Injection on JPA Listeners does not work on WildFly 10, switch to Payara or Glassfish

    @Inject
    @ChangeEvent(ChangeEvent.Type.CREATION)
    Event<ToDo> create;

    @Inject
    @ChangeEvent(ChangeEvent.Type.UPDATE)
    Event<ToDo> update;

    @PostUpdate
    public void onUpdate(ToDo todo) {
        update.fire(todo);
    }

    @PostPersist
    public void onPersist(ToDo todo) {
        create.fire(todo);
    }

}

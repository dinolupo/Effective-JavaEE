package io.github.dinolupo.doit.business.reminders.entity;

import javax.ejb.EJB;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.PostPersist;

/**
 * Created by dinolupo.github.io on 26/07/16.
 */
public class ToDoAuditor {

// CDI Injection on JPA Listeners does not work on WildFly 10, switch to Payara or Glassfish
@Inject
Event<ToDo> events;

    @PostPersist
    public void onToDoUpdate(ToDo todo) {
        System.out.printf("---------------> %s\n", todo.toString());
        events.fire(todo);
    }

}

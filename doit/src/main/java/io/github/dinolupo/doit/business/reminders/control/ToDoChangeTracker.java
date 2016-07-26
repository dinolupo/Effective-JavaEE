package io.github.dinolupo.doit.business.reminders.control;

import io.github.dinolupo.doit.business.reminders.entity.ToDo;

import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;

/**
 * Created by dinolupo.github.io on 26/07/16.
 */
public class ToDoChangeTracker {
    // only observe on success update
    public void onToDoChange(@Observes(during = TransactionPhase.AFTER_SUCCESS) ToDo todo){
        System.out.printf("########## ToDo changed and committed: %s\n", todo);
    }
}

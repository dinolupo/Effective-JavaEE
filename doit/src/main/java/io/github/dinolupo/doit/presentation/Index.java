package io.github.dinolupo.doit.presentation;

import io.github.dinolupo.doit.business.reminders.boundary.TodosManager;
import io.github.dinolupo.doit.business.reminders.entity.ToDo;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;

/**
 * Created by dinolupo.github.io on 17/07/16.
 */
@Model
public class Index {

    @Inject
    TodosManager boundary;

    ToDo todo;

    @PostConstruct
    public void init() {
        todo = new ToDo();
    }

    public ToDo getTodo() {
        return todo;
    }

    // JSF action
    public Object save() {
        this.boundary.save(todo);
        // stay on the same page
        return null;
    }

}

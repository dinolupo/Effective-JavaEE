package io.github.dinolupo.doit.presentation;

import io.github.dinolupo.doit.business.reminders.boundary.TodosManager;
import io.github.dinolupo.doit.business.reminders.entity.ToDo;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * Created by dinolupo.github.io on 17/07/16.
 */
@Model
public class Index {

    @Inject
    TodosManager boundary;

    ToDo todo;

    @Inject
    Validator validator;

    @PostConstruct
    public void init() {
        todo = new ToDo();
    }

    public ToDo getTodo() {
        return todo;
    }

    public void showValidationError(String content) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, content, content);
        FacesContext.getCurrentInstance().addMessage("", message);
    }

    // JSF action
    public Object save() {
        Set<ConstraintViolation<ToDo>> violations = validator.validate(todo);
        for (ConstraintViolation<ToDo> violation : violations) {
            showValidationError(violation.getMessage());
        }
        if (violations.isEmpty()) {
            this.boundary.save(todo);
        }
        // stay on the same page
        return null;
    }
}

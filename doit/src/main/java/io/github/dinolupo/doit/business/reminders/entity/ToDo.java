package io.github.dinolupo.doit.business.reminders.entity;


import io.github.dinolupo.doit.business.CrossCheck;
import io.github.dinolupo.doit.business.ValidEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by dinolupo.github.io on 06/07/16.
 */
@Entity
@NamedQuery(name = ToDo.findAll, query = "SELECT t FROM ToDo t")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@CrossCheck(message = "validation.todo.crosscheckfailed")
@EntityListeners(ToDoAuditor.class)
public class ToDo implements ValidEntity {

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    final static String PREFIX = "reminders.entity.";
    public final static String findAll = PREFIX + "findAll";

    //technical key
    @Id
    @GeneratedValue
    long id;

    @NotNull
    @Size(min = 3, max = 256)
    private String caption;
    private String description;
    private int priority;
    private boolean done;

    @Version
    private long version;

    public ToDo(String caption, String description, int priority) {
        this.caption = caption;
        this.description = description;
        this.priority = priority;
    }

    public ToDo() {
    }

    public long getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public boolean isValid() {
        return (priority > 10 && description != null && !description.isEmpty()) || priority <= 10;
    }
}

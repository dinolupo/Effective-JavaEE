package io.github.dinolupo.doit.business.reminders.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by dinolupo.github.io on 06/07/16.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ToDo {
    private String caption;
    private String description;
    private int priority;

    public ToDo(String caption, String description, int priority) {
        this.caption = caption;
        this.description = description;
        this.priority = priority;
    }

    public ToDo() {
    }


}

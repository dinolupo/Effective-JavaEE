package io.github.dinolupo.doit.business.reminders.entity;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dinolupo.github.io on 12/07/16.
 */
public class ToDoTest {

    @Test
    public void valid() {
        ToDo toDo = new ToDo("", "description", 11);
        assertTrue(toDo.isValid());
    }

    @Test
    public void validWithLowPriority() {
        ToDo toDo = new ToDo("caption", null, 10);
        assertTrue(toDo.isValid());
    }

    @Test
    public void notValid() {
        ToDo toDo = new ToDo("", null, 11);
        assertFalse(toDo.isValid());
    }

}
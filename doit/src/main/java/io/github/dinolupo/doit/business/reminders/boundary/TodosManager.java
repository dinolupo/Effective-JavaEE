package io.github.dinolupo.doit.business.reminders.boundary;

import io.github.dinolupo.doit.business.reminders.entity.ToDo;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dinolupo.github.io on 07/07/16.
 */
@Stateless
public class TodosManager {

    public ToDo findById(long id) {
        return new ToDo("Implement Rest Service Endpoint id="+id, "modify the test accordingly", 100);
    }


    public void delete(long id) {
        System.out.printf("Deleted Object with id=%d\n", id);
    }

    public List<ToDo> findAll() {
        List<ToDo> all = new ArrayList<>();
        all.add(findById(42));
        return all;
    }

    public void save(ToDo todo) {
        System.out.printf("Saved ToDo: %s\n", todo);
    }
}

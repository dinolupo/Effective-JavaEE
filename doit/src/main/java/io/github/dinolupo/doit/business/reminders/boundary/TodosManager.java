package io.github.dinolupo.doit.business.reminders.boundary;

import io.github.dinolupo.doit.business.reminders.entity.ToDo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dinolupo.github.io on 07/07/16.
 */
@Stateless
public class TodosManager {

    @PersistenceContext
    EntityManager entityManager;

    public ToDo findById(long id) {
        return entityManager.find(ToDo.class, id);
    }


    public void delete(long id) {
        ToDo reference = entityManager.getReference(ToDo.class, id);
        entityManager.remove(reference);
    }


    public List<ToDo> findAll() {
        return entityManager.createNamedQuery(ToDo.findAll, ToDo.class).getResultList();
    }

    public void save(ToDo todo) {
        entityManager.merge(todo);
    }
}

package io.github.dinolupo.doit.business.reminders.boundary;

import io.github.dinolupo.doit.business.logging.boundary.BoundaryLogger;
import io.github.dinolupo.doit.business.reminders.entity.ToDo;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by dinolupo.github.io on 07/07/16.
 */
@Stateless
@Interceptors(BoundaryLogger.class)
public class TodosManager {

    @PersistenceContext(unitName = "production")
    EntityManager entityManager;

    public ToDo findById(long id) {
        return entityManager.find(ToDo.class, id);
    }


    public void delete(long id) {
        try {
            ToDo reference = entityManager.getReference(ToDo.class, id);
            entityManager.remove(reference);
        } catch (EntityNotFoundException ex) {
            // we want to remove it, so do not care of the exception
        }
    }

    public ToDo updateStatus(long id, boolean done) {
        ToDo todo = findById(id);
        if (todo == null) {
            return null;
        }
        todo.setDone(done);
        return todo;
    }

    public List<ToDo> findAll() {
        return entityManager.createNamedQuery(ToDo.findAll, ToDo.class).getResultList();
    }

    public ToDo save(ToDo todo) {
        ToDo merge = entityManager.merge(todo);
        return merge;
    }
}

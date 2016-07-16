package io.github.dinolupo.doit.business.logging.boundary;

import io.github.dinolupo.doit.business.monitoring.entity.CallEvent;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * Created by dinolupo.github.io on 15/07/16.
 */
public class BoundaryLogger {

    @Inject
    Event<CallEvent> monitoring;

    @AroundInvoke
    public Object logCall(InvocationContext invocationContext) throws Exception {
        long start = System.currentTimeMillis();
        try {
            return invocationContext.proceed();
        } finally {
            long duration = System.currentTimeMillis() - start;
            monitoring.fire(new CallEvent(invocationContext.getMethod().getName(),duration));
        }
    }
}

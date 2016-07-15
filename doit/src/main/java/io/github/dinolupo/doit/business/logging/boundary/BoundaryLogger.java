package io.github.dinolupo.doit.business.logging.boundary;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * Created by dinolupo.github.io on 15/07/16.
 */
public class BoundaryLogger {

    @Inject
    LogSink LOG;

    @AroundInvoke
    public Object logCall(InvocationContext invocationContext) throws Exception {
        LOG.log("--> " + invocationContext.getMethod());
        return invocationContext.proceed();
    }
}

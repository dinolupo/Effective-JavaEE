package io.github.dinolupo.doit.business;

import javax.ejb.EJBException;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by dinolupo.github.io on 11/07/16.
 */
@Provider
public class EJBExceptionMapper implements ExceptionMapper<EJBException>{
    @Override
    public Response toResponse(EJBException exception) {
        Throwable cause = exception.getCause();
        Response unknownError = Response.serverError()
                .header("cause", exception.toString())
                .build();
        if (cause == null) {
            return unknownError;
        }

        if (cause instanceof OptimisticLockException) {
            return Response.status(Response.Status.CONFLICT)
                    .header("cause", "conflict occurred: " + cause)
                    .build();
        }

        return unknownError;
    }
}

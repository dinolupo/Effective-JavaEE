package io.github.dinolupo.doit.business.monitoring.boundary;

import io.github.dinolupo.doit.business.monitoring.entity.CallEvent;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

/**
 * Created by dinolupo.github.io on 16/07/16.
 */
@Stateless
@Path("boundary-invocations")
public class BoundaryInvocationsResource {

    @Inject
    MonitoringSink monitoringSink;

    @GET
    public List<CallEvent> expose() {
        return monitoringSink.getRecentEvents();
    }
}

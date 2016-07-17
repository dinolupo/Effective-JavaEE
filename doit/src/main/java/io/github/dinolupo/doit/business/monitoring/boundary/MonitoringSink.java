package io.github.dinolupo.doit.business.monitoring.boundary;

import io.github.dinolupo.doit.business.logging.boundary.LogSink;
import io.github.dinolupo.doit.business.monitoring.entity.CallEvent;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.xml.rpc.Call;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by dinolupo.github.io on 16/07/16.
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class MonitoringSink {

    @Inject
    private LogSink LOG;

    private CopyOnWriteArrayList<CallEvent> recentEvents;

    @PostConstruct
    public void postConstruct() {
        recentEvents = new CopyOnWriteArrayList<>();
    }

    public void onCallEvent(@Observes CallEvent callEvent) {
        LOG.log(callEvent.toString());
        recentEvents.add(callEvent);
    }

    public List<CallEvent> getRecentEvents() {
        return recentEvents;
    }

}

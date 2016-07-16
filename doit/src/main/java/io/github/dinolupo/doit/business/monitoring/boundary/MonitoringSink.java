package io.github.dinolupo.doit.business.monitoring.boundary;

import io.github.dinolupo.doit.business.logging.boundary.LogSink;
import io.github.dinolupo.doit.business.monitoring.entity.CallEvent;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * Created by dinolupo.github.io on 16/07/16.
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class MonitoringSink {

    @Inject
    LogSink LOG;

    public void onCallEvent(@Observes CallEvent callEvent) {
        LOG.log(callEvent.toString());
    }

}

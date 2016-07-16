package io.github.dinolupo.doit.business.logging.boundary;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.logging.Logger;

/**
 * Created by dinolupo.github.io on 15/07/16.
 */
public class LogSinkProducer {

    @Produces
    public LogSink produce(InjectionPoint injectionPoint) {
        Class<?> injectionTarget = injectionPoint.getMember().getDeclaringClass();
        return Logger.getLogger(injectionTarget.getName())::info;
    }

}

package io.github.dinolupo.doit.business.logging.boundary;

/**
 * Created by dinolupo.github.io on 15/07/16.
 */
@FunctionalInterface
public interface LogSink {
    void log(String msg);
}

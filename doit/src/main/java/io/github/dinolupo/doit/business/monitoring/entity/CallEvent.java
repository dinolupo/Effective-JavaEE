package io.github.dinolupo.doit.business.monitoring.entity;

/**
 * Created by dinolupo.github.io on 16/07/16.
 */
public class CallEvent {
    private String methodName;
    private long duration;

    public CallEvent(String methodName, long duration) {
        this.methodName = methodName;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "CallEvent{" +
                "methodName='" + methodName + '\'' +
                ", duration=" + duration +
                '}';
    }

    public String getMethodName() {
        return methodName;
    }

    public long getDuration() {
        return duration;
    }
}

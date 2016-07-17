package io.github.dinolupo.doit.business.monitoring.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by dinolupo.github.io on 16/07/16.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CallEvent {
    private String methodName;
    private long duration;

    public CallEvent(String methodName, long duration) {
        this.methodName = methodName;
        this.duration = duration;
    }

    public CallEvent() {
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

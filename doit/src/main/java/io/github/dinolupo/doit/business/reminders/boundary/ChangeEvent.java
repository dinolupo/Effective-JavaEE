package io.github.dinolupo.doit.business.reminders.boundary;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by dinolupo.github.io on 13/08/16.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface ChangeEvent {

    Type value();

    enum Type {
        CREATION, UPDATE;
    }

}

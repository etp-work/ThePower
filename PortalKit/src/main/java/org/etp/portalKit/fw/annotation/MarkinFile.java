package org.etp.portalKit.fw.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The purpose of this class is to provide an annotation that indicate
 * what the name is in .portalkit file.
 */
@Target({ FIELD })
@Retention(RUNTIME)
public @interface MarkinFile {
    /**
     * The JNDI name of the resource. For field annotations, the
     * default is the field name.
     */
    String name() default "";
}
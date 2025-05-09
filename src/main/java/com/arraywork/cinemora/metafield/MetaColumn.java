package com.arraywork.cinemora.metafield;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 元字段注解
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/24
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MetaColumn {

    String label() default "";

}
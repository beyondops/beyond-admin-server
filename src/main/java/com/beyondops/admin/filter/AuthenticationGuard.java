package com.beyondops.admin.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.ws.rs.NameBinding;

/**
 * Created by caiqinzhou@gmail.com on 2017/1/13.
 */
@NameBinding
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface AuthenticationGuard {

  /**
   * Check if the resource or api is authentication required.
   */
  boolean required() default true;
}

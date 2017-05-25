package com.beyondops.admin.config;

import com.beyondops.admin.common.AppResponse;
import java.util.HashMap;
import javax.annotation.Priority;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by caiqinzhou@gmail.com on 2017/1/16.
 */
@Provider
@Priority(Priorities.USER)
public class ConstraintViolationExceptionMapper
    implements ExceptionMapper<ConstraintViolationException> {

  @Override
  public Response toResponse(final ConstraintViolationException exception) {
    HashMap<Path, String> errorMsg = new HashMap<>();
    for (ConstraintViolation constraintViolation : exception.getConstraintViolations()) {
      errorMsg.put(constraintViolation.getPropertyPath(), constraintViolation.getMessage());
    }
    return AppResponse.error(errorMsg).build();
  }
}

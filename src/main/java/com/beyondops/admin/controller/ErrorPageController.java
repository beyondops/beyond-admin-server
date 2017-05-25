package com.beyondops.admin.controller;

import com.beyondops.admin.common.AppResponse;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Created by caiqinzhou@gmail.com on 2017/2/4.
 */
@RestController
public class ErrorPageController implements ErrorController {

  private static final String ERROR_PATH = "/error";

  @Autowired
  private ErrorAttributes errorAttributes;

  @RequestMapping(value = ERROR_PATH)
  public AppResponse error(HttpServletRequest request) {
    return getErrorAttributes(request, true);
  }

  @Override
  public String getErrorPath() {
    return ERROR_PATH;
  }

  /**
   * Transform error info into AppResponse.
   */
  private AppResponse getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
    RequestAttributes requestAttributes = new ServletRequestAttributes(request);
    Map<String, Object> error =
        errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    AppResponse appResponse = AppResponse.error();
    for (Entry<String, Object> entry : error.entrySet()) {
      appResponse.put(entry.getKey(), entry.getValue());
    }
    return appResponse;
  }
}

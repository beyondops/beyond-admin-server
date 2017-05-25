package com.beyondops.admin.common;

import java.util.Date;
import java.util.HashMap;
import javax.ws.rs.core.Response;

/**
 * Created by caiqinzhou@gmail.com on 2017/1/12.
 */
public class AppResponse extends HashMap<String, Object> {

  public static final String TIMESTAMP = "timestamp";
  public static final String STATUS = "status";
  public static final String ERROR = "error";
  public static final String MESSAGE = "message";

  public AppResponse() {
    this.put(TIMESTAMP, new Date().getTime());
    this.put(STATUS, Response.Status.BAD_REQUEST.getStatusCode());
  }

  /**
   * Init AppResponse with status and message.
   */
  public AppResponse(Response.Status status, Object msg) {
    this.put(TIMESTAMP, new Date().getTime());
    this.put(STATUS, status.getStatusCode());
    if (status.getStatusCode() < 400) {
      this.put(MESSAGE, msg);
    } else {
      this.put(ERROR, msg);
    }
  }

  /**
   * Build a jersey response.
   */
  public Response build() {
    int statusCode = (int) get(STATUS);
    Response.Status statusResponse = Response.Status.fromStatusCode(statusCode);
    return Response.status(statusResponse).entity(this).build();
  }

  public AppResponse put(String key, Object value) {
    super.put(key, value);
    return this;
  }

  public static AppResponse ok() {
    return new AppResponse(Response.Status.OK, "");
  }

  public static AppResponse ok(Object message) {
    return new AppResponse(Response.Status.OK, message);
  }

  public static AppResponse error() {
    return new AppResponse(Response.Status.BAD_REQUEST, "");
  }

  public static AppResponse error(Object message) {
    return new AppResponse(Response.Status.BAD_REQUEST, message);
  }

  public void setStatus(Response.Status status) {
    this.put(STATUS, status.getStatusCode());
  }

  public void setOk() {
    this.put(STATUS, Response.Status.OK.getStatusCode());
  }

  public void setMessage(String message) {
    this.put(MESSAGE, message);
  }

  public void setError(String error) {
    this.put(ERROR, error);
  }

  public int getStatus() {
    return (int) get(STATUS);
  }

  public Object getMessage() {
    return get(MESSAGE);
  }

  public Object getError() {
    return get(ERROR);
  }
}

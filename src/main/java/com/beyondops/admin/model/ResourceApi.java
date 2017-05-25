package com.beyondops.admin.model;

/**
 * Created by caiqinzhou@gmail.com on 2017/2/8.
 */
public class ResourceApi {

  /**
   * Restful api method type.
   */
  public enum ApiMethod {
    ALL, POST, GET, PUT, PATCH, DELETE
  }


  private String path;
  //Api method, 1:POST, 2:GET, 3:PUT, 4:PATCH, 5:DELETE
  private ApiMethod method;

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public ApiMethod getMethod() {
    return method;
  }

  public void setMethod(ApiMethod method) {
    this.method = method;
  }
}

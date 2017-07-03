package com.beyondops.admin.common;

import java.util.HashMap;

/**
 * Created by caiqinzhou@gmail.com on 2017/1/12.
 */
public class AppConstants {

  public static final String JWT_HEADER = "Authorization";
  public static final String JWT_HEADER_PREFIX = "Bearer ";
  //Authentication header key for jwt token
  public static final String JWT_TOKEN = "JWT_TOKEN";
  public static final String REQUEST_AUTH_USER = "REQUEST_AUTH_USER";
  //The prefix of user's validation token key in redis
  public static final String USER_VALIDATION_TOKEN_PREFIX = "auth:sysuser:";

  //Admin role id
  public static final int SYS_ROLE_ADMIN = 1;
  //Regular user role id
  public static final int SYS_ROLE_USER = 2;

  public static final int AUTH_TYPE_LOCAL = 1;
  public static final int AUTH_TYPE_LDAP = 2;

  public static final int USER_STATUS_INVALID = 0;
  public static final int USER_STATUS_VALID = 1;

  public static final String SWAGGER_JSON = "swagger.json";

  public static final HashMap<String, Integer> HTTP_METHOD = new HashMap() {{
    //1:POST, 2:GET, 3:PUT, 4:PATCH, 5:DELETE, 6:HEAD, 7:OPTIONS
    this.put("POST", 1);
    this.put("GET", 2);
    this.put("PUT", 3);
    this.put("PATCH", 4);
    this.put("DELETE", 5);
    this.put("HEAD", 6);
    this.put("OPTIONS", 7);
  }};
}

package com.beyondops.admin.common;

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
}

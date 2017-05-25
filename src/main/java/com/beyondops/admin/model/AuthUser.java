package com.beyondops.admin.model;

import java.security.Principal;

/**
 * Authentication user info.
 * Created by caiqinzhou@gmail.com on 2017/1/13.
 */
public class AuthUser implements Principal {

  private int uid;
  private String username;
  private String validationToken;
  private String clientIp;
  private Integer[] roles;

  public int getUid() {
    return uid;
  }

  public void setUid(int uid) {
    this.uid = uid;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getClientIp() {
    return clientIp;
  }

  public void setClientIp(String clientIp) {
    this.clientIp = clientIp;
  }

  public Integer[] getRoles() {
    return roles.clone();
  }

  public void setRoles(Integer[] roles) {
    this.roles = roles.clone();
  }

  public String getValidationToken() {
    return validationToken;
  }

  public void setValidationToken(String validationToken) {
    this.validationToken = validationToken;
  }

  @Override
  public String getName() {
    return username;
  }

}

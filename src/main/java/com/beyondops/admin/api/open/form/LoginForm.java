package com.beyondops.admin.api.open.form;

import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by caiqinzhou@gmail.com on 2017/1/13.
 */
@XmlRootElement
public class LoginForm {

  @NotBlank
  private String username;

  @NotBlank
  private String password;
  private int rememberMe = 0;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getRememberMe() {
    return rememberMe;
  }

  public void setRememberMe(int rememberMe) {
    this.rememberMe = rememberMe;
  }
}

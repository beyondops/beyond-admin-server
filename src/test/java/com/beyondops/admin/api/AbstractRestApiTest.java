package com.beyondops.admin.api;

import com.beyondops.admin.api.open.form.LoginForm;
import com.beyondops.admin.common.AppConstants;
import com.beyondops.admin.common.AppResponse;
import java.util.Arrays;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * Created by caiqinzhou@gmail.com on 2017/1/18.
 */

public class AbstractRestApiTest {

  private static final Logger logger = LoggerFactory.getLogger(AbstractRestApiTest.class);
  protected HttpHeaders headers;
  public static final int adminUserid = 1;
  public static final String adminUsername = "admin";
  public static final String adminPassword = "beyondops";
  public static final String testUsername = "dev";
  public static final String testPassword = "password";
  public static final String userLoginUrl = "/beyondops/admin/open/auth/login";

  @Autowired
  protected TestRestTemplate restTemplate;

  @Before
  public void abstractSetup() {
    logger.info("Setup AbstractRestApiTest.");
    headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
  }

  protected void login(String username, String password) {
    LoginForm loginForm = new LoginForm();
    loginForm.setUsername(username);
    loginForm.setPassword(password);
    HttpEntity<LoginForm> entity = buildHttpEntity(loginForm);
    ResponseEntity<AppResponse> responseEntity =
        restTemplate.postForEntity(userLoginUrl, entity, AppResponse.class);
    headers.set(AppConstants.JWT_HEADER,
        AppConstants.JWT_HEADER_PREFIX + responseEntity.getBody().get(AppConstants.JWT_TOKEN));
  }

  protected <T> HttpEntity<T> buildHttpEntity(T entity) {
    HttpEntity<T> httpEntity = new HttpEntity<>(entity, headers);
    return httpEntity;
  }

}

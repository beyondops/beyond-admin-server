package com.beyondops.admin.api.open;

import com.beyondops.admin.api.AbstractRestApiTest;
import com.beyondops.admin.api.open.form.LoginForm;
import com.beyondops.admin.common.AppConstants;
import com.beyondops.admin.common.AppResponse;
import com.beyondops.admin.model.AuthUser;
import com.beyondops.admin.service.AuthenticationService;
import com.beyondops.admin.util.StringUtil;
import javax.ws.rs.core.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by caiqinzhou@gmail.com on 2017/1/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationApiTest extends AbstractRestApiTest {

  private static final Logger logger = LoggerFactory.getLogger(AuthenticationApiTest.class);


  @Autowired
  private AuthenticationService authenticationService;

  @Before
  public void setup() {
  }

  @Test
  public void testLogin() {
    testLoginWithUsernameAndPassword(adminUsername, adminPassword);
    testLoginWithUsernameAndPassword(testUsername, testPassword);
  }

  private void testLoginWithUsernameAndPassword(String username, String password) {
    LoginForm loginForm = null;
    String apiUrl = "/beyondops/admin/open/auth/login";
    HttpEntity<LoginForm> entity = buildHttpEntity(loginForm);
    ResponseEntity<AppResponse> responseEntity =
        restTemplate.postForEntity(apiUrl, entity, AppResponse.class);
    Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),
        responseEntity.getStatusCodeValue());
    Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),
        responseEntity.getBody().getStatus());

    loginForm = new LoginForm();
    loginForm.setPassword(null);
    loginForm.setUsername(username);
    entity = buildHttpEntity(loginForm);
    responseEntity = restTemplate.postForEntity(apiUrl, entity, AppResponse.class);
    Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),
        responseEntity.getStatusCodeValue());
    Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),
        responseEntity.getBody().getStatus());

    loginForm = new LoginForm();
    loginForm.setPassword(password);
    loginForm.setUsername(null);
    entity = buildHttpEntity(loginForm);
    responseEntity = restTemplate.postForEntity(apiUrl, entity, AppResponse.class);
    Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),
        responseEntity.getStatusCodeValue());
    Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),
        responseEntity.getBody().getStatus());

    loginForm = new LoginForm();
    loginForm.setPassword("badpassword");
    loginForm.setUsername(username);
    entity = buildHttpEntity(loginForm);
    responseEntity = restTemplate.postForEntity(apiUrl, entity, AppResponse.class);
    Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),
        responseEntity.getStatusCodeValue());
    Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),
        responseEntity.getBody().getStatus());

    loginForm = new LoginForm();
    loginForm.setPassword(password);
    loginForm.setUsername(username);
    entity = buildHttpEntity(loginForm);
    responseEntity = restTemplate.postForEntity(apiUrl, entity, AppResponse.class);
    Assert
        .assertEquals(Response.Status.OK.getStatusCode(), responseEntity.getStatusCodeValue());
    Assert
        .assertEquals(Response.Status.OK.getStatusCode(), responseEntity.getBody().getStatus());

    String token = (String) responseEntity.getBody().get(AppConstants.JWT_TOKEN);
    AuthUser authUser = authenticationService.checkJwtToken(token);
    Assert.assertNotNull(authUser);
    Assert.assertEquals(username, authUser.getUsername());
    if (adminUsername.equals(username)) {
      Assert.assertEquals(adminUserid, authUser.getUid());
      Assert.assertArrayEquals(new Integer[]{AppConstants.SYS_ROLE_ADMIN},
          authUser.getRoles());
    } else {
      Assert
          .assertArrayEquals(new Integer[]{AppConstants.SYS_ROLE_USER}, authUser.getRoles());
    }
    Assert.assertNotNull(authUser.getValidationToken());

    logger.info(StringUtil.objectToJson(responseEntity.getBody()));
  }

}

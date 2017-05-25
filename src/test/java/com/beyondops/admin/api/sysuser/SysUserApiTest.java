package com.beyondops.admin.api.sysuser;

import com.beyondops.admin.api.AbstractRestApiTest;
import com.beyondops.admin.api.open.form.LoginForm;
import com.beyondops.admin.common.AppConstants;
import com.beyondops.admin.common.AppResponse;
import com.beyondops.admin.model.AuthUser;
import com.beyondops.admin.service.AuthenticationService;
import com.beyondops.admin.util.StringUtil;
import javax.ws.rs.core.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by caiqinzhou@gmail.com on 2017/1/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SysUserApiTest extends AbstractRestApiTest {

  private static final Logger logger = LoggerFactory.getLogger(SysUserApiTest.class);

  @Autowired
  AuthenticationService authenticationService;

  @Test
  public void testRefreshToken() {
    login(adminUsername, adminPassword);
    String apiUrl = "/beyondops/admin/sysuser/sysuser/refreshToken/";

    //Validate refresh successfully.
    HttpEntity<LoginForm> entity = buildHttpEntity(null);
    ResponseEntity<AppResponse> responseEntity =
        restTemplate.exchange(apiUrl + "1", HttpMethod.GET, entity, AppResponse.class);
    Assert
        .assertEquals(Response.Status.OK.getStatusCode(), responseEntity.getStatusCodeValue());
    Assert
        .assertEquals(Response.Status.OK.getStatusCode(), responseEntity.getBody().getStatus());
    String token = (String) responseEntity.getBody().get(AppConstants.JWT_TOKEN);
    AuthUser authUser = authenticationService.checkJwtToken(token);
    Assert.assertNotNull(authUser);
    Assert.assertEquals(adminUsername, authUser.getUsername());
    Assert.assertEquals(adminUserid, authUser.getUid());
    Assert.assertNotNull(authUser.getValidationToken());
    Assert.assertArrayEquals(new Integer[]{1}, authUser.getRoles());

    responseEntity =
        restTemplate.exchange(apiUrl + "0", HttpMethod.GET, entity, AppResponse.class);
    Assert
        .assertEquals(Response.Status.OK.getStatusCode(), responseEntity.getStatusCodeValue());
    Assert
        .assertEquals(Response.Status.OK.getStatusCode(), responseEntity.getBody().getStatus());

    //Refresh failed!
    responseEntity =
        restTemplate.exchange(apiUrl + "-1", HttpMethod.GET, entity, AppResponse.class);
    Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),
        responseEntity.getStatusCodeValue());
    Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),
        responseEntity.getBody().getStatus());

    logger.info(StringUtil.objectToJson(responseEntity.getBody()));
  }
}

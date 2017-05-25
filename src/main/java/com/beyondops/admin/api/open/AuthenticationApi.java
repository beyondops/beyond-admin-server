package com.beyondops.admin.api.open;

import com.beyondops.admin.api.AbstractApi;
import com.beyondops.admin.api.open.form.LoginForm;
import com.beyondops.admin.common.AppResponse;
import com.beyondops.admin.filter.AuthenticationGuard;
import com.beyondops.admin.model.AuthUser;
import com.beyondops.admin.service.AuthenticationService;
import com.beyondops.admin.util.TimeUtil;
import com.beyondops.admin.util.WebUtil;
import com.beyondops.jooq.model.Tables;
import com.beyondops.jooq.model.tables.records.SysUserRecord;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by caiqinzhou@gmail.com on 2017/1/12.
 */
@Component
@Path("/open/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON})
@Api(value = "Authentication Api", produces = "application/json")
public class AuthenticationApi extends AbstractApi {

  @Autowired
  AuthenticationService authenticationService;

  @Autowired
  private DSLContext dsl;

  @Context
  HttpServletRequest request;

  @Value("${app.jwt.key}")
  private String jwtKey;

  @Value("${app.jwt.expired-time}")
  private Integer expTime;

  /**
   * User login api.
   */
  @POST
  @Path("/login")
  @AuthenticationGuard(required = false)
  @ApiOperation(value = "Login api", response = AppResponse.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Login successfully!"),
      @ApiResponse(code = 400, message = "Wrong username or password!")})
  public Response login(@NotNull @Valid final LoginForm loginForm) {
    //Authentication
    SysUserRecord sysUserRecord =
        authenticationService.authentication(loginForm.getUsername(), loginForm.getPassword());
    if (null == sysUserRecord) {
      return AppResponse.error(i10n("admin.api.open.error1")).build();
    }
    String token = authenticationService.updateUserToken(sysUserRecord.getId(), false);
    //Get user role list
    List<Integer> roles = dsl.selectFrom(Tables.SYS_USER_ROLE)
        .where(Tables.SYS_USER_ROLE.USER_ID.eq(sysUserRecord.getId())).fetch()
        .map(rs -> Integer.valueOf(rs.getRoleId()));

    AuthUser authUser = new AuthUser();
    authUser.setUsername(sysUserRecord.getUsername());
    authUser.setUid(sysUserRecord.getId());
    authUser.setRoles(roles.toArray(new Integer[0]));
    authUser.setClientIp(WebUtil.getClientIp(request));
    authUser.setValidationToken(token);

    AppResponse result =
        WebUtil.generateJwtToken(authUser, jwtKey, expTime, loginForm.getRememberMe());
    //Update user last login time
    sysUserRecord.setLastLogin(TimeUtil.getCurrentTimestamp());
    sysUserRecord.update();
    return result.build();
  }
}

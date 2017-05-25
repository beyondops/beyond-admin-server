package com.beyondops.admin.api.sysuser;

import com.beyondops.admin.common.AppResponse;
import com.beyondops.admin.model.AuthUser;
import com.beyondops.admin.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by caiqinzhou@gmail.com on 2017/1/18.
 */

@Component
@Path("/sysuser/sysuser")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON})
@Api(value = "System User Api", produces = "application/json")
public class SysUserApi {

  private static final Logger logger = LoggerFactory.getLogger(SysUserApi.class);
  @Value("${app.jwt.key}")
  private String jwtKey;

  @Value("${app.jwt.expired-time}")
  private Integer expTime;

  /**
   * Refresh jwt token for keeping alive.
   */
  @GET
  @Path("/refreshToken/{rememberMe}")
  @ApiOperation(value = "Refresh token", response = AppResponse.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Refresh token successfully!")})
  public Response refreshToken(
      @NotNull @Min(value = 0) @PathParam("rememberMe") final int rememberMe,
      @Context SecurityContext sc) {
    logger.debug("SC: {}", sc);
    AuthUser authUser = (AuthUser) sc.getUserPrincipal();
    AppResponse result = WebUtil.generateJwtToken(authUser, jwtKey, expTime, rememberMe);
    return result.build();
  }
}

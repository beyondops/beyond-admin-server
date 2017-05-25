package com.beyondops.admin.filter;

import com.beyondops.admin.common.AppConstants;
import com.beyondops.admin.common.AppResponse;
import com.beyondops.admin.model.AuthUser;
import com.beyondops.admin.service.AuthenticationService;
import com.google.common.base.Strings;
import java.io.IOException;
import java.security.Principal;
import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User authentication filter
 * Created by caiqinzhou@gmail.com on 2017/1/13.
 */

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter
    implements ContainerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);


  @Context
  ResourceInfo resourceInfo;
  @Context
  HttpServletRequest request;
  @Context
  SecurityContext sc;
  @Autowired
  AuthenticationService authenticationService;

  /**
   * Check if request resource is an authentication api.
   */
  public static boolean isAuthenticationApi(ResourceInfo resourceInfo,
      ContainerRequestContext crc) {
    if (AppConstants.SWAGGER_JSON.equals(crc.getUriInfo().getPath())) {
      return false;
    }
    AuthenticationGuard authenticationGuard =
        resourceInfo.getResourceMethod().getAnnotation(AuthenticationGuard.class);
    if (null == authenticationGuard) {
      authenticationGuard =
          resourceInfo.getResourceClass().getAnnotation(AuthenticationGuard.class);
    }
    if (null != authenticationGuard && false == authenticationGuard.required()) {
      return false;
    }
    return true;
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    //Check if the api requires authentication
    if (!isAuthenticationApi(resourceInfo, requestContext)) {
      return;
    }
    SecurityContext sc = requestContext.getSecurityContext();
    AuthUser authUser = getAuthUserFromHeader(requestContext);
    if (null != authUser) {
      requestContext.setSecurityContext(new SecurityContext() {
        @Override
        public Principal getUserPrincipal() {
          return authUser;
        }

        @Override
        public boolean isUserInRole(String roleStr) {
          if (Strings.isNullOrEmpty(roleStr)) {
            return false;
          }
          Integer roleId = Integer.parseInt(roleStr);
          if (null == roleId) {
            return false;
          }
          for (int userRole : authUser.getRoles()) {
            if (userRole == roleId) {
              return true;
            }
          }
          return false;
        }

        @Override
        public boolean isSecure() {
          return sc.isSecure();
        }

        @Override
        public String getAuthenticationScheme() {
          return DIGEST_AUTH;
        }
      });
      return;
    }
    throw new WebApplicationException(
        new AppResponse(Response.Status.UNAUTHORIZED, "Unauthorized, please login!").build());
  }

  /**
   * Get authentication user info.
   */
  private AuthUser getAuthUserFromHeader(ContainerRequestContext requestContext) {
    final String authHeader = requestContext.getHeaderString(AppConstants.JWT_HEADER);
    if (authHeader == null || !authHeader.startsWith(AppConstants.JWT_HEADER_PREFIX)) {
      return null;
    }
    try {
      final String token =
          authHeader.substring(AppConstants.JWT_HEADER_PREFIX.length()).trim();
      return authenticationService.checkJwtToken(token);
    } catch (Exception ex) {
      ex.printStackTrace();
      logger.error("Authentication failed! " + ex.getMessage());
    }
    return null;
  }
}

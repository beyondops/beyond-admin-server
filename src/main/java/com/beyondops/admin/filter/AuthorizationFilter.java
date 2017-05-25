package com.beyondops.admin.filter;

import com.beyondops.admin.common.AppResponse;
import com.beyondops.admin.model.AuthUser;
import com.beyondops.admin.model.ResourceApi;
import com.beyondops.admin.model.ResourceApi.ApiMethod;
import com.beyondops.admin.service.AuthorizationService;
import com.google.common.base.Strings;
import java.io.IOException;
import javax.annotation.Priority;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
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
 * User authorization filter.
 * Created by caiqinzhou@gmail.com on 2017/2/8.
 */
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);

  @Context
  ResourceInfo resourceInfo;

  @Context
  SecurityContext sc;

  @Autowired
  AuthorizationService authorizationService;

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    //Check if the api requires authentication
    if (!AuthenticationFilter.isAuthenticationApi(resourceInfo, requestContext)) {
      return;
    }
    ResourceApi resourceApi = getResourcePath(resourceInfo);
    if (null == resourceApi || Strings.isNullOrEmpty(resourceApi.getPath())) {
      throw new WebApplicationException(
          new AppResponse(Response.Status.FORBIDDEN, "Forbidden! Bad resource path!").build());
    }
    logger.debug("Resource path: {}, method: {} ", resourceApi.getPath(), resourceApi.getMethod());

    if (!authorizationService
        .authorization(
            (AuthUser) sc.getUserPrincipal(),
            resourceApi)) {
      throw new WebApplicationException(
          new AppResponse(Response.Status.FORBIDDEN, "Forbidden! Not allowed!").build());
    }
  }

  /**
   * Get resource url path.
   */
  private ResourceApi getResourcePath(ResourceInfo resourceInfo) {
    String resourcePath = "";
    Path classPath = resourceInfo.getResourceClass().getAnnotation(Path.class);
    if (null != classPath) {
      resourcePath = classPath.value();
    }

    Path methodPath = resourceInfo.getResourceMethod().getAnnotation(Path.class);
    if (null != methodPath) {
      resourcePath += methodPath.value();
    }
    ResourceApi resourceApi = new ResourceApi();
    resourceApi.setPath(resourcePath);
    //Api method, 1:POST, 2:GET, 3:PUT, 4:PATCH, 5:DELETE
    POST post = resourceInfo.getResourceMethod().getAnnotation(POST.class);
    if (null != post) {
      resourceApi.setMethod(ApiMethod.POST);
      return resourceApi;
    }
    GET get = resourceInfo.getResourceMethod().getAnnotation(GET.class);
    if (null != get) {
      resourceApi.setMethod(ApiMethod.GET);
      return resourceApi;
    }
    PUT put = resourceInfo.getResourceMethod().getAnnotation(PUT.class);
    if (null != put) {
      resourceApi.setMethod(ApiMethod.PUT);
      return resourceApi;
    }
    //PATCH is not implemented.
    DELETE delete = resourceInfo.getResourceMethod().getAnnotation(DELETE.class);
    if (null != delete) {
      resourceApi.setMethod(ApiMethod.DELETE);
      return resourceApi;
    }
    return null;
  }
}

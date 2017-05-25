package com.beyondops.admin.config;

import com.beyondops.admin.api.open.AuthenticationApi;
import com.beyondops.admin.api.sysuser.SysUserApi;
import com.beyondops.admin.filter.AuthenticationFilter;
import com.beyondops.admin.filter.AuthorizationFilter;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.AcceptHeaderApiListingResource;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

/**
 * Created by caiqinzhou@gmail.com on 2017/1/12.
 */
@Component
@ApplicationPath("/beyondops/admin")
public class JerseyAdminApiConfig
    extends ResourceConfig {

  /**
   * Register jersey api here.
   */
  public JerseyAdminApiConfig() {
    // Now you can expect validation errors to be sent to the client.
    //        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
    register(AuthenticationFilter.class);
    register(AuthorizationFilter.class);
    register(ConstraintViolationExceptionMapper.class);
    register(AuthenticationApi.class);
    register(SysUserApi.class);


  }

  /**
   * Initializes Swagger Configuration.
   */
  @PostConstruct
  public void initializeSwaggerConfiguration() {
    register(ApiListingResource.class);
    register(AcceptHeaderApiListingResource.class);
    register(SwaggerSerializers.class);
    BeanConfig beanConfig = new BeanConfig();
    beanConfig.setTitle("Beyond Web Server Swagger API");
    beanConfig.setVersion("v1");
    beanConfig.setSchemes(new String[]{"http", "https"});
    beanConfig.setBasePath("/beyondops/admin");
    beanConfig.setResourcePackage("com.beyondops.admin.api");
    beanConfig.setPrettyPrint(true);
    beanConfig.setScan(true);
  }
}

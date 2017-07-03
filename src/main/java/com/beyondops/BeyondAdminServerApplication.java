package com.beyondops;

import com.beyondops.admin.config.JerseyAdminApiConfig;
import com.beyondops.admin.service.SysApiService;
import com.google.common.collect.Lists;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableAsync
@SpringBootApplication
@EnableSwagger2
public class BeyondAdminServerApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication
        .run(BeyondAdminServerApplication.class, args);
    //Scan the api and init api resources.
    context.getBean(SysApiService.class)
        .initApiResource(context.getBean(JerseyAdminApiConfig.class));
  }

  /**
   * Config jersey swagger api doc.
   */
  @Bean
  public Docket jerseyApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .groupName("beyondops")
        .apiInfo(apiInfo())
        .select()
        .build()
        .securitySchemes(Lists.newArrayList(apiKey()));
  }

  /**
   * Config swagger api info.
   */
  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Beyond Web Server Swagger API")
        .description("Beyond Web Server Swagger API")
        .contact(new Contact("Zhou Caiqin", "", "caiqinzhou@gmail.com"))
        .license("MIT License")
        .licenseUrl("https://github.com/beyondops/beyond-admin-server/blob/master/LICENSE")
        .build();
  }

  @Bean
  SecurityConfiguration security() {
    return new SecurityConfiguration(
        null,
        null,
        null,
        "Beyond Web Server",
        "Bearer ",
        ApiKeyVehicle.HEADER,
        "Authorization",
        "," /*scope separator*/);
  }

  private ApiKey apiKey() {
    return new ApiKey("Authorization", "Authorization", "header");
  }
}

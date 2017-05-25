package com.beyondops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableAsync
@SpringBootApplication
@EnableSwagger2
public class BeyondAdminServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(BeyondAdminServerApplication.class, args);
  }

  /**
   * Config jersey swagger api doc.
   */
  @Bean
  public Docket jerseyApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .groupName("Beyond Web Server Swagger API")
        .apiInfo(apiInfo())
        .select()
        .build();
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

}

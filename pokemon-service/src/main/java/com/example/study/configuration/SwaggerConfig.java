package com.example.study.configuration;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
  
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
             .select()
             .apis(RequestHandlerSelectors.basePackage("com.example.study"))
             .paths(PathSelectors.regex("/.*"))
             .build()
             .useDefaultResponseMessages(false)
             .apiInfo(apiInfo());
  }
  
  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
             .title("Pokémon Service")
             .description("Serviço de pesquisa de pokémons.")
             .version("v1")
             .build();
  }
  
  @Bean
  public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier,
                                                                       ServletEndpointsSupplier servletEndpointsSupplier,
                                                                       ControllerEndpointsSupplier controllerEndpointsSupplier,
                                                                       EndpointMediaTypes endpointMediaTypes,
                                                                       CorsEndpointProperties corsProperties,
                                                                       WebEndpointProperties webEndpointProperties,
                                                                       Environment environment) {
    Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
    var allEndpoints = new ArrayList<ExposableEndpoint<?>>();
    allEndpoints.addAll(webEndpoints);
    allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
    allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
    String basePath = webEndpointProperties.getBasePath();
    EndpointMapping endpointMapping = new EndpointMapping(basePath);
    var shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(basePath, environment, webEndpointProperties);
    return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(),
      new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping, null);
  }
  
  private boolean shouldRegisterLinksMapping(final String basePath,
                                             final Environment environment,
                                             final WebEndpointProperties webEndpointProperties) {
    return webEndpointProperties.getDiscovery().isEnabled() &&
             (StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
  }
}
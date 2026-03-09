package com.employeemanagement.EmployeeManagementSystem.configuration;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
      .allowedOrigins("http://localhost:4200")
      .allowedMethods("GET", "POST", "PUT", "DELETE")
      .allowCredentials(true)
      .allowedHeaders("*")
      .exposedHeaders("Set-Cookie"); 
  }
}

package com.jh.loginapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

    private ApiInfo loginApiInfo() {
        return new ApiInfoBuilder().title("Login API")
                .description("Login API Docs").build();
    }

    private Set<String> getConsumeContentTypes() {
        Set<String> consumes = new HashSet<>();
        consumes.add("application/json;charset=UTF-8");
        consumes.add("application/x-www-form-urlencoded");
        return consumes;
    }

    private Set<String> getProduceContentTypes() {
        Set<String> produces = new HashSet<>();
        produces.add("application/json;charset=UTF-8");
        return produces;
    }

    @Bean
    public Docket authApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("인증")
                .apiInfo(this.loginApiInfo())
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.jh.loginapi.auth"))
                .paths(PathSelectors.ant("/**"))
                .build();
    }

    @Bean
    public Docket memberApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("회원")
                .apiInfo(this.loginApiInfo())
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.jh.loginapi.member"))
                .paths(PathSelectors.ant("/**"))
                .build();
    }
}
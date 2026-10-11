package com.cskaoyan.wordmemorize.config;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.BufferingClientHttpRequestFactory;

import java.time.Duration;
import java.util.ArrayList;

@Configuration
@AutoConfigureBefore(SpringDocConfiguration.class)
public class SwaggerConfig {


    @Bean
    public GroupedOpenApi userApi(){
        String[] paths = {"/user/**", "/product/**", "/admin/**"};
        String[] packagedToMatch = { "com.cskaoyan.wordmemorize.controller.app", "com.cskaoyan.wordmemorize.controller.admin" };
        //String[] packagedToMatch = { "com.cskaoyan.wordmemorize.controller.admin" };
        return GroupedOpenApi.builder().group("王道背单词")
                .pathsToMatch(paths)
                .packagesToScan(packagedToMatch).build();
    }
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("王道背单词")
                        .version("1.0")
                        .description( "王道背单词系统接口文档")
                        .termsOfService("http://www.cskaoyan.com")
                        .license(new License().name("Apache 2.0"))
                );
    }



}



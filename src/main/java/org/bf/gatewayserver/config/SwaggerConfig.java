package org.bf.gatewayserver.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi userAPI() {
        return GroupedOpenApi.builder()
                .group("user-api")
                .displayName("USER API")
                .pathsToMatch("/v1/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi reportAPI() {
        return GroupedOpenApi.builder()
                .group("report-api")
                .displayName("REPORT API")
                .pathsToMatch("/v1/report/**")
                .build();
    }

    @Bean
    public GroupedOpenApi imageAPI() {
        return GroupedOpenApi.builder()
                .group("image-api")
                .displayName("IMAGE API")
                .pathsToMatch("/v1/image/**")
                .build();
    }

    @Bean
    public GroupedOpenApi mapAPI() {
        return GroupedOpenApi.builder()
                .group("map-api")
                .displayName("MAP API")
                .pathsToMatch("/v1/map/**")
                .build();
    }

    @Bean
    public GroupedOpenApi pointAPI() {
        return GroupedOpenApi.builder()
                .group("point-api")
                .displayName("POINT API")
                .pathsToMatch("/v1/points/**")
                .build();
    }
}

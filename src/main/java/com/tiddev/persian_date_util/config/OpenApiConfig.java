package com.tiddev.persian_date_util.config;

import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Persian Date Utility API",
                version = "1.0.0",
                description = """
                        API for comprehensive Persian (Jalali) calendar operations,
                        including date conversions, formatting, parsing, and arithmetic.
                        """
        ),
        servers = {
                @Server(
                        url = "/"
                ),
        }
)
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi persianDateApi() {
        return GroupedOpenApi.builder()
                .group("Persian Date Utility")
                .pathsToMatch("/api/persian-date/**")
                .build();
    }
}
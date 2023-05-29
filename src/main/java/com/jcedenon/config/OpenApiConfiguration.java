package com.jcedenon.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@SecurityScheme(name = "jwt", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openApiConfig() {
        Schema<?> mapSchema = new Schema<Map<String, String>>()
                .addProperty("message", new StringSchema().example("string"));
        Schema<?> errorSchema = new Schema<Map<String, String>>()
                .addProperty("error", new StringSchema().example("string"));
        return new OpenAPI()
                .info(new Info()
                        .title("RetoTecnico")
                        .description("Documentation RetoTecnico API v1.0")
                        .version("1.0")
                        .license(new License().name("Netty 2.0").url("http://springdoc.org"))
                        .termsOfService("http://swagger.io/terms/"))
                .components(new Components()
                        .addSchemas("Map", mapSchema)
                        .addSchemas("Error", errorSchema));

    }
}

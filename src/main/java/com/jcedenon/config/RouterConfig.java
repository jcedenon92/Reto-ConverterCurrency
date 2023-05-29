package com.jcedenon.config;

import com.jcedenon.handler.MonedaHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class RouterConfig {

    /**
     * Functional Endpoints for Moneda Entity (CRUD) operations using RouterFunction and HandlerFunction
     * @param handler
     * @return
     */
    @Bean
    public RouterFunction<ServerResponse> routerMoneda(MonedaHandler handler){
        return route(GET("/v3/monedas"), handler::findAll)
                .andRoute(GET("/v3/monedas/{id}"), handler::findById)
                .andRoute(POST("/v3/monedas"), handler::create)
                .andRoute(PUT("/v3/monedas"), handler::update)
                .andRoute(DELETE("/v3/monedas"), handler::delete);
    }
}

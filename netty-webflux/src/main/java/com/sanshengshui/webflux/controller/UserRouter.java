package com.sanshengshui.webflux.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;


import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * @author james mu
 * @date 2019/12/11 16:17
 */
@Configuration
public class UserRouter {
    RouterFunction<ServerResponse> getAllEmployeesRoute() {
        return route(GET("/getAllEmployees"), request -> ok().body(fromObject("Home page")));
    }
}

package com.jphilips.apigateway.config;

import com.jphilips.apigateway.config.routebuilder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GatewayRoutesConfig {

    private final String authServiceUri;
    private final String userDetailsServiceUri;


    private final RouteBuilder routeBuilder;

    @Autowired
    public GatewayRoutesConfig(RouteBuilder routeBuilder,
                               @Value("${AUTH_SERVICE_URI}") String authServiceUri,
                               @Value("${USER_DETAILS_SERVICE_URI}") String userDetailsServiceUri) {
        this.routeBuilder = routeBuilder;
        this.authServiceUri = authServiceUri;
        this.userDetailsServiceUri = userDetailsServiceUri;
    }


    @Bean
    public RouteLocator authServiceRoutes(RouteLocatorBuilder builder) {

        RouteLocatorBuilder.Builder routes = builder.routes();

        // Public route (no filter)
        routeBuilder.addPublicRoute(routes, "auth-service-public-route", "/auth/**", authServiceUri);

        // ----Secured-routes----
        // Auth service
        routeBuilder.addAdminRoute(routes, "auth-service-admin-route", "/admin/auth/users/**", authServiceUri);
        routeBuilder.addUserRoute(routes, "auth-service-user-route", "/users/**", authServiceUri);

        routeBuilder.addUserRoute(routes, "user-details-service-user-route", "/user-details/**", userDetailsServiceUri);


        return routes.build();
    }

}

package com.jphilips.apigateway.config.routebuilder;

import com.jphilips.apigateway.filter.JwtValidationGatewayFilterFactory;
import com.jphilips.shared.enums.EnumRole;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RouteBuilder {

    private final JwtValidationGatewayFilterFactory jwtValidation;

    public void addAdminRoute(RouteLocatorBuilder.Builder builder, String id, String path, String uri) {
        builder.route(id, r -> r
                .path(path)
                .filters(f -> f.filter(
                        jwtValidation.apply(config ->
                                config.setRoles(List.of(EnumRole.ADMIN.name()))
                        )
                ))
                .uri(uri));
    }

    public void addUserRoute(RouteLocatorBuilder.Builder builder, String id, String path, String uri) {
        builder.route(id, r -> r
                .path(path)
                .filters(f -> f.filter(
                        jwtValidation.apply(config ->
                                config.setRoles(List.of(EnumRole.USER.name()))
                        )
                ))
                .uri(uri));
    }

    public void addPublicRoute(RouteLocatorBuilder.Builder builder, String id, String path, String uri) {
        builder.route(id, r -> r
                .path(path)
                .uri(uri));
    }
}
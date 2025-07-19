package com.jphilips.apigateway.config;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RoleBasedAccessConfig {
    private List<String> roles;
}
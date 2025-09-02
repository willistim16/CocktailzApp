package com.cocktailz.cocktailzclean.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig {
    // You don’t need to add anything here yet,
    // this just enables @PreAuthorize and @PostAuthorize.
}

package com.jh.loginapi.config;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private JwtConfig jwtConfig;
    public JwtSecurityConfig(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(
            new JwtAuthenticationFilter(jwtConfig),
            UsernamePasswordAuthenticationFilter.class
        );
    }
}

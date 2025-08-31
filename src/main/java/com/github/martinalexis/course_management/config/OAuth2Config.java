package com.github.martinalexis.course_management.config;

import com.github.martinalexis.course_management.auth.handler.v1.OAuth2AuthenticationSuccessHandler;
import com.github.martinalexis.course_management.auth.service.v1.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Separate configuration class for OAuth2 components.
 * This approach eliminates circular dependencies by separating
 * OAuth2 configuration from the main security configuration.
 * 
 * @author Alexis Martin
 * @version 1.0
 */
@Configuration
public class OAuth2Config {

    /**
     * Creates the OAuth2AuthenticationSuccessHandler as a bean.
     * This approach avoids circular dependencies by creating the handler
     * in a separate configuration class.
     */
    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler(AuthService authService) {
        return new OAuth2AuthenticationSuccessHandler(authService);
    }
} 
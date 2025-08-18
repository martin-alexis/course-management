package com.github.martinalexis.course_managment.config;

import com.github.martinalexis.course_managment.auth.entryPoint.v1.CustomAuthenticationEntryPoint;
import com.github.martinalexis.course_managment.auth.filter.v1.JwtAuthenticationFilter;
import com.github.martinalexis.course_managment.auth.handler.v1.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Main security configuration class for the application.
 *
 * This class configures:
 * - OAuth2 authentication with Google (via handler, no specific endpoints)
 * - JWT-based authentication for API endpoints
 * - Local authentication endpoints (/api/v1/auth/**)
 * - Session management policies
 * - CORS and CSRF settings
 * - Public and protected endpoint access rules
 *
 * @author Alexis Martin
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    /**
     * Single security filter chain that handles all authentication scenarios.
     *
     * Configuration includes:
     * - Local auth endpoints (/api/v1/auth/**) - completely public, no JWT filter
     * - OAuth2 flow (/oauth2/**) - handled by Spring Security OAuth2 with custom handler
     * - Protected endpoints (/api/v1/users/**) - require JWT authentication
     *
     * @param httpSecurity the HTTP security configuration builder
     * @param clientRegistrationRepository repository containing OAuth2 client registrations
     * @param oAuth2AuthenticationSuccessHandler custom handler for OAuth2 success
     * @return configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   ClientRegistrationRepository clientRegistrationRepository,
                                                   OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        // Local authentication endpoints - completely public
                        .requestMatchers("/api/v1/auth/**", "api/docs").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        // OAuth2 endpoints - handled by Spring Security OAuth2
                        .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()

                        // Protected endpoints - require authentication
                        .requestMatchers("/api/v1/users/**").authenticated()

                        // Any other request requires authentication by default
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint))

                .authenticationProvider(authenticationProvider())

                // Add JWT filter BEFORE username/password authentication filter
                // But it won't interfere with local auth endpoints due to permitAll()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // Disable form login and basic auth
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // Configure OAuth2 login with custom success handler
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureUrl("/login?error=true")
                )
                .build();
    }

    /**
     * Configures the authentication manager for the application.
     *
     * The authentication manager is responsible for:
     * - Coordinating authentication between different providers
     * - Managing authentication flows
     * - Handling authentication exceptions
     *
     * @param authenticationConfiguration the authentication configuration
     * @return configured AuthenticationManager
     * @throws Exception if configuration fails
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures the authentication provider for local user authentication.
     *
     * This provider:
     * - Uses the configured UserDetailsService to load user information
     * - Applies password encoding for secure password comparison
     * - Handles local user authentication (email/password)
     *
     * @return configured DaoAuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Configures the password encoder for secure password handling.
     *
     * Uses BCrypt algorithm which:
     * - Automatically generates and manages salt
     * - Provides strong cryptographic hashing
     * - Is resistant to rainbow table attacks
     * - Is the industry standard for password hashing
     *
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
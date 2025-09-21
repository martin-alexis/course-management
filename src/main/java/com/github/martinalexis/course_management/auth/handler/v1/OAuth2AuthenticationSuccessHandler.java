package com.github.martinalexis.course_management.auth.handler.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.martinalexis.course_management.auth.dto.v1.AuthResponseDto;
import com.github.martinalexis.course_management.auth.service.v1.AuthService;
import com.github.martinalexis.course_management.auth.service.v1.facade.AuthUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles successful OAuth2 authentication with Google.
 *
 * This handler is responsible for:
 * - Processing successful OAuth2 logins from Google
 * - Extracting user information from OAuth2 response
 * - Calling the authentication service to process/create users
 * - Returning JWT tokens and user information as JSON response
 * - Providing a seamless authentication flow for SPAs
 *
 * The handler is automatically invoked by Spring Security after successful
 * OAuth2 authentication, eliminating the need for redirect-based flows.
 *
 * @author Alexis Martin
 * @version 1.0
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthUseCase authUseCase;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Handles successful OAuth2 authentication.
     *
     * This method is automatically called by Spring Security after successful
     * OAuth2 authentication with Google. It:
     *
     * 1. Extracts user information from the OAuth2 authentication object
     * 2. Calls the authentication service to process the OAuth2 user
     * 3. Returns a JSON response containing JWT tokens and user information
     * 4. Handles any errors that occur during processing
     *
     * The response includes:
     * - accessToken: JWT token for API access (short-lived)
     * - refreshToken: JWT token for token renewal (long-lived)
     * - user: complete user information
     * - expiresIn: token expiration time in milliseconds
     *
     * @param request the HTTP request that triggered the authentication
     * @param response the HTTP response to write the authentication result to
     * @param authentication the successful authentication object containing OAuth2 user details
     * @throws IOException if response writing fails
     * @throws ServletException if servlet processing fails
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try {
            // Extract OAuth2 user information
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email");
            String name = oAuth2User.getAttribute("given_name");
            String lastname = oAuth2User.getAttribute("family_name");

            // Process the OAuth2 user through the authentication service
            AuthResponseDto authResponse = authUseCase.processOAuth2User(email, name, lastname);

            // Set response headers and write JSON response
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json;charset=UTF-8");
            objectMapper.writeValue(response.getOutputStream(), authResponse);

            log.info("OAuth2 authentication successful for user: {}", email);
        } catch (Exception ex) {
            log.error("OAuth2 success handling failed: {}", ex.getMessage(), ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "OAuth2 processing error");
        }
    }
}
package com.github.martinalexis.course_management.auth.filter.v1;

import com.github.martinalexis.course_management.auth.service.v1.JwtService;
import com.github.martinalexis.course_management.auth.service.v1.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter that intercepts HTTP requests to validate JWT tokens.
 * 
 * This filter is responsible for:
 * - Intercepting all incoming HTTP requests
 * - Extracting JWT tokens from Authorization headers
 * - Validating token authenticity and expiration
 * - Setting up Spring Security context for authenticated users
 * - Allowing public endpoints to pass through without authentication
 * 
 * The filter integrates with Spring Security's authentication mechanism
 * and provides stateless JWT-based authentication for the API.
 * 
 * @author Alexis Martin
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Filters HTTP requests to implement JWT-based authentication.
     * 
     * This method is called for every HTTP request and performs the following:
     * 
     * 1. **Public Endpoint Check**: Allows requests to `/api/v1/auth/**` to pass through
     * 2. **Token Extraction**: Looks for "Bearer" token in Authorization header
     * 3. **Token Validation**: Verifies JWT signature and expiration
     * 4. **User Loading**: Loads user details from the database
     * 5. **Context Setup**: Establishes Spring Security authentication context
     * 6. **Request Continuation**: Allows the request to proceed to the controller
     * 
     * **Filter Behavior:**
     * - If no token: request continues (may be blocked by other security rules)
     * - If invalid token: request continues (will be blocked by endpoint security)
     * - If valid token: user is authenticated and request proceeds
     * 
     * **Security Context:**
     * - Sets up UsernamePasswordAuthenticationToken with user authorities
     * - Establishes user identity for the current request
     * - Enables @PreAuthorize and other security annotations to work
     * 
     * @param request the HTTP request being processed
     * @param response the HTTP response (not modified by this filter)
     * @param filterChain the filter chain to continue processing
     * @throws ServletException if servlet processing fails
     * @throws IOException if I/O operations fail
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        // Skip JWT processing for public authentication endpoints
        String servletPath = request.getServletPath();
        if (servletPath.startsWith("/api/v1/auth/") ||
                servletPath.startsWith("/v3/api-docs") ||
                servletPath.startsWith("/swagger-ui") ||
                servletPath.equals("/swagger-ui.html")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Extract JWT token from Authorization header
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String idUser;
        
        // Check if Authorization header exists and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Extract the JWT token (remove "Bearer " prefix)
        jwt = authHeader.substring(7);
        
        try {
            // Extract user ID from JWT token
            idUser = jwtService.extractIdUser(jwt);
            
            // If user ID extracted and no existing authentication context
            if (idUser != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Load user details from database
                Integer id = Integer.parseInt(idUser);
                UserDetails userDetails = this.userDetailsService.loadUserById(id);
                
                // Validate JWT token against user details
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    
                    // Create authentication token with user details and authorities
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,        // Principal (user details)
                            null,              // Credentials (not needed for JWT)
                            userDetails.getAuthorities()  // User authorities/roles
                    );
                    
                    // Set additional authentication details
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    
                    // Establish authentication context for the current request
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    log.debug("JWT authentication successful for user: {}", idUser);
                }
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }
        
        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
} 
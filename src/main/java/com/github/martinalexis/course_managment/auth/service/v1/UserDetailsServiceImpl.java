package com.github.martinalexis.course_managment.auth.service.v1;

import com.github.martinalexis.course_managment.user.model.UserModel;
import com.github.martinalexis.course_managment.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of UserDetailsService for Spring Security integration.
 * 
 * This service is responsible for:
 * - Loading user information from the database
 * - Converting application UserModel to Spring Security UserDetails
 * - Providing user authorities and authentication status
 * - Supporting both local and OAuth2 user authentication
 * 
 * The service integrates with Spring Security's authentication mechanism
 * and enables JWT token generation and validation.
 * 
 * @author Alexis Martin
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads user details by username (user ID) for Spring Security.
     * <p>
     * This method is called by Spring Security during authentication to:
     * - Load user information from the database
     * - Create UserDetails object with user credentials and authorities
     * - Support JWT token validation and generation
     * <p>
     * **Important Notes:**
     * - The username parameter is actually the user ID (as string)
     * - OAuth2 users have password set to "OAUTH" (not used for authentication)
     * - Local users have their actual hashed password
     * - All users are assigned "USER" authority
     * <p>
     * **UserDetails Structure:**
     * - username: user ID as string (used as JWT subject)
     * - password: hashed password or "OAUTH" for OAuth2 users
     * - authorities: "USER" role for all authenticated users
     * - enabled: always true (users are enabled by default)
     *
     * @param email the user's email address for authentication
     * @return UserDetails object containing user authentication information
     * @throws UsernameNotFoundException if user is not found in the database
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Find user in database by email
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Create Spring Security UserDetails object
        return User.builder()
                .username(user.getEmail())  // Use email as username
                .password(user.getPassword() == null ? "OAUTH" : user.getPassword()) // Handle OAuth2 vs local users
                .authorities("USER") // Default authority
                .build();
    }

    /**
     * Loads user details by user ID for JWT token validation.
     * 
     * This custom method is specifically used for:
     * - JWT token validation in the authentication filter
     * - Loading user information after token parsing
     * - Converting user ID from JWT subject to UserDetails
     * 
     * **Important Notes:**
     * - The ID parameter is the user's database primary key
     * - OAuth2 users have password set to "OAUTH" (not used for authentication)
     * - Local users have their actual hashed password
     * - All users are assigned "USER" authority
     * - Username in returned UserDetails is the user ID as string
     * 
     * @param id the user's database ID (extracted from JWT token)
     * @return UserDetails object containing user authentication information
     * @throws UsernameNotFoundException if user is not found in the database
     */
    public UserDetails loadUserById(Integer id) throws UsernameNotFoundException {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + id));

        return User.builder()
                .username(user.getIdUser().toString()) // Use ID as username
                .password(user.getPassword() == null ? "OAUTH" : user.getPassword())
                .authorities("USER")
                .build();
    }
}

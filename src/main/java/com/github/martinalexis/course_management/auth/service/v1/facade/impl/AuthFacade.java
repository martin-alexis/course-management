package com.github.martinalexis.course_management.auth.service.v1.facade.impl;

import com.github.martinalexis.course_management.auth.dto.v1.AuthRequestDto;
import com.github.martinalexis.course_management.auth.dto.v1.AuthResponseDto;
import com.github.martinalexis.course_management.auth.dto.v1.RefreshTokenRequestDto;
import com.github.martinalexis.course_management.auth.dto.v1.RegisterRequestDto;
import com.github.martinalexis.course_management.auth.exceptions.v1.RefreshTokenExpiredException;
import com.github.martinalexis.course_management.auth.mapper.v1.AuthMapper;
import com.github.martinalexis.course_management.auth.service.v1.AuthService;
import com.github.martinalexis.course_management.auth.service.v1.JwtService;
import com.github.martinalexis.course_management.auth.service.v1.UserDetailsServiceImpl;
import com.github.martinalexis.course_management.auth.service.v1.facade.AuthUseCase;
import com.github.martinalexis.course_management.user.model.UserModel;
import com.github.martinalexis.course_management.user.service.v1.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthFacade implements AuthUseCase {

    private final AuthService authService;
    private final UserService userService;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthMapper authMapper;

    @Override
    @Transactional
    public AuthResponseDto register(RegisterRequestDto request) {
        UserModel newUser = authMapper.registerRequestToEntity(request);

        UserModel savedUser = userService.registerNewUser(newUser);

        UserDetails userDetails = userDetailsService.loadUserById(savedUser.getIdUser());

        String jwt = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthResponseDto.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .expiresIn(jwtService.getJwtExpiration())
                .build();
    }


    @Override
    @Transactional
    public AuthResponseDto login(AuthRequestDto request) {

        authService.authenticateCredentials(request.getEmail(), request.getPassword());

        UserModel user = userService.findByEmailOrThrow(request.getEmail());

        UserDetails userDetails = userDetailsService.loadUserById(user.getIdUser());

        String jwt = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthResponseDto.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .expiresIn(jwtService.getJwtExpiration())
                .build();
    }


    @Override
    @Transactional
    public AuthResponseDto processOAuth2User(String email, String name, String lastname) {
        UserModel user = userService.findOrCreateOAuth2User(email, name, lastname);

        UserDetails userDetails = userDetailsService.loadUserById(user.getIdUser());
        String jwt = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthResponseDto.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .expiresIn(jwtService.getJwtExpiration())
                .build();
    }

    @Override
    @Transactional
    public AuthResponseDto refreshToken(RefreshTokenRequestDto refreshToken) {
        String idUser = jwtService.extractIdUser(refreshToken.getRefreshToken());

        if (idUser == null || jwtService.isTokenExpired(refreshToken.getRefreshToken())) {
            throw new RefreshTokenExpiredException();
        }

        Integer id = Integer.parseInt(idUser);
        UserDetails userDetails = userDetailsService.loadUserById(id);

        String newAccessToken = jwtService.generateToken(userDetails);

        return AuthResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .expiresIn(jwtService.getJwtExpiration())
                .build();
    }
}

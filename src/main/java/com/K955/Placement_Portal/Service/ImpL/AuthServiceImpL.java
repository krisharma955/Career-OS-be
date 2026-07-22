package com.K955.Placement_Portal.Service.ImpL;

import com.K955.Placement_Portal.DTOs.Auth.AuthResponse;
import com.K955.Placement_Portal.DTOs.Auth.LoginRequest;
import com.K955.Placement_Portal.DTOs.Auth.RefreshTokenRequest;
import com.K955.Placement_Portal.DTOs.Auth.SignupRequest;
import com.K955.Placement_Portal.Entity.Company;
import com.K955.Placement_Portal.Entity.RefreshToken;
import com.K955.Placement_Portal.Entity.Student;
import com.K955.Placement_Portal.Entity.User;
import com.K955.Placement_Portal.Enums.Role;
import com.K955.Placement_Portal.Exceptions.BadRequestException;
import com.K955.Placement_Portal.Exceptions.ResourceNotFoundException;
import com.K955.Placement_Portal.Repository.CompanyRepository;
import com.K955.Placement_Portal.Repository.RefreshTokenRepository;
import com.K955.Placement_Portal.Repository.StudentRepository;
import com.K955.Placement_Portal.Repository.UserRepository;
import com.K955.Placement_Portal.Security.JwtUtil;
import com.K955.Placement_Portal.Service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpL implements AuthService {

    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenExpiration;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    @Override
    public AuthResponse signup(SignupRequest request) {
        Boolean check = userRepository.existsByEmail(request.email());
        if(check) throw new BadRequestException("User with email: " +request.email()+ " already exists.");

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .role(request.role())
                .build();
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        if(user.getRole().equals(Role.STUDENT)) {
            Student student = Student.builder()
                    .user(user)
                    .build();
            studentRepository.save(student);
        }
        else if(user.getRole().equals(Role.COMPANY)) {
            Company company = Company.builder()
                    .user(user)
                    .build();
            companyRepository.save(company);
        }

        String accessToken = jwtUtil.generateAccessToken(user);

        String refreshToken = jwtUtil.generateRefreshToken(user);
        RefreshToken refreshToken1 = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiryDate(Instant.now().plusMillis(refreshTokenExpiration))
                .build();
        refreshTokenRepository.save(refreshToken1);

        return new AuthResponse(accessToken, refreshToken, user.getName(), user.getEmail(), user.getRole());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.email()));

        String accessToken = jwtUtil.generateAccessToken(user);

        refreshTokenRepository.deleteByUserId(user.getId()); //delete the prev refresh token to get a new one
        refreshTokenRepository.flush();  // force delete SQL to execute

        String refreshToken = jwtUtil.generateRefreshToken(user);
        RefreshToken refreshToken1 = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiryDate(Instant.now().plusMillis(refreshTokenExpiration))
                .build();
        refreshTokenRepository.save(refreshToken1);

        return new AuthResponse(accessToken, refreshToken, user.getName(), user.getEmail(), user.getRole());
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.token())
                .orElseThrow(() -> new BadRequestException("Refresh Token not found!"));

        if(refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new BadRequestException("Refresh Token has expired, please login again");
        }

        User user = refreshToken.getUser();
        String accessToken = jwtUtil.generateAccessToken(user);

        refreshTokenRepository.delete(refreshToken);
        refreshTokenRepository.flush();

        String newRefreshToken = jwtUtil.generateRefreshToken(user);
        RefreshToken newRefreshTokenEntity = RefreshToken.builder()
                .token(newRefreshToken)
                .user(user)
                .expiryDate(Instant.now().plusMillis(refreshTokenExpiration))
                .build();
        refreshTokenRepository.save(newRefreshTokenEntity);

        return new AuthResponse(accessToken, newRefreshToken, user.getName(), user.getEmail(), user.getRole());
    }

    @Override
    public void logout(String token) {
        String email = jwtUtil.extractUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", email));

        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Refresh Token", user.getId().toString()));

        refreshTokenRepository.delete(refreshToken);
    }

}

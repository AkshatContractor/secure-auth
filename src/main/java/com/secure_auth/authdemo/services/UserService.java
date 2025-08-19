package com.secure_auth.authdemo.services;

import com.secure_auth.authdemo.dto.request.ResetPassEmailReqDto;
import com.secure_auth.authdemo.dto.request.ResetPassRequestDto;
import com.secure_auth.authdemo.dto.request.UserRequestDto;
import com.secure_auth.authdemo.dto.response.PasswordResetInitiationResponse;
import com.secure_auth.authdemo.dto.response.UserResponseDto;
import com.secure_auth.authdemo.models.PasswordResetToken;
import com.secure_auth.authdemo.models.User;
import com.secure_auth.authdemo.repositories.PasswordResetRepo;
import com.secure_auth.authdemo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetRepo passwordResetRepo;

    public UserResponseDto registerUser(UserRequestDto requestDto) {
        if (repo.existsByUsername(requestDto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (repo.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Map DTO to Entity
        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        //map responseDto back to the responseDto DTO
        User savedUser = repo.save(user);
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(savedUser.getId());
        responseDto.setEmail(savedUser.getEmail());
        responseDto.setUsername(savedUser.getUsername());

        return responseDto;
    }

    public PasswordResetInitiationResponse resetPassword(ResetPassRequestDto resetPassRequestDto) {
        String username = resetPassRequestDto.getUsername();
        Optional<User> userOptional = repo.findByUsername(username);

        if(userOptional.isPresent()){
            User user = userOptional.get();

            Optional<PasswordResetToken> tokenOptional = passwordResetRepo.findByUserAndExpirationDateAfter(user, LocalDateTime.now());
            if(tokenOptional.isPresent()) {
                return new PasswordResetInitiationResponse(false, "A password reset link has already been sent to this email. Please check your inbox.");
            }

            passwordResetRepo.deleteByUser(user);
            String token = UUID.randomUUID().toString();
            LocalDateTime expiryTime = LocalDateTime.now().plusSeconds(30);

            PasswordResetToken resetToken = new PasswordResetToken(token, user, expiryTime);
            passwordResetRepo.save(resetToken);

            String resetLink = "https://your-frontend-url/reset-password?token=\" + token";
            String email = user.getEmail();

            ResetPassEmailReqDto emailDto = new ResetPassEmailReqDto(email, resetLink);

            WebClient webClient = WebClient.builder().build();
            webClient.post()
                    .uri("http://localhost:8081/api/reset/send-link")
                    .body(Mono.just(emailDto), ResetPassEmailReqDto.class)
                    .retrieve()
                    .toBodilessEntity()
                    .subscribe(
                            response -> System.out.println("Email request sent successfully."),
                            error -> System.err.println("Error communicating with email service: " + error.getMessage())
                    );
            return new PasswordResetInitiationResponse(true, "If an account with that username exists, a password reset link has been sent.");
        }
        return new PasswordResetInitiationResponse(true, "If an account with that username exists, a password reset link has been sent.");
    }
}

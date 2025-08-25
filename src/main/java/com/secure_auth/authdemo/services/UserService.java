package com.secure_auth.authdemo.services;

import com.secure_auth.authdemo.dto.request.ConfirmResetPasswordRequestDto;
import com.secure_auth.authdemo.dto.request.ResetPassEmailReqDto;
import com.secure_auth.authdemo.dto.request.ResetPassRequestDto;
import com.secure_auth.authdemo.dto.request.UserRequestDto;
import com.secure_auth.authdemo.dto.response.PasswordResetInitiationResponse;
import com.secure_auth.authdemo.dto.response.UserResponseDto;
import com.secure_auth.authdemo.enums.NewPassEnum;
import com.secure_auth.authdemo.enums.UserRoleAssignEnum;
import com.secure_auth.authdemo.models.PasswordResetToken;
import com.secure_auth.authdemo.models.User;
import com.secure_auth.authdemo.repositories.PasswordResetRepo;
import com.secure_auth.authdemo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private UserRepo userRepo;

    @Value("${FRONTEND_URL}")
    private String frontendBaseUrl;

    @Value("${RESET_PASS_EMAIL_SERVICE}")
    private String email_service_reset_pass;
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
        user.setRole(UserRoleAssignEnum.USER);

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
        //if only user is present then some action(by email of user)
        if(userOptional.isPresent()){
            User user = userOptional.get();


            Optional<PasswordResetToken> tokenOptional = passwordResetRepo.findByUserAndExpirationDateAfter(user, LocalDateTime.now());
            // if the token is already present and is active then return from this point
            if(tokenOptional.isPresent()) {
                return new PasswordResetInitiationResponse(false, "A password reset link has already been sent to this email. Please check your inbox.");
            }
            //if token is not active then delete first
            passwordResetRepo.deleteByUser(user);
            //generate new token
            String token = UUID.randomUUID().toString();
            LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(2);
            //create new record
            PasswordResetToken resetToken = new PasswordResetToken(token, user, expiryTime);
            passwordResetRepo.save(resetToken);

            String resetLink = String.format("%s/reset-pass-confirm?token=%s", frontendBaseUrl, token);
            String email = user.getEmail();

            ResetPassEmailReqDto emailDto = new ResetPassEmailReqDto(email, resetLink);

            WebClient webClient = WebClient.builder().build();
            webClient.post()
                    .uri(email_service_reset_pass)
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

    public NewPassEnum confirmResetPasswordReq(ConfirmResetPasswordRequestDto confirmResetPasswordRequestDto) {
        String token = confirmResetPasswordRequestDto.getToken();
        String newPassword = confirmResetPasswordRequestDto.getNew_password();

        if(token == null || newPassword == null) {
            return NewPassEnum.INVALID;
        }

        Optional<PasswordResetToken> passwordResetTokenOptional = passwordResetRepo.findByToken(token);

        // 1 check if token is present or not
        if (passwordResetTokenOptional.isEmpty()) {
            return NewPassEnum.INVALID;
        }

        PasswordResetToken passwordResetToken = passwordResetTokenOptional.get();
        // 2 check for expired token now
        if (passwordResetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            passwordResetRepo.delete(passwordResetToken);
            return NewPassEnum.EXPIRED;
        }

        User user = passwordResetToken.getUser();
        // if user is not linked to token then return from here
        if(user == null) {
            return NewPassEnum.INVALID;
        }

        //3 update password if everything is fine
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        //4 delete token after one use
        passwordResetRepo.delete(passwordResetToken);
        return NewPassEnum.SUCCESS;
    }
}

package com.secure_auth.authdemo.controllers;

import com.secure_auth.authdemo.components.JwtConfig;
import com.secure_auth.authdemo.dto.request.ConfirmResetPasswordRequestDto;
import com.secure_auth.authdemo.dto.request.OtpRequestDto;
import com.secure_auth.authdemo.dto.request.ResetPassRequestDto;
import com.secure_auth.authdemo.dto.request.UserRequestDto;
import com.secure_auth.authdemo.dto.response.LoginSuccessDto;
import com.secure_auth.authdemo.dto.response.PasswordChangedDto;
import com.secure_auth.authdemo.dto.response.PasswordResetInitiationResponse;
import com.secure_auth.authdemo.dto.response.UserResponseDto;
import com.secure_auth.authdemo.enums.NewPassEnum;
import com.secure_auth.authdemo.services.OtpCallerService;
import com.secure_auth.authdemo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtConfig jwtconfigure;

    @Autowired
    private OtpCallerService otpCallerService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        UserResponseDto responseDto = userService.registerUser(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody UserRequestDto userRequestDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userRequestDto.getEmail(), userRequestDto.getPassword()));
        LoginSuccessDto loginSuccessDto = new LoginSuccessDto();
        if (authentication.isAuthenticated()) {
            String email = userRequestDto.getEmail();
            if(email == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
            }
            otpCallerService.generateOtp(email).subscribe();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Otp Sent to Registered  Email");
        }
        loginSuccessDto.setToken("Not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(loginSuccessDto);
    }

    @PostMapping("/verify-otp")
    public Mono<ResponseEntity<LoginSuccessDto>> verifyOtp(@RequestBody OtpRequestDto otpRequestDto) {
        String email = otpRequestDto.getEmail();
        String otp = otpRequestDto.getOtp();

        return otpCallerService.verifyOtp(email, otp)
                .map(otpResponseDto -> {
                    switch (otpResponseDto.getOtpResponseEnum()) {
                        case SUCCESS:
                            String token = jwtconfigure.generateToken(email);
                            LoginSuccessDto loginSuccess = new LoginSuccessDto();
                            loginSuccess.setToken(token);
                            return ResponseEntity.status(HttpStatus.OK).body(loginSuccess);
                        case NOT_FOUND:
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginSuccessDto());
                        case INVALID:
                            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginSuccessDto());
                        case EXPIRED:
                            return ResponseEntity.status(HttpStatus.GONE).body(new LoginSuccessDto());
                        default:
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginSuccessDto());
                    }
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginSuccessDto()))
                .onErrorResume(e -> {
                    System.err.println("Error calling OTP service: " + e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginSuccessDto()));
                });
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPassRequestDto resetPassRequestDto) {
        try{
            PasswordResetInitiationResponse response = userService.resetPassword(resetPassRequestDto);
            if(!response.isSuccess()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
            }
            return ResponseEntity.status(HttpStatus.OK).body(response.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred. Please try again later.");
        }
    }

    @PostMapping("/confirm-reset-password")
    public ResponseEntity<PasswordChangedDto> confirmResetPassword(@RequestBody ConfirmResetPasswordRequestDto confirmResetPasswordRequestDto) {
        NewPassEnum newPassEnumResponse  = userService.confirmResetPasswordReq(confirmResetPasswordRequestDto);
        PasswordChangedDto passwordChangedDto = new PasswordChangedDto();
        switch (newPassEnumResponse) {
            case SUCCESS:
                passwordChangedDto.setPass_change_response("Password updated successfully");
                return ResponseEntity.status(HttpStatus.OK).body(passwordChangedDto);
            case INVALID:
                passwordChangedDto.setPass_change_response("Invalid link please request a new one");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(passwordChangedDto);
            case EXPIRED:
                passwordChangedDto.setPass_change_response("The password reset link has expired. Please request a new one");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(passwordChangedDto);
            default:
                passwordChangedDto.setPass_change_response("An unexpected error occurred");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(passwordChangedDto);
        }
    }
}

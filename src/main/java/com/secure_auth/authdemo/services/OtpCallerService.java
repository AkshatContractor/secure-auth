package com.secure_auth.authdemo.services;

import com.secure_auth.authdemo.dto.request.OtpRequestDto;
import com.secure_auth.authdemo.dto.response.OtpResponseDto;
import com.secure_auth.authdemo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class OtpCallerService {

    @Autowired
    private UserRepo repo;

    private final WebClient webClient;

    public OtpCallerService(WebClient.Builder builder,
                            @Value("${otp.service.url:http://localhost:8081}") String otpServiceUrl) {
        this.webClient = builder.baseUrl(otpServiceUrl).build();
    }

    public Mono<Void> generateOtp(String email) {
        return webClient.post()
                .uri("/api/otp/generate")  // Use relative path
                .bodyValue(Map.of("email", email))
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<OtpResponseDto> verifyOtp(String email, String otp) {
        return webClient.post()
                .uri("/api/otp/verify-otp")  // Use relative path
                .bodyValue(Map.of("email", email, "otp", otp))
                .retrieve()
                .bodyToMono(OtpResponseDto.class);
    }
}

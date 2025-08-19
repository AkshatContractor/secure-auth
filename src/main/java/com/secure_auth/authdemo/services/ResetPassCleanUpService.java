package com.secure_auth.authdemo.services;

import com.secure_auth.authdemo.repositories.PasswordResetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ResetPassCleanUpService {

    @Autowired
    private PasswordResetRepo passwordResetRepo;

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanExpiredResetPassLink() {
        System.out.println("Clean up is in progress");
        passwordResetRepo.deleteAllByExpirationDateBefore(LocalDateTime.now());
    }
}

package com.secure_auth.authdemo.repositories;

import com.secure_auth.authdemo.models.PasswordResetToken;
import com.secure_auth.authdemo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetRepo extends JpaRepository<PasswordResetToken, Long> {


    @Transactional
    void deleteByUser(User user);

    @Transactional
    Optional<PasswordResetToken> findByUserAndExpirationDateAfter(User user, LocalDateTime localDateTime);

    @Transactional
    void deleteAllByExpirationDateBefore(LocalDateTime dateTime);

}

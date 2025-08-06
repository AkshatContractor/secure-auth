package com.secure_auth.authdemo.services;

import com.secure_auth.authdemo.dto.request.UserRequestDto;
import com.secure_auth.authdemo.dto.response.UserResponseDto;
import com.secure_auth.authdemo.models.User;
import com.secure_auth.authdemo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
}

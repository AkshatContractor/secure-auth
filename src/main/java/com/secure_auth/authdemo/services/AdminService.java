package com.secure_auth.authdemo.services;

import com.secure_auth.authdemo.dto.request.admin.UserRoleAssignDto;
import com.secure_auth.authdemo.enums.UserRoleAssignEnum;
import com.secure_auth.authdemo.exception.custom.UserNotFoundExcep;
import com.secure_auth.authdemo.models.User;
import com.secure_auth.authdemo.repositories.UserRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    private final UserRepo userRepo;

    public AdminService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void assignUserRole(UserRoleAssignDto userRoleAssignDto) {
        String username = userRoleAssignDto.getUsername();
        UserRoleAssignEnum userRoleAssignEnum = userRoleAssignDto.getRole();
        Optional<User> userOptional = userRepo.findByUsername(username);

        if(userOptional.isEmpty()) {
            throw new UserNotFoundExcep("User not found " + username);
        }

        User user = userOptional.get();
        user.setRole(userRoleAssignEnum);

        userRepo.save(user);
    }
}

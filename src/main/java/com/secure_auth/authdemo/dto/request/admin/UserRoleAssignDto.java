package com.secure_auth.authdemo.dto.request.admin;

import com.secure_auth.authdemo.enums.UserRoleAssignEnum;
import com.secure_auth.authdemo.models.User;

public class UserRoleAssignDto {

    private UserRoleAssignEnum role;
    private String username;

    public UserRoleAssignDto() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRoleAssignDto(UserRoleAssignEnum role, String username) {
        this.role = role;
        this.username = username;
    }

    public UserRoleAssignEnum getRole() {
        return role;
    }

    public void setRole(UserRoleAssignEnum role) {
        this.role = role;
    }
}

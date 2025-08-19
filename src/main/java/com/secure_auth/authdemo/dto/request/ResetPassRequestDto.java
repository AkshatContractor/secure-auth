package com.secure_auth.authdemo.dto.request;

public class ResetPassRequestDto {

    private String username;

    public ResetPassRequestDto(){};

    public ResetPassRequestDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

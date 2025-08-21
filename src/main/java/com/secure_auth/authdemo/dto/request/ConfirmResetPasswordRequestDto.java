package com.secure_auth.authdemo.dto.request;

public class ConfirmResetPasswordRequestDto {
    String new_password;
    String token;

    public ConfirmResetPasswordRequestDto() {}

    public ConfirmResetPasswordRequestDto(String new_password, String token) {
        this.new_password = new_password;
        this.token = token;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

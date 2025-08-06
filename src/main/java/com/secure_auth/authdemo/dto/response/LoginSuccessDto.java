package com.secure_auth.authdemo.dto.response;


public class LoginSuccessDto {
    private String token;

    public LoginSuccessDto() {};

    public LoginSuccessDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginSuccessDto{token='" + token + "'}";
    }
}

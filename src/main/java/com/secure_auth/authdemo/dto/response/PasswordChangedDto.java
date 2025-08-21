package com.secure_auth.authdemo.dto.response;

public class PasswordChangedDto {

    private String pass_change_response;

    public PasswordChangedDto() {}

    public PasswordChangedDto(String pass_change_response) {
        this.pass_change_response = pass_change_response;
    }

    public String getPass_change_response() {
        return pass_change_response;
    }

    public void setPass_change_response(String pass_change_response) {
        this.pass_change_response = pass_change_response;
    }
}

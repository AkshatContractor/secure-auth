package com.secure_auth.authdemo.dto.request;

public class ResetPassEmailReqDto {
    String email;
    String resetLink;

    public ResetPassEmailReqDto(){};

    public ResetPassEmailReqDto(String email, String resetLink) {
        this.email = email;
        this.resetLink = resetLink;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResetLink() {
        return resetLink;
    }

    public void setResetLink(String resetLink) {
        this.resetLink = resetLink;
    }
}

package com.secure_auth.authdemo.dto.request;

public class OtpRequestDto {
    private String email;
    private String otp;

    public OtpRequestDto() {}

    public OtpRequestDto(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

    public OtpRequestDto(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}

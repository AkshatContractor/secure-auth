package com.secure_auth.authdemo.dto.response;

import com.secure_auth.authdemo.enums.OtpResponseEnum;

public class OtpResponseDto {

    private String otp_response;
    private OtpResponseEnum otpResponseEnum;

    public OtpResponseDto() {}

    public OtpResponseDto(String otp_response) {
        this.otp_response = otp_response;
    }

    public OtpResponseDto(String otp_response, OtpResponseEnum otpResponseEnum) {
        this.otp_response = otp_response;
        this.otpResponseEnum = otpResponseEnum;
    }

    public String getOtp_response() {
        return otp_response;
    }

    public void setOtp_response(String otp_response) {
        this.otp_response = otp_response;
    }

    public OtpResponseEnum getOtpResponseEnum() {
        return otpResponseEnum;
    }

    public void setOtpResponseEnum(OtpResponseEnum otpResponseEnum) {
        this.otpResponseEnum = otpResponseEnum;
    }
}

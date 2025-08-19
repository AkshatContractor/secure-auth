package com.secure_auth.authdemo.dto.response;

public class PasswordResetInitiationResponse {
    private boolean success;
    private String message;

    public PasswordResetInitiationResponse() {};

    public PasswordResetInitiationResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

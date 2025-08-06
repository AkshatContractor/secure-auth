package com.secure_auth.authdemo.dto.response;

public class HelloWorldResponseDto {
    private String response;

    public HelloWorldResponseDto() {
    }

    public String getResponse() {
        return response;
    }

    public HelloWorldResponseDto(String response) {
        this.response = response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}

package com.secure_auth.authdemo.controllers;

import com.secure_auth.authdemo.dto.response.HelloWorldResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api")
public class HelloWorldController {

    @GetMapping("/hello")
    @ResponseBody
    public ResponseEntity<HelloWorldResponseDto> helloWorld(){
        HelloWorldResponseDto responseDto = new HelloWorldResponseDto();
        responseDto.setResponse("Hello World");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}

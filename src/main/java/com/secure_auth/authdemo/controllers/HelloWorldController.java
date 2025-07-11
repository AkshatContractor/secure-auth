package com.secure_auth.authdemo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api")
public class HelloWorldController {

    @GetMapping("/hello")
    @ResponseBody
    public String helloWorld(){
        return "Hello World";
    }
}

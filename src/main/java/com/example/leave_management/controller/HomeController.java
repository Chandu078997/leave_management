package com.example.leave_management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Leave Management API is running 🚀. Try /employees or /leaves endpoints.";
    }
}

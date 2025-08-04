package com.personalApp.personal_service.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/swagger-ui.html";
    }

    @GetMapping("/swagger")
    public String swagger() {
        return "redirect:/swagger-ui.html";
    }
}

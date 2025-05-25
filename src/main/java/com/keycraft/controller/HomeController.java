package com.keycraft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "forward:/client/index.html";
    }
    
    @GetMapping("/admin")
    public String admin() {
        return "forward:/client/index.html";
    }
    
    @GetMapping("/{path:[^\\.]*}")
    public String redirect() {
        return "forward:/client/index.html";
    }
}
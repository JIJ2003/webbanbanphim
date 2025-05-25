package com.keycraft.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.nio.file.Files;

@Controller
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<String> index() {
        return serveIndexHtml();
    }
    
    @GetMapping("/admin")
    public ResponseEntity<String> admin() {
        return serveIndexHtml();
    }
    
    @GetMapping("/{path:[^\\.]*}")
    public ResponseEntity<String> redirect() {
        return serveIndexHtml();
    }
    
    private ResponseEntity<String> serveIndexHtml() {
        try {
            Resource resource = new ClassPathResource("static/index.html");
            if (resource.exists()) {
                String content = Files.readString(resource.getFile().toPath());
                return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(content);
            }
        } catch (IOException e) {
            // Fall back to serving the frontend directly
        }
        
        // Serve the client index.html from the client directory
        try {
            Resource resource = new ClassPathResource("../../../client/index.html");
            String content = Files.readString(resource.getFile().toPath());
            return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(content);
        } catch (Exception e) {
            return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body("<!DOCTYPE html><html><head><title>KeyCraft</title></head><body><div id='root'></div><script type='module' src='/client/src/main.tsx'></script></body></html>");
        }
    }
}
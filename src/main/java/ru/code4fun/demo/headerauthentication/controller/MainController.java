package ru.code4fun.demo.headerauthentication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("")
    public ResponseEntity<String> hello() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Hello, " + (authentication == null ? "Anonymous" : authentication.getName()));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> profile() {
        return ResponseEntity.ok("Hello From Restricted Area");
    }
}

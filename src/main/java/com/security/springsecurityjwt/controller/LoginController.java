package com.security.springsecurityjwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @GetMapping(value = "/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("登录成功 ... ");
    }

    @GetMapping(value = "/profile")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Authentication> profile(Authentication authentication) {
        return ResponseEntity.ok(authentication);
    }
}

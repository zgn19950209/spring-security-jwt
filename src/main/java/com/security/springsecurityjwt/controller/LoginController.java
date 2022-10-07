package com.security.springsecurityjwt.controller;

import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Created 2022/10/7 13:52
 **/
@RestController
public class LoginController {

    @GetMapping(value = "/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("登录成功 ... ");
    }

    @GetMapping(value = "/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User> profile(Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(user);
    }
}

package com.security.springsecurityjwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<String> profile() {
        return ResponseEntity.ok("profile ... ");
    }
}

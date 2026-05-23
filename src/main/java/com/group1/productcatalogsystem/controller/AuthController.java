package com.group1.productcatalogsystem.controller;

import com.group1.productcatalogsystem.dto.request.ProductRequest;
import com.group1.productcatalogsystem.dto.response.ProductResponse;
import com.group1.productcatalogsystem.util.JwtTokenUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    @PostMapping("/login")
    public ResponseEntity<String> createProduct(@RequestBody Map<String,String> request) {
        String username = request.get("username");
        String password = request.get("password");
        // giả sử username và password ok
        System.out.println("username:"+username);
        System.out.println("password:"+password);
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username, password)
        );
        String token = jwtTokenUtil.generateToken(auth);
        return ResponseEntity.ok(token);
    }
}

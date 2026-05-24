package com.group1.productcatalogsystem.controller;

import com.group1.productcatalogsystem.dto.request.ProductRequest;
import com.group1.productcatalogsystem.dto.request.RegisterRequest;
import com.group1.productcatalogsystem.dto.response.AccountResponse;
import com.group1.productcatalogsystem.dto.response.ProductResponse;
import com.group1.productcatalogsystem.entity.Account;
import com.group1.productcatalogsystem.service.AccountService;
import com.group1.productcatalogsystem.util.JwtTokenUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<AccountResponse> register(@Valid @RequestBody RegisterRequest request) {
        AccountResponse response = accountService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping("/login")
    public ResponseEntity<String> createProduct(@RequestBody Map<String,String> request) {
        String username = request.get("username");
        String password = request.get("password");
        System.out.println("username:"+username);
        System.out.println("password:"+password);
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username, password)
        );
        String token = jwtTokenUtil.generateToken(auth);
        return ResponseEntity.ok(token);
    }
    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser(@AuthenticationPrincipal Account user) {
        System.out.println("id::" + user.getId());
        System.out.println("username::" + user.getUsername());
        return ResponseEntity.ok("I can see me");
    }
}

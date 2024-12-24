package com.example.ignite_core.Auth;

import com.example.ignite_core.Auth.Model.JwtResponse;
import com.example.ignite_core.Auth.Model.LoginRequest;
import com.example.ignite_core.User.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserEntity user) {
        return ResponseEntity.ok(authService.register(user));
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest);
        return ResponseEntity.ok(new JwtResponse(token));
    }


}

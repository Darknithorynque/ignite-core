package com.example.ignite_core.Auth;

import com.example.ignite_core.Auth.Model.JwtResponse;
import com.example.ignite_core.Auth.Model.LoginRequest;
import com.example.ignite_core.User.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    AuthService authService;

    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value ="/register")
    public ResponseEntity<?> register(@RequestBody UserEntity user) {
        return ResponseEntity.ok(authService.register(user));
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest);
        return ResponseEntity.ok(new JwtResponse(token));
    }


}

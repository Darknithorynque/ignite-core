package com.example.ignite_core.Auth;

import com.example.ignite_core.Auth.Model.LoginRequest;
import com.example.ignite_core.Config.SecurityConfig;
import com.example.ignite_core.User.UserEntity;
import com.example.ignite_core.User.UserRepository;
import com.example.ignite_core.Utlility.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final SecurityConfig securityConfig;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, SecurityConfig securityConfig, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.securityConfig = securityConfig;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public String register(UserEntity user) {
        UserEntity registeredUser = new UserEntity();
        registeredUser.setName(user.getName());
        registeredUser.setPassword(securityConfig.passwordEncoder().encode(user.getPassword()));
        registeredUser.setSex(user.getSex());
        registeredUser.setEmail(user.getEmail());

         userRepository.save(registeredUser);

         return "Registered successfully..";
    }

    public String login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        return jwtUtil.generateToken(loginRequest.getUsername());
    }
}

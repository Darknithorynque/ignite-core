package com.example.ignite_core.Auth;

import com.example.ignite_core.Auth.Model.LoginRequest;
import com.example.ignite_core.Config.SecurityConfig;
import com.example.ignite_core.User.UserEntity;
import com.example.ignite_core.User.UserRepository;
import com.example.ignite_core.Utlility.JwtUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, @Lazy AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(UserEntity user) {
        UserEntity registeredUser = new UserEntity(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getAge(), user.getSex());
        registeredUser.setPassword(passwordEncoder.encode(user.getPassword()));
         userRepository.save(registeredUser);

         return "Registered successfully..";
    }

    public String login(LoginRequest loginRequest) {
        try {
            System.out.println("password: " + loginRequest.getPassword());
            UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + loginRequest.getEmail()));

            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                throw new RuntimeException("Bad credentials");
            }

            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        return jwtUtil.generateToken(loginRequest.getEmail());
    } catch (Exception e) {
        throw new RuntimeException("Authentication failed: " + e.getMessage());
    }
    }

}

package com.example.ignite_core.Auth;

import com.example.ignite_core.Auth.Model.LoginRequest;
import com.example.ignite_core.Config.SecurityConfig;
import com.example.ignite_core.User.UserEntity;
import com.example.ignite_core.User.UserRepository;
import com.example.ignite_core.Utlility.InvalidUserException;
import com.example.ignite_core.Utlility.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    public static final Logger log = LoggerFactory.getLogger(AuthService.class);

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
        log.info("register user: {}", user.getEmail());
        UserEntity registeredUser = new UserEntity(user.getId(), user.getName(), user.getHeight(), user.getHeight(), user.getEmail(), user.getPassword(), user.getSex(), user.getAge(), user.getAllergies());
        registeredUser.setPassword(passwordEncoder.encode(user.getPassword()));
         userRepository.save(registeredUser);

         return "Registered successfully..";
    }

    public String changePassword(LoginRequest loginRequest) {
        Optional<UserEntity> user = userRepository.findByEmail(loginRequest.getEmail());
        if (user.isEmpty()) {
            throw new InvalidUserException("User is empty");
        }
        user.get().setPassword(passwordEncoder.encode(loginRequest.getPassword()));

        return "Password changed successfully..\n"+"new password: "+user.get().getPassword();
    }

    public String changeEmail(LoginRequest loginRequest) {
        Optional<UserEntity> user = userRepository.findByEmail(loginRequest.getEmail());
        if (user.isEmpty()) {
            throw new InvalidUserException("User is empty");
        }

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            user.get().setEmail(loginRequest.getEmail());
        } else { throw new InvalidUserException("Passwords do not match"); }

        return "Email changed successfully..\n"+"new email: "+user.get().getEmail();
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

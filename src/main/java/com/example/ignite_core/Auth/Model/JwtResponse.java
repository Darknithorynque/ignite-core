package com.example.ignite_core.Auth.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
    private String token;

    public JwtResponse (String token) {
        this.token = token;
    }

}

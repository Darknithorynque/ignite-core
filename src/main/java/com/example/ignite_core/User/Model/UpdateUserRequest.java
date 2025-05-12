package com.example.ignite_core.User.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    private Long id;

    private String name;

    private int age;

    private double height;

    private double weight;

    private boolean sex;

}

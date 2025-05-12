package com.example.ignite_core.User.Model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double height;

    private double weight;

    private String email;

    private String password;

    private Boolean sex;
    
    private int age;


}

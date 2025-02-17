package com.example.ignite_core.User;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.usertype.UserType;
import org.springframework.context.annotation.Primary;
import org.springframework.data.util.QTypeContributor;

import java.util.ArrayList;
import java.util.List;


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

    @ElementCollection
    @CollectionTable(name = "user_allergies", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "allergy")
    private List<String> allergies;

}

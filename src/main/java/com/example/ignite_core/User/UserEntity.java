package com.example.ignite_core.User;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.usertype.UserType;
import org.springframework.context.annotation.Primary;
import org.springframework.data.util.QTypeContributor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Setter
@Getter
@Table(name = "user")
public class UserEntity {
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getSex() {
        return sex;
    }

    public int getAge() {
        return age;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    private String name;

    public UserEntity(Long id, String name, double height, double weight, String email, String password, Boolean sex, int age, List<String> allergies) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.email = email;
        this.password = password;
        this.sex = sex;
        this.age = age;
        this.allergies = allergies;
    }

    private double height;
    private double weight;

    public UserEntity(){

    }

    private String email;
    private String password;
    private Boolean sex;
    private int age;

    @ElementCollection
    @CollectionTable(name = "user_allergies", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "allergy")
    private List<String> allergies;

}

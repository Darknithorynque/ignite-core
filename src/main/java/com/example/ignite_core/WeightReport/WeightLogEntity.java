package com.example.ignite_core.WeightReport;

import com.example.ignite_core.User.UserEntity;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WeightLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Basic.class)
    private Long id;

    @JsonView(Views.Basic.class)
    private double previousWeight;

    @JsonView(Views.Basic.class)
    private double newWeight;

    @CreationTimestamp
    @JsonView(Views.Basic.class)
    private LocalDate loggedAt;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;

}

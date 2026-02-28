package com.green_home_project.model;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private boolean canSell = false;
    private boolean canCare = false;

    private boolean active = true;

    public User() {}

    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.canCare = false;
        this.canSell = false;
        this.active = true;
    }
    @Enumerated(EnumType.STRING)
    private Role role;
}
package com.green_home_project.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private Role role;
    private java.time.LocalDateTime lastSeen;
    // ================= FAVORITES =================
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "plant_id")
    )
    private List<Plant> favorites = new ArrayList<>();

    public User() {}

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.canCare = false;
        this.canSell = false;
        this.active = true;
        this.role = role;
        this.favorites = new ArrayList<>();
    }
    public enum Role {
        USER,
        SELLER,
        ADMIN
    }
    public java.time.LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(java.time.LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }
}
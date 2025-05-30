package com.mhetre.kitchen.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;
    private String name;
    private String plan;
    private boolean paused;

    @Enumerated(EnumType.STRING)
    private UserState state;
}

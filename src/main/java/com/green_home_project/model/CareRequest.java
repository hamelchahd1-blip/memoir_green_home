package com.green_home_project.model;
import jakarta.persistence.*;


    @Entity
    public class CareRequest {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        private User requester;

        @ManyToOne
        private User caregiver;

        private String description;

        private boolean accepted = false;
    }


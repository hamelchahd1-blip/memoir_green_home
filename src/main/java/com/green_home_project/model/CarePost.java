package com.green_home_project.model;

import jakarta.persistence.*;

    @Entity
    public class CarePost {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String title;
        private String description;

        private String imageUrl;
        @ManyToOne
        private User caregiver;

        public User getCaregiver() {
            return caregiver;
        }

        public void setCaregiver(User caregiver) {
            this.caregiver = caregiver;
        }
    }


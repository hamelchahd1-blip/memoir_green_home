package com.green_home_project.repository;

import com.green_home_project.model.Plant;
import com.green_home_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlantRepository extends JpaRepository<Plant, Long> {

    List<Plant> findByNameContainingIgnoreCase(String name);
    
    List<Plant> findByUser(User user); // ✅ NEW
}
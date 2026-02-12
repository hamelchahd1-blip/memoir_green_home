package com.green_home_project.repository;

import com.green_home_project.model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlantRepository extends JpaRepository<Plant, Long> {

    List<Plant> findByOwnerId(Long ownerId); // النباتات حسب صاحبها
    List<Plant> findByNameContainingIgnoreCase(String name); // بحث بالنبات
}
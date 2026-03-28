package com.green_home_project.controller;

import com.green_home_project.dto.PlantDTO;
import com.green_home_project.model.Plant;
import com.green_home_project.service.PlantService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    // ================= ALL PLANTS =================
    @GetMapping
    public List<PlantDTO> getAllPlants() {
        return plantService.getAllPlants();
    }

    // ================= ADD PLANT (SELLER ONLY) =================

    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public PlantDTO addPlant(@Valid @RequestBody Plant plant) {
        return plantService.addPlant(plant);
    }
    // ================= MY PLANTS =================
    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("/my-plants")
    public List<PlantDTO> getMyPlants() {
        return plantService.getMyPlants();
    }
}
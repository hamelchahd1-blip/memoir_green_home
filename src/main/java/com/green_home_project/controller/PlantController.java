package com.green_home_project.controller;

import com.green_home_project.model.Plant;
import com.green_home_project.service.PlantService;
import org.springframework.web.bind.annotation.*;
import com.green_home_project.dto.PlantDTO;
import java.util.List;
@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    @GetMapping
    public List<PlantDTO> getAllPlants() {
        return plantService.getAllPlants();
    }

    @GetMapping("/{id}")
    public PlantDTO getPlant(@PathVariable Long id) {
        return plantService.getPlantById(id);
    }

    @PostMapping
    public PlantDTO addPlant(@RequestBody Plant plant) {
        return plantService.addPlant(plant);
    }

    @PutMapping("/{id}")
    public PlantDTO updatePlant(@PathVariable Long id, @RequestBody Plant plant) {
        return plantService.updatePlant(id, plant);
    }

    @DeleteMapping("/{id}")
    public String deletePlant(@PathVariable Long id) {
        plantService.deletePlant(id);
        return "Plant deleted with id: " + id;
    }
}
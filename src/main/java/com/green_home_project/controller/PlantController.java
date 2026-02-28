package com.green_home_project.controller;

import com.green_home_project.dto.PlantDTO;
import com.green_home_project.model.Plant;
import com.green_home_project.service.PlantService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*")
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
    public PlantDTO getPlantById(@PathVariable Long id) {
        return plantService.getPlantById(id);
    }

    @PostMapping
    public PlantDTO addPlant(@RequestBody Plant plant) {
        return plantService.addPlant(plant);
    }

    @PutMapping("/{id}")
    public PlantDTO updatePlant(@PathVariable Long id,
                                @RequestBody Plant plant) {
        return plantService.updatePlant(id, plant);
    }

    @DeleteMapping("/{id}")
    public void deletePlant(@PathVariable Long id) {
        plantService.deletePlant(id);
    }

    @GetMapping("/search")
    public List<PlantDTO> searchPlants(@RequestParam String name) {
        return plantService.searchByName(name);
    }

    @PostMapping("/{id}/upload-image")
    public PlantDTO uploadImage(@PathVariable Long id,
                                @RequestParam("file") MultipartFile file) {
        return plantService.savePlantImage(id, file);
    }
}
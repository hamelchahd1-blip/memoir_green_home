package com.green_home_project.service;

import com.green_home_project.dto.PlantDTO;
import com.green_home_project.model.Plant;
import com.green_home_project.exception.ResourceNotFoundException;
import com.green_home_project.repository.PlantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlantService {

    private final PlantRepository plantRepository;

    public PlantService(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    // Convert Plant -> DTO
    private PlantDTO convertToDTO(Plant plant) {
        return new PlantDTO(plant.getId(), plant.getName(), plant.getDescription());
    }

    public List<PlantDTO> getAllPlants() {
        return plantRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PlantDTO getPlantById(Long id) {
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plant not found with id: " + id));
        return convertToDTO(plant);
    }

    public PlantDTO addPlant(Plant plant) {
        Plant saved = plantRepository.save(plant);
        return convertToDTO(saved);
    }

    public PlantDTO updatePlant(Long id, Plant plantDetails) {
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plant not found with id: " + id));

        plant.setName(plantDetails.getName());
        plant.setDescription(plantDetails.getDescription());
        Plant updated = plantRepository.save(plant);
        return convertToDTO(updated);
    }

    public void deletePlant(Long id) {
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plant not found with id: " + id));
        plantRepository.delete(plant);
    }
}
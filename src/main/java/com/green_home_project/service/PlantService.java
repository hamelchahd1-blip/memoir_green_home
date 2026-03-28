package com.green_home_project.service;

import com.green_home_project.dto.PlantDTO;
import com.green_home_project.model.Plant;
import com.green_home_project.model.User;
import com.green_home_project.repository.PlantRepository;
import com.green_home_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlantService {

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private UserRepository userRepository;

    // ================= ADD PLANT =================
    public PlantDTO addPlant(Plant plant) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        plant.setUser(user);

        return convertToDTO(plantRepository.save(plant));
    }

    // ================= GET ALL =================
    public List<PlantDTO> getAllPlants() {
        return plantRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ================= GET MY PLANTS =================
    public List<PlantDTO> getMyPlants() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return plantRepository.findByUser(user)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ================= CONVERT =================
    private PlantDTO convertToDTO(Plant plant) {
        return new PlantDTO(
                plant.getId(),
                plant.getName(),
                plant.getDescription(),
                plant.getImageUrl()
        );
    }
}
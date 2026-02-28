package com.green_home_project.service;

import com.green_home_project.dto.PlantDTO;
import com.green_home_project.model.Plant;
import com.green_home_project.model.User;
import com.green_home_project.repository.PlantRepository;
import com.green_home_project.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.green_home_project.model.Role;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlantService {

    private final PlantRepository plantRepository;
    private final UserRepository userRepository;

    public PlantService(PlantRepository plantRepository,
                        UserRepository userRepository) {
        this.plantRepository = plantRepository;
        this.userRepository = userRepository;
    }

    // ================= DTO CONVERTER =================
    private PlantDTO convertToDTO(Plant plant) {
        return new PlantDTO(
                plant.getId(),
                plant.getName(),
                plant.getDescription(),
                plant.getImageUrl()
        );
    }

    // ================= GET ALL =================
    public List<PlantDTO> getAllPlants() {
        return plantRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ================= GET BY ID =================
    public PlantDTO getPlantById(Long id) {
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plant not found"));

        return convertToDTO(plant);
    }

    // ================= ADD PLANT (OWNER FROM TOKEN) =================
    public PlantDTO addPlant(Plant plant) {

        String email =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ CHECK ROLE
        if (user.getRole() != Role.SELLER) {
            throw new RuntimeException("Only sellers can add plants");
        }

        plant.setUser(user);

        return convertToDTO(plantRepository.save(plant));
    }

    // ================= UPDATE =================
    public PlantDTO updatePlant(Long id, Plant plantDetails) {

        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plant not found"));

        plant.setName(plantDetails.getName());
        plant.setDescription(plantDetails.getDescription());

        return convertToDTO(plantRepository.save(plant));
    }

    // ================= DELETE =================
    public void deletePlant(Long id) {
        plantRepository.deleteById(id);
    }

    // ================= SEARCH =================
    public List<PlantDTO> searchByName(String name) {
        return plantRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ================= GET MY PLANTS =================
    public List<PlantDTO> getMyPlants() {

        String email =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return plantRepository.findByUser(user)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ================= UPLOAD IMAGE =================
    public PlantDTO savePlantImage(Long id, MultipartFile file) {

        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plant not found"));

        try {

            Path uploadDir = Paths.get("uploads");

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String originalFilename = file.getOriginalFilename();
            String extension =
                    originalFilename.substring(originalFilename.lastIndexOf("."));

            String fileName =
                    "plant-" + id + "-" + System.currentTimeMillis() + extension;

            Path filePath = uploadDir.resolve(fileName);

            Files.write(filePath, file.getBytes());

            plant.setImageUrl("/uploads/" + fileName);

            return convertToDTO(plantRepository.save(plant));

        } catch (Exception e) {
            throw new RuntimeException("Failed to store file");
        }
    }
}
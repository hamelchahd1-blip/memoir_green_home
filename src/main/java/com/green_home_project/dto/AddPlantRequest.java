package com.green_home_project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddPlantRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private String imageUrl;
}

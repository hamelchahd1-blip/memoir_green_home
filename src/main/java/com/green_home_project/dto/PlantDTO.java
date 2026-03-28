package com.green_home_project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantDTO {

    private Long id;

    @NotBlank(message = "Plant name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    private String imageUrl;
}
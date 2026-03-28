package com.green_home_project.dto;

import lombok.Data;
import java.util.List;

@Data
public class SellerDashboardDTO {

    private int totalPlants;
    private int totalFavorites;
    private List<PlantDTO> myPlants;
}
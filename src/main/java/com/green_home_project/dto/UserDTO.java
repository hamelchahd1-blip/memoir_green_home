package com.green_home_project.dto;

import com.green_home_project.model.Role;
import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String role;
    private boolean canSell;
    private boolean canCare;
}

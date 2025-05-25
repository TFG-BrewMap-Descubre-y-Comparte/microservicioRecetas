package com.project.recipes.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserDTO {
    private Integer id_user;
    private String name;
    private String email;
    private String username;
    private String role;
}


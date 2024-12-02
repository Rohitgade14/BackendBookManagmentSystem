package com.krios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
     @NotBlank(message = "Name is mandatory")
    private String name;
     @Email(message = "Email should be valid")
     @NotBlank(message = "Email is mandatory")
    private String email;
     @NotBlank(message = "Location is mandatory")
    private String location;
}

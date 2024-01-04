package com.taskFlow.demo.Model.DTOs.Response;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserResponseDTO {
    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    @NotNull(message = "Username cannot be null")
    @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters")
    private String username;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;
}

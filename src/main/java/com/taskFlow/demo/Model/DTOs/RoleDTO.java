package com.taskFlow.demo.Model.DTOs;

import com.taskFlow.demo.Model.Entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotNull(message = "Role name cannot be null")
    @Size(min = 1, max = 50, message = "Role name must be between 1 and 50 characters")
    private String role_name;

}

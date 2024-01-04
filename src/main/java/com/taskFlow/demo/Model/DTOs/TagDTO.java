package com.taskFlow.demo.Model.DTOs;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO {
    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotNull(message = "Tag name cannot be null")
    @Size(min = 1, max = 50, message = "Tag name must be between 1 and 50 characters")
    private String le_nom;

    @NotNull(message = "Description cannot be null")
    @Size(max = 255, message = "Description can be at most 255 characters")
    private String description;

    private String image;
}

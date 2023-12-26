package com.taskFlow.demo.Model.DTOs;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO {
    private Long id;
    private  String le_nom;
    private  String description;
    private String image;
}

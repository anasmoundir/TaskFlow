package com.taskFlow.demo.Model.DTOs.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RsponseUserDto {
    private Long id;
    private String username;
    private String email;
}

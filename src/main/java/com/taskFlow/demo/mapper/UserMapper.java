package com.taskFlow.demo.mapper;

import com.taskFlow.demo.Model.DTOs.Response.UserResponseDTO;
import com.taskFlow.demo.Model.DTOs.UserDTO;
import com.taskFlow.demo.Model.Entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(User user);
    User toEntity(UserDTO userDto);
    UserResponseDTO toResponseDto(UserDTO userDto);

}
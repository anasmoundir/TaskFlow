package com.taskFlow.demo.Service;

import com.taskFlow.demo.Model.DTOs.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserDTO registerUser(UserDTO userDTO);

    UserDTO createUser(UserDTO userDto);

    UserDTO updateUser(Long userId, UserDTO userDto);

    void deleteUser(Long userId);

    UserDTO getUser(Long userId);

    List<UserDTO> getAllUsers();
}

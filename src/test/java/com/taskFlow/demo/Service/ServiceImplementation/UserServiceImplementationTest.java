package com.taskFlow.demo.Service.ServiceImplementation;

import com.taskFlow.demo.Exceptions.UserValidationException;
import com.taskFlow.demo.Model.DTOs.UserDTO;
import com.taskFlow.demo.Model.Entities.Role;
import com.taskFlow.demo.Model.Entities.User;
import com.taskFlow.demo.Repository.UserRepo;
import com.taskFlow.demo.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest

class UserServiceImplementationTest {

//    @Mock
//    private UserRepo userRepo;
//
//    @Mock
//    private UserMapper userMapper;
//
//    @InjectMocks
//    private UserServiceImplementation userService;
//    @Test
//    public void test_create_user_invalid_input() {
//        UserDTO userDto = new UserDTO();
//        userDto.setId(1L);
//        userDto.setUsername("");
//        userDto.setEmail("test@example.com");
//        userDto.setPassword("password");
//
//        assertThrows(UserValidationException.class, () -> userService.createUser(userDto));
//    }
//
//    @Test
//    public void test_create_user_existing_email() {
//        UserDTO userDto = new UserDTO();
//        userDto.setId(1L);
//        userDto.setUsername("testUser");
//        userDto.setEmail("existing@example.com");
//        userDto.setPassword("password");
//
//        when(userRepo.existsByUsername(userDto.getUsername())).thenReturn(false);
//        when(userRepo.existsByEmail(userDto.getEmail())).thenReturn(true);
//
//        assertThrows(UserValidationException.class, () -> userService.createUser(userDto));
//    }
//    @Test
//    public void test_create_user_maximum_input() {
//        UserDTO userDto = new UserDTO();
//        userDto.setId(1L);
//        userDto.setUsername("testUser");
//        userDto.setEmail("test@example.com");
//        userDto.setPassword("password");
//
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("testUser");
//        user.setEmail("test@example.com");
//        user.setPassword("password");
//
//        Set<Role> roles = new HashSet<>();
//        Role role = new Role();
//        role.setId(1L);
//        role.setRoleName("USER");
//        roles.add(role);
//
//        when(userMapper.toEntity(userDto)).thenReturn(user);
//        when(userRepo.save(user)).thenReturn(user);
//        when(userMapper.toDto(user)).thenReturn(userDto);
//
//        UserDTO result = userService.createUser(userDto);
//
//        assertEquals(userDto, result);
//    }

}
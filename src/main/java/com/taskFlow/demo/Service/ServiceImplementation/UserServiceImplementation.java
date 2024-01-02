package com.taskFlow.demo.Service.ServiceImplementation;

import com.taskFlow.demo.Exceptions.UserNotFoundException;
import com.taskFlow.demo.Exceptions.UserValidationException;
import com.taskFlow.demo.Model.DTOs.UserDTO;
import com.taskFlow.demo.Model.Entities.Role;
import com.taskFlow.demo.Model.Entities.User;
import com.taskFlow.demo.Repository.RoleRepo;
import com.taskFlow.demo.Repository.UserRepo;
import com.taskFlow.demo.Service.UserService;
import com.taskFlow.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserServiceImplementation implements UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImplementation(UserRepo userRepo, RoleRepo roleRepo, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.userMapper = userMapper;
        this.passwordEncoder=passwordEncoder;
    }
    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        boolean userExists = userRepo.existsByUsername(userDTO.getUsername());
        if (userExists) {
            throw new UserValidationException("Username already exists");
        }
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepo.findByRoleName("USER"));
        user.setRoles(roles);
        user = userRepo.save(user);
        return userMapper.toDto(user);
    }
    @Override
    public UserDTO createUser(UserDTO userDto) {
        User user = userMapper.toEntity(userDto);
        Set<Role> roles = getDefaultRoles();
        user.setRoles(roles);
        user = userRepo.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDto) {
        User existingUser = userRepo.findById(userDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        User updatedUser = userMapper.toEntity(userDto);
        updatedUser.setId(existingUser.getId());
        updatedUser.setRoles(existingUser.getRoles());
        updatedUser = userRepo.save(updatedUser);
        return userMapper.toDto(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepo.delete(user);
    }

    @Override

    public UserDTO getUser(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return userMapper.toDto(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepo.findAll();
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    private Set<Role> getDefaultRoles() {
        Optional<Role> defaultRole = Optional.ofNullable(roleRepo.findByRoleName("USER"));
        return Set.of(defaultRole.orElseGet(() -> createDefaultRole("USER")));
    }

    private Role createDefaultRole(String roleName) {
        Role role = new Role();
        role.setRoleName(roleName);
        return roleRepo.save(role);
    }
}
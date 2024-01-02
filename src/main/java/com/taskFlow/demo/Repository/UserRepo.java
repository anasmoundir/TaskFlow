package com.taskFlow.demo.Repository;

import com.taskFlow.demo.Model.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository

public interface UserRepo extends JpaRepository<User,Long> {
    boolean existsByUsername(String username);

    Object existsByEmail(String email);
}

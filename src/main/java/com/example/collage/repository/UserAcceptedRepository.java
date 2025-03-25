package com.example.collage.repository;

import com.example.collage.entity.User;
import com.example.collage.entity.Users_accepted;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAcceptedRepository extends JpaRepository<Users_accepted, Long> {
    List<User> findByUsernameContainingIgnoreCase(String username);

}

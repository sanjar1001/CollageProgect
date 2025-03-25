package com.example.collage.repository;

import com.example.collage.entity.User;
import com.example.collage.entity.Users_accepted;
import com.example.collage.entity.Users_refusal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRefusalRepository extends JpaRepository<Users_refusal, Long> {
    List<User> findByUsernameContainingIgnoreCase(String username);
}

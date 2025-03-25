package com.example.collage.service;

import com.example.collage.entity.User;
import com.example.collage.entity.Users_accepted;
import com.example.collage.entity.Users_refusal;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface UserService  {
    List<User> findAll();//Получить всех пользователей

    List<Users_accepted> findAllAccepted();//Получить всех пользователей

    List<User> findUserByUsername(String username);//Искать пользователя по Имени

    ResponseEntity<InputStreamResource> downloadFile(String username); //Скачать файлы пользователя

    void deleteUser(Long id) throws IOException;

    User userDetails(long id);
}

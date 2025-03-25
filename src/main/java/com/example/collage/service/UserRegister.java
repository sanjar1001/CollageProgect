package com.example.collage.service;

import com.example.collage.entity.FileUploadResult;
import com.example.collage.entity.User;
import com.example.collage.exception.FileStorageException;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface UserRegister {

    User addUser(String username, String phoneNumber, String password, int counter, LocalDate date);
    FileUploadResult uploadFile(List<MultipartFile> file, String username, String phoneNumber) throws FileStorageException;
}

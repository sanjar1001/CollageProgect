package com.example.collage.service.ServiceImpl;

import com.example.collage.entity.FileUploadResult;
import com.example.collage.entity.User;
import com.example.collage.exception.FileStorageException;
import com.example.collage.repository.UserRepository;
import com.example.collage.service.UserRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class UserRegisterServiceImpl implements UserRegister {

    private final UserRepository userRepository;

    public User addUser(String username, String phoneNumber, String fileUrl, int counter, LocalDate date) {
        User user = new User();
        user.setUsername(username);
        user.setPhoneNumber(phoneNumber);
        user.setFileUrl(fileUrl);
        user.setFileCount(counter);
        user.setCreatedAt(date);
        user.setStatus(null);
        return  userRepository.save(user);
    } //Добавление заявки в базу данных

    public FileUploadResult uploadFile(List<MultipartFile> files, String username, String phoneNumber) throws FileStorageException {

        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("Файлы не загружены.");
        }

        String uploadDir = "upload/";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String zipFilePath = uploadDir + username + ".zip";
        int counter = 0;
        LocalDate date = LocalDate.now();

        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (MultipartFile file : files) {
                ZipEntry entry = new ZipEntry(file.getOriginalFilename());
                zos.putNextEntry(entry);
                zos.write(file.getBytes());
                zos.closeEntry();
                counter++;
            }

        } catch (IOException e) {
            throw new FileStorageException("Ошибка при создании ZIP-файла.");
        }

        return new FileUploadResult(zipFilePath, counter, date);
    }

}

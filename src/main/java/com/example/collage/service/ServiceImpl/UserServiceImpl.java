package com.example.collage.service.ServiceImpl;

import com.example.collage.entity.User;
import com.example.collage.entity.Users_accepted;
import com.example.collage.entity.Users_refusal;
import com.example.collage.exception.NotFoundException;
import com.example.collage.repository.UserAcceptedRepository;
import com.example.collage.repository.UserRefusalRepository;
import com.example.collage.repository.UserRepository;
import com.example.collage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final UserAcceptedRepository userAcceptedRepository;
    private final UserRefusalRepository userRefusalRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    } //Получить всех пользователей

    public List<Users_accepted> findAllAccepted() {
        List<Users_accepted> users_accepted = userAcceptedRepository.findAll();
        if (users_accepted.isEmpty()) {
            throw new NotFoundException("Пользователи не найдены!");
        }
        return users_accepted;
    } //Получить всех принятых пользователей

    public List<Users_refusal> findAllRefusal() {
        return userRefusalRepository.findAll();
    } //Получить всех отказанных пользователей

    public List<User> findUserByUsername(String username) {
        if (username == null && username.trim().isEmpty()){
            return userRepository.findAll();
        }
        return userRepository.findByUsernameContainingIgnoreCase(username);

    }//Искать пользователя по Имени

    public User userDetails(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
    }

    public ResponseEntity<InputStreamResource> downloadFile(String username) {
        // Путь к архиву
        String zipFilePath = "upload/" + username + ".zip";
        System.out.println("File path: " + zipFilePath);

        // Создаём файл
        File file = new File(zipFilePath);

        // Проверяем, существует ли файл
        if (!file.exists()) {
            System.out.println("File not found: " + zipFilePath);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Создаём InputStream для файла
        try (InputStream inputStream = new FileInputStream(file)) {
            // Кодируем имя файла в UTF-8 для Content-Disposition
            String encodedFilename = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8)
                    .replace("+", "%20"); // Пробелы заменяем на "%20"

            // Возвращаем файл как InputStreamResource
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename)
                    .body(new InputStreamResource(inputStream));

        } catch (IOException e) {
            System.out.println("Error occurred while processing the file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public void deleteUser(Long id) throws IOException {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("пользователь не найден"));


        Path file = Path.of("upload" , user.getUsername() + ".zip");

        System.out.println(file );


            if (!Files.deleteIfExists(file)) {
                throw new IOException("Не удалось удалить файл  ");
            }


            userRepository.deleteById(id);


    } // Удалить пользователя по ID


}


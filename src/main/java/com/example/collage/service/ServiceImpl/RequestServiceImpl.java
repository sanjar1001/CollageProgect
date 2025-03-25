package com.example.collage.service.ServiceImpl;

import com.example.collage.entity.User;
import com.example.collage.entity.Users_accepted;
import com.example.collage.entity.Users_refusal;
import com.example.collage.exception.NotFoundException;
import com.example.collage.repository.UserAcceptedRepository;
import com.example.collage.repository.UserRefusalRepository;
import com.example.collage.repository.UserRepository;
import com.example.collage.service.UserRequestService;
import com.example.collage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements UserRequestService {

    private final UserRepository userRepository;
    private  final UserAcceptedRepository userAcceptedRepository;
    private final UserRefusalRepository userRefusalRepository;
    private final UserService userService;


    public ResponseEntity<?> getUserAcceptedById(long id) {
        Users_accepted user = userAcceptedRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        return ResponseEntity.ok(user);
    } //Найти по ID

    public Optional<Users_refusal> getUserRefusalById(long id) {
        Users_refusal user = userRefusalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        return Optional.of(user);
    }//Найти по ID


    public void deleteUserAcceptedById(long id) throws IOException {
        Users_accepted user =  userAcceptedRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("пользователь не найден"));

        Path file = Path.of("upload" , user.getUsername() + ".zip");

        if (!Files.deleteIfExists(file)) {
            throw new IOException("Не удалось удалить файл  ");
        }

        userAcceptedRepository.deleteById(id);
    }

    public void deleteUserRefusalById(long id) throws IOException {
        Users_refusal user = userRefusalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("пользователь не найден"));

        Path file = Path.of("upload" , user.getUsername() + ".zip");

        if (!Files.deleteIfExists(file)) {
            throw new IOException("Не удалось удалить файл  ");
        }

        if (file == null){
            throw new IOException("Не удалось найти файл");
        }

        userRefusalRepository.deleteById(id);
    }

    public Users_accepted addUserAccepted(String username, String phoneNumber,
                                          String fileUrl, int counter, LocalDate date,
                                          boolean status) {
        Users_accepted acceptedUser = new Users_accepted();
        acceptedUser.setUsername(username);
        acceptedUser.setPhoneNumber(phoneNumber);
        acceptedUser.setFileUrl(fileUrl);
        acceptedUser.setFileCount(counter);
        acceptedUser.setCreatedAt(date);
        acceptedUser.setStatus(status);
         return userAcceptedRepository.save(acceptedUser);

    }//Добавление принятых пользователей в базу

    public boolean acceptedUsers(long id) throws IOException {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден!"));

       if (Boolean.TRUE.equals(user.getStatus())){
           throw new BadRequestException("Этот пользователь ранне был принят");
       }

        if (Boolean.FALSE.equals(user.getStatus())){
            throw new BadRequestException("Этот пользователь ранне был отказан");
        }

       if (user.getStatus() == null){
           user.setStatus(Boolean.TRUE);
       }

       Users_accepted userSaved = addUserAccepted(
               user.getUsername(),
               user.getPhoneNumber(),
               user.getFileUrl(),
               user.getFileCount(),
               user.getCreatedAt(),
               user.getStatus()
       );

       userRepository.delete(user);


       return true;

    }// принятие и отправка сообщение для принятия

    public Users_refusal addUserRefusal(String username, String phoneNumber,
                                            String fileUrl, int counter, LocalDate date,
                                            boolean status) {
            Users_refusal refusalUser = new Users_refusal();
            refusalUser.setUsername(username);
            refusalUser.setPhoneNumber(phoneNumber);
            refusalUser.setFileUrl(fileUrl);
            refusalUser.setFileCount(counter);
            refusalUser.setCreatedAt(date);
            refusalUser.setStatus(status);
            return userRefusalRepository.save(refusalUser);

        }//Добавление отказанных пользователей в базу

    public boolean refusalUsers(long id) throws BadRequestException {


            User user = userRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Пользователь не найден!"));

            if (Boolean.TRUE.equals(user.getStatus())){
                throw new BadRequestException("Этот пользователь ранне был принято");
            }

            if (Boolean.FALSE.equals(user.getStatus())){
                throw new BadRequestException("Этот пользователь ранне был отказано");
            }

            if (user.getStatus() == null){
                user.setStatus(Boolean.FALSE);
            }

            addUserRefusal(
                    user.getUsername(),
                    user.getPhoneNumber(),
                    user.getFileUrl(),
                    user.getFileCount(),
                    user.getCreatedAt(),
                    user.getStatus()

            );

        userRepository.delete(user);


        return true;

        }// принятие и отправка сообщение для ОТКАЗА

    public ResponseEntity<InputStreamResource> downloadFile(String username) throws IOException {

        //Создаем ссылку на архив
        String zipFilePath = "upload/" + username + ".zip";

        //Создаем файл для проверки(Это не файла ссылка)
        File file = new File(zipFilePath);

        System.out.println(file.getAbsolutePath());

        //Проверка существует ли?
        if (!file.exists()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //Читаем содержимое для скачивания файла
        InputStream inputStream = new FileInputStream(file);

        //Кодируем имя файла для правильной установки
        String encodedFilename = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8)
                .replace("+", "%20");


        // Возвращаем файл как InputStreamResource
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename) // filename* вместо filename
                .body(new InputStreamResource(inputStream));


    }

}

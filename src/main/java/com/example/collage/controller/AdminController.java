package com.example.collage.controller;

import com.example.collage.entity.FileUploadResult;
import com.example.collage.entity.Users_accepted;
import com.example.collage.entity.Users_refusal;
import com.example.collage.exception.FileStorageException;
import com.example.collage.exception.NotFoundException;
import com.example.collage.repository.UserAcceptedRepository;
import com.example.collage.repository.UserRefusalRepository;
import com.example.collage.service.UserRegister;
import com.example.collage.service.UserRequestService;
import com.example.collage.service.UserService;
import lombok.RequiredArgsConstructor;
import com.example.collage.entity.User;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")

public class AdminController {

    private final UserService userService;
    private final UserAcceptedRepository userAcceptedRepository;
    private final UserRefusalRepository userRefusalRepository;
    private final UserRequestService userRequestService;
    private final UserRegister userRegister;

    @GetMapping("/users") // Получит все заявки
    public List<User> users() {
        return userService.findAll();
    }

    @GetMapping("/user/details/{id}") // Подробнее о заявки
    public ResponseEntity<?> Details(@PathVariable long id) {
        try {
            User user = userService.userDetails(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch (NotFoundException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/user/accepted/list") //Список принятых заявок
    public ResponseEntity<List<Users_accepted>> userAccepted() {
        try {
            List<Users_accepted> users_accepted = userAcceptedRepository.findAll();
            return  ResponseEntity.ok(users_accepted);
        }catch (NotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/accepted/details/{id}") // Подробнее о принятых заявки
    public ResponseEntity<?> userAcceptedDetails(@PathVariable long id) {
        try {
            ResponseEntity<?> user = userRequestService.getUserAcceptedById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch (NotFoundException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("Пользователь успешно удален!");
        }catch (NotFoundException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Не смогли найти файл");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Не известная ошибка");
        }


    }

    @DeleteMapping("/user/accepted/delete/{id}")
    public ResponseEntity<?> deleteUserAccepted(@PathVariable long id) {
        try {
            userRequestService.deleteUserAcceptedById(id);
            return ResponseEntity.ok("Пользователь успешно удален!");
        }catch (NotFoundException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Не смогли найти файл");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Не известная ошибка");
        }

    }

    @DeleteMapping("/user/refusal/delete/{id}")
    public ResponseEntity<?> deleteUserRefusal(@PathVariable Long id) {
        try {
            userRequestService.deleteUserRefusalById(id);
            return ResponseEntity.ok("Пользователь успешно удален!");
        }catch (NotFoundException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Не смогли найти файл");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Не известная ошибка");
        }


    }

    @GetMapping("/user/refusal/list") //Список отказанных заявок
    public ResponseEntity<List<Users_refusal>> userRefusal() {
        try {
            List<Users_refusal> users_refusal = userRefusalRepository.findAll();
            return  ResponseEntity.ok(users_refusal);
        }catch (NotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/refusal/details/{id}") // Подробнее об отказанных заявки
    public ResponseEntity<?> userRefusalDetails(@PathVariable long id) {
        try {
            Optional<Users_refusal> user = userRequestService.getUserRefusalById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch (NotFoundException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/user/accepted/{id}")
    public ResponseEntity<?>  userAccepted(@PathVariable long id) {
        try {
            Boolean bool = userRequestService.acceptedUsers(id);
            return new ResponseEntity<>(bool, HttpStatus.OK);
        }catch (NotFoundException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/user/refusal/{id}")
    public ResponseEntity<?>  userRefusal(@PathVariable long id) {
        try {
            Boolean bool = userRequestService.refusalUsers(id);
            return new ResponseEntity<>(bool, HttpStatus.OK);
        }catch (NotFoundException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/user/download")
    public ResponseEntity<?> downloadFile(@RequestParam String username) throws IOException {
        try {
            return userRequestService.downloadFile(username);
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Файл отсутствует");
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Не известная ошибка");
        }
    }

    @PostMapping("/user/registration")
    public ResponseEntity<?> userRegistration(@RequestParam String username,
                                              @RequestParam String phoneNumber,
                                              @RequestParam("file") List<MultipartFile> files)
    throws IOException{

        try {

            FileUploadResult result = userRegister.uploadFile(files, username, phoneNumber);
            User user = userRegister.addUser(username,phoneNumber, result.getFileUrl(), result.getCounter(), result.getDate());

            return ResponseEntity.ok("Файлы загружены: " + result.getFileUrl());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
        } catch (FileStorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка загрузки файлов: " + e.getMessage());
        }
    }


}

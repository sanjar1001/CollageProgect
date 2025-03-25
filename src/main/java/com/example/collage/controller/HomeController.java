package com.example.collage.controller;

import com.example.collage.entity.FileUploadResult;
import com.example.collage.entity.User;
import com.example.collage.entity.Users_accepted;
import com.example.collage.entity.Users_refusal;
import com.example.collage.exception.FileStorageException;
import com.example.collage.repository.UserAcceptedRepository;
import com.example.collage.repository.UserRepository;
import com.example.collage.service.UserRequestService;
import com.example.collage.service.UserRegister;
import com.example.collage.service.ServiceImpl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final UserServiceImpl userServiceImpl;
    private final UserRepository userRepository;
    private final UserAcceptedRepository   userAcceptedRepository;
    private final UserRegister userRegister;
    private final UserRequestService userRequestService;


    @GetMapping("/users")  // Получит все заявки
    public List<User> users() {
        return userServiceImpl.findAll();
    }

    @GetMapping("/")
    public String home(Model model) {
        List<User> users = userServiceImpl.findAll();
        model.addAttribute("users", users);
        return "home";
    }

    @GetMapping("/user/accepted")
    public String userAccepted(Model model) {
        List<Users_accepted> users = userServiceImpl.findAllAccepted();
        model.addAttribute("users", users);
        return "userAccepted";
    }
    @GetMapping("/user/refusal")
    public String userRefusal(Model model) {
        List<Users_refusal> users = userServiceImpl.findAllRefusal();
        model.addAttribute("users", users);
        return "userRefusal";
    }

    @GetMapping("/registration")
    public String registration() {
        return "register";
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<String> addUser(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("username") String username,
            @RequestParam("phoneNumber") String phoneNumber
    ) throws IOException, FileStorageException {

        FileUploadResult result = userRegister.uploadFile(files, username,phoneNumber);

        User user = userRegister.addUser(username,phoneNumber, result.getFileUrl(), result.getCounter(), result.getDate());

        return ResponseEntity.ok("File uploaded and zipped successfully.");
    }

    @GetMapping("/downloadFile")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam String username) throws IOException {
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
        InputStream inputStream = new FileInputStream(file);

        // Кодируем имя файла в UTF-8 для Content-Disposition
        String encodedFilename = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8)
                .replace("+", "%20"); // Пробелы заменяем на "%20"

        // Возвращаем файл как InputStreamResource
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename) // filename* вместо filename
                .body(new InputStreamResource(inputStream));
    }



    @GetMapping("/username")
    public String findByUsername(Model model,
                                 @RequestParam String username) {
        List<User> users = userServiceImpl.findUserByUsername(username);
        model.addAttribute("users", users);
        return "home";
    }

    @GetMapping("/user/details")
    public String userDetails(@RequestParam Long id, Model model){
        User user = userRepository.findById(id).orElse(null);
        if(id != null){
            model.addAttribute("user", user);
            return "details";
        }else{
            return "redirect:/error";
        }
    }

    @GetMapping("/user/accepted/details")
    public String userDetailsAccepted(@RequestParam Long id, Model model){
        Users_accepted user = userAcceptedRepository.findById(id).orElse(null);
        model.addAttribute("user", user);
        return "details";
    }

    @PostMapping("/deleteuser")
    public String deleteCar(@RequestParam(name = "id") Long id){
        userRepository.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/user/accepted")
    public String acceptedUser(@RequestParam(name = "id") long id) throws IOException {
        boolean success = userRequestService.acceptedUsers(id);
        if(success){
            return "redirect:/user/accepted";
        }else {
            return "redirect:/errorPage";  // Ошибка
        }
    }

    @PostMapping("/user/refusal")
    public String RefusalUser(@RequestParam(name = "id") long id) throws BadRequestException {
        boolean success = userRequestService.refusalUsers(id);
        if(success){
            return "redirect:/user/refusal";
        }else {
            return "redirect:/errorPage";  // Ошибка
        }
    }










}

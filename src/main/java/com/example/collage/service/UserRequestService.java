package com.example.collage.service;

import com.example.collage.entity.Users_accepted;
import com.example.collage.entity.Users_refusal;
import org.apache.coyote.BadRequestException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Service
public interface UserRequestService {
    Users_accepted addUserAccepted(String username, String phoneNumber, String fileUrl, int counter, LocalDate date, boolean status);
    boolean acceptedUsers(long id) throws IOException;

    Users_refusal addUserRefusal(String username, String phoneNumber, String fileUrl, int counter, LocalDate date, boolean status);
    boolean refusalUsers(long id) throws BadRequestException;

    ResponseEntity<?> getUserAcceptedById(long id);

    Optional<Users_refusal> getUserRefusalById(long id);

    void deleteUserAcceptedById(long id) throws IOException;

    void deleteUserRefusalById(long id) throws IOException;

    ResponseEntity<InputStreamResource> downloadFile(String username) throws IOException;

}

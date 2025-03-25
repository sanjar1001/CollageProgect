package com.example.collage.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "user_acceped")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users_accepted {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "file_count")
    private int fileCount;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "status")
    private Boolean status;


}

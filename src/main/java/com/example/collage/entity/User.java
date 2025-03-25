package com.example.collage.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "document_url")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

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



    public boolean isStatus() {
        return status;
    }
}

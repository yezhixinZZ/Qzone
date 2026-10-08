package com.example.qzone.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name = "users") public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
  @Column(nullable = false, unique = true, length = 32) public String username;
  @Column(nullable = false) public String password;
  @Column(name = "created_at", nullable = false) public LocalDateTime createdAt = LocalDateTime.now();
}

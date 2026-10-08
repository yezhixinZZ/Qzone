package com.example.qzone.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name = "posts") public class Post {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false) public User user;
  @Column(nullable = false, length = 1000) public String content;
  @Column(name = "image_url") public String imageUrl;
  @Column(name = "created_at", nullable = false) public LocalDateTime createdAt = LocalDateTime.now();
}

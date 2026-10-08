package com.example.qzone.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name = "comments") public class Comment {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id") public User user;
  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "post_id") public Post post;
  @Column(nullable = false, length = 500) public String content;
  @Column(name = "created_at") public LocalDateTime createdAt = LocalDateTime.now();
}

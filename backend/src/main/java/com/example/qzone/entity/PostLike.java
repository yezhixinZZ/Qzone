package com.example.qzone.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name = "likes", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"})) public class PostLike {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id") public User user;
  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "post_id") public Post post;
  @Column(name = "created_at") public LocalDateTime createdAt = LocalDateTime.now();
}

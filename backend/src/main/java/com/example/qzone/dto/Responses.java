package com.example.qzone.dto;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;
public class Responses {
  public record CurrentUser(Long id, String username) implements Serializable {}
  public record CommentView(Long id, String username, String content, LocalDateTime createdAt) implements Serializable {}
  public record PostView(Long id, Long userId, String username, String content, String imageUrl, LocalDateTime createdAt, long likeCount, boolean liked, List<CommentView> comments) implements Serializable {}
}

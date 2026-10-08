package com.example.qzone.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class Requests {
  public record Auth(@NotBlank @Size(min = 3, max = 32) String username, @NotBlank @Size(min = 6, max = 64) String password) {}
  public record PostInput(@NotBlank @Size(max = 1000) String content, String imageUrl) {}
  public record CommentInput(@NotBlank @Size(max = 500) String content) {}
}

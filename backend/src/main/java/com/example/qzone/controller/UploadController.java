package com.example.qzone.controller;
import com.example.qzone.dto.ApiException;
import com.example.qzone.service.AuthService;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController @RequestMapping("/api/uploads") public class UploadController {
  private final AuthService auth; @Value("${app.upload-dir}") String uploadDir;
  public UploadController(AuthService auth) { this.auth = auth; }
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) Map<String, String> upload(@RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
    auth.require(session); if (file.isEmpty() || file.getContentType() == null || !file.getContentType().startsWith("image/")) throw new ApiException("请选择图片文件");
    String original = file.getOriginalFilename(); String extension = original != null && original.contains(".") ? original.substring(original.lastIndexOf('.')) : ".jpg"; String fileName = UUID.randomUUID() + extension;
    Path directory = Path.of(uploadDir).toAbsolutePath(); Files.createDirectories(directory); Files.copy(file.getInputStream(), directory.resolve(fileName), StandardCopyOption.REPLACE_EXISTING); return Map.of("url", "/uploads/" + fileName);
  }
}

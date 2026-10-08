package com.example.qzone.controller;
import com.example.qzone.dto.Requests;
import com.example.qzone.dto.Responses.CurrentUser;
import com.example.qzone.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth") public class AuthController {
  private final AuthService auth; public AuthController(AuthService auth) { this.auth = auth; }
  @PostMapping("/register") CurrentUser register(@Valid @RequestBody Requests.Auth input, HttpSession session) { return auth.register(input, session); }
  @PostMapping("/login") CurrentUser login(@Valid @RequestBody Requests.Auth input, HttpSession session) { return auth.login(input, session); }
  @GetMapping("/me") CurrentUser me(HttpSession session) { return auth.current(session); }
  @PostMapping("/logout") Map<String, Boolean> logout(HttpSession session) { session.invalidate(); return Map.of("ok", true); }
}

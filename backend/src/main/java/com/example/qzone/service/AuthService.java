package com.example.qzone.service;
import com.example.qzone.dto.ApiException;
import com.example.qzone.dto.Requests;
import com.example.qzone.dto.Responses.CurrentUser;
import com.example.qzone.entity.User;
import com.example.qzone.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service public class AuthService {
  private final UserRepository users; private final PasswordEncoder encoder;
  public AuthService(UserRepository users, PasswordEncoder encoder) { this.users = users; this.encoder = encoder; }
  public CurrentUser register(Requests.Auth input, HttpSession session) { if (users.existsByUsername(input.username())) throw new ApiException("用户名已被使用"); User user = new User(); user.username = input.username(); user.password = encoder.encode(input.password()); users.save(user); session.setAttribute("userId", user.id); return view(user); }
  public CurrentUser login(Requests.Auth input, HttpSession session) { User user = users.findByUsername(input.username()).orElseThrow(() -> new ApiException("用户名或密码错误")); if (!encoder.matches(input.password(), user.password)) throw new ApiException("用户名或密码错误"); session.setAttribute("userId", user.id); return view(user); }
  public CurrentUser current(HttpSession session) { return view(require(session)); }
  public User require(HttpSession session) { Object id = session.getAttribute("userId"); if (!(id instanceof Long)) throw new ApiException("请先登录"); return users.findById((Long) id).orElseThrow(() -> new ApiException("用户不存在")); }
  private CurrentUser view(User user) { return new CurrentUser(user.id, user.username); }
}

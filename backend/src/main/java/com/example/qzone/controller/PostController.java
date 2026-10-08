package com.example.qzone.controller;
import com.example.qzone.dto.ApiException;
import com.example.qzone.dto.Requests;
import com.example.qzone.dto.Responses.PostView;
import com.example.qzone.service.AuthService;
import com.example.qzone.service.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/posts") public class PostController {
  private final PostService posts; private final AuthService auth; public PostController(PostService posts, AuthService auth) { this.posts = posts; this.auth = auth; }
  @GetMapping List<PostView> list(HttpSession session) { try { return posts.listFor(auth.require(session)); } catch (ApiException exception) { return posts.list(); } }
  @PostMapping void create(@Valid @RequestBody Requests.PostInput input, HttpSession session) { posts.create(auth.require(session), input); }
  @DeleteMapping("/{id}") void delete(@PathVariable Long id, HttpSession session) { posts.delete(auth.require(session), id); }
  @PostMapping("/{id}/likes") void like(@PathVariable Long id, HttpSession session) { posts.toggleLike(auth.require(session), id); }
  @PostMapping("/{id}/comments") void comment(@PathVariable Long id, @Valid @RequestBody Requests.CommentInput input, HttpSession session) { posts.comment(auth.require(session), id, input); }
}

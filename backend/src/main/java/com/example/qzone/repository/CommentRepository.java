package com.example.qzone.repository;
import com.example.qzone.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CommentRepository extends JpaRepository<Comment, Long> { List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId); }

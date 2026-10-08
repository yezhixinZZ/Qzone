package com.example.qzone.repository;
import com.example.qzone.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PostLikeRepository extends JpaRepository<PostLike, Long> { boolean existsByUserIdAndPostId(Long userId, Long postId); long countByPostId(Long postId); void deleteByUserIdAndPostId(Long userId, Long postId); }

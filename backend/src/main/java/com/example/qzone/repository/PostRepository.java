package com.example.qzone.repository;

import com.example.qzone.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
        select p
        from Post p
        join fetch p.user
        order by p.createdAt desc
        """)
    List<Post> findAllWithUserOrderByCreatedAtDesc();

    @Query("""
    select p.user.id
    from Post p
    where p.id = :postId
    """)
    Long findUserIdByPostId(@Param("postId") Long postId);
}
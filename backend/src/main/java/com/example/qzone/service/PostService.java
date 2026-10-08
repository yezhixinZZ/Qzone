package com.example.qzone.service;

import com.example.qzone.dto.ApiException;
import com.example.qzone.dto.Requests;
import com.example.qzone.dto.Responses.*;
import com.example.qzone.entity.*;
import com.example.qzone.repository.*;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {
    private final PostRepository posts;
    private final PostLikeRepository likes;
    private final CommentRepository comments;

    public PostService(PostRepository posts, PostLikeRepository likes, CommentRepository comments) {
        this.posts = posts;
        this.likes = likes;
        this.comments = comments;
    }

    @Cacheable("publicPosts")
    @Transactional(readOnly = true)
    public List<PostView> list() {
        return build(null);
    }

    @Transactional(readOnly = true)
    public List<PostView> listFor(User user) {
        return build(user.id);
    }

    @CacheEvict(value = "publicPosts", allEntries = true)
    public void create(User user, Requests.PostInput input) {
        Post post = new Post();
        post.user = user;
        post.content = input.content();
        post.imageUrl = input.imageUrl();
        posts.save(post);
    }

    @CacheEvict(value = "publicPosts", allEntries = true)
    @Transactional
    public void delete(User user, Long postId) {

        Long postUserId = posts.findUserIdByPostId(postId);

        if (postUserId == null) {
            throw new ApiException("动态不存在");
        }

        if (!postUserId.equals(user.id)) {
            throw new ApiException("只能删除自己的动态");
        }

        posts.deleteById(postId);
    }

    @CacheEvict(value = "publicPosts", allEntries = true)
    @Transactional
    public void toggleLike(User user, Long postId) {
        Post post = get(postId);

        if (likes.existsByUserIdAndPostId(user.id, postId)) {
            likes.deleteByUserIdAndPostId(user.id, postId);
        } else {
            PostLike like = new PostLike();
            like.user = user;
            like.post = post;
            likes.save(like);
        }
    }

    @CacheEvict(value = "publicPosts", allEntries = true)
    public void comment(User user, Long postId, Requests.CommentInput input) {
        Comment comment = new Comment();
        comment.user = user;
        comment.post = get(postId);
        comment.content = input.content();
        comments.save(comment);
    }

    private List<PostView> build(Long viewerId) {
        return posts.findAllWithUserOrderByCreatedAtDesc()
                .stream()
                .map(post -> view(post, viewerId))
                .toList();
    }

    private Post get(Long id) {
        return posts.findById(id).orElseThrow(() -> new ApiException("动态不存在"));
    }

    private PostView view(Post post, Long viewerId) {
        return new PostView(post.id, post.user.id, post.user.username, post.content, post.imageUrl, post.createdAt, likes.countByPostId(post.id), viewerId != null && likes.existsByUserIdAndPostId(viewerId, post.id), comments.findByPostIdOrderByCreatedAtAsc(post.id).stream().map(comment -> new CommentView(comment.id, comment.user.username, comment.content, comment.createdAt)).toList());
    }
}

package com.blog.donaldkisaka.Service;

import com.blog.donaldkisaka.dto.CreatePost;
import com.blog.donaldkisaka.dto.UpdatePost;
import com.blog.donaldkisaka.model.Post;
import com.blog.donaldkisaka.model.User;
import com.blog.donaldkisaka.repository.PostRepository;
import com.blog.donaldkisaka.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Post createPost(CreatePost input, String userEmail) {
        User author = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post post = new Post();
        post.setTitle(input.getTitle());
        post.setContent(input.getContent());
        post.setAuthor(author);
        post.setPublished(false);

        return postRepository.save(post);
    }

    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAllByDeletedAtIsNull(pageable);
    }

    public Post getPostById(Long id, String userEmail) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (!post.getPublished()) {
            if (!post.getAuthor().getEmail().equals(userEmail)) {
                try {
                    throw new AccessDeniedException("Access denied to unpublished post");
                } catch (AccessDeniedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return post;
    }

    public Page<Post> getPublishedPosts(Pageable pageable) {
        return postRepository.findAllByPublishedAndDeletedAtIsNull(true, pageable);
    }

    public Page<Post> getUserPosts(String userEmail, Pageable pageable) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return postRepository.findAllByAuthorAndDeletedAtIsNull(user, pageable);
    }

    public Post updatePost(Long id, UpdatePost input, String userEmail) {
        Post existingPost = postRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

       if (!existingPost.getAuthor().getEmail().equals(userEmail)) {
           throw new org.springframework.security.access.AccessDeniedException("You are not authorized to update this post!");
       }

        existingPost.setTitle(input.getTitle());
        existingPost.setContent(input.getContent());

        return postRepository.save(existingPost);
    }

    public Post togglePublishStatus(Long postId, String userEmail) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (!post.getAuthor().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("You are not authorized to publish this post!");
        }

        post.setPublished(!post.getPublished());
        return postRepository.save(post);
    }

    public Post publishPost(Long postId, String userEmail) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

       if (!post.getAuthor().getEmail().equals(userEmail)) {
           throw new AccessDeniedException("You are not authorized to publish this post!");
       }

        post.setPublished(true);
        return postRepository.save(post);
    }

    public void SoftDeletePost(Long postId, String userEmail) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (!post.getAuthor().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("You are not authorized to delete this post!");
        }
        post.setDeletedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    public Page<Post> searchPublishedPosts(String keyword, Pageable pageable) {
        return postRepository.searchByTitleOrContentAndPublishedAndDeletedAtIsNull(
                keyword, true, pageable
        )

    }
}

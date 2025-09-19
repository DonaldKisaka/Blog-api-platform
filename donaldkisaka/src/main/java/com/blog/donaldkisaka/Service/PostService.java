package com.blog.donaldkisaka.Service;

import com.blog.donaldkisaka.dto.CreatePost;
import com.blog.donaldkisaka.dto.UpdatePost;
import com.blog.donaldkisaka.model.Post;
import com.blog.donaldkisaka.model.User;
import com.blog.donaldkisaka.repository.PostRepository;
import com.blog.donaldkisaka.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setTitle(input.getTitle());
        post.setContent(input.getContent());
        post.setAuthor(author);
        post.setPublished(false);
        post.setCreatedAt(java.time.LocalDateTime.now());

        return postRepository.save(post);
    }

    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAllByDeletedAtIsNull(pageable);
    }

    public Post getPostById(Long id) {
        return postRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public Page<Post> getPublishedPosts(Pageable pageable) {
        return postRepository.findAllByPublishedAndDeletedAtIsNull(true, pageable);
    }

    public Post updatePost(Long postId, UpdatePost input, String userEmail) {
        Post existingPost = postRepository.findByIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        String authorEmail = existingPost.getAuthor().getEmail();

        if (!authorEmail.equals(userEmail)) {
            throw new RuntimeException("You are not authorized to update this post!");
        }

        existingPost.setTitle(input.getTitle());
        existingPost.setContent(input.getContent());

        return postRepository.save(existingPost);
    }

    public Post publishPost(Long postId) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setPublished(true);
        return postRepository.save(post);
    }

    public void SoftDeletePost(Long postId, String userEmail) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        String authorEmail = post.getAuthor().getEmail();

        if (!authorEmail.equals(userEmail)) {
            throw new RuntimeException("You are not authorized to delete this post!");
        }

        post.setDeletedAt(java.time.LocalDateTime.now());
        postRepository.save(post);
    }
}

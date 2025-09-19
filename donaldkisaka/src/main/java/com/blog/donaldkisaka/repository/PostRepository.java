package com.blog.donaldkisaka.repository;

import com.blog.donaldkisaka.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<Post> findByIdAndDeletedAtIsNull(Long postId);

    Page<Post> findAllByPublishedAndDeletedAtIsNull(Boolean published, Pageable pageable);

    Page<Post> findAllByPublishedAtBeforeAndDeletedAtIsNull(LocalDateTime publishedAt, Pageable pageable);
}

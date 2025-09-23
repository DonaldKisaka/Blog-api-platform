package com.blog.donaldkisaka.repository;

import com.blog.donaldkisaka.model.Post;
import com.blog.donaldkisaka.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<Post> findByIdAndDeletedAtIsNull(Long id);

    Page<Post> findAllByPublishedAndDeletedAtIsNull(Boolean published, Pageable pageable);

    Page<Post> findAllByAuthorAndDeletedAtIsNull(User author, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.published = :published AND p.deletedAt IS NULL AND " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "ORDER BY p.createdAt DESC")
    Page<Post> searchByTitleOrContentAndPublishedAndDeletedAtIsNull(
            @Param("keyword") String keyword,
            @Param("published") Boolean published,
            Pageable pageable
    );
}

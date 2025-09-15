package com.blog.donaldkisaka.repository;

import com.blog.donaldkisaka.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}

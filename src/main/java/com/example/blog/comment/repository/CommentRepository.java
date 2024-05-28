package com.example.blog.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blog.comment.entity.CommentEntity;

/**
 * CommentRepository.
 */
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}

package com.example.blog.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blog.post.entity.PostEntity;

/**
 * PostRepository.
 */
@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

  /**
   * Find all by order by modified at desc.
   */
  List<PostEntity> findAllByOrderByModifiedAtDesc();
  // Repo interface에 JPA의 일련의 규칙을 따라 메서드를 생성하면, 자동으로 이에 대한 impletation method가 생성됨.
}
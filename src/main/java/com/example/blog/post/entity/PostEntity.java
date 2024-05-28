package com.example.blog.post.entity;

import com.example.blog.common.entity.Timestamped;
import com.example.blog.post.dto.PostRequestDto;
import com.example.blog.user.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PostEntity.
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_post")
public class PostEntity extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id")
  private Long postId;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "content", nullable = false)
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  // FetchType.LAZY는 연관 관계로 걸린 엔티티가 참조 되어야 하는 시점에 읽는 방법.
  // JPA N + 1 Problem을 방지하기 위한 가장 기초적인 옵션 값.
  @JoinColumn(name = "username", referencedColumnName = "username")
  // 외래 키를 매핑할 때 사용하는 어노테이션, name = "매핑할 외래 키 컬럼명, referencedColumnName = 대상 테이블의 컬럼명
  // 해당 어노테이션을 생략해도 연관 관계가 걸려 있을 경우, 자동으로 외래 키를 탐색함.
  private UserEntity userEntity;

  /**
   * Initializer using Builder.
   */
  @Builder
  public PostEntity(PostRequestDto requestDto, UserEntity userEntity) {
    this.title = requestDto.getTitle();
    this.content = requestDto.getContent();
    this.userEntity = userEntity;
  }

  /**
   * Update post.
   */
  public void update(PostRequestDto requestDto) {
    this.title = requestDto.getTitle();
    this.content = requestDto.getContent();
  }
}

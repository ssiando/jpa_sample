package com.example.blog.comment.entity;

import com.example.blog.common.entity.Timestamped;
import com.example.blog.post.entity.PostEntity;
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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_comment")
public class CommentEntity extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long commentId;

  @Column(name = "content", nullable = false)
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", referencedColumnName = "post_id")
  private PostEntity postEntity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "username", referencedColumnName = "username")
  private UserEntity userEntity;
}

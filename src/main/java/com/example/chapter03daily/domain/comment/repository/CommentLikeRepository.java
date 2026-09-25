package com.example.chapter03daily.domain.comment.repository;

import com.example.chapter03daily.domain.comment.entity.Comment;
import com.example.chapter03daily.domain.comment.entity.CommentLike;
import com.example.chapter03daily.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    boolean existsByCommentIdAndUserId(Long comment, Long user);

    long countByCommentId(Long commentId);
}

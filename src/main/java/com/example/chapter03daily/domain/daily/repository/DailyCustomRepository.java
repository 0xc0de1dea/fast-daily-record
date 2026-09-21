package com.example.chapter03daily.domain.daily.repository;

import com.example.chapter03daily.domain.comment.entity.Comment;
import com.example.chapter03daily.domain.daily.dto.DailyDto;
import com.example.chapter03daily.domain.daily.entity.Daily;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DailyCustomRepository {

    Daily findByIdWithQuerydsl(Long id);

    List<Comment> findCommentsByIdQuerydsl(Long id);

    Page<DailyDto.Response> findAllWithQuerydsl(Pageable pageable);
}

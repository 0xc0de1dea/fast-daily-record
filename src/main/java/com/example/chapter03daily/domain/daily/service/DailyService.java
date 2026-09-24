package com.example.chapter03daily.domain.daily.service;

import com.example.chapter03daily.common.exception.ErrorCode;
import com.example.chapter03daily.common.exception.ServiceException;
import com.example.chapter03daily.domain.comment.dto.CommentDto;
import com.example.chapter03daily.domain.daily.dto.DailyDetailResponse;
import com.example.chapter03daily.domain.daily.dto.DailyDto;
import com.example.chapter03daily.domain.daily.entity.Daily;
import com.example.chapter03daily.domain.daily.repository.DailyRepository;
import com.example.chapter03daily.domain.user.entity.User;
import com.example.chapter03daily.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyService {

    private final DailyRepository dailyRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final DailyCacheService dailyCacheService;

    @Transactional
    public DailyDto.Response create(org.springframework.security.core.userdetails.User user, DailyDto.Request request) {
        String email = user.getUsername();

        Daily savedDaily = dailyRepository.saveAndFlush(
                new Daily(
                        request.getTitle(),
                        request.getContent(),
                        email,
                        passwordEncoder.encode(request.getPassword())
                )
        );

        return DailyDto.Response.build(
                savedDaily.getTitle(),
                savedDaily.getContent(),
                savedDaily.getAuthor(),
                savedDaily.getCreatedAt(),
                null
        );
    }

    @Transactional(readOnly = true)
    public Page<DailyDto.Response> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("modifiedAt").descending()
        );

        Page<Daily> pageDaily = dailyRepository.findAll(pageable);

        return pageDaily.map(daily ->
                DailyDto.Response.build(
                        daily.getTitle(),
                        daily.getContent(),
                        daily.getAuthor(),
                        daily.getCreatedAt(),
                        daily.getModifiedAt()
                ));
    }

    @Transactional(readOnly = true)
    public Page<DailyDto.Response> findAllWithQuerydsl(int page, int size) {
        Pageable pageable = PageRequest.of(
                page,
                size
        );

        return dailyRepository.findAllWithQuerydsl(pageable);
    }

    @Transactional(readOnly = true)
    public DailyDetailResponse findOne(long id) {
        DailyDetailResponse cached = dailyCacheService.getDailyCache(id);

        if (cached != null) {
            return cached;
        }

        Daily saved = dailyRepository.findById(id)
                .orElseThrow(
                        () -> new ServiceException(ErrorCode.DAILY_NOT_FOUND)
                );

        List<CommentDto.Response> commentDtoList = saved.getComments()
                .stream()
                .map(comment -> CommentDto.Response.build(
                        comment.getDaily().getId(),
                        comment.getContent(),
                        comment.getAuthor(),
                        comment.getCreatedAt(),
                        comment.getModifiedAt()
                ))
                .toList();

        DailyDetailResponse detail = DailyDetailResponse.build(
                saved.getTitle(),
                saved.getContent(),
                saved.getAuthor(),
                saved.getCreatedAt(),
                saved.getModifiedAt(),
                commentDtoList
        );

        dailyCacheService.saveDailyCache(id, detail);

        return detail;
    }

    @Transactional(readOnly = true)
    public DailyDetailResponse findOneWithQuerydsl(long id) {
        DailyDetailResponse cached = dailyCacheService.getDailyCache(id);

        if (cached != null) {
            return cached;
        }

        Daily saved = dailyRepository.findByIdWithQuerydsl(id);

        if (saved == null) {
            throw new ServiceException(ErrorCode.DAILY_NOT_FOUND);
        }

        List<CommentDto.Response> commentDtoList =
                dailyRepository.findCommentsByIdQuerydsl(id)
                .stream()
                .map(comment -> CommentDto.Response.build(
                        comment.getDaily().getId(),
                        comment.getContent(),
                        comment.getAuthor(),
                        comment.getCreatedAt(),
                        comment.getModifiedAt()
                ))
                .toList();

        DailyDetailResponse detail = DailyDetailResponse.build(
                saved.getTitle(),
                saved.getContent(),
                saved.getAuthor(),
                saved.getCreatedAt(),
                saved.getModifiedAt(),
                commentDtoList
        );

        dailyCacheService.saveDailyCache(id, detail);

        return detail;
    }

    @Transactional
    public DailyDto.Response update(org.springframework.security.core.userdetails.User user, Long id, DailyDto.Request request) {
        String email = user.getUsername();

        User savedUser = userRepository.findUserByEmail(email)
                .orElseThrow(
                        () -> new ServiceException(ErrorCode.USER_NOT_FOUND)
                );

        Daily savedDaily = dailyRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorCode.DAILY_NOT_FOUND)
        );

        if (savedUser.getName().equals(savedDaily.getAuthor())) {
            throw new ServiceException(ErrorCode.USER_NOT_MATCHED);
        }

        if (!passwordEncoder.matches(request.getPassword(), savedDaily.getPassword())) {
            throw new ServiceException(ErrorCode.INVALID_PASSWORD);
        }

        String title = request.getTitle();
        String content = request.getContent();

        savedDaily.update(title, content);

        return DailyDto.Response.build(
                savedDaily.getTitle(),
                savedDaily.getContent(),
                savedDaily.getAuthor(),
                null,
                savedDaily.getModifiedAt()
        );
    }

    @Transactional
    public void delete(org.springframework.security.core.userdetails.User user, Long id, DailyDto.Request request) {
        String email = user.getUsername();

        User savedUser = userRepository.findUserByEmail(email)
                .orElseThrow(
                        () -> new ServiceException(ErrorCode.USER_NOT_FOUND)
                );

        Daily savedDaily = dailyRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorCode.DAILY_NOT_FOUND)
        );

        if (!passwordEncoder.matches(request.getPassword(), savedDaily.getPassword())) {
            throw new ServiceException(ErrorCode.INVALID_PASSWORD);
        }

        dailyRepository.deleteById(id);
    }
}

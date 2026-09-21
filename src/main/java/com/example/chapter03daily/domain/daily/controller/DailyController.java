package com.example.chapter03daily.domain.daily.controller;

import com.example.chapter03daily.common.dto.ApiResponse;
import com.example.chapter03daily.domain.daily.dto.DailyDetailResponse;
import com.example.chapter03daily.domain.daily.dto.DailyDto;
import com.example.chapter03daily.domain.daily.service.DailyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dailies")
public class DailyController {

    private final DailyService dailyService;

    @PostMapping
    public ResponseEntity<ApiResponse<DailyDto.Response>> create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody DailyDto.Request request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(dailyService.create(user, request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ApiResponse.PageResponse<DailyDto.Response>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(ApiResponse.ok(dailyService.findAll(page, size)));

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok(dailyService.findAllWithQuerydsl(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DailyDetailResponse>> findOne(
            @PathVariable long id
    ) {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(ApiResponse.ok(dailyService.findOne(id)));

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok(dailyService.findOneWithQuerydsl(id)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<DailyDto.Response>> update(
            @AuthenticationPrincipal User user,
            @PathVariable long id,
            @Valid @RequestBody DailyDto.Request request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok(dailyService.update(user, id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @Valid @RequestBody DailyDto.Request request
    ) {
        dailyService.delete(user, id, request);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.noContent());
    }
}

package com.example.chapter03daily.domain.daily.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class DailyDto {

    @Getter
    @AllArgsConstructor
    public static class Request {

        private String title;

        private String content;

        private String password;
    }

    @Getter
    @AllArgsConstructor
    @Builder(access = AccessLevel.PRIVATE)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({"title", "content", "author", "likes", "createdAt", "modifiedAt"})
    public static class Response {

        protected String title;

        protected String content;

        protected String author;

        protected Long likes;

        protected LocalDateTime createdAt;

        protected LocalDateTime modifiedAt;

        public Response() {

        }

        public static Response build(
                String title,
                String content,
                String author,
                Long likes,
                LocalDateTime createdAt,
                LocalDateTime modifiedAt
        ) {
            return Response.builder()
                    .title(title)
                    .content(content)
                    .author(author)
                    .likes(likes)
                    .createdAt(createdAt)
                    .modifiedAt(modifiedAt)
                    .build();
        }
    }
}

package com.example.chapter03daily.domain.daily.entity;

import com.example.chapter03daily.common.entity.BaseEntity;
import com.example.chapter03daily.domain.comment.entity.Comment;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "dailies",
        indexes = {
                @Index(
                        name = "idx_daily_modified_at",
                        columnList = "modified_at"
                )
        }
)
public class Daily extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "일정 제목은 필수입니다.")
    @Size(max = 30, message = "일정 제목은 최대 30자까지 입력할 수 있습니다.")
    @Column(nullable = false, length = 30)
    private String title;

    @NotBlank(message = "일정 내용은 필수입니다.")
    @Size(max = 200, message = "일정 내용은 최대 200자까지 입력할 수 있습니다.")
    @Column(nullable = false, length = 200)
    private String content;

    @NotBlank(message = "작성자명은 필수입니다.")
    @Column(nullable = false)
    private String author;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String password;

    @OneToMany(
            mappedBy = "daily",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments = new ArrayList<>();

    public Daily(String title, String content, String author, String password) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.password = password;
    }

    public void update(
            String title, String content
    ) {
        this.title = title == null ? this.title : title;
        this.content = content == null ? this.content : content;
    }
}

package com.springboot.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
public class CommentPostDto {
    @NotNull(message = "Member ID 필요")
    private long memberId;

    private long postId;

    @NotBlank(message = "댓글은 비워 둘수 없습니다.")
    @Size(min = 1, max = 500, message = "댓글은 1 ~ 500자 이내여야 합니다.")
    private String comment;
}

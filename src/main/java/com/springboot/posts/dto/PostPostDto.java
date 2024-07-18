package com.springboot.posts.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class PostPostDto {
    @NotNull
    @Length(max = 25, message = "게시글의 제목은 25자 이내여야 합니다.")
    private String title;

    @NotNull
    private long memberId; // NotBlack 사용 불가


    private long postId;


    @Length(min = 1, max = 500, message = "내용의 길이는 1 ~ 500자 이내여야 합니다.")
    private String content;

}

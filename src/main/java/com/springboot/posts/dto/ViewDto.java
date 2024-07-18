package com.springboot.posts.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class ViewDto {
    @Getter
    @NoArgsConstructor
    @Setter
    public static class ViewPostDto {
        private long postId;

        @NotNull
        private long memberId;
    }
}

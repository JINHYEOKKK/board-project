package com.springboot.posts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PostPatchDto {
    private long postId;

    private String title;

    private String content;

    public void setPostId(long postId) {
        this.postId = postId;
    }
}

package com.springboot.posts.dto;

import com.springboot.comment.dto.CommentResponseDto;
import com.springboot.comment.entity.Comment;
import com.springboot.posts.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
    private long postId;

    private long memberId;

    private String title;

    private String content;

    private int likeCount;

    private int viewCount;

    private List<CommentResponseDto> comments;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private Post.PostStatus postStatus;

    public static class PostResponseDto2 {
        @Getter
        @Setter
        private Post.PostStatus postStatus;
    }
}

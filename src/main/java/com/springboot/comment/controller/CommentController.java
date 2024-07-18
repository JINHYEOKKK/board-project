package com.springboot.comment.controller;

import com.springboot.comment.dto.CommentPostDto;
import com.springboot.comment.entity.Comment;
import com.springboot.comment.mapper.CommentMapper;
import com.springboot.comment.service.CommentService;
import com.springboot.utils.UriCreator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/v1/comments")
public class CommentController {
    private final CommentMapper commentMapper;
    private final CommentService commentService;
    private final static String COMMENT_DEFAULT_URL = "/v1/comments";

    public CommentController(CommentMapper commentMapper, CommentService commentService) {
        this.commentMapper = commentMapper;
        this.commentService = commentService;
    }

    @PostMapping("/{post-id}")
    public ResponseEntity postComment(@RequestBody CommentPostDto commentPostDto, @PathVariable("post-id") long postId) {
        commentPostDto.setPostId(postId);
        Comment comment = commentService.createComment(commentMapper.commentPostDtoToComment(commentPostDto));
        URI location = UriCreator.createUri(COMMENT_DEFAULT_URL, comment.getCommentId());
        return ResponseEntity.created(location).build();
    }

}

package com.springboot.comment.mapper;

import com.springboot.comment.dto.CommentPostDto;
import com.springboot.comment.dto.CommentResponseDto;
import com.springboot.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "memberId", target = "member.memberId")
    @Mapping(source = "postId", target = "post.postId")
    Comment commentPostDtoToComment(CommentPostDto commentPostDto);
    CommentResponseDto commentToCommentResponseDto(Comment comment);
//    List<Comment> commentsPostDtoToComment


}

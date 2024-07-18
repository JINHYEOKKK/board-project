package com.springboot.comment.mapper;

import com.springboot.comment.dto.CommentPostDto;
import com.springboot.comment.dto.CommentResponseDto;
import com.springboot.comment.entity.Comment;
import com.springboot.member.entity.Member;
import com.springboot.posts.entity.Post;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-18T14:50:20+0900",
    comments = "version: 1.5.1.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.1.jar, environment: Java 11.0.22 (Azul Systems, Inc.)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public Comment commentPostDtoToComment(CommentPostDto commentPostDto) {
        if ( commentPostDto == null ) {
            return null;
        }

        Comment comment = new Comment();

        comment.setMember( commentPostDtoToMember( commentPostDto ) );
        comment.setPost( commentPostDtoToPost( commentPostDto ) );
        comment.setComment( commentPostDto.getComment() );

        return comment;
    }

    @Override
    public CommentResponseDto commentToCommentResponseDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentResponseDto commentResponseDto = new CommentResponseDto();

        commentResponseDto.setCommentId( comment.getCommentId() );
        commentResponseDto.setComment( comment.getComment() );

        return commentResponseDto;
    }

    protected Member commentPostDtoToMember(CommentPostDto commentPostDto) {
        if ( commentPostDto == null ) {
            return null;
        }

        Member member = new Member();

        member.setMemberId( commentPostDto.getMemberId() );

        return member;
    }

    protected Post commentPostDtoToPost(CommentPostDto commentPostDto) {
        if ( commentPostDto == null ) {
            return null;
        }

        Post post = new Post();

        post.setPostId( commentPostDto.getPostId() );

        return post;
    }
}

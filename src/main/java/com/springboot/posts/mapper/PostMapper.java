package com.springboot.posts.mapper;

import com.springboot.comment.dto.CommentResponseDto;

import com.springboot.posts.dto.PostPatchDto;
import com.springboot.posts.dto.PostPostDto;
import com.springboot.posts.dto.PostResponseDto;
import com.springboot.posts.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(source = "memberId", target = "member.memberId")
    @Mapping(source = "postId",target = "postId")
    Post postPostDtoToPost(PostPostDto postPostDto);
//    PostResponseDto postToPostResponseDto(Post post);
    Post postPatchDtoToPost(PostPatchDto postPatchDto);
    List<PostResponseDto> postsToPostResponseDtos(List<Post> postList);

    default PostResponseDto postCommentToPostResponseDto(Post post) {
        PostResponseDto postResponseDto = new PostResponseDto();

        postResponseDto.setPostId(post.getPostId());
        postResponseDto.setMemberId(post.getMember().getMemberId());
        postResponseDto.setTitle(post.getTitle());
        postResponseDto.setContent(post.getContent());
//        postResponseDto.setView(post.getView());
        postResponseDto.setLikeCount(post.getLikeCount());
        postResponseDto.setCreatedAt(post.getCreatedAt());
        postResponseDto.setModifiedAt(post.getModifiedAt());
        if(post.getComments().isEmpty()) {
            postResponseDto.setPostStatus(post.getPostStatus());
        }else {
            postResponseDto.setPostStatus(Post.PostStatus.POST_QUESTION_ANSWERED);
        }

        List<CommentResponseDto> commentResponseDtoList = post.getComments().stream()
                .map(comment -> {
                    CommentResponseDto commentResponseDto = new CommentResponseDto();
                    commentResponseDto.setMemberId(comment.getMember().getMemberId());
                    commentResponseDto.setCommentId(comment.getCommentId());
                    commentResponseDto.setComment(comment.getComment());
                    commentResponseDto.setPostId(comment.getPost().getPostId());
                    return commentResponseDto;
                }).collect(Collectors.toList());
        postResponseDto.setComments(commentResponseDtoList);
        return postResponseDto;
    }

    default PostResponseDto.PostResponseDto2 postToPostResponseDto(Post post) {
        PostResponseDto.PostResponseDto2 postResponseDto = new PostResponseDto.PostResponseDto2();

        postResponseDto.setPostStatus(Post.PostStatus.POST_QUESTION_DEACTIVATED);
        return postResponseDto;
    }

}

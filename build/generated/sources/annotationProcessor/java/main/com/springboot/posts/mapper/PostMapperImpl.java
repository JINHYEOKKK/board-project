package com.springboot.posts.mapper;

import com.springboot.member.entity.Member;
import com.springboot.posts.dto.PostPatchDto;
import com.springboot.posts.dto.PostPostDto;
import com.springboot.posts.dto.PostResponseDto;
import com.springboot.posts.entity.Post;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-19T20:05:39+0900",
    comments = "version: 1.5.1.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.1.jar, environment: Java 11.0.22 (Azul Systems, Inc.)"
)
@Component
public class PostMapperImpl implements PostMapper {

    @Override
    public Post postPostDtoToPost(PostPostDto postPostDto) {
        if ( postPostDto == null ) {
            return null;
        }

        Post post = new Post();

        post.setMember( postPostDtoToMember( postPostDto ) );
        post.setPostId( postPostDto.getPostId() );
        post.setTitle( postPostDto.getTitle() );
        post.setContent( postPostDto.getContent() );

        return post;
    }

    @Override
    public Post postPatchDtoToPost(PostPatchDto postPatchDto) {
        if ( postPatchDto == null ) {
            return null;
        }

        Post post = new Post();

        post.setPostId( postPatchDto.getPostId() );
        post.setTitle( postPatchDto.getTitle() );
        post.setContent( postPatchDto.getContent() );

        return post;
    }

    @Override
    public List<PostResponseDto> postsToPostResponseDtos(List<Post> postList) {
        if ( postList == null ) {
            return null;
        }

        List<PostResponseDto> list = new ArrayList<PostResponseDto>( postList.size() );
        for ( Post post : postList ) {
            list.add( postCommentToPostResponseDto( post ) );
        }

        return list;
    }

    protected Member postPostDtoToMember(PostPostDto postPostDto) {
        if ( postPostDto == null ) {
            return null;
        }

        Member member = new Member();

        member.setMemberId( postPostDto.getMemberId() );

        return member;
    }
}

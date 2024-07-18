package com.springboot.posts.mapper;

import com.springboot.member.entity.Member;
import com.springboot.posts.dto.LikeDto;
import com.springboot.posts.entity.Like;
import com.springboot.posts.entity.Post;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-18T14:50:20+0900",
    comments = "version: 1.5.1.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.1.jar, environment: Java 11.0.22 (Azul Systems, Inc.)"
)
@Component
public class LikeMapperImpl implements LikeMapper {

    @Override
    public Like likePostDtoToLike(LikeDto.Post requestBody) {
        if ( requestBody == null ) {
            return null;
        }

        Like like = new Like();

        like.setPost( postToPost( requestBody ) );
        like.setMember( postToMember( requestBody ) );

        return like;
    }

    protected Post postToPost(LikeDto.Post post) {
        if ( post == null ) {
            return null;
        }

        Post post1 = new Post();

        post1.setPostId( post.getPostId() );

        return post1;
    }

    protected Member postToMember(LikeDto.Post post) {
        if ( post == null ) {
            return null;
        }

        Member member = new Member();

        member.setMemberId( post.getMemberId() );

        return member;
    }
}

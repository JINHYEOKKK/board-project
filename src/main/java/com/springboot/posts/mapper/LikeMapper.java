package com.springboot.posts.mapper;

import com.springboot.posts.dto.LikeDto;
import com.springboot.posts.entity.Like;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LikeMapper {
    @Mapping(source = "postId" ,target = "post.postId")
    @Mapping(source = "memberId" ,target = "member.memberId")
    Like likePostDtoToLike(LikeDto.Post requestBody);
}

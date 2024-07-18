package com.springboot.posts.repository;

import com.springboot.member.entity.Member;
import com.springboot.posts.entity.Like;
import com.springboot.posts.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndMember(Post post, Member member);
}

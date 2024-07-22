package com.springboot.posts.repository;


import com.springboot.member.entity.Member;
import com.springboot.posts.entity.Post;
import com.springboot.posts.entity.View;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ViewRepository extends JpaRepository<View, Long> {
    Optional<View> findByMemberAndPost(Member member, Post post);
}

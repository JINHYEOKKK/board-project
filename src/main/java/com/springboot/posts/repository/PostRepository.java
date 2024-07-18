package com.springboot.posts.repository;

import com.springboot.member.entity.Member;
import com.springboot.posts.entity.Post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
}

package com.springboot.posts.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import com.springboot.member.repository.MemberRepository;
import com.springboot.member.service.MemberService;
import com.springboot.posts.entity.Like;
import com.springboot.posts.entity.Post;

import com.springboot.posts.repository.LikeRepository;
import com.springboot.posts.repository.PostRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.channels.Pipe;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public PostService(PostRepository postRepository, LikeRepository likeRepository, MemberRepository memberRepository, MemberService memberService) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.memberRepository = memberRepository;
        this.memberService = memberService;
    }

    public Post createPost(Post post) {
//        verifyExistsPost(post.getPostId());
        postRepository.save(post);
        return post;
    }

    public Post updatePost(Post post) {
        Post findPost = findVerifiedPost(post.getPostId());

        Optional.ofNullable(post.getTitle())
                .ifPresent(title -> findPost.setTitle(title));
        Optional.ofNullable(post.getContent())
                .ifPresent(content -> findPost.setContent(content));

        findPost.setModifiedAt(LocalDateTime.now());

        return postRepository.save(findPost);
    }

    public Post findPost(long postId) {
        Post post = findVerifiedPost(postId);
        if(post.getPostStatus().equals(Post.PostStatus.POST_QUESTION_DEACTIVATED)) {
            throw new BusinessLogicException(ExceptionCode.POST_NOT_EXISTS);
        }
        return post;
    }

//    public void createView(Post post) {
//        post.setView(new View());
//
//        postRepository.save(post);
//    }

    public Page<Post> findPosts(int page, int size) {
        return postRepository.findAll(PageRequest.of(page - 1, size,
                Sort.by("postId").descending()));
    }

    private Post findVerifiedPost(long postId) {
        Optional<Post> optionalPost =
                postRepository.findById(postId);
        Post post =
                optionalPost.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.POST_NOT_EXISTS));
        return post;
    }

    private void verifyExistsPost(long postId) {
        Optional<Post> findPost = postRepository.findById(postId);
        if (findPost.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.POST_EXISTS);
        }
    }

    public void toggleLike(Like like) {
        Post post = postRepository.findById(like.getPost().getPostId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.POST_NOT_EXISTS));

        // 위코드는 post 레포지토리를 주입받아 메서드를 사용하여 id 를 찾았고, 이 코드는
        Member member = memberService.findVerifiedMember(like.getMember().getMemberId());

        Optional<Like> optionalLike = likeRepository.findByPostAndMember(post, member);

        if (optionalLike.isPresent()) {
            Like findLike = optionalLike.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
            findLike.removePost(post);
            findLike.removeMember(member);
            likeRepository.delete(findLike);
        } else {
            Like addLike = new Like();
            addLike.setPost(post);
            addLike.setMember(member);
            likeRepository.save(addLike);
        }
    }
}

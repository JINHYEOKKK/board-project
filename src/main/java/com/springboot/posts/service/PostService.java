package com.springboot.posts.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import com.springboot.member.repository.MemberRepository;
import com.springboot.member.service.MemberService;
import com.springboot.posts.entity.Like;
import com.springboot.posts.entity.Post;

import com.springboot.posts.entity.View;
import com.springboot.posts.repository.LikeRepository;
import com.springboot.posts.repository.PostRepository;

import com.springboot.posts.repository.ViewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.channels.Pipe;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final MemberService memberService;
    private final ViewRepository viewRepository;

    public PostService(PostRepository postRepository, LikeRepository likeRepository, MemberService memberService, ViewRepository viewRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.memberService = memberService;
        this.viewRepository = viewRepository;
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
        Optional<Post> optionalPost = postRepository.findById(postId);

        Post findPost = optionalPost.orElseThrow(() -> new BusinessLogicException(ExceptionCode.POST_NOT_EXISTS));

        if(findPost.getPostStatus().equals(Post.PostStatus.POST_QUESTION_DEACTIVATED)) {
            throw new BusinessLogicException(ExceptionCode.POST_NOT_EXISTS);
        }

        Member findMember = memberService.findVerifiedMember(findPost.getMember().getMemberId());

        Optional<View> optionalView = viewRepository.findByMemberAndPost(findMember,findPost);

        if (optionalView.isPresent()){
            return findPost;
        } else {
            View addView = new View();
            addView.setPost(findPost);
            addView.setMember(findMember);
            addView.getPost().incrementViewCount();
            viewRepository.save(addView);
        }
        return findPost;
    }

    public Page<Post> findPosts(int page, int size, String sort, String standard) {
        Pageable pageable = createPageable(page, size, sort, standard);

        return postRepository.findByPostStatusNotAndPostStatusNot(pageable, Post.PostStatus.POST_QUESTION_DELETED, Post.PostStatus.POST_QUESTION_DEACTIVATED);
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

//    public void toggleView(View view) {
//        Post post = findPost(view.getPost().getPostId());
//        Member member = memberService.findVerifiedMember(view.getMember().getMemberId());
//
//        Optional<View> optionalView = viewRepository.findByMemberAndPost(member, post);
//
//        if (optionalView.isPresent()) {
//            return;
//        } else {
//            View addView = new View();
//            addView.setPost(post);
//            addView.getPost().incrementViewCount();
//            addView.setMember(member);
//            viewRepository.save(addView);
//        }
//    }

    public void toggleLike(Like like) {
        Post post = postRepository.findById(like.getPost().getPostId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.POST_NOT_EXISTS));

        // 위코드는 post 레포지토리를 주입받아 메서드를 사용하여 id 를 찾았고, 이 코드는
        Member member = memberService.findVerifiedMember(like.getMember().getMemberId());
        Optional<Like> optionalLike = likeRepository.findByPostAndMember(post, member);

        if (optionalLike.isPresent()) {
            Like findLike = optionalLike.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
            findLike.getPost().decrementLikeCount();
            findLike.removePost(post);
            findLike.removeMember(member);
            likeRepository.delete(findLike);
        } else {
            Like addLike = new Like();
            addLike.setPost(post);
            addLike.getPost().incrementLikeCount();
            addLike.setMember(member);
            likeRepository.save(addLike);
        }
    }
    // standard: 어떤 컬럼명 기준으로
    // sort:어떤 방식의 정렬
    private Pageable createPageable(int page, int size, String sort, String standard) {
        if(sort.equals("DESC")) {
            return PageRequest.of(page, size, Sort.by(standard).descending());
//            return PageRequest.of(page, size, Sort.Direction.DESC,standard);
        }else {
            return PageRequest.of(page, size, Sort.by(standard).ascending());
        }
    }
}

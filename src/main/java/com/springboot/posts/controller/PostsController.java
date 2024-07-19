package com.springboot.posts.controller;

import com.springboot.posts.dto.LikeDto;
import com.springboot.posts.dto.PostPatchDto;
import com.springboot.posts.dto.PostPostDto;
import com.springboot.posts.entity.Like;
import com.springboot.posts.entity.Post;
import com.springboot.posts.mapper.LikeMapper;
import com.springboot.posts.mapper.PostMapper;
import com.springboot.posts.service.PostService;
import com.springboot.response.MultiResponseDto;
import com.springboot.response.SingleResponseDto;
import com.springboot.utils.UriCreator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/posts")
@Validated
public class PostsController {
    private final PostMapper postMapper;
    private final PostService postService;
    private final static String POST_DEFAULT_URL = "/v1/posts";
    private final LikeMapper likeMapper;

    public PostsController(PostMapper postMapper, PostService postService, LikeMapper likeMapper) {
        this.postMapper = postMapper;
        this.postService = postService;
        this.likeMapper = likeMapper;
    }

    @PostMapping
    public ResponseEntity postPost(@Valid @RequestBody PostPostDto postPostDto) {

        Post post = postService.createPost(postMapper.postPostDtoToPost(postPostDto));
        URI location = UriCreator.createUri(POST_DEFAULT_URL, post.getPostId());
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity postLike(@PathVariable @Positive long postId,
                                   @RequestBody @Valid LikeDto.Post requestBody) {
        requestBody.setPostId(postId);
        postService.toggleLike(likeMapper.likePostDtoToLike(requestBody));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{post-id}")
    public ResponseEntity patchPost(@PathVariable("post-id") @Positive long postId,
                                    @Valid @RequestBody PostPatchDto postPatchDto) {
        postPatchDto.setPostId(postId);

        Post post =
                postService.updatePost(postMapper.postPatchDtoToPost(postPatchDto));

        return new ResponseEntity<>(
                new SingleResponseDto<>(postMapper.postCommentToPostResponseDto(post)),
                HttpStatus.OK);
    }

    @GetMapping("/{post-id}")
    public ResponseEntity getPost(@PathVariable("post-id") @Positive long postId){
        Post post = postService.findPost(postId);
        return new ResponseEntity<>(
                new SingleResponseDto<>(postMapper.postCommentToPostResponseDto(post)),
        HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getPosts(@RequestParam @Positive int page,
                                   @RequestParam @Positive int size,
                                   @RequestParam String sort,
                                   @RequestParam String standard) {
        Page<Post> postPage = postService.findPosts(page -1, size, sort, standard);
        List<Post> posts = postPage.getContent();

        return new ResponseEntity(
                new MultiResponseDto<>(postMapper.postsToPostResponseDtos(posts), postPage),
                HttpStatus.OK
        );
    }
}

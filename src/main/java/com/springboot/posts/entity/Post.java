package com.springboot.posts.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.springboot.comment.entity.Comment;
import com.springboot.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postId;

    // 엔티티 간의 양방향 연관관계에서 발생할 수 있는 순환 참조 문제를
    // 해결하기 위해 JsonManagedReference와 JsonBackReference를 사용
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();

    // 부모엔터티에 @JsonManagedReference 애너테이션을 붙혀 밑 set 메서드 생략가능
//    public void setComment(Comment comment) {
//        comments.add(comment);
//        if(comment.getPost() != this){
//            comment.setPost(this);
//        }
//    }

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID") // 외래ㅣ
    private Member member;

//    @OneToOne(mappedBy = "post")
//    private View view;

    @OneToOne(mappedBy = "post", cascade = CascadeType.MERGE)
    private Like like;

    // 외부 if : 만약 view 가 null이 아니라면 참이므로 내부 if문이 실행
    // 내부 if : view에서 view에 있는 post를 가져와 이 필드에 있는 값들이 있는지 확인. 없다면 추가
    //
//    public void setView(View view) {
//        if(view != null) {
//            if(view.getPost() != this) {
//                view.setPost(this);
//            }
//            this.view = view;
//        }
//    }

    public void setLike(Like like) {
        if(like != null) {
            if(like.getPost() != this) {
                like.setPost(this);
            }
            this.like = like;
        }
    }

//    @OneToMany(mappedBy = "post")
//    private View view;

//    @OneToMany(mappedBy = "post")
//    private Like like;
//
//    @OneToMany(mappedBy = "post")
//    private DisLike disLike;

    @Enumerated(EnumType.STRING)
    private PostStatus postStatus = PostStatus.POST_QUESTION_REGISTERED;

    @Column(nullable = false)
    private String title; // 제목

    @Column(nullable = false)
    private String content; // 내용

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime modifiedAt = LocalDateTime.now();

    public enum PostStatus {
        POST_QUESTION_REGISTERED(1, "질문 등록 상태"),
        POST_QUESTION_ANSWERED(2, "담변 완료 상태"),
        POST_QUESTION_DELETED(3, "질문 삭제 상태"),
        POST_QUESTION_DEACTIVATED(4, "질문 비활성화 상태"); // 회원 탈퇴시 비활성화

        @Getter
        private int stepNumber;
        @Getter
        private String stepDescription;

        PostStatus(int stepNumber, String stepDescription) {
            this.stepNumber = stepNumber;
            this.stepDescription = stepDescription;
        }
    }

    public void removeLike(Like like) {
        this.like = null;
        if(like.getPost() == this){
            like.removePost(this);
        }

    }

}
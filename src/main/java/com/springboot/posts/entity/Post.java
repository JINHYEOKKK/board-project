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

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID") // 외래키 join 컬럼을 하는 이유는 Post 테이블에 MEMBER_ID 라는 컬럼을 직접 가지기 위해
    private Member member;

    @Column
    private int likeCount = 0;

    @Column
    private int viewCount = 0;

    @OneToMany(mappedBy = "post", cascade = CascadeType.MERGE)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<View> views = new ArrayList<>();

    // Post 에 있는 views 는 매개변수로 오는 view로 세팅
    // 만약 매개변수 view 의 post를 가져와서 확인했는데, 데이터가 이 post가 아니라면, view의 post를 여기이 post로 세팅해준다.
    public void setView(View view) {
        this.views.add(view);
       if (view.getPost() != this) {
           view.setPost(this);
       }
    }

    public void setLikes(Like like) {
        this.likes.add(like);
        if (like.getPost() != this) {
            like.setPost(this);
        }
    }

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
        this.likes = null;
        if(like.getPost() == this){
            like.removePost(this);
        }
    }

    // like 를 증가시켜주는 메서드
    public void incrementLikeCount() {
        this.likeCount++;
    }

    // like 를 취소 시켜주는 메서드
    public void decrementLikeCount() {
        this.likeCount--;
    }

    public void incrementViewCount() {
        this.viewCount++;
    }
}

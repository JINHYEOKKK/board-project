package com.springboot.comment.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.springboot.posts.entity.Post;
import com.springboot.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentId;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "POST_ID")
    private Post post;

    // 자식엔터티에 @JsonBackReference 애너테이션을 붙혀 밑 set 메서드 생략가능
//    public void setPost(Post post) {
//        this.post = post;
//        if(!post.getComments().contains(this)) {
//            post.setComment(this);
//        }
//    }

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public void setMember(Member member) {
        this.member = member;
        if(!member.getComments().contains(this)) {
            member.setComment(this);
        }
    }

    @Column
    private String comment;

    @Column
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum CommentStatus {
        COMMENT_REGISTERED(1, "질문 등록 상태"),
        COMMENT_DEACTIVATED(2, "질문 비활성화 상태");

        @Getter
        private int statusCode;

        @Getter
        private String statusMessage;

        CommentStatus(int statusCode, String statusMessage) {
            this.statusCode = statusCode;
            this.statusMessage = statusMessage;
        }
    }

}

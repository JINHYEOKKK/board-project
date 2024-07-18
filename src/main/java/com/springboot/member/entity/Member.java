package com.springboot.member.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.springboot.comment.entity.Comment;
import com.springboot.posts.entity.Like;
import com.springboot.posts.entity.Post;

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
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;

    @OneToMany(mappedBy = "member")
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();

    public void setComment(Comment comment) {
        comments.add(comment);
        if(comment.getMember() != this) {
            comment.setMember(this);
        }
    }

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @Column(nullable = false)
    private String name;

//    @OneToMany(mappedBy = "member")
//    private List<View> views = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Like> likes = new ArrayList<>();

//    public void setView(View view) {
//        if(view.getMember() != this) {
//            view.setMember(this);
//        }
//        this.views.add(view);
//    }

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private LocalDateTime modifiedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    public enum MemberStatus {
        MEMBER_ACTIVE("활동중"),
        MEMBER_SLEEP("휴면 상태"),
        MEMBER_QUIT("탈퇴 상태");

        @Getter
        private String status;

        MemberStatus(String status) {
            this.status = status;
        }
    }

    public void removeLike(Like like) {
        this.likes.remove(like);
        if(like.getMember() == this) {
            like.removeMember(this);
        }
    }

}

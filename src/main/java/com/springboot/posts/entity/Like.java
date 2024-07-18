package com.springboot.posts.entity;

import com.springboot.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "Likes")
@Getter
@Setter
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long likeId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @Column(nullable = false, unique = true)
    private LocalDateTime createdAt = LocalDateTime.now();

    public void setMember(Member member) {
        if(!member.getLikes().contains(this)) {
            member.getLikes().add(this);
        }
        this.member = member;
    }

    public void setPost(Post post) {
        this.post = post;
        if(post.getLike() != this) {
            post.setLike(this);
        }
    }

    public void removePost(Post post) {
        this.post = null;
        if(post.getLike() == this) {
            post.removeLike(this);
        }
    }

    public void removeMember(Member member) {
        this.member = null;
        if(member.getLikes().contains(this)) {
            member.removeLike(this);
        }
    }


}

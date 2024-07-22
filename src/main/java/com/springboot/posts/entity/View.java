package com.springboot.posts.entity;

import com.springboot.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class View {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long viewId;

    // JoinColumn 이유: View 엔티티에서 POST_ID 컬럼을 외래키로 등록하려고.
    // cascade: 무언가를 금방 사라지지 않고 오래 지속되게 한다라는 것이 Persistence의 목적.
    // db 접근 최소화 하기 위해 1차캐시에 남아있음.
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public void setMember(Member member) {
        this.member = member;
        if(!member.getViews().contains(this)) {
            member.setView(this);
        }
    }

    public void setPost(Post post) {
        this.post = post;
        if(!post.getViews().contains(this)) {
            post.setView(this);
        }
    }
}


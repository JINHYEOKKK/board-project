//package com.springboot.posts.entity;
//
//import com.springboot.member.entity.Member;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import javax.persistence.*;
//
//@Entity
//@Setter
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//public class View {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long viewId;
//
//    @OneToOne(cascade = CascadeType.MERGE)
//    @JoinColumn(name = "POST_ID")
//    private Post post;
//
//    @ManyToOne(cascade = CascadeType.MERGE)
//    @JoinColumn(name = "MEMBER_ID")
//    private Member member;
//
//    public void setMember(Member member) {
//        if(!member.getViews().contains(this)) {
//            member.getViews().add(this);
//        }
//        this.member = member;
//    }
//
//    public void setPost(Post post) {
//        this.post = post;
//        if(post.getView() != this) {
//            post.setView(this);
//        }
//    }
//}
//

package com.springboot.member.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import com.springboot.member.repository.MemberRepository;
import com.springboot.posts.entity.Post;
import com.springboot.response.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member createMember(Member member) {
        verifyExistsEmail(member.getEmail());
        memberRepository.save(member);
        return member;
    }

    public Member updateMember(Member member) {
        Member findedMember = findVerifiedMember(member.getMemberId());

        Optional.ofNullable(findedMember.getName())
                .ifPresent(name -> findedMember.setName(member.getName()));
        Optional.ofNullable(findedMember.getPhone())
                .ifPresent(name -> findedMember.setPhone(member.getPhone()));

        return memberRepository.save(findedMember);
    }

    public Member getMember(long memberId) {
       return findVerifiedMember(memberId);
    }

    public Page<Member> getMembers(int page, int size) {
        // 페이징은 자동으로 해줌
        // 페이지를 memberId를 기준으로 정렬하고, 내림차순으로 정렬한다.
        return memberRepository.findAll(PageRequest.of(page, size,
                Sort.by("memberId").descending()));
    }

//    public void deleteMember(long memberId) {
//        Member findMember = findVerifiedMember(memberId);
//        findMember.setMemberStatus(Member.MemberStatus.MEMBER_QUIT);
//    }


    // 사용자가 존재하는지 검증, 존재하지 않는다면 만들어둔 예외를 던짐
    public Member findVerifiedMember(long memberId){
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        Member member =
                optionalMember.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        return member;
    }

    // email 로 사용자의 중복여부를 검증
    private void verifyExistsEmail(String email) {
        Optional<Member> findUser = memberRepository.findByEmail(email);
        if (findUser.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }
    }
    // 서비스 계층에서는 사용자 탈퇴 시 사용자가 작성한 게시글의 상태를 업데이트한 후 사용자를 삭제
    public void deleteMember(long memberId) {
        // member 객체를 생성후 delete HTTP 요청을 보낸 사용자의 memberId를 가져와 설정해줌. 못찾으면 예외던짐
        // 회원 탈퇴시 화원을 db에서 삭제하지 않고, MEMBER_QUIT
        Member member = findVerifiedMember(memberId);
        member.setMemberStatus(Member.MemberStatus.MEMBER_QUIT);
        List<Post> posts = member.getPosts();
        for(Post post : posts) {
            post.setPostStatus(Post.PostStatus.POST_QUESTION_DEACTIVATED);
        }
        memberRepository.save(member);
    }
}

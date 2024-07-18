package com.springboot.member.controller;

import com.springboot.member.dto.MemberPatchDto;
import com.springboot.member.dto.MemberPostDto;
import com.springboot.member.entity.Member;
import com.springboot.member.mapper.MemberMapper;
import com.springboot.member.service.MemberService;
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

@RestController // RequestBody로 json -> dto 객체받기
@RequestMapping("/v1/members") // 경로 지정
@Validated // dto 객체 검증하기위해
public class MemberController {
    private static final String POST_DEFAULT_URL = "/v1/posts";
    private final MemberService memberService;
    private final MemberMapper mapper;


    // 서비스 계층과 매퍼를 멤버 컨트롤러에 DI의존성 주입
    // 서비스계층: 클라이언트로부터 요청을 DTO 객체로 받아서, Entity 객체로 변환하여 넘겨줌, 그럼 서비스 로직 실행후 다시 넘어옴.
    // 매퍼: 서비스계층으로 객체를 넘겨줄때나, 클라이언트로 응답을 할때 적절한 타입의 객체로 매핑해주는 MapStruct 기능
    public MemberController(MemberService memberService, MemberMapper mapper) {
        this.memberService = memberService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity postMember(@Valid @RequestBody MemberPostDto memberPostDto) {
        Member member = memberService.createMember(mapper.memberPostDtoToMember(memberPostDto));
        URI location = UriCreator.createUri(POST_DEFAULT_URL, member.getMemberId());

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{member-id}")
    public ResponseEntity patchMember(@PathVariable("member-id") @Positive long memberId,
                                      @RequestBody @Valid MemberPatchDto memberPatchDto) {
        memberPatchDto.setMemberId(memberId);

        Member member =
                memberService.updateMember(mapper.memberPatchDtoToMember(memberPatchDto));

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.memberToMemberResponseDto(member)),
                HttpStatus.OK);
    }

    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") @Positive long memberId) {
         Member member = memberService.getMember(memberId);
         return new ResponseEntity<>(
                 new SingleResponseDto<>(mapper.memberToMemberResponseDto(member)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getMembers(@RequestParam @Positive int page,
                                     @RequestParam @Positive int size) {
        Page<Member> pageMembers = memberService.getMembers(page -1, size);
        List<Member> memberList = pageMembers.getContent(); // 페이지의 메서드 getContent() 의 반환타입은 List<T>
        return new ResponseEntity<>(
                new MultiResponseDto<>(
                        mapper.membersToMemberResponseDtos(memberList), pageMembers), HttpStatus.OK);
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(@PathVariable("member-id") @Positive long memberId) {
        memberService.deleteMember(memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

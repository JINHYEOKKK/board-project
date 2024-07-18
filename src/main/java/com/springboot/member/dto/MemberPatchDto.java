package com.springboot.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberPatchDto {
    private long memberId;
    private String name;
    private String phone;
    private String email;
}

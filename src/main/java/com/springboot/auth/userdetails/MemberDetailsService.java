package com.springboot.auth.userdetails;

/**
 * 1. 클라이언트가 서버 측에 로그인 인증 요청(Username/Password를 서버 측에 전송)
 * 2. 로그인 인증을 담당하는 Security Filter(`JwtAuthenticationFilter`)가 클라이언트의 로그인 인증 정보 수신
 * 3. Security Filter가 수신한 로그인 인증 정보를 AuthenticationManager에게 전달해 인증 처리를 위임
 * 4. AuthenticationManager가 **Custom UserDetailsService**(`MemberDetailsService`)에게 사용자의 UserDetails 조회를 위임
 * 5. **Custom UserDetailsService**(`MemberDetailsService`)가 사용자의 크리덴셜을 DB에서 조회한 후, AuthenticationManager에게 사용자의 **UserDetails**를 전달
 * 6. AuthenticationManager가 **로그인 인증 정보와 UserDetails의 정보를 비교해 인증 처리**
 * 7. JWT 생성 후, 클라이언트의 응답으로 전달
 */

import com.springboot.auth.utills.JwtAuthorityUtils;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import com.springboot.member.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 1번부터 7번 과정 중에서 우리는 `JwtAuthenticationFilter` 구현(2번 ~ 3번, 7번), `MemberDetailsService`(5번)을 구현합니다.
 *
 * 4번과 6번은 Spring Security의 AuthenticationManager가 대신 처리해 주므로 신경 쓸 필요가 없습니다.
 */

@Component
public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final JwtAuthorityUtils jwtAuthorityUtils;

    public MemberDetailsService(MemberRepository memberRepository, JwtAuthorityUtils jwtAuthorityUtils) {
        this.memberRepository = memberRepository;
        this.jwtAuthorityUtils = jwtAuthorityUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findByEmail(username);

        Member findMember = optionalMember.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        return new MemberDetails(findMember);
    }

    private final class MemberDetails extends Member implements UserDetails {
        MemberDetails(Member member) {
            setMemberId(member.getMemberId());
            setEmail(member.getEmail());
            setPassword(member.getPassword());
            setRoles(member.getRoles());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return jwtAuthorityUtils.createAuthority(this.getRoles());
        }

        @Override
        public String getUsername() {
            return getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }


}

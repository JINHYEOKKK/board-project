package com.springboot.auth.utills;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.swing.plaf.ListUI;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthorityUtils {

    @Value("${mail.address.admin}")
    private String adminMailAddress;

    private final List<GrantedAuthority> ADMIN_ROLES =
            AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");

    private final List<GrantedAuthority> USER_ROLES =
            AuthorityUtils.createAuthorityList("ROLE_USER");

    private List<String> ADMIN_ROLE_STRING = List.of("ADMIN", "USER");

    private List<String> USER_ROLE_STRING = List.of("USER");

    // 위에서 설정한 어드민 email 형식이라면 어드민 권한 주고, 아니면 일반 사용자권한 줌
    public List<GrantedAuthority> createAuthority(String email) {
        if(email.equals(adminMailAddress)) {
            return ADMIN_ROLES;
        }
            return USER_ROLES;
    }

    public List<GrantedAuthority> createAuthority(List<String> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE " + role))
                .collect(Collectors.toList());
    }

    public List<String> createRoles(String email) {
        if(email.equals(adminMailAddress)) {
            return ADMIN_ROLE_STRING;
        }
        return USER_ROLE_STRING;
    }
}

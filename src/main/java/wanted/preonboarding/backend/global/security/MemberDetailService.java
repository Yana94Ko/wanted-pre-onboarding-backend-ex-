package wanted.preonboarding.backend.global.security;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import wanted.preonboarding.backend.member.entity.Member;
import wanted.preonboarding.backend.member.service.MemberService;

@Component
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {
    private final MemberService memberService;
    @Override
    public UserDetails loadUserByUsername(String insertedUserId) throws UsernameNotFoundException {
        Member member = memberService.getMemberByEmail(insertedUserId)
                .orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다"));

        List<GrantedAuthority> authorities = member.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        return new User(member.getEmail(), member.getPassword(), authorities);
    }

}

package wanted.preonboarding.backend.member.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import wanted.preonboarding.backend.member.entity.Member;
import wanted.preonboarding.backend.member.entity.MemberRoles;

@Builder
@Getter
public class MemberResponse {
    private Long id;

    private String email;
    private String password;

    private List<MemberRoles> roles;

    public static MemberResponse of (Member member){
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRoles())
                .build();
    }

}

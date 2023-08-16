package wanted.preonboarding.backend.member.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRequest {
    @Getter
    public static class Create {
        @Pattern(regexp = "^.*@.*$", message = "이메일은 @ 을 포함해야 합니다")
        private String email;
        @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다")
        @Setter
        private String password;
    }
}

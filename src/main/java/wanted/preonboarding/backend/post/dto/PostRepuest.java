package wanted.preonboarding.backend.post.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.preonboarding.backend.member.dto.MemberRequest;

@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class PostRepuest {
    @Getter
    @Builder
    public static class Create {
        private String title;
        private String content;
    }

    @Getter
    @Builder
    public static class Update {
        private Long id;
        private MemberRequest.idOnly author;
        private String title;
        private String content;
    }

}

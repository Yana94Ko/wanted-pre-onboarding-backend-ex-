package wanted.preonboarding.backend.post.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class PostRepuest {
    @Getter
    @Builder
    public static class Create {
        private String title;
        private String content;
    }

}

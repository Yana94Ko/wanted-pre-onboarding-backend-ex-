package wanted.preonboarding.backend.post.dto;

import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import wanted.preonboarding.backend.member.dto.MemberResponse;
import wanted.preonboarding.backend.post.entity.Post;

@Builder
@Getter
public class PostResponse {
    private Long id;
    private MemberResponse author;
    private String title;
    private String content;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    public static PostResponse of(Post post){
        return PostResponse.builder()
                .id(post.getId())
                .author(MemberResponse.of(post.getAuthor()))
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}

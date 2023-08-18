package wanted.preonboarding.backend.member.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.preonboarding.backend.global.Auditable;
import wanted.preonboarding.backend.post.entity.Post;

@Getter
@Builder
@Entity
@Table(name = "MEMBER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private List<MemberRoles> roles = new ArrayList<>(List.of(MemberRoles.ROLE_USER));

    @OneToMany(mappedBy = "author")
    private List<Post> posts = new ArrayList<>();

    public static Member of (String email, String password) {
        return Member.builder()
                .email(email)
                .password(password)
                .build();
    }

}

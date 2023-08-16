package wanted.preonboarding.backend.post.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import wanted.preonboarding.backend.member.entity.Member;
import wanted.preonboarding.backend.post.entity.Post;

public interface PostRepo extends JpaRepository<Post, Long> {
}

package wanted.preonboarding.backend.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.preonboarding.backend.post.entity.Post;

public interface PostRepo extends JpaRepository<Post, Long> {
}

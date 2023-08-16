package wanted.preonboarding.backend.member.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import wanted.preonboarding.backend.member.entity.Member;

public interface MemberRepo extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}

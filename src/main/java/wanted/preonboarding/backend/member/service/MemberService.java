package wanted.preonboarding.backend.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wanted.preonboarding.backend.member.dto.MemberRequest;
import wanted.preonboarding.backend.member.entity.Member;
import wanted.preonboarding.backend.member.repository.MemberRepo;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepo memberRepo;

    @Transactional
    public void signUpMember(MemberRequest.Create memberDto) {
        Member member = Member.of(memberDto.getEmail(), memberDto.getPassword());
        memberRepo.save(member);
    }

}

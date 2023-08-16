package wanted.preonboarding.backend.post.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wanted.preonboarding.backend.global.exception.customExceptions.MemberNotFoundException;
import wanted.preonboarding.backend.member.entity.Member;
import wanted.preonboarding.backend.member.repository.MemberRepo;
import wanted.preonboarding.backend.post.dto.PostRepuest.Create;
import wanted.preonboarding.backend.post.entity.Post;
import wanted.preonboarding.backend.post.repository.PostRepo;

@Service
@RequiredArgsConstructor
public class PostService {
    private final MemberRepo memberRepo;
    private final PostRepo postRepo;
    @Transactional
    public void createPost (Create postDto, String email){
        Member member = memberRepo.findByEmail(email)
                .orElseThrow(()-> new MemberNotFoundException(email));
        Post post = Post.of(member, postDto.getTitle(), postDto.getContent());
        postRepo.save(post);
    }

}

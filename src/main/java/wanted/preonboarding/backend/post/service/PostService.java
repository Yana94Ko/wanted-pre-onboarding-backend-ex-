package wanted.preonboarding.backend.post.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.preonboarding.backend.global.exception.customExceptions.MemberNotFoundException;
import wanted.preonboarding.backend.global.exception.customExceptions.PostNotFoundException;
import wanted.preonboarding.backend.member.entity.Member;
import wanted.preonboarding.backend.member.repository.MemberRepo;
import wanted.preonboarding.backend.post.dto.PostRepuest.Create;
import wanted.preonboarding.backend.post.dto.PostResponse;
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

    @Transactional(readOnly = true)
    public List <PostResponse> getPostList(int pageNo, int pageSize, String sortBy){
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        List<Post> posts = postRepo.findAll(pageable).getContent();
        List<PostResponse> postList =  posts.stream()
                .map(post -> {
                    Hibernate.initialize(post.getAuthor());
                    return PostResponse.of(post);
                })
                .collect(Collectors.toList());

        return postList;
    }

    @Transactional(readOnly = true)
    public PostResponse getPostInfo(Long id){
        Post post =  postRepo.findById(id).orElseThrow(()-> new PostNotFoundException(id));
        return PostResponse.of(post);
    }
}

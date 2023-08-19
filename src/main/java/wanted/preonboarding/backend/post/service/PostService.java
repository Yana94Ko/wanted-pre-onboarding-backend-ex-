package wanted.preonboarding.backend.post.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.preonboarding.backend.global.exception.customExceptions.MemberNotFoundException;
import wanted.preonboarding.backend.global.exception.customExceptions.PostNotFoundException;
import wanted.preonboarding.backend.member.entity.Member;
import wanted.preonboarding.backend.member.repository.MemberRepo;
import wanted.preonboarding.backend.post.dto.PostRepuest;
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

    @Transactional
    public PostResponse updatePost(Long id, PostRepuest.Update postDto, String email){
        // 수정 전 게시글 엔티티 불러오기
        Post previousPost = postRepo.findById(id).orElseThrow(()->new PostNotFoundException(id));
        // 현재 로그인된 사용자 불러오기
        Member member = memberRepo.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(email));

        // 수정전 게시글 작성자의 Email과 Auth 처리되어있는 이메일 일치 여부 확인후 수정 진행
        if (previousPost.getAuthor().getEmail().equals(email)) {
            previousPost.updatePost(Post.of(member, postDto.getTitle(), postDto.getContent()));
        } else {
            throw new AccessDeniedException("게시글 수정 권한이 없습니다.");
        }
        return PostResponse.of(previousPost);
    }

    @Transactional
    public void deletePost(Long id, String email) {
        Post post = postRepo.findById(id).orElseThrow(() -> new PostNotFoundException(id));

        Member member = memberRepo.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(email));

        if (post.getAuthor().getEmail().equals(email)) {
            postRepo.delete(post);
        } else {
            throw new AccessDeniedException("게시글 삭제 권한이 없습니다.");
        }
    }

}

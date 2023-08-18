package wanted.preonboarding.backend.post.api;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wanted.preonboarding.backend.post.dto.PostRepuest;
import wanted.preonboarding.backend.post.dto.PostResponse;
import wanted.preonboarding.backend.post.service.PostService;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping()
    ResponseEntity<String> createPost (@Valid @RequestBody PostRepuest.Create postDto,
            @AuthenticationPrincipal UserDetails userDetails){
        postService.createPost(postDto, userDetails.getUsername());
        return new ResponseEntity<>("게시글 작성에 성공했습니다.", HttpStatus.OK);
    }

    @GetMapping()
    ResponseEntity<List<PostResponse>> getPostList(
            @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "createdAt", required = false) String sortBy
    ){
        List<PostResponse> postList = postService.getPostList(pageNo, pageSize, sortBy);
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

}
package wanted.preonboarding.backend.global.exception.customExceptions;

public class PostNotFoundException extends RuntimeException {
    private Long postId;

    public PostNotFoundException(Long postId) {
        super("Post not found with ID: " + postId);
        this.postId = postId;
    }

    public Long getMemberId() {
        return postId ;
    }
}

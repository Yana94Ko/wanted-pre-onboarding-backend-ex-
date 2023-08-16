package wanted.preonboarding.backend.global.exception.customExceptions;

public class MemberNotFoundException extends RuntimeException {
    private Long memberId;
    private String memberEmail;

    public MemberNotFoundException(Long memberId) {
        super("Member not found with ID: " + memberId);
        this.memberId = memberId;
    }
    public MemberNotFoundException(String memberEmail) {
        super("Member not found with E-mail: " + memberEmail);
        this.memberEmail = memberEmail;
    }

    public Long getMemberId() {
        return memberId ;
    }
    public String getMemberEmail() {
        return memberEmail ;
    }
}

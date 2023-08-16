package wanted.preonboarding.backend.global.exception;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
//Unauthorized, Payment Required, Not Found, Method Not Allowed, Not Acceptable,
    OK(200, HttpStatus.OK, "Ok"),

    BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "Bad request"),
    SPRING_BAD_REQUEST(10001, HttpStatus.BAD_REQUEST, "Spring-detected bad request"),
    VALIDATION_ERROR(10002, HttpStatus.BAD_REQUEST, "Validation error"),
    NOT_FOUND(10003, HttpStatus.NOT_FOUND, "Requested resource is not found"),
    MEMBER_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Member not found with ID: %d"),

    INTERNAL_ERROR(20000, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),
    SPRING_INTERNAL_ERROR(20001, HttpStatus.INTERNAL_SERVER_ERROR, "Spring-detected internal error"),
    DATA_ACCESS_ERROR(20002, HttpStatus.INTERNAL_SERVER_ERROR, "Data access error"),

    // Spring security 관련 예외처리
    AUTHENTICATION_ERROR(403, HttpStatus.FORBIDDEN, "AuthenticationException, authentication fails"),
    ACCESSDENIED_ERROR(403, HttpStatus.FORBIDDEN, "AccessDeniedException, access is denied as the authentication does not hold a required authority or ACL privilege"),
    INSUFFICIENTAUTHENTICATION_ERROR(403, HttpStatus.FORBIDDEN, "InsufficientAuthenticationException, access is denied as the authentication does not provide a sufficient level of trust"),
    JWT_SIGNATURE_ERROR(403, HttpStatus.FORBIDDEN, "SignatureException, The secret key for generating and decrypting JWT is different"),
    JWT_MALFORMED_ERROR(403, HttpStatus.FORBIDDEN, "MalformedJwtException, An attempt to decrypt a forged JWT has been detected"),
    JWT_EXPIRED_ERROR(403, HttpStatus.FORBIDDEN, "ExpiredJwtException, A decryption attempt was detected for an expired JWT")
    ;

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;


    public static ErrorCode valueOf(HttpStatusCode httpStatusCode) {
        if (httpStatusCode == null) { throw new GeneralException("HttpStatus is null."); }

        return Arrays.stream(values())
                .filter(errorCode -> errorCode.getHttpStatus() == httpStatusCode)
                .findFirst()
                .orElseGet(() -> {
                    if (httpStatusCode.is4xxClientError()) { return ErrorCode.BAD_REQUEST; }
                    else if (httpStatusCode.is5xxServerError()) { return ErrorCode.INTERNAL_ERROR; }
                    else { return ErrorCode.OK; }
                });
    }

    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
    }
    public String getMessage(Throwable e, Long id) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.name(), this.getCode());
    }

}
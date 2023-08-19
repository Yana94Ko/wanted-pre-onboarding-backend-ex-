package wanted.preonboarding.backend.global.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.validation.ConstraintViolationException;
import java.security.SignatureException;
import javax.naming.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import wanted.preonboarding.backend.global.exception.customExceptions.MemberNotFoundException;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Spring security 예외처리
     */
    // 가장 먼저 처리될 예외들을 상위에 배치
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e) {
        ErrorCode errorCode = ErrorCode.ACCESSDENIED_ERROR;
        String errorMessage = String.format(errorCode.getMessage(), e.getMessage());
        ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.FORBIDDEN, false, errorCode.getCode(), errorMessage);
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }
    @ExceptionHandler({InsufficientAuthenticationException.class})
    public ResponseEntity<Object> handleAccessDeniedException(InsufficientAuthenticationException e) {
        ErrorCode errorCode = ErrorCode.INSUFFICIENTAUTHENTICATION_ERROR;
        String errorMessage = String.format(errorCode.getMessage(), e.getMessage());
        ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.UNAUTHORIZED, false, errorCode.getCode(), errorMessage);
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }

    @ExceptionHandler({SignatureException.class})
    public ResponseEntity<Object> handleSignatureException(SignatureException e) {
        ErrorCode errorCode = ErrorCode.JWT_SIGNATURE_ERROR;
        String errorMessage = String.format(errorCode.getMessage(), e.getMessage());
        ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.UNAUTHORIZED, false, errorCode.getCode(), errorMessage);
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }

    @ExceptionHandler({MalformedJwtException.class})
    public ResponseEntity<Object> handleMalformedLinkException(MalformedJwtException e) {
        ErrorCode errorCode = ErrorCode.JWT_MALFORMED_ERROR;
        String errorMessage = String.format(errorCode.getMessage(), e.getMessage());
        ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.BAD_REQUEST, false, errorCode.getCode(), errorMessage);
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }

    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException e) {
        ErrorCode errorCode = ErrorCode.JWT_EXPIRED_ERROR;
        String errorMessage = String.format(errorCode.getMessage(), e.getMessage());
        ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.UNAUTHORIZED, false, errorCode.getCode(), errorMessage);
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<Object> handleAuthenticationexception(AuthenticationException e) {
        ErrorCode errorCode = ErrorCode.AUTHENTICATION_ERROR;
        String errorMessage = String.format(errorCode.getMessage(), e.getMessage());
        ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.UNAUTHORIZED, false, errorCode.getCode(), errorMessage);
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }

    /**
     *  @Validated로 Service에서 binding error 발생시 처리
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
        return handleExceptionInternal(e, ErrorCode.VALIDATION_ERROR, request);
    }
    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    @ExceptionHandler
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException( MethodArgumentTypeMismatchException e, WebRequest request) {
        return handleExceptionInternal(e, ErrorCode.BAD_REQUEST, request);
    }
    @ExceptionHandler(MemberNotFoundException.class)
    protected ResponseEntity<ApiErrorResponse> handleMemberNotFoundException(MemberNotFoundException e, WebRequest request) {
        ErrorCode errorCode = ErrorCode.MEMBER_NOT_FOUND;
        String errorMessage = String.format(errorCode.getMessage(), e.getMemberId()==null ? e.getMemberEmail(): e.getMemberId());
        ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.NOT_FOUND, false, errorCode.getCode(), errorMessage);
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }
    @ExceptionHandler
    protected ResponseEntity<Object> general(GeneralException e, WebRequest request) {
        return handleExceptionInternal(e, e.getErrorCode(), request);
    }

    @ExceptionHandler
    protected ResponseEntity<Object> exception(Exception e, WebRequest request) {
        return handleExceptionInternal(e, ErrorCode.INTERNAL_ERROR, request);
    }

    /*
     *  @Valid, @Validated로 Controller에서 binding error 발생시 처리
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        if( e instanceof MethodArgumentNotValidException){
            return handleExceptionInternal(e, ErrorCode.VALIDATION_ERROR, request);
        }
        return super.handleExceptionInternal(e, body, headers, statusCode, request);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, ErrorCode errorCode, WebRequest request) {
        return handleExceptionInternal(e, errorCode, HttpHeaders.EMPTY, errorCode.getHttpStatus(), request);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, ErrorCode errorCode, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleExceptionInternal(
                e,
                ApiErrorResponse.of(status, false, errorCode.getCode(), errorCode.getMessage(e)),
                headers,
                status,
                request
        );
    }

}
// TODO : 단위테스트, 슬라이스 테스트 추가하기
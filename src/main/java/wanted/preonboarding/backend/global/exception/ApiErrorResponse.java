package wanted.preonboarding.backend.global.exception;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
class ApiErrorResponse {

    private final HttpStatus httpStatus;
    private final Boolean success;
    private final Integer errorCode;
    private final String message;

    public static ApiErrorResponse of(HttpStatus httpStatus, Boolean success, Integer errorCode, String message) {
        return new ApiErrorResponse(httpStatus, success, errorCode, message);
    }

    public static ApiErrorResponse of(HttpStatus httpStatus, Boolean success, ErrorCode errorCode) {
        return new ApiErrorResponse(httpStatus, success, errorCode.getCode(), errorCode.getMessage());
    }

    public static ApiErrorResponse of(HttpStatus httpStatus, Boolean success, ErrorCode errorCode, Exception e) {
        return new ApiErrorResponse(httpStatus, success, errorCode.getCode(), errorCode.getMessage(e));
    }

    public static ApiErrorResponse of(HttpStatus httpStatus, Boolean success, ErrorCode errorCode, String message) {
        return new ApiErrorResponse(httpStatus, success, errorCode.getCode(), errorCode.getMessage(message));
    }

}

package vn.anpha.storage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    USER_EXISTED("User already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED("User already not exists", HttpStatus.NOT_FOUND),
    USER_PASSWORD_NOT_EXACTLY("User and password are not exactly", HttpStatus.BAD_REQUEST),
    Token_Not_Valid("Token is not valid", HttpStatus.BAD_REQUEST),
    RefreshToken_Not_Valid("RefreshToken is not valid", HttpStatus.BAD_REQUEST),
    Role_Is_EXIST("Role is exists", HttpStatus.NOT_FOUND),
    UNAUTHORIZED("User do not have permission", HttpStatus.FORBIDDEN),
    UNAUTHOTICATED("Unautheticated", HttpStatus.UNAUTHORIZED);

    private String message;
    private HttpStatusCode statusCode;

    private ErrorCode(String message, HttpStatusCode statusCode) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

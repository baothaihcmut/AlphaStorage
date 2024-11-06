package vn.anpha.storage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    USER_EXISTED("User already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED("User already not exists", HttpStatus.NOT_FOUND),
    USER_PASSWORD_NOT_EXACTLY("User and password are not exactly", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_EXACTLY("Password are not exactly", HttpStatus.BAD_REQUEST),
    Token_Not_Valid("Token is not valid", HttpStatus.BAD_REQUEST),
    RefreshToken_Not_Valid("RefreshToken is not valid", HttpStatus.BAD_REQUEST),
    Role_Is_EXIST("Role is exists", HttpStatus.NOT_FOUND),
    UNAUTHORIZED("User do not have permission", HttpStatus.FORBIDDEN),
    UNAUTHOTICATED("Unautheticated", HttpStatus.UNAUTHORIZED),

    // FOR Company
    COMPANY_EXISTED("Company already exists", HttpStatus.BAD_REQUEST),
    COMPANY_NOT_EXISTED("Company not exists", HttpStatus.BAD_REQUEST),

    // FOR Buy Data
    THIS_TYPE_DOES_NOT_EXIST("This type does not exist", HttpStatus.BAD_REQUEST),

    // FOR SERVER
    METHOD_NOT_ALLOW("Method not Exist", HttpStatus.METHOD_NOT_ALLOWED),
    // ELSE
    SERVER_ERROR("Server error", HttpStatus.INTERNAL_SERVER_ERROR);

    ;

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
